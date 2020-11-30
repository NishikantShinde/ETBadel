package com.mnf.etbadel.ui.chats;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mnf.etbadel.R;
import com.mnf.etbadel.model.ChatModel;
import com.mnf.etbadel.model.MessageModel;
import com.mnf.etbadel.ui.NavigationInterface;
import com.mnf.etbadel.ui.chats.adapters.ChatAdapter;
import com.mnf.etbadel.util.AppConstants;
import com.mnf.etbadel.util.HideShowProgressView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatFragment extends Fragment {

    HideShowProgressView hideShowProgressView;
    @BindView(R.id.all_messages_txt)
    TextView allMessagesTxt;
    @BindView(R.id.all_messages_layout)
    LinearLayout allMessagesLayout;
    @BindView(R.id.unread_messages_txt)
    TextView unreadMessagesTxt;
    @BindView(R.id.unread_messages_layout)
    LinearLayout unreadMessagesLayout;
    @BindView(R.id.online_users_txt)
    TextView onlineUsersTxt;
    @BindView(R.id.online_users_layout)
    LinearLayout onlineUsersLayout;
    @BindView(R.id.separator)
    View separator;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ArrayList<ChatModel> chatModels = new ArrayList<>();
    ChatAdapter chatAdapter;
    NavigationInterface navigationInterface;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    int userId;
    @BindView(R.id.no_chats_text)
    TextView noChatsText;
    private String selectedTab = "All";

    public ChatFragment(NavigationInterface navigationInterface, HideShowProgressView hideShowProgressView) {
        this.navigationInterface = navigationInterface;
        this.hideShowProgressView = hideShowProgressView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        chatAdapter = new ChatAdapter(getContext(), chatModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);
        sharedPreferences = getContext().getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
        fetchChats();
        allMessagesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allMessagesLayout.setBackground(getResources().getDrawable(R.drawable.rounded_view_primary_color));
                unreadMessagesLayout.setBackground(getResources().getDrawable(R.drawable.rounded_view));
                onlineUsersLayout.setBackground(getResources().getDrawable(R.drawable.rounded_view));
                selectedTab = "All";
                fetchChats();
            }
        });

        unreadMessagesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allMessagesLayout.setBackground(getResources().getDrawable(R.drawable.rounded_view));
                unreadMessagesLayout.setBackground(getResources().getDrawable(R.drawable.rounded_view_primary_color));
                onlineUsersLayout.setBackground(getResources().getDrawable(R.drawable.rounded_view));
                selectedTab = "unread";
                fetchChats();
            }
        });
        onlineUsersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allMessagesLayout.setBackground(getResources().getDrawable(R.drawable.rounded_view));
                unreadMessagesLayout.setBackground(getResources().getDrawable(R.drawable.rounded_view));
                onlineUsersLayout.setBackground(getResources().getDrawable(R.drawable.rounded_view_primary_color));
                selectedTab = "Online";
                fetchChats();
            }
        });
        return view;
    }

    private void fetchChats() {
        hideShowProgressView.showProgress();
        databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_CHAT_TABLE);
        databaseReference.orderByChild("user1Id").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatModels = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    chatModels.add(ds.getValue(ChatModel.class));
                }
                databaseReference.orderByChild("user2Id").equalTo(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<ChatModel> tempChatModels = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            tempChatModels.add(ds.getValue(ChatModel.class));
                        }
                        for (int i = 0; i < tempChatModels.size(); i++) {
                            boolean isPresent = false;
                            for (int j = 0; j < chatModels.size(); j++) {
                                if (tempChatModels.get(i).getChatId().equals(chatModels.get(j).getChatId())) {
                                    isPresent = true;
                                }
                            }
                            if (!isPresent) {
                                chatModels.add(tempChatModels.get(i));
                            }
                        }
                        if (chatModels.size()>0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            noChatsText.setVisibility(View.GONE);
                            if (selectedTab.equals("unread")) {
                                sortToUnread(chatModels);
                            } else {
                                hideShowProgressView.hideProgress();
                                chatAdapter.updateList(chatModels);
                            }
                        }else {
                            recyclerView.setVisibility(View.GONE);
                            noChatsText.setVisibility(View.VISIBLE);
                        }
                        hideShowProgressView.hideProgress();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        hideShowProgressView.hideProgress();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideShowProgressView.hideProgress();
            }
        });

    }

    private void sortToUnread(ArrayList<ChatModel> chatModels) {
        ArrayList<ChatModel> newChatModels = new ArrayList<>();
        for (ChatModel chatModel : chatModels) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_MESSAGE_TABLE);
            databaseReference.orderByChild("chatId").equalTo(chatModel.getChatId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (selectedTab.equals("unread")) {
                        ArrayList<MessageModel> messageModels = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            MessageModel messageModel = snapshot.getValue(MessageModel.class);
                            if (messageModel.getReceiverId() == userId && !messageModel.isRead()) {
                                newChatModels.add(chatModel);
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        hideShowProgressView.hideProgress();
        if (newChatModels.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noChatsText.setVisibility(View.GONE);
            chatAdapter.updateList(newChatModels);
        } else {
            recyclerView.setVisibility(View.GONE);
            noChatsText.setVisibility(View.VISIBLE);
        }
    }
}