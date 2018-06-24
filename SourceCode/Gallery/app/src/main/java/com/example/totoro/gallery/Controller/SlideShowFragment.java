package com.example.totoro.gallery.Controller;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.totoro.gallery.Common.DataProvider;
import com.example.totoro.gallery.Model.Photo;
import com.example.totoro.gallery.R;

import java.util.ArrayList;
import java.util.List;

public class SlideShowFragment extends Fragment {
    private View view;
    private ViewFlipper viewFlipper;
    private int size = 0;
    private List<Photo> photos;
    private Animation in, out;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_slide_show, container, false);
        viewFlipper = view.findViewById(R.id.view_flipper);
        in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_show_fade_in);
        out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_show_fade_out);
        String albumName = getArguments().getString("PHOTOS");
        photos = new ArrayList<>();
        for (int i = 0; i < DataProvider.albums.size(); i++) {
            if (DataProvider.albums.get(i).getName().equals(albumName)) {
                photos = DataProvider.albums.get(i).getPhotos();
                break;
            }
        }
        size = photos.size();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setClickable(false);
            Glide.with(getActivity())
                    .load(photos.get(i).getPath())
                    .apply(new RequestOptions()
                            .override(400, 240)
                            .placeholder(R.color.windowBackground)
                            .centerCrop())
                    .into(imageView);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setClickable(false);
        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        return view;
    }
}
