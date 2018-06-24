package com.example.totoro.gallery.Common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.totoro.gallery.R;

public class Unit {


    public static void showToolbar(Activity activity, LinearLayout bottomNavigationView, Toolbar toolbar) {

        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        bottomNavigationView.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }

    public static void hideToolbar(Activity activity, LinearLayout bottomNavigationView, Toolbar toolbar) {
        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight() + 100).setInterpolator(new AccelerateInterpolator()).start();
        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

    }

}
