package com.krealif.beritaku.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class News {
    @PrimaryKey
    @ColumnInfo(name="id")
    @NonNull
    private String id;

    @ColumnInfo(name="title")
    private String title;

    @ColumnInfo(name="body")
    private String body;

    @ColumnInfo(name="author")
    private String author;

    @ColumnInfo(name="published")
    private String published;

    @ColumnInfo(name="ageRating")
    private int ageRating;

    @ColumnInfo(name="category")
    private int category;

    public News() {
    }

    @Ignore
    public News(String title, String body, String author, String published, int ageRating, int category) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.published = published;
        this.ageRating = ageRating;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public int getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(int ageRating) {
        this.ageRating = ageRating;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
