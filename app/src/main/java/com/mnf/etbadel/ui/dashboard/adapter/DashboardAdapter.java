package com.mnf.etbadel.ui.dashboard.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mnf.etbadel.MainActivity;
import com.mnf.etbadel.R;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.ChatModel;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.model.MessageModel;
import com.mnf.etbadel.model.NotificationModel;
import com.mnf.etbadel.model.UserModel;
import com.mnf.etbadel.ui.login.LoginActivity;
import com.mnf.etbadel.util.AppConstants;
import com.mnf.etbadel.util.SucessDialogFragment;
import com.mnf.etbadel.util.TradeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.NotificationHolder> {

    Context context;
    MainActivity activity;
    ArrayList<ItemModel> itemModels;
    private int selectedImg = 0;

    HideShowProgress hideShowProgress;
    DatabaseReference databaseReference;

    public interface HideShowProgress {
        void showProgress();

        void hideProgress();
    }

    //    NavigationInterface navigationInterface;
    public DashboardAdapter(MainActivity activity, Context context, ArrayList<ItemModel> itemModels, HideShowProgress hideShowProgress) {
        this.activity = activity;
        this.context = context;
        this.itemModels = itemModels;
        this.hideShowProgress = hideShowProgress;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item_cell_layout, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        ArrayList<String> imageList = new ArrayList<>();
        ItemModel itemModel = itemModels.get(position);

        if (itemModel.getImg1_url() != null) {
            selectedImg = 0;
            imageList.add(itemModel.getImg1_url());
            Glide.with(context).load(itemModel.getImg1_url()).into(holder.itemImg);
        }
        if (itemModel.getImg2_url() != null) {
            holder.cardSubImg1.setVisibility(View.VISIBLE);
            holder.subImg1.setVisibility(View.VISIBLE);
            imageList.add(itemModel.getImg2_url());
            Glide.with(context).load(itemModel.getImg2_url()).into(holder.subImg1);
        } else {
            holder.cardSubImg1.setVisibility(View.INVISIBLE);
            holder.subImg1.setVisibility(View.INVISIBLE);
        }
        if (itemModel.getImg3_url() != null) {
            holder.cardSubImg2.setVisibility(View.VISIBLE);
            holder.subImg2.setVisibility(View.VISIBLE);
            imageList.add(itemModel.getImg3_url());
            Glide.with(context).load(itemModel.getImg3_url()).into(holder.subImg2);
        } else {
            holder.cardSubImg2.setVisibility(View.INVISIBLE);
            holder.subImg2.setVisibility(View.INVISIBLE);
        }
        holder.prevImgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImg == 1 && imageList.get(0) != null) {
                    selectedImg = 0;
                    Glide.with(context).load(imageList.get(0)).into(holder.itemImg);
                    Glide.with(context).load(imageList.get(1)).into(holder.subImg1);
                } else if (selectedImg == 2 && imageList.get(1) != null) {
                    selectedImg = 1;
                    Glide.with(context).load(imageList.get(1)).into(holder.itemImg);
                    Glide.with(context).load(imageList.get(2)).into(holder.subImg2);
                }
            }
        });

        holder.subImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageList.size() > 2) {
                    if (selectedImg == 0) {
                        selectedImg = 1;
                        Glide.with(context).load(imageList.get(1)).into(holder.itemImg);
                        Glide.with(context).load(imageList.get(0)).into(holder.subImg1);
                        Glide.with(context).load(imageList.get(2)).into(holder.subImg2);
                    } else if (selectedImg == 1) {
                        selectedImg = 0;
                        Glide.with(context).load(imageList.get(0)).into(holder.itemImg);
                        Glide.with(context).load(imageList.get(1)).into(holder.subImg1);
                        Glide.with(context).load(imageList.get(2)).into(holder.subImg2);
                    } else if (selectedImg == 2) {
                        selectedImg = 0;
                        Glide.with(context).load(imageList.get(0)).into(holder.itemImg);
                        Glide.with(context).load(imageList.get(1)).into(holder.subImg1);
                        Glide.with(context).load(imageList.get(2)).into(holder.subImg2);
                    }
                } else {
                    if (selectedImg == 0) {
                        selectedImg = 1;
                        Glide.with(context).load(imageList.get(1)).into(holder.itemImg);
                        Glide.with(context).load(imageList.get(0)).into(holder.subImg1);
                    } else if (selectedImg == 1) {
                        selectedImg = 0;
                        Glide.with(context).load(imageList.get(0)).into(holder.itemImg);
                        Glide.with(context).load(imageList.get(1)).into(holder.subImg1);
                    }
                }
            }
        });

        holder.subImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImg == 0) {
                    selectedImg = 2;
                    Glide.with(context).load(imageList.get(2)).into(holder.itemImg);
                    Glide.with(context).load(imageList.get(1)).into(holder.subImg1);
                    Glide.with(context).load(imageList.get(0)).into(holder.subImg2);
                } else if (selectedImg == 1) {
                    selectedImg = 0;
                    Glide.with(context).load(imageList.get(0)).into(holder.itemImg);
                    Glide.with(context).load(imageList.get(1)).into(holder.subImg1);
                    Glide.with(context).load(imageList.get(2)).into(holder.subImg2);
                } else if (selectedImg == 2) {
                    selectedImg = 0;
                    Glide.with(context).load(imageList.get(0)).into(holder.itemImg);
                    Glide.with(context).load(imageList.get(1)).into(holder.subImg1);
                    Glide.with(context).load(imageList.get(2)).into(holder.subImg2);
                }
            }
        });

        holder.nxtImgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImg == 0 && imageList.size() > 1) {
                    selectedImg = 1;
                    Glide.with(context).load(imageList.get(1)).into(holder.itemImg);
                    Glide.with(context).load(imageList.get(0)).into(holder.subImg1);
                } else if (selectedImg == 1 && imageList.size() > 2) {
                    selectedImg = 2;
                    Glide.with(context).load(imageList.get(2)).into(holder.itemImg);
                    Glide.with(context).load(imageList.get(1)).into(holder.subImg2);
                }
            }
        });
        if (itemModel.getC_date() != null) {
            String dT;
            if (itemModel.getC_date().length() > 19) {
                dT = itemModel.getC_date().substring(0, itemModel.getC_date().lastIndexOf("."));
            } else {
                dT = itemModel.getC_date();
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            String dateString = "";
            try {
                date = dateFormat.parse(dT);
                dateString = dateFormat1.format(date);
                holder.dateTxtview.setText(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.dateTxtview.setText("");
        }

        holder.areaTxtview.setText(itemModel.getArea_Name());
        holder.itemNameTxtview.setText(itemModel.getTitle());
        holder.cndtTxtview.setText(itemModel.getCondition());
        holder.dscrTxtview.setText(itemModel.getDescription());
        holder.locTxtview.setText(itemModel.getLocation());
        holder.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
                int senderId = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
                if (senderId == 0) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {
                    TradeFragment.CallbackInterface callbackInterface = new TradeFragment.CallbackInterface() {
                        @Override
                        public void doCallbackOnClick() {
                            NotificationModel notificationModel = new NotificationModel();
                            notificationModel.setUser_Id(itemModel.getUser_Id());
                            notificationModel.setSender_Id(senderId);
                            notificationModel.setItem_Id(itemModel.getId());
                            notificationModel.setType_Id(0);
                            hideShowProgress.showProgress();
                            Controller.getInstance(context).saveNotification(notificationModel, new NotificationSaveCallback());
                        }
                    };


                    TradeFragment alertDialog = new TradeFragment();
                    alertDialog.setInterfaceInstance(callbackInterface);
                    alertDialog.show(activity.getSupportFragmentManager(), "fragment_alert");


                }
            }
        });

//        alertDialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//
//            }
//        });

    }

    public void updateList(ArrayList<ItemModel> itemModels) {
        this.itemModels = itemModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.prev_imgview)
        ImageView prevImgview;
        @BindView(R.id.item_img)
        ImageView itemImg;
        @BindView(R.id.nxt_imgview)
        ImageView nxtImgview;
        @BindView(R.id.date_txtview)
        TextView dateTxtview;
        @BindView(R.id.dscr_txtview)
        TextView dscrTxtview;
        @BindView(R.id.item_name_txtview)
        TextView itemNameTxtview;
        @BindView(R.id.loc_txtview)
        TextView locTxtview;
        @BindView(R.id.area_txtview)
        TextView areaTxtview;
        @BindView(R.id.cndt_txtview)
        TextView cndtTxtview;
        @BindView(R.id.submit_btn)
        Button submitBtn;
        @BindView(R.id.sub_img_1)
        ImageView subImg1;
        @BindView(R.id.sub_img_2)
        ImageView subImg2;
        @BindView(R.id.card_sub_img_1)
        CardView cardSubImg1;
        @BindView(R.id.card_sub_img_2)
        CardView cardSubImg2;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class NotificationSaveCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONObject model = jsonObject.getJSONObject("Model");
                            Gson gson = new Gson();
                            NotificationModel notificationModel = gson.fromJson(model.toString(), NotificationModel.class);
                            findFirebase(notificationModel);

                        } else {
                            hideShowProgress.hideProgress();
                            String error = jsonObject.getString("Message");
                            Log.e("status", "error " + error);
                            AppConstants.showErroDIalog(error, activity.getSupportFragmentManager());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            hideShowProgress.hideProgress();
            AppConstants.showErroDIalog(context.getResources().getString(R.string.server_unreachable_error), activity.getSupportFragmentManager());
        }
    }

    private void saveChat(NotificationModel notificationModel) {
        ChatModel chatModel = new ChatModel();
        chatModel.setUser1Id(notificationModel.getSender_Id());
        chatModel.setUser1Name(notificationModel.getSenderName());
        chatModel.setUser2Id(notificationModel.getUser_Id());
        chatModel.setUser2Name(notificationModel.getUserName());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        chatModel.setLastMessage("");
        chatModel.setLastMessageDateTime(formattedDate);
        databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_CHAT_TABLE);
        String id = databaseReference.push().getKey();
        chatModel.setChatId(id);
        databaseReference.child(id).setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                DatabaseReference databaseReferenceMessage = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_MESSAGE_TABLE);
//                String messageId = databaseReferenceMessage.push().getKey();
//                MessageModel messageModel = new MessageModel();
//                messageModel.setChatId(id);
//                messageModel.setMessageTxt("");
//                messageModel.setSenderId(notificationModel.getSender_Id());
//                messageModel.setReceiverId(notificationModel.getUser_Id());
//                messageModel.setItemId(notificationModel.getItem_Id());
//                Calendar c = Calendar.getInstance();
//                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//                String formattedDate = df.format(c.getTime());
//                messageModel.setDateTime(formattedDate);
//                addMessageToFirebase(messageModel,messageId,databaseReferenceMessage);
//                hideShowProgress.hideProgress();
            }
        });
        //Controller.getInstance(context).saveChat(chatModel, new SaveChatCallBack(notificationModel));
    }

    private class SaveChatCallBack implements Callback<ResponseBody> {
        NotificationModel notificationModel;

        public SaveChatCallBack(NotificationModel notificationModel) {
            this.notificationModel = notificationModel;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONObject model = jsonObject.getJSONObject("Model");
                            Gson gson = new Gson();
                            ChatModel chatModel = gson.fromJson(model.toString(), ChatModel.class);
                            databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_CHAT_TABLE);
                            String id = databaseReference.push().getKey();
                            databaseReference.child(id).setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DatabaseReference databaseReferenceMessage = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_MESSAGE_TABLE);
                                    String messageId = databaseReferenceMessage.push().getKey();
                                    MessageModel messageModel = new MessageModel();
                                    messageModel.setChatId(id);
                                    messageModel.setMessageTxt("");
                                    messageModel.setSenderId(notificationModel.getSender_Id());
                                    messageModel.setReceiverId(notificationModel.getUser_Id());
                                    messageModel.setItemId(notificationModel.getItem_Id());
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                    String formattedDate = df.format(c.getTime());
                                    messageModel.setDateTime(formattedDate);
                                    addMessageToFirebase(messageModel,messageId,databaseReferenceMessage);
                                }
                            });

                        } else {
                            hideShowProgress.hideProgress();
                            String error = jsonObject.getString("Message");
                            Log.e("status", "error " + error);
                            AppConstants.showErroDIalog(error, activity.getSupportFragmentManager());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            hideShowProgress.hideProgress();
            AppConstants.showErroDIalog(context.getResources().getString(R.string.server_unreachable_error), activity.getSupportFragmentManager());
        }
    }

    private void findFirebase(NotificationModel notificationModel) {

        DatabaseReference databaseReferenceMessage = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_MESSAGE_TABLE);
        String messageId = databaseReferenceMessage.push().getKey();
        MessageModel messageModel = new MessageModel();
        messageModel.setMessageTxt("");
        messageModel.setMessageId(messageId);
        messageModel.setSenderId(notificationModel.getSender_Id());
        messageModel.setReceiverId(notificationModel.getUser_Id());
        messageModel.setItemId(notificationModel.getItem_Id());
        messageModel.setRead(false);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        messageModel.setDateTime(formattedDate);
        databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_CHAT_TABLE);
        databaseReference.orderByChild("user1Id").equalTo(notificationModel.getSender_Id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ChatModel> chatModels = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    chatModels.add(ds.getValue(ChatModel.class));
                }
                if (chatModels.size() > 0) {
                    for (ChatModel temp : chatModels) {
                        if (temp.getUser2Id() == notificationModel.getUser_Id()) {
                            messageModel.setChatId(temp.getChatId() + "");
                            addMessageToFirebase(messageModel, messageId, databaseReferenceMessage);
                            break;
                        }
                    }
                } else {
                    databaseReference.orderByChild("user2Id").equalTo(notificationModel.getSender_Id()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                chatModels.add(ds.getValue(ChatModel.class));
                            }
                            if (chatModels.size() > 0) {
                                for (ChatModel temp : chatModels) {
                                    if (temp.getUser1Id() == notificationModel.getUser_Id()) {
                                        messageModel.setChatId(temp.getChatId() + "");
                                        addMessageToFirebase(messageModel, messageId, databaseReferenceMessage);
                                        break;
                                    }
                                }
                            } else {
                                saveChat(notificationModel);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void addMessageToFirebase(MessageModel messageModel, String messageId, DatabaseReference databaseReferenceMessage) {
        databaseReferenceMessage.child(messageId).setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideShowProgress.hideProgress();
                SucessDialogFragment sucessDialogFragment = new SucessDialogFragment();
                sucessDialogFragment.show(activity.getSupportFragmentManager(), "");
                Log.e("status", "success");
            }
        });
    }

}
