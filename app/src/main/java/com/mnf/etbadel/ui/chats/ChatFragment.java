package com.mnf.etbadel.ui.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mnf.etbadel.R;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.ui.NavigationInterface;
import com.mnf.etbadel.ui.ads.adapter.AdsAdapter;
import com.mnf.etbadel.ui.chats.adapters.ChatAdapter;
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
    ArrayList<ItemModel> itemModelList= new ArrayList<>();
    ChatAdapter chatAdapter;
    NavigationInterface navigationInterface;
    public ChatFragment(NavigationInterface navigationInterface, HideShowProgressView hideShowProgressView) {
        this.navigationInterface=navigationInterface;
        this.hideShowProgressView = hideShowProgressView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this,view);
        chatAdapter = new ChatAdapter(getContext(), itemModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);
        return view;
    }
}