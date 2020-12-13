package com.mnf.etbadel.ui.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.controller.services.NotificationSendService;
import com.mnf.etbadel.model.ChatModel;
import com.mnf.etbadel.model.Client;
import com.mnf.etbadel.model.Data;
import com.mnf.etbadel.model.MessageModel;
import com.mnf.etbadel.model.MyResponse;
import com.mnf.etbadel.model.Sender;
import com.mnf.etbadel.model.Token;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.ui.chats.adapters.MessageAdapter;
import com.mnf.etbadel.util.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.online_dot_img)
    ImageView onlineDotImg;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bar_layout)
    AppBarLayout barLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.text_send)
    EditText textSend;
    @BindView(R.id.btn_send)
    ImageButton btnSend;
    @BindView(R.id.bottom)
    RelativeLayout bottom;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    int userid, frontId = 0;
    String chatId;
    ChatModel chatModel;
    ArrayList<MessageModel> messageModels;
    MessageAdapter messageAdapter;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.blocked)
    TextView blocked;
    @BindView(R.id.you_block)
    TextView youBlock;
    @BindView(R.id.unlock_txt)
    TextView unlockTxt;
    @BindView(R.id.bottomUnblock)
    LinearLayout bottomUnblock;

    NotificationSendService notificationSendService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        recyclerView.setHasFixedSize(true);
        bottomUnblock.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatId = getIntent().getStringExtra(AppConstants.CHAT_KEY);
        sharedPreferences = getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        userid = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
        databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_MESSAGE_TABLE);
        getBlockedUsers();
        readChatData();
        readMessages();
        notificationSendService= Client.getClient("https://fcm.googleapis.com/").create(NotificationSendService.class);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textSend.getText().toString().trim();
                if (!msg.equals("")) {
                    sendMessage(userid, frontId, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                textSend.setText("");
            }
        });

        unlockTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                Controller controller= Controller.getInstance(MessageActivity.this);
                controller.unblockUser(userid, frontId,new GetBlockUserCallback());
            }
        });
    }

    private void getBlockedUsers() {
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Controller controller = Controller.getInstance(MessageActivity.this);
        controller.getBlockedUsers(userid, new GetBlockUserCallback());
    }

    private void readChatData() {
        DatabaseReference databaseReferenceChats = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_CHAT_TABLE);
        databaseReferenceChats.orderByChild("chatId").equalTo(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    chatModel = snapshot.getValue(ChatModel.class);
                    assert chatModel != null;
                    if (chatModel.getUser1Id() == userid) {
                        frontId = chatModel.getUser2Id();
                        if (chatModel.getUser2Name()!=null){
                            username.setText(chatModel.getUser2Name());
                        }
                        if (chatModel.getUser2Profile()!=null){
                            Glide.with(MessageActivity.this).load(chatModel.getUser2Profile()).placeholder(R.drawable.sample).into(profileImage);
                        }
                    } else {
                        frontId = chatModel.getUser1Id();
                        if (chatModel.getUser1Name()!=null){
                            username.setText(chatModel.getUser1Name());
                        }
                        if (chatModel.getUser1Profile()!=null){
                            Glide.with(MessageActivity.this).load(chatModel.getUser1Profile()).placeholder(R.drawable.sample).into(profileImage);
                        }
                    }
                    if (chatModel.isStarted()){
                        bottom.setVisibility(View.VISIBLE);
                    }else {
                        bottom.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(int sender, int receiver, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_MESSAGE_TABLE);
        DatabaseReference databaseReferenceChats = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_CHAT_TABLE).child(chatId);
        String id = databaseReference.push().getKey();
        MessageModel messageModel = new MessageModel();
        messageModel.setMessageId(id);
        messageModel.setChatId(chatId + "");
        messageModel.setReceiverId(receiver);
        messageModel.setSenderId(sender);
        messageModel.setMessageTxt(message);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        messageModel.setDateTime(formattedDate);
        messageModel.setRead(false);
        databaseReference.child(id).setValue(messageModel);
        chatModel.setLastMessage(message);
        chatModel.setLastMessageDateTime(formattedDate);
        sendNotification(receiver,userid, message);
        databaseReferenceChats.setValue(chatModel);
    }

    private void sendNotification(int receiver, int userid, String message) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Tokens");
        Query query= databaseReference.orderByKey().equalTo(receiver+"");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Token token= dataSnapshot.getValue(Token.class);
                    Data data= new Data(userid+"", R.mipmap.ic_launcher, message, "New Message", receiver+"");
                    Sender sender= new Sender(data, token.getToken());

                    notificationSendService.sendNotification(sender).enqueue(
                            new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.isSuccessful()){
                                        if (response.body().success!=1){

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("","");
                                }
                            }
                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessages() {
        messageModels = new ArrayList<>();
        databaseReference.orderByChild("chatId").equalTo(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MessageModel messageModel = snapshot.getValue(MessageModel.class);
                    messageModels.add(messageModel);
                    messageAdapter = new MessageAdapter(MessageActivity.this, messageModels);
                    recyclerView.setAdapter(messageAdapter);

                    if (messageModel.getSenderId() != userid) {
                        if (!messageModel.isRead()) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_MESSAGE_TABLE).child(messageModel.getMessageId());
                            messageModel.setRead(true);
                            databaseReference.setValue(messageModel);
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.block:
                blockUser();
                return true;
//            case R.id.delete:
//                return true;
        }
        return false;
    }

    private void blockUser() {
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Controller controller = Controller.getInstance(MessageActivity.this);
        controller.blockUser(userid, frontId, new BlockUserCallback());
    }

    private class BlockUserCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {

                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        progressBar.setVisibility(View.GONE);
                        progressLayout.setVisibility(View.GONE);
                        if (jsonObject.getBoolean("Success")) {
                            finish();
                        } else {
                            String error = jsonObject.getString("Message");
                            Log.e("status", "error " + error);
                            AppConstants.showErroDIalog(error, getSupportFragmentManager());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.e("status", "failed");
            progressBar.setVisibility(View.GONE);
            progressLayout.setVisibility(View.GONE);
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getSupportFragmentManager());
        }
    }

    private class GetBlockUserCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {

                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        if (jsonObject.getBoolean("Success")) {
                            JSONArray model = jsonObject.getJSONArray("Model");
                            if (model != null) {
                                Gson gson = new Gson();
                                ArrayList<UserModel> userModels = gson.fromJson(model.toString(), new TypeToken<List<UserModel>>() {
                                }.getType());
                                checkUser(userModels);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                progressLayout.setVisibility(View.GONE);
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            progressLayout.setVisibility(View.GONE);
                            String error = jsonObject.getString("Message");
                            Log.e("status", "error " + error);
                            AppConstants.showErroDIalog(error, getSupportFragmentManager());
                            bottom.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.e("status", "failed");
            progressBar.setVisibility(View.GONE);
            progressLayout.setVisibility(View.GONE);
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getSupportFragmentManager());
            bottom.setVisibility(View.GONE);
        }
    }

    private void checkUser(ArrayList<UserModel> userModels) {
        for (UserModel userModel : userModels) {
            if (userModel.getId() == frontId) {
                bottom.setVisibility(View.GONE);
                bottomUnblock.setVisibility(View.VISIBLE);
                blocked.setVisibility(View.VISIBLE);
                unlockTxt.setVisibility(View.VISIBLE);
                youBlock.setVisibility(View.GONE);
            }
        }
        getFrontUserdBlockList();
    }

    private void getFrontUserdBlockList() {
        Controller controller = Controller.getInstance(MessageActivity.this);
        controller.getBlockedUsers(frontId, new GetFrontBlockUserCallback());
    }

    private class GetFrontBlockUserCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {

                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        if (jsonObject.getBoolean("Success")) {
                            JSONArray model = jsonObject.getJSONArray("Model");
                            if (model != null) {
                                Gson gson = new Gson();
                                ArrayList<UserModel> userModels = gson.fromJson(model.toString(), new TypeToken<List<UserModel>>() {
                                }.getType());
                                checkFrontUser(userModels);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                progressLayout.setVisibility(View.GONE);
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            progressLayout.setVisibility(View.GONE);
                            String error = jsonObject.getString("Message");
                            Log.e("status", "error " + error);
                            AppConstants.showErroDIalog(error, getSupportFragmentManager());
                            bottom.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.e("status", "failed");
            progressBar.setVisibility(View.GONE);
            progressLayout.setVisibility(View.GONE);
            AppConstants.showErroDIalog(getResources().getString(R.string.server_unreachable_error), getSupportFragmentManager());
            bottom.setVisibility(View.GONE);
        }
    }

    private void checkFrontUser(ArrayList<UserModel> userModels) {
        for (UserModel userModel : userModels) {
            if (userModel.getId() == userid) {
                bottom.setVisibility(View.GONE);
                bottomUnblock.setVisibility(View.VISIBLE);
                blocked.setVisibility(View.GONE);
                unlockTxt.setVisibility(View.GONE);
                youBlock.setVisibility(View.VISIBLE);
            }
        }
        progressBar.setVisibility(View.GONE);
        progressLayout.setVisibility(View.GONE);
    }
}