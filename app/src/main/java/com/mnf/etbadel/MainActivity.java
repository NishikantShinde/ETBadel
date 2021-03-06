package com.mnf.etbadel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.AgreementModel;
import com.mnf.etbadel.model.ChatModel;
import com.mnf.etbadel.model.DropdownModel;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.model.MessageModel;
import com.mnf.etbadel.model.Token;
import com.mnf.etbadel.ui.NavigationInterface;
import com.mnf.etbadel.ui.additem.AddItemFragment;
import com.mnf.etbadel.ui.ads.AdsFragment;
import com.mnf.etbadel.ui.agreement.AgreementFragment;
import com.mnf.etbadel.ui.changelanguage.ChangeLanguage;
import com.mnf.etbadel.ui.chats.ChatFragment;
import com.mnf.etbadel.ui.chats.MessageActivity;
import com.mnf.etbadel.ui.chats.adapters.MessageAdapter;
import com.mnf.etbadel.ui.dashboard.DashboardFragment;
import com.mnf.etbadel.ui.login.LoginActivity;
import com.mnf.etbadel.ui.notifications.NotificationsFragment;
import com.mnf.etbadel.ui.profile.MyProfileActivity;
import com.mnf.etbadel.ui.profile.ProductsFragment;
import com.mnf.etbadel.ui.profile.ProfileSenderFragment;
import com.mnf.etbadel.util.AppConstants;
import com.mnf.etbadel.util.HideShowProgressView;
import com.mnf.etbadel.util.LogoutFragment;
import com.mnf.etbadel.util.ReplaceFragmentInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nikartm.support.ImageBadgeView;

import static com.mnf.etbadel.util.AppConstants.FRAGMENT_ADD_PRODUCTS;
import static com.mnf.etbadel.util.AppConstants.FRAGMENT_CHAT_LIST;
import static com.mnf.etbadel.util.AppConstants.FRAGMENT_DASHBOARD;
import static com.mnf.etbadel.util.AppConstants.FRAGMENT_NOTIFICATION_LIST;
import static com.mnf.etbadel.util.AppConstants.FRAGMENT_PRODUCTS;
import static com.mnf.etbadel.util.AppConstants.FRAGMENT_SENDER_PROFILE;

public class MainActivity extends AppCompatActivity implements ReplaceFragmentInterface, NavigationInterface, HideShowProgressView {

    Drawable drawableNotification;
    Drawable drawableChat;
    Menu menu;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.menu_layout_bottom)
    LinearLayout menuLayoutBottom;
    private Drawer result = null;
    Toolbar toolbar;
    BadgeStyle style;
    ImageBadgeView ibvIconNotification;
    ImageBadgeView ibvIconChat;
    LinearLayout floatingActionButton;
    ImageBadgeView imageBadgeView;
    //    DrawerHeaderView drawerHeaderView;
    private Controller controller;
    private int selectedCategory = 0;
    private String searchKeyword = "";
    private int senderId = 0;
    SharedPreferences sharedPreferences;
    HideShowProgressView hideShowProgressView;
    int itemId = 0;
    private String mLanguageCode = "en";
    private AgreementModel agreementModel;
    PrimaryDrawerItem primaryDrawerItem;
    DrawerBuilder drawerBuilder;
    List<IDrawerItem> primaryDrawerItems;
    int requestCode=1000;
    String fragmentTag = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==this.requestCode){
            primaryDrawerItems.add(primaryDrawerItem);
            drawerBuilder.withDrawerItems(primaryDrawerItems);
            result.setSelection(1, false);
            result.setSelectionAtPosition(0);
//            setFragmentToDisplay(FRAGMENT_DASHBOARD, null, false);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(AppConstants.SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        String lcode= sharedPreferences.getString(AppConstants.LANG,"en");
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(lcode.toLowerCase()));
        } else {
            config.locale = new Locale(lcode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
        setContentView(R.layout.activity_main_main);
        ButterKnife.bind(this);
        hideShowProgressView=this;
//        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home_dashboard, R.id.navigation_dashboard, R.id.navigation_notifications_dashboard)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = findViewById(R.id.floating_button);
        imageBadgeView = findViewById(R.id.ibv_icon_notification);
        setSupportActionBar(toolbar);
        setNavigationView(toolbar,savedInstanceState);

        init();
        drawableNotification = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_notifications_black_24dp, null);
        drawableChat = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home_black_24dp, null);
        style = new BadgeStyle(BadgeStyle.Style.DEFAULT, R.layout.menu_action_item_badge, Color.parseColor("#FE0665"), Color.parseColor("#CC0548"), Color.parseColor("#EEEEEE"));
        imageBadgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentToDisplay(FRAGMENT_NOTIFICATION_LIST, null, true);
            }
        });

        ibvIconChat= findViewById(R.id.ibv_icon_chat);
        ibvIconChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentToDisplay(FRAGMENT_CHAT_LIST, null, true);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
                if (userId != 0) {
                    setFragmentToDisplay(FRAGMENT_ADD_PRODUCTS, null, true);
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent,requestCode);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (fragmentTag.equals("profile sender fragment")){
            setFragmentToDisplay(FRAGMENT_SENDER_PROFILE, null, false);
        }
    }

    private void init() {
        int lang = 0;
        controller = Controller.getInstance(this);
        mLanguageCode=sharedPreferences.getString(AppConstants.LANG,"en");
        assert mLanguageCode != null;
        if (mLanguageCode.equals("ar")){
            lang=1;
        }
//        controller.getCategoriesDropdown(lang, new CategoriesCallback());
//        controller.getItems(searchKeyword, selectedCategory, new ItemsCallback());
        controller.getAgreement(lang, new GetAgreementCallback());
    }

    @Override
    protected void onResume() {
        super.onResume();

        int userId = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
        if (userId != 0) {
            controller.unreadByUser(userId, new UnreadNotificationsCallback());
            DatabaseReference databaseReferenceToken= FirebaseDatabase.getInstance().getReference("Tokens");
            Token tokenl= new Token(FirebaseInstanceId.getInstance().getToken());
            databaseReferenceToken.child(userId+"").setValue(tokenl);
        } else {
            imageBadgeView.clearBadge();
            ibvIconChat.clearBadge();
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_MESSAGE_TABLE);
        databaseReference.orderByChild("receiverId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int newChat=0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MessageModel messageModel=snapshot.getValue(MessageModel.class);
                    if (messageModel!=null){
                        if (!messageModel.isRead()){
                            newChat++;
                        }
                    }
                }
                if (newChat!=0){
                    ibvIconChat.setBadgeValue(newChat);
                }else {
                    ibvIconChat.clearBadge();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setNavigationView(Toolbar toolbar, Bundle savedInstanceState) {
//        drawerHeaderView = new DrawerHeaderView(this);
        String[] menus = getResources().getStringArray(R.array.menuList);
        drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withHeaderPadding(true)
                .withDrawerGravity(Gravity.LEFT)
                .addDrawerItems(

                ) // add the items we want to use with our Drawer

                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            setFragmentToDisplay(Long.valueOf(drawerItem.getIdentifier()).intValue(), null, false);
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(false);

        primaryDrawerItems = new ArrayList<>();
//        .withIcon(R.drawable.ic_home_black_24dp)
        primaryDrawerItems.add(new PrimaryDrawerItem().withSelectable(true).withName(menus[0]).withIcon(R.drawable.ic_icon05_home).withIdentifier(FRAGMENT_DASHBOARD));
        primaryDrawerItems.add(new SwitchDrawerItem().withName(menus[1]).withIcon(R.drawable.ic_icon05_notification).withIdentifier(AppConstants.FRAGMENT_NOTIFICATION));
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[2]).withIcon(R.drawable.ic_icon05_my_ads).withIdentifier(AppConstants.FRAGMENT_ADS));
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[3]).withIcon(R.drawable.ic_icon05_my_profile).withIdentifier(AppConstants.FRAGMENT_PROFILE));
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[4]).withIcon(R.drawable.ic_icon05_terms).withIdentifier(AppConstants.FRAGMENT_SERVICE_AGREEMENT));
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[5]).withIcon(R.drawable.ic_icon05_privacy).withIdentifier(AppConstants.FRAGMENT_PRIVACY_AGREEMENT));
//        primaryDrawerItems.add(new DividerDrawerItem());
        int userId = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);

        mLanguageCode= sharedPreferences.getString(AppConstants.LANG,"en");
        if (mLanguageCode.equals("en")){
            SwitchDrawerItem item=new SwitchDrawerItem().withName(menus[7]).withIdentifier(AppConstants.SWITCH_TO_ARABIC).withChecked(true).withOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        sharedPreferences.edit().putString(AppConstants.LANG,"en").apply();
                    }else {
                        sharedPreferences.edit().putString(AppConstants.LANG,"ar").apply();
                    }
                    Intent intent= new Intent(MainActivity.this, ChangeLanguage.class);
                    startActivity(intent);
                    finish();
                }
            });
            primaryDrawerItems.add(item);
        }else {
            SwitchDrawerItem item=new SwitchDrawerItem().withName(menus[7]).withIdentifier(AppConstants.SWITCH_TO_ARABIC).withChecked(false).withOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        sharedPreferences.edit().putString(AppConstants.LANG,"en").apply();
                    }else {
                        sharedPreferences.edit().putString(AppConstants.LANG,"ar").apply();
                    }
                    Intent intent= new Intent(MainActivity.this, ChangeLanguage.class);
                    startActivity(intent);
                    finish();
                }
            });
            primaryDrawerItems.add(item);
        }
        primaryDrawerItem=new PrimaryDrawerItem().withName(menus[6]).withIcon(R.drawable.ic_icon05_logout).withIdentifier(AppConstants.FRAGMENT_LOGOUT);
        if (userId != 0) {
            primaryDrawerItems.add(primaryDrawerItem);
        }
        drawerBuilder.withDrawerItems(primaryDrawerItems);
        result = drawerBuilder.build();
        result.setSelection(1, false);
        result.setSelectionAtPosition(0);
    }

    private void updateIcon() {

    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
//        this.menu=menu;
//        ActionItemBadge.update(this, menu.findItem(R.id.navigation_notifications_dashboard), drawableNotification, style, 3);
//        ActionItemBadge.update(this, menu.findItem(R.id.navigation_home_dashboard), drawableChat, style, 10);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml
//
//        int id=item.getItemId();
//        if(id==R.id.navigation_notifications_dashboard){
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void setFragmentToDisplay(int index, Bundle data, boolean isBottomMenu) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (!isBottomMenu) {
            switch (index) {
                case FRAGMENT_DASHBOARD:
                    fragment = new DashboardFragment(hideShowProgressView);
                    fragmentTag = "dashboardFragment";
                    menuLayoutBottom.setVisibility(View.VISIBLE);
//                title.setText(G7Constants.DASHBOARD_TITLE);
                    break;
                case AppConstants.FRAGMENT_PROFILE:
                    int userId = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
                    if (userId != 0) {
                        Intent i = new Intent(MainActivity.this, MyProfileActivity.class);
                        startActivity(i);
                    } else {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivityForResult(intent,requestCode);
                    }

                    break;
                case FRAGMENT_SENDER_PROFILE:
                    fragment = new ProductsFragment(senderId,hideShowProgressView);
                    fragmentTag = "profileFragment";
                    menuLayoutBottom.setVisibility(View.VISIBLE);
                    break;
                case AppConstants.FRAGMENT_ADS:
                    int userid = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
                    if (userid != 0) {
                        fragment = new AdsFragment(hideShowProgressView);
                        fragmentTag = "ads fragment";
                        menuLayoutBottom.setVisibility(View.VISIBLE);
                    } else {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivityForResult(intent,requestCode);
                    }

                    break;

                case AppConstants.FRAGMENT_LOGOUT:
                    LogoutFragment alertDialog = new LogoutFragment(this, primaryDrawerItems, primaryDrawerItem, drawerBuilder);
                    alertDialog.show(getSupportFragmentManager(), "fragment_alert");

                    break;
                case AppConstants.FRAGMENT_SERVICE_AGREEMENT:
                    fragment = new AgreementFragment("s",agreementModel);
                    fragmentTag = "service fragment";
                    menuLayoutBottom.setVisibility(View.VISIBLE);
                    break;
                case AppConstants.FRAGMENT_PRIVACY_AGREEMENT:
                    fragment = new AgreementFragment("p",agreementModel);
                    fragmentTag = "privacy fragment";
                    menuLayoutBottom.setVisibility(View.VISIBLE);
                    break;

            }
        } else {
            if (index == FRAGMENT_NOTIFICATION_LIST) {
                int userId = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
                if (userId != 0) {
                    fragment = new NotificationsFragment(this,hideShowProgressView);
                    fragmentTag = "notification list";
                    menuLayoutBottom.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent,requestCode);
                }
            }

            if (index == FRAGMENT_CHAT_LIST) {
                int userId = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
                if (userId != 0) {
                    fragment = new ChatFragment(this,hideShowProgressView);
                    fragmentTag = "chat fragment";
                    menuLayoutBottom.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent,requestCode);
                }
            }

            if (index == FRAGMENT_PRODUCTS) {
                fragment = new ProfileSenderFragment(itemId,hideShowProgressView);
                fragmentTag = "profile sender fragment";
                menuLayoutBottom.setVisibility(View.VISIBLE);
            }

            if (index == FRAGMENT_ADD_PRODUCTS) {
                int userId = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
                if (userId != 0) {
                    fragment = new AddItemFragment(this);
                    fragmentTag = "Add Item fragment";
                    menuLayoutBottom.setVisibility(View.GONE);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent,requestCode);
                }

            }
        }
        if (fragment != null) {
            fragmentTransaction.replace(R.id.frame_container, fragment, fragmentTag);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void performCLick(int i) {
        itemId = i;
        setFragmentToDisplay(FRAGMENT_PRODUCTS, null, true);
    }

    @Override
    public void NavigateFragment(int i) {
        if (i == 0) {
            setFragmentToDisplay(FRAGMENT_DASHBOARD, null, false);
        } else {
            senderId = i;
            setFragmentToDisplay(FRAGMENT_SENDER_PROFILE, null, false);
        }
    }

    @Override
    public void updateNotificationCount() {
        int userId = sharedPreferences.getInt(AppConstants.SF_USER_ID, 0);
        if (userId != 0) {
            controller.unreadByUser(userId, new UnreadNotificationsCallback());
        }
    }

    @Override
    public void showProgress() {
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private class CategoriesCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONArray model = jsonObject.getJSONArray("Model");
                            Gson gson = new Gson();
                            List<DropdownModel> dropdownModelList = gson.fromJson(model.toString(), new TypeToken<List<DropdownModel>>() {
                            }.getType());
                        } else {
                            String error = jsonObject.getString("Message");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    }

    private class ItemsCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        String modelStr = jsonObject.getString("Model");
                        if (!modelStr.equals("null")) {
                            JSONArray model = jsonObject.getJSONArray("Model");
                            Gson gson = new Gson();
                            List<ItemModel> itemModelList = gson.fromJson(model.toString(), new TypeToken<List<ItemModel>>() {
                            }.getType());
                        } else {
                            String error = jsonObject.getString("Message");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    }

    private class UnreadNotificationsCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {

                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        int modelStr = jsonObject.getInt("Model");
                        if (modelStr != 0) {
                            imageBadgeView.setBadgeValue(modelStr);
                        } else {
                            imageBadgeView.clearBadge();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            imageBadgeView.clearBadge();
        }
    }

    private class GetAgreementCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = AppConstants.getJsonResponseFromRaw(response);
                        assert jsonObject != null;
                        if (jsonObject.getBoolean("Success")){
                            JSONArray model= jsonObject.getJSONArray("Model");
                            Gson gson = new Gson();
                            List<AgreementModel> agreementModels = gson.fromJson(model.toString(), new TypeToken<List<AgreementModel>>() {
                            }.getType());
                            agreementModel= agreementModels.get(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    }
}