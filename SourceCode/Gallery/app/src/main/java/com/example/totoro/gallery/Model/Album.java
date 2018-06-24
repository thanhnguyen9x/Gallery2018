package com.example.totoro.gallery.Model;

import com.example.totoro.gallery.Controller.TimeLineAlbumAdapter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Album implements Serializable {
    private String name;
    private Photo background;
    private List<Photo> photos;

    private TimeLineAlbumAdapter timeLineAlbumAdapter;

    public TimeLineAlbumAdapter getTimeLineAlbumAdapter() {
        return timeLineAlbumAdapter;
    }

    public void setTimeLineAlbumAdapter(TimeLineAlbumAdapter timeLineAlbumAdapter) {
        this.timeLineAlbumAdapter = timeLineAlbumAdapter;
    }

    public Album(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Photo getBackground() {
        return background;
    }

    public void setBackground(Photo background) {
        this.background = background;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Album() {
    }

    public Album(String name, Photo background, List<Photo> photos) {
        this.name = name;
        this.background = background;
        this.photos = photos;
    }

}
