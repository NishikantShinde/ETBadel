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
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.ui.NavigationInterface;
import com.mnf.etbadel.ui.ads.adapter.AdsAdapter;
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
    ArrayList<ChatModel> chatModels= new ArrayList<>();
    ChatAdapter chatAdapter;
    NavigationInterface navigationInterface;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    public ChatFragment(NavigationInterface navigationInterface, HideShowProgressView hideShowProgressView) {
        this.navigationInterface=navigationInterface;
        this.hideShowProgressView = hideShowProgressView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this,view);
        chatAdapter = new ChatAdapter(getContext(), chatModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);
        sharedPreferences= getContext().getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        fetchChats();
        return view;
    }

    private void fetchChats() {
        int userId = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
        databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_CHAT_TABLE);
        databaseReference.orderByChild("user1Id").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ChatModel> chatModels = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    chatModels.add(ds.getValue(ChatModel.class));
                }
                databaseReference.orderByChild("user2Id").equalTo(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            chatModels.add(ds.getValue(ChatModel.class));
                        }
                        chatAdapter.updateList(chatModels);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}