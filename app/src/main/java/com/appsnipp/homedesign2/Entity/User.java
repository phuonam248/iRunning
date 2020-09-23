package com.appsnipp.homedesign2.Entity;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class User implements Comparable<User> {
    @PropertyName("id")
    private String id;
    @PropertyName("username")
    private String name;

    @PropertyName("weight")
    private String weight;

    private String password;
    @PropertyName("email")
    private String email;
    @PropertyName("score")
    private Long score;
    @PropertyName("distance")
    private String distance;
    @PropertyName("totalCal")
    private String totalCalo;
    @PropertyName("status")
    private  String status;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", weight='" + weight + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", score=" + score +
                ", distance='" + distance + '\'' +
                ", totalCalo='" + totalCalo + '\'' +
                ", status='" + status + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String avatar;

    public User(String id, String name, String weight, String email, Long score, String distance, String totalCalo) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.email = email;
        this.score = score;
        this.distance = distance;
        this.totalCalo = totalCalo;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    public User(String id, String name, String email, Long score, String distance, String totalCalo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.score = score;
        this.distance = distance;
        this.totalCalo = totalCalo;
    }

    public User() {

    }

    public User(String id, String name, String password, String email,
                Long score, String distance, String totalCalo, String avatar) {
        super();
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;

        this.score = score;

        this.distance = distance;
        this.totalCalo = totalCalo;
        this.avatar = avatar;

    }

    public User(String id, String password, String email) {
        super();
        this.id = id;
        this.password = password;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTotalCalo() {
        return totalCalo;
    }

    public void setTotalCalo(String totalCalo) {
        this.totalCalo = totalCalo;
    }


    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int compareTo(User o) {
        return this.score.compareTo(o.getScore());
    }
}

