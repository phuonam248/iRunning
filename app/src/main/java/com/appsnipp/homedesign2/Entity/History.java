package com.appsnipp.homedesign2.Entity;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.Date;
@IgnoreExtraProperties
public class History {

    @PropertyName("userId")
    String userId;
    @PropertyName("date")
    String date;
    @PropertyName("duration")
    String duration;
    @PropertyName("distance")
    String distance;
    @PropertyName("calories")
    String calories;
    @PropertyName("score")
    long score;


    public History(String _userId, String _date, String _duration, String _distance, String _calories, long _score) {
        this.userId = _userId;
        this.date = _date;
        this.duration = _duration;
        this.distance = _distance;
        this.calories = _calories;
        this.score = _score;
    }

    public History() {
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getDuration() {
        return duration;
    }

    public String getDistance() {
        return distance;
    }

    public String getCalories() {
        return calories;
    }

    public long getScore() {
        return score;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public void setScore(long score) {
        this.score = score;
    }
}


