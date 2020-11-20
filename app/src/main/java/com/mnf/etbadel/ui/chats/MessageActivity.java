package com.mnf.etbadel.ui.chats;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.mnf.etbadel.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

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