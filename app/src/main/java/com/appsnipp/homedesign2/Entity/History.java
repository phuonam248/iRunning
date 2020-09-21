package com.appsnipp.homedesign2.Entity;

import java.util.Date;

public class History {
    String _userId;
    String _date;
    String _duration;
    String _distance;
    String _calories;
    long _score;

    public History(String date, String duration, String distance, String calories, long score) {
        this._date = date;
        this._duration = duration;
        this._distance = distance;
        this._calories = calories;
        this._score = score;
    }

    public String getDate() {
        return _date;
    }

    public String getDration() {
        return _duration;
    }

    public String getDistance() {
        return _distance;
    }

    public String getCalories() {
        return _calories;
    }

    public long getSCore() {
        return _score;
    }
}
