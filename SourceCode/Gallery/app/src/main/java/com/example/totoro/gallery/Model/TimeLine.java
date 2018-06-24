package com.example.totoro.gallery.Model;

import com.example.totoro.gallery.Controller.TimeLineAdapter;
import com.example.totoro.gallery.Controller.TimeLineAlbumAdapter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TimeLine implements Serializable {
    private Date time;
    private List<Photo> photos;
    private TimeLineAdapter.DatePhotoAdapter datePhotoAdapter = null;

    public TimeLine() {
    }

    public TimeLine(Date time) {
        this.time = time;
    }

    public TimeLine(Date time, List<Photo> photos) {
        this.time = time;
        this.photos = photos;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public TimeLineAdapter.DatePhotoAdapter getDatePhotoAdapter() {
        return datePhotoAdapter;
    }

    public void setDatePhotoAdapter(TimeLineAdapter.DatePhotoAdapter datePhotoAdapter) {
        this.datePhotoAdapter = datePhotoAdapter;
    }
}