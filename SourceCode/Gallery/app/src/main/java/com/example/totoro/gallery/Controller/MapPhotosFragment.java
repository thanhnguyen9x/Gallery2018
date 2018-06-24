package com.example.totoro.gallery.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.totoro.gallery.Common.DataProvider;
import com.example.totoro.gallery.Model.Photo;
import com.example.totoro.gallery.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapPhotosFragment extends Fragment implements OnMapReadyCallback {

    MapView mapView;
    View view;
    private HashMap<Marker, Photo> mMarkersHashMap;

    public MapPhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map_photos, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMarkersHashMap = new HashMap<>();
        for (Photo photo : DataProvider.photos) {
            if (photo.getLat() != null && photo.getLng() != null) {
                LatLng latLng = new LatLng(photo.getLat(), photo.getLng());
                Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng));
//                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker3));
                mMarkersHashMap.put(marker, photo);
                googleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(10.762625, 106.683069))
//                .title("University Of Science").snippet("I'm going to School !!!"));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(new LatLng(10.762625, 106.683069))
                .zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.762625, 106.683069), 6));

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.showInfoWindow();
            }
        });
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public MarkerInfoWindowAdapter() {
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View view = getLayoutInflater().inflate(R.layout.item_photo_map, null);
            Photo photo = mMarkersHashMap.get(marker);
            TextView markerLabel = (TextView) view.findViewById(R.id.markerLabel);
            ImageView markerIcon = (ImageView) view.findViewById(R.id.markerIcon);
            //markerLabel.setText(photo.getAlbum());
            Glide.with(getContext())
                    .load(photo.getPath())
                    .apply(new RequestOptions()
                            .override(240, 240)
                            .placeholder(R.color.windowBackground)
                            .centerCrop())
                    .into(markerIcon);
            return view;
        }
    }
}
