package com.appsnipp.homedesign2.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.homedesign2.Entity.DietMeals;
import com.appsnipp.homedesign2.R;

import java.util.List;

public class RecyclerMealAdapter extends RecyclerView.Adapter<RecyclerMealAdapter.MealViewHolder> {
    private static final String TAG="RecyclerMealAdapter";
    private List<DietMeals> _dietMeals;
    private Context _context;
    private OnMealListener _onMealListener;

    public RecyclerMealAdapter(List<DietMeals> dietMeals, Context context, OnMealListener onMealListener) {
        _dietMeals = dietMeals;
        _context = context;
        _onMealListener = onMealListener;
    }

    @NonNull
    @Override
    public RecyclerMealAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item_layout, parent, false);
        return new MealViewHolder(itemView,_onMealListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerMealAdapter.MealViewHolder holder, int position) {
        String _name=_dietMeals.get(position).getName();
        int _imgID= _dietMeals.get(position).getPhotoId();
        holder.imageViewMeal.setImageResource(_imgID);
        holder.textViewMealName.setText(_name);
        holder.type=_dietMeals.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return _dietMeals==null ? 0 : _dietMeals.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageViewMeal;
        private TextView textViewMealName;
        OnMealListener onMealListener;
        private int type;
        public MealViewHolder(@NonNull View itemView, OnMealListener onMealListener) {
            super(itemView);
            imageViewMeal=(ImageView)itemView.findViewById(R.id.imageViewMeal);
            textViewMealName=(TextView)itemView.findViewById(R.id.textViewMeal);

            this.onMealListener=onMealListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            onMealListener.onMealItemClick(getAdapterPosition(),type);
        }
    }
    public interface OnMealListener{
        void onMealItemClick(int position,int type);
    }

}
