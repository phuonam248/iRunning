package com.appsnipp.homedesign2.Entity;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.Date;
@IgnoreExtraProperties
public class History {

    @PropertyName("_userId")
    String _userId;

    @PropertyName("_date")
    String _date;
    @PropertyName("_duration")
    String _duration;
    @PropertyName("_distance")
    String _distance;
    @PropertyName("_calories")
    String _calories;
    @PropertyName("_score")
    long _score;

    public History(String _userId, String _date, String _duration, String _distance, String _calories, long _score) {
        this._userId = _userId;
        this._date = _date;
        this._duration = _duration;
        this._distance = _distance;
        this._calories = _calories;
        this._score = _score;
    }

    public History(String date, String duration, String distance, String calories, long score) {
        this._date = date;
        this._duration = duration;
        this._distance = distance;
        this._calories = calories;
        this._score = score;
    }

    public String get_userId() {
        return _userId;
    }

    public void set_userId(String _userId) {
        this._userId = _userId;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_duration() {
        return _duration;
    }

    public void set_duration(String _duration) {
        this._duration = _duration;
    }

    public String get_distance() {
        return _distance;
    }

    public void set_distance(String _distance) {
        this._distance = _distance;
    }

    public String get_calories() {
        return _calories;
    }

    public void set_calories(String _calories) {
        this._calories = _calories;
    }

    public long get_score() {
        return _score;
    }

    public void set_score(long _score) {
        this._score = _score;
    }

    @Override
    public String toString() {
        return "History{" +
                "_userId='" + _userId + '\'' +
                ", _date='" + _date + '\'' +
                ", _duration='" + _duration + '\'' +
                ", _distance='" + _distance + '\'' +
                ", _calories='" + _calories + '\'' +
                ", _score=" + _score +
                '}';
    }

    public String getDate() {
        return _date;
    }

    public String getDuration() {
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
