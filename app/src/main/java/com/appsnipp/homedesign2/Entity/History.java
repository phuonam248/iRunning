package com.appsnipp.homedesign2.Entity;

import java.util.Date;

public class History {
    String _date;
    String _duration;
    int _score;
    double _distance;
    double _calories;

    public History(String date, String duration, int score, double distance, double calories) {
        this._date = date;
        this._duration = duration;
        this._score = score;
        this._distance = distance;
        this._calories = calories;
    }

    public String getDate() {
        return _date;
    }

    public String getDration() {
        return _duration;
    }

    public int getScore() {
        return _score;
    }

    public double getDistance() {
        return _distance;
    }

    public double getCalories() {
        return _calories;
    }
}
