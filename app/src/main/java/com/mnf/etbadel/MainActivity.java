package com.mnf.etbadel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.utils.BadgeStyle;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mnf.etbadel.ui.dashboard.DashboardFragment;
import com.mnf.etbadel.ui.notifications.NotificationsFragment;
import com.mnf.etbadel.ui.profile.ProfileFragment;
import com.mnf.etbadel.ui.profile.ProfileSenderFragment;
import com.mnf.etbadel.util.AppConstants;
import com.mnf.etbadel.util.ReplaceFragmentInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

import ru.nikartm.support.ImageBadgeView;

import static com.mnf.etbadel.util.AppConstants.FRAGMENT_ADDPRODUCT;
import static com.mnf.etbadel.util.AppConstants.FRAGMENT_NOTIFICATION_LIST;

public class MainActivity extends AppCompatActivity implements ReplaceFragmentInterface {

    Drawable drawableNotification;
    Drawable drawableChat;
    Menu menu;
    private Drawer result = null;
    Toolbar toolbar;
    BadgeStyle style;
    ImageBadgeView ibvIconNotification;
//    DrawerHeaderView drawerHeaderView;

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
        setSupportActionBar(toolbar);
        setNavigationView(toolbar, savedInstanceState);
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
        primaryDrawerItems.add(new PrimaryDrawerItem().withSelectable(true).withName(menus[0]).withIcon(R.drawable.ic_home_black_24dp).withIdentifier(AppConstants.FRAGMENT_DASHBOARD));
        primaryDrawerItems.add(new SwitchDrawerItem().withName(menus[1]).withIcon(R.drawable.ic_notifications_black_24dp).withIdentifier(AppConstants.FRAGMENT_NOTIFICATION));
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[2]).withIcon(R.drawable.ic_loud_speaker).withIdentifier(AppConstants.FRAGMENT_ADS));
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[3]).withIcon(R.drawable.ic_baseline_person_24).withIdentifier(AppConstants.FRAGMENT_PROFILE));
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[4]).withIcon(R.drawable.ic_icons8_document).withIdentifier(AppConstants.FRAGMENT_SERVICE_AGREEMENT));
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[5]).withIcon(R.drawable.ic_shield).withIdentifier(AppConstants.FRAGMENT_PRIVACY_AGREEMENT));
//        primaryDrawerItems.add(new DividerDrawerItem());
        primaryDrawerItems.add(new PrimaryDrawerItem().withName(menus[6]).withIcon(R.drawable.ic_logout).withIdentifier(AppConstants.FRAGMENT_LOGOUT));

        drawerBuilder.withDrawerItems(primaryDrawerItems);
        result = drawerBuilder.build();
//        result.setGravity(View.LAYOUT_DIRECTION_RTL);
        result.getRecyclerView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);


//        new RecyclerViewCacheUtil<IDrawerItem>().withCacheSize(2).apply(result.getRecyclerView(), result.getDrawerItems());
        result.setSelection(1, false);
//        result.getDrawerLayout().getChildAt(0).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

//        result.setSelectionAtPosition(0);
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
                case AppConstants.FRAGMENT_DASHBOARD:
                    fragment = new DashboardFragment();
                    fragmentTag = "dashboardFragment";
//                title.setText(G7Constants.DASHBOARD_TITLE);
                    break;
            case AppConstants.FRAGMENT_PROFILE:
                fragment = new ProfileFragment();
                fragmentTag = "profileFragment";
                break;

            }
        }else {
            if(index==AppConstants.FRAGMENT_NOTIFICATION_LIST){
                fragment=new NotificationsFragment();
               fragmentTag="notification list";
            }

            if(index==AppConstants.FRAGMENT_ADDPRODUCT){
                fragment=new ProfileSenderFragment();
               fragmentTag="notification list";
            }
        }
        if (fragment != null) {
            fragmentTransaction.replace(R.id.frame_container, fragment, fragmentTag);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void performCLick() {
        setFragmentToDisplay(FRAGMENT_ADDPRODUCT,null,true);
    }
}