package com.example.totoro.gallery.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.totoro.gallery.Common.DataProvider;
import com.example.totoro.gallery.Model.Album;
import com.example.totoro.gallery.Model.TimeLine;
import com.example.totoro.gallery.R;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private Context context;
    private List<Album> albums;

    public AlbumAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album_layout, parent, false);
        return new AlbumAdapter.AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, final int position) {


        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        int pxHeight = displayMetrics.heightPixels;

        final Album album = albums.get(position);
        holder.albumTitle.setText(album.getName());
        holder.albumSize.setText(String.valueOf(album.getPhotos().size()) + " items");
        try {
            album.setTimeLineAlbumAdapter(new TimeLineAlbumAdapter(new DataProvider(context).getTimeLines(album), context));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Glide.with(context)
                .load(album.getBackground().getPath())
                .apply(new RequestOptions()
                        .override(pxWidth, 260)
                        .placeholder(R.color.windowBackground)
                        .centerCrop())
                .into(holder.albumBackground);

        holder.albumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataProvider dataProvider = new DataProvider(context);
                try {
                    List<TimeLine> timeLines = dataProvider.getTimeLines(album);
                    Intent intent = new Intent(context, AlbumActivity.class);
                    intent.putExtra("TIMELINE", (Serializable) timeLines);
                    intent.putExtra("ALBUM_NAME", album.getName());
                    context.startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        CardView albumLayout;
        ImageView albumBackground;
        TextView albumTitle;
        TextView albumSize;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            albumLayout = itemView.findViewById(R.id.album_layout);
            albumBackground = itemView.findViewById(R.id.album_background);
            albumTitle = itemView.findViewById(R.id.album_title);
            albumSize = itemView.findViewById(R.id.album_size);
        }
    }
}
