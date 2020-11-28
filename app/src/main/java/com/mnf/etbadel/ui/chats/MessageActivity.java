package com.mnf.etbadel.ui.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mnf.etbadel.R;
import com.mnf.etbadel.model.ChatModel;
import com.mnf.etbadel.model.MessageModel;
import com.mnf.etbadel.ui.chats.adapters.MessageAdapter;
import com.mnf.etbadel.util.AppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

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
    int userid,frontId;
    String chatId;
    ChatModel chatModel;
    ArrayList<MessageModel> messageModels;
    MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatId= getIntent().getStringExtra(AppConstants.CHAT_KEY);
        sharedPreferences= getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        userid= sharedPreferences.getInt(AppConstants.SF_USER_ID,0);
        databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_MESSAGE_TABLE);
        readChatData();
        readMessages();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg= textSend.getText().toString().trim();
                if (!msg.equals("")){
                    sendMessage(userid, frontId,msg);
                }else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                textSend.setText("");
            }
        });
    }

    private void readChatData() {
        DatabaseReference databaseReferenceChats= FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_MESSAGE_TABLE);
        databaseReferenceChats.orderByChild("chatId").equalTo(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatModel= dataSnapshot.getValue(ChatModel.class);
                if (chatModel.getUser1Id()==userid){
                    frontId= chatModel.getUser2Id();
                }else {
                    frontId=chatModel.getUser1Id();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(int sender, int receiver, String message){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_MESSAGE_TABLE);
        DatabaseReference databaseReferenceChats= FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_MESSAGE_TABLE).child(chatId);
        String id=databaseReference.push().getKey();
        MessageModel messageModel= new MessageModel();
        messageModel.setChatId(chatId+"");
        messageModel.setReceiverId(receiver);
        messageModel.setSenderId(sender);
        messageModel.setMessageTxt(message);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        messageModel.setDateTime(formattedDate);
        databaseReference.child(id).setValue(messageModel);
        chatModel.setLastMessage(message);
        chatModel.setLastMessageDateTime(formattedDate);
        databaseReferenceChats.setValue(chatModel);
    }
    private void readMessages() {
        messageModels= new ArrayList<>();
        databaseReference.orderByChild("chatId").equalTo(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageModels.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    messageModels.add(snapshot.getValue(MessageModel.class));
                    messageAdapter= new MessageAdapter(MessageActivity.this, messageModels);
                    recyclerView.setAdapter(messageAdapter);
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
//        switch (item.getItemId()) {
//            case R.id.block:
//                return true;
//            case R.id.delete:
//                return true;
//        }
        return false;
    }
}