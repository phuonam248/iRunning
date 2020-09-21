package com.appsnipp.homedesign2.Entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class News implements Serializable {
    private String id;

    private String uri;

    private String date;

    private String title;

    private String author;

    private String shortDes;

    private String authorPhotoId;

    private String backgroundPhotoId;

    public News(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShortDes() {
        return shortDes;
    }

    public void setShortDes(String shortDes) {
        this.shortDes = shortDes;
    }

    public String getAuthorPhotoId() {
        return authorPhotoId;
    }

    public void setAuthorPhotoId(String authorPhotoId) {
        this.authorPhotoId = authorPhotoId;
    }

    public String getBackgroundPhotoId() {
        return backgroundPhotoId;
    }

    public void setBackgroundPhotoId(String backgroundPhotoId) {
        this.backgroundPhotoId = backgroundPhotoId;
    }

    public News(String date, String title, String author, String shortDes, String authorPhotoId, String backgroundPhotoId, String uri) {
        this.uri = uri;
        this.date = date;
        this.title = title;
        this.author = author;
        this.shortDes = shortDes;
        this.authorPhotoId = authorPhotoId;
        this.backgroundPhotoId = backgroundPhotoId;
    }
    public News(String id, String date, String title, String author, String shortDes, String authorPhotoId, String backgroundPhotoId, String uri) {
        this.id = id;
        this.uri = uri;
        this.date = date;
        this.title = title;
        this.author = author;
        this.shortDes = shortDes;
        this.authorPhotoId = authorPhotoId;
        this.backgroundPhotoId = backgroundPhotoId;
    }

    @Override
    public String toString() {
        return "News{" +
                "uri='" + uri + '\'' +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", shortDes='" + shortDes + '\'' +
                ", authorPhotoId='" + authorPhotoId + '\'' +
                ", backgroundPhotoId='" + backgroundPhotoId + '\'' +
                '}';
    }
}