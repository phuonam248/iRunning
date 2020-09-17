package com.appsnipp.homedesign2.Entity;

public class Ingredient {
    boolean _check= false;
    int _photoId;
    String _name;
    String _unit;
    double _quantity;

    public Ingredient(int photoId, String name, String unit, double quantity) {
        this._photoId = photoId;
        this._name = name;
        this._unit = unit;
        this._quantity = quantity;
    }

    public int getPhotoId() {
        return _photoId;
    }

    public String getName() {
        return _name;
    }

    public String getUnit() {
        return _unit;
    }

    public double getQuantity() {
        return _quantity;
    }

    public void setPhotoId(int photoId) {
        this._photoId = photoId;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setUnit(String unit) {
        this._unit = unit;
    }

    public void setQuantity(double quantity) {
        this._quantity = quantity;
    }

    public boolean isChecked() {
        return _check;
    }

    public void setCheck(boolean check) {
        this._check = check;
    }
}
