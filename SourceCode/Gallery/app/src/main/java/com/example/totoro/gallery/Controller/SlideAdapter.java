package com.example.totoro.gallery.Controller;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.totoro.gallery.Model.Photo;
import com.example.totoro.gallery.R;
import com.example.totoro.gallery.Common.Unit;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.List;

/**
 * Created by Totoro on 2/22/2018.
 */

public class SlideAdapter extends PagerAdapter {
    private List<Photo> photos;
    private int position;
    LayoutInflater inflater;
    Context context;
    public static boolean flag = true;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    PhotoView photoDetailView;
    FrameLayout layout;
    Animation.AnimationListener listener;

    public SlideAdapter(Context context, List<Photo> photos, int position) {
        this.photos = photos;
        this.position = position;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_photo_detail, container, false);
        photoDetailView = view.findViewById(R.id.photo_Detail_View);
        layout = view.findViewById(R.id.frame_container_id);

        listener = new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                System.out.println("End Animation!");
                //load_animations();
            }
        };

        this.position = position;
        final Photo photo = photos.get(position);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final int pxWidth = displayMetrics.widthPixels;
        final int pxHeight = displayMetrics.heightPixels;

        Glide.with(this.context).
                load(photo.getPath())
                .into(photoDetailView);

        container.addView(view);
        photoDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, photo.getName(), Toast.LENGTH_SHORT).show();
                //setAsWallpaper(Photo.decodeFile(photo.getPath(),pxWidth,pxHeight));
                handlePhoto();
            }
        });

        photoDetailView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        return view;
    }

    private void handlePhoto() {
        if (flag == true) {
            Unit.showToolbar((Activity) context, PhotoDetailActivity.navigationBottom, PhotoDetailActivity.toolbar);
            flag = false;
        } else {
            Unit.hideToolbar((Activity) context, PhotoDetailActivity.navigationBottom, PhotoDetailActivity.toolbar);
            flag = true;
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setAsWallpaper(Bitmap bitmap) {
        try {
            WallpaperManager wm = WallpaperManager.getInstance(context);
            wm.setBitmap(bitmap);
            Toast.makeText(context,
                    context.getString(R.string.toast_wallpaper_set),
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,
                    context.getString(R.string.toast_wallpaper_set_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void deletePhoto(int id) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                new File(photos.get(id).getPath()).getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");
        int isDelete = contentResolver.delete(filesUri, where, selectionArgs);

        if (isDelete > 0) {
            if (PhotoDetailActivity.explore.equals("EXPLORE")) {
                try {
                    ExploreFragment.timeLineAdapter.deletePhoto(photos.get(id), photos.get(id).getDate());

                    photos.remove(photos.get(id));
                    notifyDataSetChanged();
                } catch (Exception e) {
                    Log.i("remove", "deletePhoto");
                }
            }
        }
    }

    public  void rotatePhoto(){
        Animation rotateAnim = AnimationUtils.loadAnimation(context, R.anim.rorate_animation);
        LayoutAnimationController animController = new LayoutAnimationController(rotateAnim, 0);
        layout.setLayoutAnimation(animController);
    }
}
