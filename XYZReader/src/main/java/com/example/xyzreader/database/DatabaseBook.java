package com.example.xyzreader.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DatabaseBook {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String author;
    private String body;
    private String thumb;
    private String photo;
    private Float aspect_ratio;
    private String published_date;

    public DatabaseBook(int id, String title, String author, String body, String thumb, String photo, Float aspect_ratio, String published_date) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.body = body;
        this.thumb = thumb;
        this.photo = photo;
        this.aspect_ratio = aspect_ratio;
        this.published_date = published_date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public String getThumb() {
        return thumb;
    }

    public String getPhoto() {
        return photo;
    }

    public Float getAspect_ratio() {
        return aspect_ratio;
    }

    public String getPublished_date() {
        return published_date;
    }
}