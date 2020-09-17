package com.appsnipp.homedesign2.Entity;

public class User {

    private int id;
    private String name;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String facebook;
    private int score;
    private String confirm;
    private double distance;
    private double totalCalo;
    private String avatar;


    public User(String name, String password, String email, String confirm) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.confirm = confirm;
    }

    public User() {
        super();
    }

    public User(int id, String name, String password, String email, String phone, String address, String facebook,
                int score, String confirm, double distance, double totalCalo, String avatar) {
        super();
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.facebook = facebook;
        this.score = score;
        this.confirm = confirm;
        this.distance = distance;
        this.totalCalo = totalCalo;
        this.avatar = avatar;

    }

    public User(int id, String password, String email) {
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

    public String getPhone() {
        return phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTotalCalo() {
        return totalCalo;
    }

    public void setTotalCalo(double totalCalo) {
        this.totalCalo = totalCalo;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", facebook='" + facebook + '\'' +
                ", score=" + score +
                ", confirm='" + confirm + '\'' +
                ", distance=" + distance +
                ", totalCalo=" + totalCalo +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
