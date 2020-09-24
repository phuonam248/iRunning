package com.appsnipp.homedesign2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appsnipp.homedesign2.Adapter.IngredientAdapter;
import com.appsnipp.homedesign2.Adapter.RecyclerDayAdapter;
import com.appsnipp.homedesign2.Adapter.RecyclerIngredientsAdapter;
import com.appsnipp.homedesign2.Adapter.RecyclerMealAdapter;
import com.appsnipp.homedesign2.Entity.DayOfWeek;
import com.appsnipp.homedesign2.Entity.DietMeals;
import com.appsnipp.homedesign2.Entity.Ingredient;
import com.appsnipp.homedesign2.Others.Helper;
import com.appsnipp.homedesign2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class DietActivity extends AppCompatActivity implements RecyclerMealAdapter.OnMealListener,
        RecyclerIngredientsAdapter.OnIngredientListener,
        RecyclerDayAdapter.OnDayListener{
    BottomNavigationView bottomNavigationView;
    private static final String TAG = "DietActivity";

    private static List<DietMeals> _dietMeals;
    private static List<Ingredient> _ingredientList;

    private static RecyclerMealAdapter _recyclerBreakfastAdapter;
    private static RecyclerMealAdapter _recyclerLunchAdapter;
    private static RecyclerMealAdapter _recyclerDinnerAdapter;

    private static RecyclerView _breakfastRecyclerView;
    private static RecyclerView _lunchRecyclerView;
    private static RecyclerView _dinnerRecyclerView;

    private static RecyclerView _rclViewPicker;
    private static RecyclerIngredientsAdapter _ingredientsAdapter;
    private static RecyclerView _dayRecyclerView;

    private static List<List<DietMeals>> _dietBreakfastInWeek;
    private static List<List<DietMeals>> _dietLunchInWeek;
    private static List<List<DietMeals>> _dietDinnerInWeek;

    private static List<RecyclerMealAdapter> _breakfastInWeekAdapter;
    private static List<RecyclerMealAdapter> _lunchInWeekAdapter;
    private static List<RecyclerMealAdapter> _dinnerInWeekAdapter;

    private List<DayOfWeek> _dayOfWeeks;
    private static RecyclerDayAdapter _dayOfWeekAdapter;

    private static int _dayID;

    private AlertDialog dialog;

    private static Helper helper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        initComponents();
        setUpBotNav();
    }

    private void setUpBotNav() {
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        bottomNavigationView.setSelectedItemId(R.id.navigation2);
    }

    private void setUpBotCurvedNav() {
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        bottomNavigationView.setSelectedItemId(R.id.navigation2);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation1:
                    Intent news = new Intent(DietActivity.this, NewsActivity.class);
                    startActivity(news);
                    return true;
                case R.id.navigation2:
                    return true;
                case R.id.navigation3:
                    Intent home = new Intent(DietActivity.this, MainActivity.class);
                    startActivity(home);
                    return true;
                case R.id.navigation4:
                    Intent songs = new Intent(DietActivity.this, ListSongActivity.class);
                    startActivity(songs);
                    return true;
                case R.id.navigation5:
                    Intent setting = new Intent(DietActivity.this, SettingActivity.class);
                    startActivity(setting);
                    return true;
            }
            return false;
        }
    };

    private void initComponents() {
        initListDietMeal();
        initWeekAdapter();
        initDayOfWeek();
        setRecyclerView();
        initBottomNav();

    }

    private void initDayOfWeek() {
        _dayOfWeeks=new ArrayList<>(6);

        DayOfWeek _day=new DayOfWeek("MON");
        _dayOfWeeks.add(_day);
        _day=new DayOfWeek("TUE");
        _dayOfWeeks.add(_day);
        _day=new DayOfWeek("WED");
        _dayOfWeeks.add(_day);
        _day=new DayOfWeek("THU");
        _dayOfWeeks.add(_day);
        _day=new DayOfWeek("FRI");
        _dayOfWeeks.add(_day);
        _day=new DayOfWeek("SAT");
        _dayOfWeeks.add(_day);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL,false);
        _dayOfWeekAdapter = new RecyclerDayAdapter(_dayOfWeeks,this,this);
        _dayRecyclerView = (RecyclerView)findViewById(R.id.rclVDayOfWeek);
        _dayRecyclerView.setLayoutManager(layoutManager);
        _dayRecyclerView.setAdapter(_dayOfWeekAdapter);
    }

    private void initBottomNav() {

    }

    private void setRecyclerView() {
        _recyclerBreakfastAdapter = _breakfastInWeekAdapter.get(_dayID);
        _breakfastRecyclerView = (RecyclerView) findViewById(R.id.rclVBreakfast);
        _breakfastRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        _breakfastRecyclerView.setAdapter(_recyclerBreakfastAdapter);
        new ItemTouchHelper(breakfastItemTouchHelper).attachToRecyclerView(_breakfastRecyclerView);

        _recyclerLunchAdapter = _lunchInWeekAdapter.get(_dayID);
        _lunchRecyclerView = (RecyclerView) findViewById(R.id.rclVLunch);
        _lunchRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        _lunchRecyclerView.setAdapter(_recyclerLunchAdapter);
        new ItemTouchHelper(lunchItemTouchHelper).attachToRecyclerView(_lunchRecyclerView);

        _recyclerDinnerAdapter = _dinnerInWeekAdapter.get(_dayID);
        _dinnerRecyclerView = (RecyclerView) findViewById(R.id.rclVDinner);
        _dinnerRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        _dinnerRecyclerView.setAdapter(_recyclerDinnerAdapter);
        new ItemTouchHelper(dinnerItemTouchHelper).attachToRecyclerView(_dinnerRecyclerView);
    }

    private void initWeekAdapter() {
        _dayID = 0;

        initBreakfastInWeekAdapter();
        initLunchInWeekAdapter();
        initDinnerInWeekAdapter();

    }

    private void initBreakfastInWeekAdapter() {
        _breakfastInWeekAdapter = new ArrayList<>(6);
        _dietBreakfastInWeek = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            List<DietMeals> _dMeal = new ArrayList<>();
            RecyclerMealAdapter _rclMealAdapter = new RecyclerMealAdapter(_dMeal,
                    this, this);
            _dietBreakfastInWeek.add(_dMeal);
            _breakfastInWeekAdapter.add(_rclMealAdapter);
        }
    }

    private void initLunchInWeekAdapter() {
        _lunchInWeekAdapter = new ArrayList<>(6);
        _dietLunchInWeek = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            List<DietMeals> _dMeal = new ArrayList<>();
            RecyclerMealAdapter _rclMealAdapter = new RecyclerMealAdapter(_dMeal,
                    this, this);
            _dietLunchInWeek.add(_dMeal);
            _lunchInWeekAdapter.add(_rclMealAdapter);
        }
    }

    private void initDinnerInWeekAdapter() {
        _dinnerInWeekAdapter = new ArrayList<>(6);
        _dietDinnerInWeek = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            List<DietMeals> _dMeal = new ArrayList<>();
            RecyclerMealAdapter _rclMealAdapter = new RecyclerMealAdapter(_dMeal,
                    this, this);
            _dietDinnerInWeek.add(_dMeal);
            _dinnerInWeekAdapter.add(_rclMealAdapter);
        }
    }

    private void initMealInWeekAdapter(List<List<DietMeals>> mealLists,
                                       List<RecyclerMealAdapter> mealAdapter) {
        mealAdapter = new ArrayList<>(6);
        mealLists = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            List<DietMeals> _dMeal = new ArrayList<>();
            RecyclerMealAdapter _rclMealAdapter = new RecyclerMealAdapter(_dMeal,
                    this, this);
            mealLists.add(_dMeal);
            mealAdapter.add(_rclMealAdapter);
        }
    }


    private void initListDietMeal() {
        _dietMeals = new ArrayList<>();
        _dietMeals.add(new DietMeals("Bò xào dứa",
                "https://www.youtube.com/watch?v=KO0D9FMwUt0",
                helper.fromImageIdToInt("im_bo_xao_dua", this),
                20, 60, 60, 90, 132));
        _dietMeals.get(0).addIngredient(new Ingredient(
                helper.fromImageIdToInt("img_beef", this),
                "Thịt bò", "g", 200));
        _dietMeals.get(0).addIngredient(new Ingredient(
                helper.fromImageIdToInt("img_pineapple", this),
                "Dứa", "g", 100));
        _dietMeals.get(0).addIngredient(new Ingredient(
                helper.fromImageIdToInt("img_black_pepper", this),
                "Tiêu xay", "g", 5));
        _dietMeals.get(0).addIngredient(new Ingredient(
                helper.fromImageIdToInt("img_sugar", this),
                "Đường", "g", 10));
        _dietMeals.get(0).addIngredient(new Ingredient(
                helper.fromImageIdToInt("img_salt", this),
                "Muối", "g", 10));
        _dietMeals.get(0).addIngredient(new Ingredient(
                helper.fromImageIdToInt("img_tomato", this),
                "Cà chua", "g", 50));

        _dietMeals.add(new DietMeals("Yến mạch nấu chuối",
                "https://www.youtube.com/watch?v=KO0D9FMwUt0",
                helper.fromImageIdToInt("im_yen_mach_chuoi", this),
                10, 0, 0, 7.6, 150));
        _dietMeals.get(1).addIngredient(new Ingredient(
                helper.fromImageIdToInt("img_oat", this),
                "Yến mạch", "g", 38));
        _dietMeals.get(1).addIngredient(new Ingredient(
                helper.fromImageIdToInt("img_banana", this),
                "Chuối", "g", 50));
    }

    @Override
    public void onMealItemClick(int position,int type) {
        DietMeals _dMeal=null;
        switch (type){
            case R.id.buttonFindFood0:
                _dMeal = _dietBreakfastInWeek.get(_dayID).get(position);
                break;
            case R.id.buttonFindFood1:
                _dMeal = _dietLunchInWeek.get(_dayID).get(position);
                break;
            case R.id.buttonFindFood2:
                _dMeal = _dietLunchInWeek.get(_dayID).get(position);
                break;
        }
        AlertDialog.Builder _builder = new AlertDialog.Builder(DietActivity.this);
        View _view = getLayoutInflater().inflate(R.layout.food_info_layout, null);
        _builder.setView(_view);
        AlertDialog dialog = _builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setLayout(1000, 1700);
        initMealInfoLayout(_view, _dMeal);
    }

    private void initMealInfoLayout(View view, final DietMeals dMeal) {
        ImageView _imgMeal = (ImageView) view.findViewById(R.id.imgVMeal);
        TextView _txtMeal = (TextView) view.findViewById(R.id.textViewMeal);
        Button _btnYoutube = (Button) view.findViewById(R.id.buttonYoutube);
        ListView _listView = (ListView) view.findViewById(R.id.listViewIngredient);
        IngredientAdapter _adapter =
                new IngredientAdapter(this, 0, dMeal.getIngridient());
        _listView.setAdapter(_adapter);
        _imgMeal.setImageResource(dMeal.getPhotoId());
        _txtMeal.setText(dMeal.getName());
        _btnYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = helper.intentToVideoUrl(dMeal.getVideoUri());
                startActivity(intent);
            }
        });
    }

    public void btnFindFood_onClick(View view) {
        AlertDialog.Builder _builder = new AlertDialog.Builder(DietActivity.this);
        View _view = getLayoutInflater().inflate(R.layout.ingredient_picker_layout, null);
        _builder.setView(_view);
        dialog = _builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setLayout(900, 1000);
        /* DUNG MO NO RA LAM GI */
        initIngredientsList();
        initRecyclerViewPicker(_view, dialog,view);
    }

    private void initIngredientsList() {
        _ingredientList = new ArrayList<>();
        _ingredientList.add(new Ingredient(
                R.drawable.img_lime,
                "Chanh", "", 0));
        _ingredientList.add(new Ingredient(
                helper.fromImageIdToInt("img_apple", this),
                "Táo", "", 0));
        _ingredientList.add(new Ingredient(
                R.drawable.img_beef,
                "Thịt bò", "g", 100));
        _ingredientList.add(new Ingredient(
                R.drawable.img_pineapple,
                "Dứa", "g", 50));
        _ingredientList.add(new Ingredient(
                R.drawable.img_pepper,
                "Tiêu xay", "g", 30));
        _ingredientList.add(new Ingredient(
                R.drawable.img_sugar,
                "Đường", "g", 200));
        _ingredientList.add(new Ingredient(
                R.drawable.img_salt,
                "Muối", "g", 20));
        _ingredientList.add(new Ingredient(
                R.drawable.img_tomato,
                "Cà chua", "quả", 2));
        _ingredientList.add(new Ingredient(
                helper.fromImageIdToInt("img_banana", this),
                "Chuối", "quả", 2));
        _ingredientList.add(new Ingredient(R.drawable.img_oat,
                "Yến mạch", "g", 2));
    }

    private void initRecyclerViewPicker(View view, final AlertDialog dialog,View button) {
        final int _buttonID=button.getId();
        _ingredientsAdapter = new RecyclerIngredientsAdapter(_ingredientList,
                this, this);
        GridLayoutManager _gridLayoutManager = new GridLayoutManager(this, 5);
        _rclViewPicker = (RecyclerView) view.findViewById(R.id.recyclerViewPicker);
        _rclViewPicker.setLayoutManager(_gridLayoutManager);
        _rclViewPicker.setAdapter(_ingredientsAdapter);
        Button _buttonOk = view.findViewById(R.id.buttonChoose);
        _buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> _selectedIngredientName = _ingredientsAdapter.getSelectedName();
                DietMeals _result = helper.findProperDietMeal(
                        (ArrayList<String>) _selectedIngredientName,
                        (ArrayList<DietMeals>) _dietMeals);
                setResult(_result,_buttonID);
                dialog.cancel();
            }
        });
    }

    private void setResult(DietMeals result,int buttonID) {
        result.setType(buttonID);
        switch (buttonID){
            case R.id.buttonFindFood0:
                _dietBreakfastInWeek.get(_dayID).add(result);
                _breakfastRecyclerView.getAdapter().notifyItemInserted(_dietBreakfastInWeek.get(_dayID).size());
                break;
            case R.id.buttonFindFood1:
                _dietLunchInWeek.get(_dayID).add(result);
                _lunchRecyclerView.getAdapter().notifyItemInserted(_dietLunchInWeek.get(_dayID).size());
                break;
            case R.id.buttonFindFood2:
                _dietDinnerInWeek.get(_dayID).add(result);
                _dinnerRecyclerView.getAdapter().notifyItemInserted(_dietDinnerInWeek.get(_dayID).size());
                break;
        }
    }

    @Override
    public void onIngredientItemClick(View view, int position, ImageView imageView) {
        Ingredient _ingredient = _ingredientList.get(position);
        _ingredient.setCheck(!_ingredient.isChecked());
        imageView.setVisibility(_ingredient.isChecked() ? view.VISIBLE : view.GONE);
    }

    public void cancel_onCLick(View view) {
        dialog.cancel();
    }

    @Override
    public void onDayItemListener(View view, int position) {
        _dayOfWeekAdapter.resetSelected();
        _dayOfWeekAdapter.setSelectedPosition(view, position);
        _dayID=position;
        _breakfastRecyclerView.setAdapter(_breakfastInWeekAdapter.get(_dayID));
        _lunchRecyclerView.setAdapter(_lunchInWeekAdapter.get(_dayID));
        _dinnerRecyclerView.setAdapter(_dinnerInWeekAdapter.get(_dayID));
    }

    ItemTouchHelper.SimpleCallback breakfastItemTouchHelper = new ItemTouchHelper.
            SimpleCallback(0,ItemTouchHelper.RIGHT| ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            _dietBreakfastInWeek.get(_dayID).remove(viewHolder.getAdapterPosition());
            _recyclerBreakfastAdapter.notifyDataSetChanged();
        }
    };

    ItemTouchHelper.SimpleCallback lunchItemTouchHelper = new ItemTouchHelper.
            SimpleCallback(0,ItemTouchHelper.RIGHT| ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            _dietLunchInWeek.get(_dayID).remove(viewHolder.getAdapterPosition());
            _recyclerLunchAdapter.notifyDataSetChanged();
        }
    };

    ItemTouchHelper.SimpleCallback dinnerItemTouchHelper = new ItemTouchHelper.
            SimpleCallback(0,ItemTouchHelper.RIGHT| ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            _dietDinnerInWeek.get(_dayID).remove(viewHolder.getAdapterPosition());
            _recyclerDinnerAdapter.notifyDataSetChanged();
        }
    };

}