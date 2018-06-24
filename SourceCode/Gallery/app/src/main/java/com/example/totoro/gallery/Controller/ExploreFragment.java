package com.example.totoro.gallery.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.totoro.gallery.Common.DataProvider;
import com.example.totoro.gallery.R;

public class ExploreFragment extends Fragment {

    private RecyclerView recyclerView;
    public static   TimeLineAdapter timeLineAdapter;
    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        showPhotos();
        return view;
    }

    public void showPhotos() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        timeLineAdapter = new TimeLineAdapter(DataProvider.timeLines,getContext());
        recyclerView.setAdapter(timeLineAdapter);


    }
}
