package com.example.totoro.gallery.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.totoro.gallery.Model.Photo;
import com.example.totoro.gallery.Model.TimeLine;
import com.example.totoro.gallery.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimeLineAlbumAdapter extends RecyclerView.Adapter<TimeLineAlbumAdapter.TimeLineViewHolder> {

    List<TimeLine> timeLines;
    Context context;
    View view;
    TimeLine timeLine;
    DatePhotoAdapter datePhotoAlbumAdapter;


    public TimeLineAlbumAdapter(List<TimeLine> timeLines, Context context) {
        this.timeLines = timeLines;
        this.context = context;
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_timeline, parent, false);
        return new TimeLineViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        timeLine = timeLines.get(position);
        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MM yyyy");
        String day = dateFormat.format(timeLine.getTime());
        holder.date.setText(day);

        datePhotoAlbumAdapter = new TimeLineAlbumAdapter.DatePhotoAdapter(timeLine.getPhotos(), timeLine);
        holder.listPhoto.setAdapter(new DatePhotoAdapter(timeLine.getPhotos(), timeLine));
        holder.listPhoto.setLayoutManager(new GridLayoutManager(context, 4));
    }


    @Override
    public int getItemCount() {
        return timeLines.size();
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        RecyclerView listPhoto;

        public TimeLineViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            listPhoto = itemView.findViewById(R.id.list_photo_timeline);
        }
    }

    public class DatePhotoAdapter extends RecyclerView.Adapter<DatePhotoAdapter.DatePhotoViewHolder> {
        List<Photo> photos;
        TimeLine timeLine;

        public DatePhotoAdapter(List<Photo> photos, TimeLine timeLine) {
            this.photos = photos;
            this.timeLine = timeLine;
        }

        @NonNull
        @Override
        public DatePhotoAdapter.DatePhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_photo, parent, false);
            return new DatePhotoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DatePhotoAdapter.DatePhotoViewHolder holder, final int position) {
            viewPhotos(holder, photos, position);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPhoto(photos, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }

        public class DatePhotoViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public DatePhotoViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.photo_date);
            }
        }
    }

    private void viewPhotos(DatePhotoAdapter.DatePhotoViewHolder holder, List<Photo> photos, int position) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        int pxHeight = displayMetrics.heightPixels;

        final Photo photo = photos.get(position);
        Glide.with(context)
                .load(photo.getPath())
                .thumbnail(1f)
                .apply(new RequestOptions()
                        .override(pxWidth / 3, 180)
                        .placeholder(R.color.windowBackground)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop())
                .into(holder.imageView);
    }

    private void viewPhoto(List<Photo> photos, int position) {
        Intent intent = new Intent(context, PhotoDetailActivity.class);
        int index = 0;
        final Date date = photos.get(position).getDate();
        for (int i = 0; i < timeLines.size(); i++) {
            if (timeLines.get(i).getTime().getDay() == (date.getDay())
                    && timeLines.get(i).getTime().getMonth() == date.getMonth()
                    && timeLines.get(i).getTime().getYear() == date.getYear()) {
                break;
            } else {
                index = index + timeLines.get(i).getPhotos().size();
            }
        }
        int pos = position + index;
        intent.putExtra("LOCATION", pos);
        intent.putExtra("ALBUM", photos.get(position).getAlbum());
        intent.putExtra("EXPLORE", "");
        intent.putExtra("DATE", photos.get(position).getDate());
        context.startActivity(intent);
    }

}

