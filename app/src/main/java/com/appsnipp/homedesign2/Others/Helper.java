package com.appsnipp.homedesign2.Others;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.appsnipp.homedesign2.Entity.DietMeals;

import java.util.ArrayList;
import java.util.HashMap;

public class Helper {
    private static final String TAG="Helper";
    private HashMap<String, Integer> _mealMap;

    public DietMeals findProperDietMeal(ArrayList<String> chosenIngredientList, ArrayList<DietMeals> dietMealList) {
        _mealMap = new HashMap<>();
        for (int i = 0; i < chosenIngredientList.size(); ++i) {
            for (int j = 0; j < dietMealList.size(); ++j) {
                if (dietMealList.get(j).checkExistIngredient(chosenIngredientList.get(i))) {
                    if (!_mealMap.containsKey(dietMealList.get(j).getName())) {
                        _mealMap.put(dietMealList.get(j).getName(), 1);
                    }
                    else {
                        int curNumOfAppearance = _mealMap.get(dietMealList.get(j).getName());
                        curNumOfAppearance++;
                        _mealMap.replace(dietMealList.get(j).getName(), curNumOfAppearance);
                    }
                }
            }
        }
        return getMostNumAppearanceName(dietMealList);
    }

    public DietMeals getMostNumAppearanceName(ArrayList<DietMeals> dietMealList) {
        int max = 0;
        DietMeals res = null;
        for (int i = 0; i < dietMealList.size(); ++i) {
            if (_mealMap.containsKey(dietMealList.get(i).getName())
                    && (_mealMap.get(dietMealList.get(i).getName()) > max)){
                max = _mealMap.get(dietMealList.get(i).getName());
                res = dietMealList.get(i);
            }
        }
        return res;
    }

    public Intent intentToVideoUrl(String videoUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(videoUri));
        return intent;
    }

    public int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public int fromImageIdToInt(String imageId, Context context){

        Resources resources = context.getResources();
        int resID=resources.getIdentifier(imageId,"drawable",context.getPackageName());
        Log.d(TAG, "fromImageIdToInt: "+resID);
        return resID;
    }
}
