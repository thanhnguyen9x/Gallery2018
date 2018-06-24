package com.example.totoro.gallery.Controller;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.totoro.gallery.Common.DataProvider;
import com.example.totoro.gallery.Model.TimeLine;
import com.example.totoro.gallery.R;

import java.util.List;

public class AlbumActivity extends AppCompatActivity {
    private FrameLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    RecyclerView recyclerView;
    String albumName;
    TextView toolbar_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_album);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        TextView tvTitleAlbum = (TextView)findViewById(R.id.title_album);

        Intent intent = getIntent();
        albumName = intent.getStringExtra("ALBUM_NAME");
        tvTitleAlbum.setText(albumName);

        List<TimeLine> timeLines = (List<TimeLine>) intent.getSerializableExtra("TIMELINE");
        recyclerView = findViewById(R.id.recyclerView_timeline_of_album);
        showAlbums(timeLines);
    }

    public void showAlbums(List<TimeLine> timeLines) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(new TimeLineAlbumAdapter(timeLines, this));
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.slide_show_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_slide_show) {
            Bundle bundle = new Bundle();
            bundle.putString("PHOTOS", albumName);
            SlideShowFragment slideShowFragment = new SlideShowFragment();
            slideShowFragment.setArguments(bundle);

            android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.drawer_layout_album, slideShowFragment, "slide show");
            fragmentTransaction.addToBackStack("SlideShowFragment");
            fragmentTransaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
