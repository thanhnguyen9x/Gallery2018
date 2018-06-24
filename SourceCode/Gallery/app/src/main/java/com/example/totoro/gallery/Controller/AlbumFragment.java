package com.example.totoro.gallery.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoro.gallery.Common.DataProvider;
import com.example.totoro.gallery.Model.Album;
import com.example.totoro.gallery.Model.Photo;
import com.example.totoro.gallery.R;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.Date;

public class AlbumFragment extends Fragment {

    private RecyclerView recyclerView;
    private View view;
    public static AlbumAdapter albumAdapter;

    public AlbumFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_album);
        showAlbums();
        return view;
    }

    public void showAlbums() {
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
//                2,StaggeredGridLayoutManager.VERTICAL);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);
        albumAdapter = new AlbumAdapter(getContext(), DataProvider.albums);
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

}
