package com.mnf.etbadel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mnf.etbadel.controller.Controller;
import com.mnf.etbadel.model.DropdownModel;
import com.mnf.etbadel.model.ItemModel;
import com.mnf.etbadel.ui.NavigationInterface;
import com.mnf.etbadel.ui.additem.AddItemFragment;
import com.mnf.etbadel.ui.ads.AdsFragment;
import com.mnf.etbadel.ui.dashboard.DashboardFragment;
import com.mnf.etbadel.ui.notifications.NotificationsFragment;
import com.mnf.etbadel.ui.profile.MyProfileActivity;
import com.mnf.etbadel.ui.profile.ProductsFragment;
import com.mnf.etbadel.ui.profile.ProfileSenderFragment;
import com.mnf.etbadel.util.AppConstants;
import com.mnf.etbadel.util.ReplaceFragmentInterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nikartm.support.ImageBadgeView;

import static com.mnf.etbadel.util.AppConstants.FRAGMENT_ADD_PRODUCTS;
import static com.mnf.etbadel.util.AppConstants.FRAGMENT_DASHBOARD;
import static com.mnf.etbadel.util.AppConstants.FRAGMENT_PRODUCTS;
import static com.mnf.etbadel.util.AppConstants.FRAGMENT_NOTIFICATION_LIST;
import static com.mnf.etbadel.util.AppConstants.FRAGMENT_SENDER_PROFILE;

public class MainActivity extends AppCompatActivity implements ReplaceFragmentInterface, NavigationInterface {

    Drawable drawableNotification;
    Drawable drawableChat;
    Menu menu;
    private Drawer result = null;
    Toolbar toolbar;
    BadgeStyle style;
    ImageBadgeView ibvIconNotification;
    LinearLayout floatingActionButton;
//    DrawerHeaderView drawerHeaderView;
    private Controller controller;
    private int selectedCategory=0;
    private String searchKeyword="";
    private int senderId=0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        fragment.onActivityResult(requestCode, resultCode, data);
//        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
//            // or get a single image only
//            Image image = ImagePicker.getFirstImageOrNull(data);
////            images.add(position,image);
////            setImageToFragment(position,image);
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main);
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
        floatingActionButton=findViewById(R.id.floating_button);
        setSupportActionBar(toolbar);
        setNavigationView(toolbar, savedInstanceState);
        init();
        drawableNotification = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_notifications_black_24dp, null);
        drawableChat = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home_black_24dp, null);
        style = new BadgeStyle(BadgeStyle.Style.DEFAULT, R.layout.menu_action_item_badge, Color.parseColor("#FE0665"), Color.parseColor("#CC0548"), Color.parseColor("#EEEEEE"));
        ibvIconNotification=findViewById(R.id.ibv_icon_notification);
        ibvIconNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentToDisplay(FRAGMENT_NOTIFICATION_LIST,null,true);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentToDisplay(FRAGMENT_ADD_PRODUCTS,null,true);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void init() {
        int lang=0;
        controller= Controller.getInstance(this);
        controller.getCategoriesDropdown(lang, new CategoriesCallback());
        controller.getItems(searchKeyword, selectedCategory, new ItemsCallback());
    }

    private void setNavigationView(Toolbar toolbar, Bundle savedInstanceState) {
//        drawerHeaderView = new DrawerHeaderView(this);
        String[] menus = getResources().getStringArray(R.array.menuList);
        DrawerBuilder drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withHeaderPadding(true)
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

        List<IDrawerItem> primaryDrawerItems = new ArrayList<>();
//        .withIcon(R.drawable.ic_home_black_24dp)
        primaryDrawerItems.add(new PrimaryDrawerItem().withSelectable(true).withName(menus[0]).withIcon(R.drawable.ic_icon05_home).withIdentifier(FRAGMENT_DASHBOARD));
        primaryDrawerItems.add(new SwitchDrawerItem().withName(menus[1]).withIcon(R.drawable.ic_icon05_notification).withIdentifier(AppConstants.FRAGMENT_NOTIFICATION));
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[2]).withIcon(R.drawable.ic_icon05_my_ads).withIdentifier(AppConstants.FRAGMENT_ADS));
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[3]).withIcon(R.drawable.ic_icon05_my_profile).withIdentifier(AppConstants.FRAGMENT_PROFILE));
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[4]).withIcon(R.drawable.ic_icon05_terms).withIdentifier(AppConstants.FRAGMENT_SERVICE_AGREEMENT));
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[5]).withIcon(R.drawable.ic_icon05_privacy).withIdentifier(AppConstants.FRAGMENT_PRIVACY_AGREEMENT));
//        primaryDrawerItems.add(new DividerDrawerItem());
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[6]).withIcon(R.drawable.ic_icon05_logout).withIdentifier(AppConstants.FRAGMENT_LOGOUT));
        primaryDrawerItems.add(new SwitchDrawerItem().withName(menus[7]).withIdentifier(AppConstants.SWITCH_TO_ARABIC));

        drawerBuilder.withDrawerItems(primaryDrawerItems);
        result = drawerBuilder.build();
//        result.setGravity(View.LAYOUT_DIRECTION_RTL);
//        result.getRecyclerView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);


//        new RecyclerViewCacheUtil<IDrawerItem>().withCacheSize(2).apply(result.getRecyclerView(), result.getDrawerItems());
        result.setSelection(1, false);
//        result.getDrawerLayout().getChildAt(0).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        result.setSelectionAtPosition(0);
//        if (savedInstanceState == null) {
//            // set the selection to the item with the identifier 11
//            result.setSelection(1, false);
////            setFragmentToDisplay(1, null, false);
//
//            //set the active profile
////            headerResult.setActiveProfile(profile3);
//        }
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
        String fragmentTag = "";
        if(!isBottomMenu) {
            switch (index) {
                case FRAGMENT_DASHBOARD:
                    fragment = new DashboardFragment();
                    fragmentTag = "dashboardFragment";
//                title.setText(G7Constants.DASHBOARD_TITLE);
                    break;
            case AppConstants.FRAGMENT_PROFILE:
                Intent i= new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(i);
                break;
            case AppConstants.FRAGMENT_SENDER_PROFILE:
                fragment = new ProductsFragment(senderId);
//                fragment = new ProductsFragment();
                fragmentTag = "profileFragment";
                break;
            case AppConstants.FRAGMENT_PRIVACY_AGREEMENT:
//                Intent i= new Intent(MainActivity.this, MyProfileActivity.class);
//                startActivity(i);
                break;

            case AppConstants.FRAGMENT_ADS:
                fragment = new AdsFragment();
                fragmentTag = "profileFragment";
                    break;

            }
        }else {
            if(index==AppConstants.FRAGMENT_NOTIFICATION_LIST){
                fragment=new NotificationsFragment(this);
               fragmentTag="notification list";
            }

            if(index==AppConstants.FRAGMENT_PRODUCTS){
                fragment=new ProfileSenderFragment();
               fragmentTag="profile sender frahment";
            }

            if(index==AppConstants.FRAGMENT_ADD_PRODUCTS){
                fragment=new AddItemFragment(this,0);
               fragmentTag="Add Item fragment";
            }
        }
        if (fragment != null) {
            fragmentTransaction.replace(R.id.frame_container, fragment, fragmentTag);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void performCLick() {
        setFragmentToDisplay(FRAGMENT_PRODUCTS,null,true);
    }

    @Override
    public void NavigateFragment(int i) {
        if (i==0){
            setFragmentToDisplay(FRAGMENT_DASHBOARD,null,false);
        }else {
            senderId = i;
            setFragmentToDisplay(FRAGMENT_SENDER_PROFILE,null,false);
        }
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
                            List<DropdownModel> dropdownModelList = gson.fromJson(model.toString(), new TypeToken<List<DropdownModel>>(){}.getType());
                        }else {
                            String error= jsonObject.getString("Message");
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
                        }else {
                            String error= jsonObject.getString("Message");
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