package com.appsnipp.homedesign2.Entity;

import java.util.ArrayList;

public class DietMeals {
    private int _photoId;
    private double _card;
    private double _lipid;
    private double _ldl;
    private double _protein;
    private double _calo;
    ArrayList<Double> _content;
    String _name;
    String _videoUri;
    ArrayList<Ingredient> _ingredient;
    private int _type;

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        this._type = type;
    }

    public DietMeals(String name, String videoUri, int photoId,
                     double card, double lipid, double ldl, double protein, double calo) {
        _name = name;
        _videoUri = videoUri;
        _photoId = photoId;
        _card = card;
        _calo = calo;
        _lipid = lipid;
        _ldl = ldl;
        _protein = protein;
        _ingredient=new ArrayList<>();
    }

    public void setCard(double card) {
        _card = card;
    }

    public void setLipid(double lipid) {
        _lipid = lipid;
    }

    public void setLdl(double ldl) {
        _ldl = ldl;
    }

    public void setProtein(double protein) {
        _protein = protein;
    }

    public void setCalo(double calo) {
        _calo = calo;
    }

    public double getCard() {
        return _card;
    }

    public double getLipid() {
        return _lipid;
    }

    public double getLdl() {
        return _ldl;
    }

    public double getProtein() {
        return _protein;
    }

    public double getCalo() {
        return _calo;
    }

    public void addIngredient(Ingredient newIngredient) {
        _ingredient.add(newIngredient);
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setVideoUri(String videoUri) {
        this._videoUri = videoUri;
    }

    public void setPhotoId(int photoId) {
        this._photoId = photoId;
    }

    public void setIngridientList(ArrayList<Ingredient> ingridient) {
        this._ingredient = ingridient;
    }

    public String getVideoUri() {
        return _videoUri;
    }

    public int getPhotoId() {
        return _photoId;
    }

    public ArrayList<Ingredient> getIngridient() {
        return _ingredient;
    }

    public boolean checkExistIngredient(String ingName) {
        for (int i = 0; i < _ingredient.size(); ++i) {
            Ingredient ingredient = _ingredient.get(i);
            if (ingName.equals(ingredient.getName()))
                return true;
        }
        return false;
    }

}