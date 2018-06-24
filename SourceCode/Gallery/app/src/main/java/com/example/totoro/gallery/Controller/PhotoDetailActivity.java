package com.example.totoro.gallery.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.totoro.gallery.Common.DataProvider;
import com.example.totoro.gallery.Common.Unit;
import com.example.totoro.gallery.Model.Album;
import com.example.totoro.gallery.Model.Photo;
import com.example.totoro.gallery.Model.TimeLine;
import com.example.totoro.gallery.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoDetailActivity extends AppCompatActivity {

    private ImageButton send;
    private ImageButton rotate;
    private ImageButton delete;
    private Button btn_delete;
    private Button btn_cancel;

    public static LinearLayout navigationBottom;
    public static Toolbar toolbar;
    public static View dialogDelete;
    public static View dialogDeleteBg;
    SlideAdapter slideAdapter = null;

    public static String explore = "";
    private int location = 0;

    private Intent intent;
    private List<TimeLine> timeLines;
    private List<Photo> photos;
    public static String albumName = "";
    private PhotoView photoDetailView;
    Context context;
    ViewPager slide_photo;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        mapView();

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        context = this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Unit.hideToolbar(this, this.navigationBottom, this.toolbar);
        hideDialog(dialogDelete);
        SlideAdapter.flag = true;

        intent = getIntent();
        location = intent.getIntExtra("LOCATION", 0);
        albumName = intent.getStringExtra("ALBUM");
        explore = intent.getStringExtra("EXPLORE");

        timeLines = new ArrayList<>();
        photos = new ArrayList<>();
        try {
            if (explore.equals("EXPLORE")) {
                timeLines = DataProvider.timeLines;
            } else {
                // get album current
                Album albumViewer = new Album();
                for (Album album : DataProvider.albums) {
                    if (album.getName().equals(albumName)) {
                        albumViewer = album;
                        break;
                    }
                }
                timeLines = new DataProvider(this).getTimeLines(albumViewer);
            }
            for (TimeLine timeLine : timeLines) {
                for (Photo photo : timeLine.getPhotos())
                    photos.add(photo);
            }
        } catch (Exception ex) {
            Log.e("Error: ", ex.toString());
        }

        slide_photo = findViewById(R.id.slide_photo);
        slideAdapter = new SlideAdapter(this, photos, location);
        slide_photo.setAdapter(slideAdapter);
        slide_photo.setCurrentItem(location);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(photos.get(location).getPath());
                Uri uri;
                try {
                    if (Build.VERSION.SDK_INT >= 24)
                        uri = shareAPI24(file);
                    else uri = shareAPI23(file);
                } catch (Exception e) {
                    uri = shareAPI23(file);
                }

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(dialogDelete);
                deletePhoto();
            }
        });

        dialogDeleteBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog(dialogDelete);
            }
        });
    }

    public Uri shareAPI24(File file) {
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", file);
        return uri;
    }

    public Uri shareAPI23(File file) {
        Uri uri = Uri.fromFile(file);
        return uri;
    }

    private void deletePhoto() {
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideAdapter.deletePhoto(slide_photo.getCurrentItem());
                hideDialog(dialogDelete);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog(dialogDelete);
            }
        });
    }

    private void mapView() {
        navigationBottom = findViewById(R.id.navigation_bottom);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dialogDelete = findViewById(R.id.dialogDeletePhoto);
        dialogDeleteBg = findViewById(R.id.dialogDeletePhotoBg);

        send = findViewById(R.id.send);
        delete = findViewById(R.id.delete);
        btn_delete = findViewById(R.id.btn_delete);
        btn_cancel = findViewById(R.id.btn_cancel);
        photoDetailView = findViewById(R.id.photo_Detail_View);

    }

    public void showDialog(View dialogDelete) {
        dialogDeleteBg.setVisibility(View.VISIBLE);
        dialogDeleteBg.setClickable(true);
        dialogDelete.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }

    public void hideDialog(View dialogDelete) {

        dialogDelete.animate().withEndAction(new Runnable() {
            @Override
            public void run() {
                dialogDeleteBg.setClickable(false);
                dialogDeleteBg.setVisibility(View.INVISIBLE);
            }
        }).translationY(dialogDelete.getMeasuredHeight() + 100).setInterpolator(new AccelerateInterpolator()).start();
    }
}
