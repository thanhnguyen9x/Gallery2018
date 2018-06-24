package com.example.totoro.gallery.Common;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.ArraySet;
import android.util.Log;

import com.example.totoro.gallery.Model.Album;
import com.example.totoro.gallery.Model.Photo;
import com.example.totoro.gallery.Model.TimeLine;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Totoro on 3/11/2018.
 */

public class DataProvider {

    public static List<Photo> photos;
    public static Set<String> albumNames;
    public static List<Album> albums;
    public static Set<Date> times;
    public static List<TimeLine> timeLines;
    private Context context;

    public DataProvider(Context context) {
        this.context = context;
    }

    public List<Photo> getPhotos() throws IOException {
        photos = new ArrayList<>();
        Cursor cursor = context.getContentResolver()
                .query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        MediaStore.Images.Media.DEFAULT_SORT_ORDER);

        cursor.moveToFirst();
        Photo photo = new Photo();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            File fileImage = new File(path);
            Date date = new Date(fileImage.lastModified());
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            String album = new File(path).getParentFile().getName();
            Long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
            int width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
            int height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
            ExifInterface exifInterface = new ExifInterface(path);
            float[] latLng = new float[2];
            if (exifInterface.getLatLong(latLng)) {
                photo = new Photo(id, date, path, name, size, width, height, album, latLng[0], latLng[1]);
            } else {
                photo = new Photo(id, date, path, name, size, width, height, album, null, null);
            }
            photos.add(photo);
            cursor.moveToNext();
        }
        cursor.close();
        Log.i("tab", "size: " + photos.size());
        return photos;
    }


    public List<Album> getAlbums() throws IOException {
        Set<String> albumNames = getAlbumName();
        albums = new ArrayList<>();
        for (String name : albumNames) {
            albums.add(new Album(name));
        }
        List<Photo> photosOfAlbum;
        for (Album album : albums) {
            photosOfAlbum = new ArrayList<>();
            for (Photo pt : photos) {
                if (pt.getAlbum().equals(album.getName())) {
                    photosOfAlbum.add(pt);
                }
            }
            album.setPhotos(photosOfAlbum);
            album.setBackground(photosOfAlbum.get(0));
        }
        return albums;
    }

    public List<TimeLine> getTimeLines() throws IOException {
        Set<Date> dates = getTimes();
        Object[] dates1 = dates.toArray();
        timeLines = new ArrayList<>();
        for (int i = dates1.length - 1; i >= 0; i--) {
            timeLines.add(new TimeLine((Date) dates1[i]));
        }
        List<Photo> photosOfTimeLine = new ArrayList<>();
        for (TimeLine timeLine : timeLines) {
            for (Photo photo : photos) {
                if ((timeLine.getTime().getDate() == photo.getDate().getDate())
                        && (timeLine.getTime().getMonth() == photo.getDate().getMonth())
                        && (timeLine.getTime().getYear() == photo.getDate().getYear())) {
                    photosOfTimeLine.add(photo);
                }
            }
            timeLine.setPhotos(photosOfTimeLine);
            photosOfTimeLine = new ArrayList<>();
        }
        return timeLines;
    }

    public List<TimeLine> getTimeLines(Album album) throws IOException {
        Set<Date> dates = getTimes(album);
        Object[] dates1 = dates.toArray();
        List<TimeLine> timeLines = new ArrayList<>();
        for (int i = dates1.length - 1; i >= 0; i--) {
            timeLines.add(new TimeLine((Date) dates1[i]));
        }
        List<Photo> photosOfTimeLine;
        for (TimeLine timeLine : timeLines) {
            photosOfTimeLine = new ArrayList<>();
            for (Photo photo : album.getPhotos()) {
                if (timeLine.getTime().getDate() == photo.getDate().getDate()
                        && timeLine.getTime().getMonth() == photo.getDate().getMonth()
                        && timeLine.getTime().getYear() == photo.getDate().getYear()) {
                    photosOfTimeLine.add(photo);
                }
            }
            timeLine.setPhotos(photosOfTimeLine);
        }
        return timeLines;
    }

    private Set<String> getAlbumName() {
        albumNames = new TreeSet<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            File fileImage = new File(path);
            String albumName = fileImage.getParentFile().getName();
            albumNames.add(albumName);
            cursor.moveToNext();
        }
        cursor.close();
        return albumNames;
    }

    private Set<Date> getTimes() throws IOException {
        times = new TreeSet<>();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date todayWithZeroTime = new Date();
        for (Photo p : photos) {
            Date time = p.getDate();
            try {
                todayWithZeroTime = formatter.parse(formatter.format(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.i("time", "getTimes: " + todayWithZeroTime.toString());
            times.add(todayWithZeroTime);
        }
        return times;
    }

    private Set<Date> getTimes(Album album) {
        Set<Date> times = new TreeSet<>();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date todayWithZeroTime = new Date();
        for (Photo photo : album.getPhotos()) {
            if (photo != null) {
                String path = photo.getPath();
                File fileImage = new File(path);
                Date time = new Date(fileImage.lastModified());
                try {
                    todayWithZeroTime = formatter.parse(formatter.format(time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                times.add(todayWithZeroTime);
            }
        }
        return times;
    }
}
