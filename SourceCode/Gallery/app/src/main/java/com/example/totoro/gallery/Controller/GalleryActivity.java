package com.example.totoro.gallery.Controller;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.totoro.gallery.Common.DataProvider;
import com.example.totoro.gallery.R;

import java.io.IOException;

public class GalleryActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView toolbar_title;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private NavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        try {
            if (initPermission()) {
                MapView();
                Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(myToolbar);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setElevation(0);

                toggle = new ActionBarDrawerToggle(
                        this, drawerLayout, R.string.open, R.string.close);
                drawerLayout.addDrawerListener(toggle);
                toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
                toggle.syncState();

                TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager());

                adapter.addFragment(new AlbumFragment(), "Album");
                adapter.addFragment(new ExploreFragment(), "Explore");
                adapter.addFragment(new MapPhotosFragment(), "Map");
                viewPager.setAdapter(adapter);

                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                tabLayout.getTabAt(1).select();


                /*------------ getData form device.------------*/

                DataProvider dataProvider = new DataProvider(this);
                try {
                    dataProvider.getPhotos();
                    dataProvider.getAlbums();
                    dataProvider.getTimeLines();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.about_id:
                                Log.i("click nav", "about");
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.add(R.id.main_container, new AboutFragment(), "AboutFragment");
                                fragmentTransaction.addToBackStack("AboutFragment");
                                fragmentTransaction.commit();
                                break;
                        }
                        drawerLayout.closeDrawer(Gravity.START);
                        return false;
                    }
                });
            }
        } catch (Exception e) {
            Log.i("permission", "permission");
        }

    }

    public void MapView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation_drawer_top);
        toolbar_title = findViewById(R.id.title_app);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
}
