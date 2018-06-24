package com.example.totoro.gallery.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.example.totoro.gallery.Common.DataProvider;
import com.example.totoro.gallery.Model.Photo;
import com.example.totoro.gallery.Model.TimeLine;
import com.example.totoro.gallery.R;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


// lớp này là adapter cho explore
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {
    List<TimeLine> timeLines;
    Context context;
    View view;
    TimeLine timeLine;
    DatePhotoAdapter datePhotoAdapter;

    public TimeLineAdapter(List<TimeLine> timeLines, Context context) {
        this.context = context;
        this.timeLines = timeLines;
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

        datePhotoAdapter = new DatePhotoAdapter(timeLine.getPhotos(), timeLine);
        holder.listPhoto.setHasFixedSize(true);
        holder.listPhoto.setItemViewCacheSize(20);
        holder.listPhoto.setDrawingCacheEnabled(true);
        holder.listPhoto.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        holder.listPhoto.setAdapter(datePhotoAdapter);
        holder.listPhoto.setLayoutManager(new GridLayoutManager(context, 4));
        timeLine.setDatePhotoAdapter(datePhotoAdapter);
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

    public void deletePhoto(Photo photo, Date time) {
        for (int i = 0; i < timeLines.size(); i++) {
            if (timeLines.get(i).getTime().getDate() == time.getDate() &&
                    timeLines.get(i).getTime().getMonth() == time.getMonth()
                    && timeLines.get(i).getTime().getYear() == time.getYear()) {
                Log.i("deletePhoto", "timeline size: " + timeLines.size());
                Log.i("deletePhoto", "i:  " + i);
                int count = timeLines.get(i).getPhotos().size();
                timeLines.get(i).getDatePhotoAdapter().removeAt(photo);
            }
        }
    }


    public class DatePhotoAdapter extends RecyclerView.Adapter<DatePhotoAdapter.DatePhotoViewHolder> {
        List<Photo> photos;
        TimeLine timeLine;
        public DatePhotoAdapter(List<Photo> photos, TimeLine timeLine) {
            this.photos = photos;
            this.timeLine = timeLine;
        }

        public DatePhotoAdapter() {
        }

        @NonNull
        @Override
        public DatePhotoAdapter.DatePhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_photo, parent, false);
            return new DatePhotoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final DatePhotoAdapter.DatePhotoViewHolder holder, final int position) {
            viewPhotos(holder, photos, position);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewDetailPhoto(photos.get(position), position);
                    Log.i("p", "p: " + position);
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

        public void removeAt(Photo photo) {
            Log.i("ab", "removeAt: " + photo.getId());
            Log.i("ab", "size: " + photos.size());
            for (int i = 0; i < photos.size(); i++) {
                if (photo.getId() == (photos.get(i).getId())) {
                    Log.i("ab", "truoc: " + photos.size());
                    photos.remove(photo);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, photos.size());
                    Log.i("ab", "sau: " + photos.size());

                }
            }

            if (photos.size() == 0) {
                deleteAt(timeLine);
            }
        }
    }


    public void deleteAt(TimeLine timeLine) {
        for (int i = 0; i < timeLines.size(); i++) {
            if (timeLines.get(i).getTime().getDate() == timeLine.getTime().getDate() &&
                    timeLines.get(i).getTime().getMonth() == timeLine.getTime().getMonth()
                    && timeLines.get(i).getTime().getYear() == timeLine.getTime().getYear()) {

                timeLines.remove(timeLine);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, timeLines.size());
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
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.color.windowBackground)
                        .centerCrop())
                .into(holder.imageView);
    }

    private void viewDetailPhoto(Photo photo, int position) {
        Intent intent = new Intent(context, PhotoDetailActivity.class);
        int tong = 0;
        final Date date = photo.getDate();
        for (int i = 0; i < timeLines.size(); i++) {
            if (timeLines.get(i).getTime().getDay() == (date.getDay())
                    && timeLines.get(i).getTime().getMonth() == date.getMonth()
                    && timeLines.get(i).getTime().getYear() == date.getYear()) {
                break;
            } else {
                tong = tong + timeLines.get(i).getPhotos().size();
            }
        }
        int pos = position + tong;

        intent.putExtra("POSITION", position);
        intent.putExtra("LOCATION", pos);
        intent.putExtra("EXPLORE", "EXPLORE");
        intent.putExtra("DATE", photo.getDate());
        context.startActivity(intent);
    }


}

