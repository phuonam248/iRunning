package com.appsnipp.homedesign2.Entity;

public class News {
    String _uri;
    String _date;
    String _title;
    String _author;
    String _shortDes; // 20 words only
    int _authorPhotoId;
    int _backgroundPhotoId;

    public News(String date, String title, String author, String shortDes, int authorPhotoId, int backgroundPhotoId, String uri) {
        this._date = date;
        this._title = title;
        this._author = author;
        this._shortDes = shortDes;
        this._authorPhotoId = authorPhotoId;
        this._backgroundPhotoId = backgroundPhotoId;
        this._uri = uri;
    }

    public String getUri() {
        return _uri;
    }

    public String getDate() {
        return _date;
    }

    public String getTitle() {
        return _title;
    }

    public String getAuthor() {
        return _author;
    }

    public String getShortDes() {
        return _shortDes + "...";
    }

    public int getAuthorPhotoId() {
        return _authorPhotoId;
    }

    public int getBackgroundPhotoId() {
        return _backgroundPhotoId;
    }
}