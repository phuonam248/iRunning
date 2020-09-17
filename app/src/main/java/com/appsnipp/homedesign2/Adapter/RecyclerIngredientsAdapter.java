package com.appsnipp.homedesign2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.homedesign2.Entity.Ingredient;
import com.appsnipp.homedesign2.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerIngredientsAdapter extends
        RecyclerView.Adapter<RecyclerIngredientsAdapter.IngredientViewHolder> {
    private List<Ingredient> _ingredientList;
    private Context _context;
    private OnIngredientListener _onIngredientListener;

    public RecyclerIngredientsAdapter(List<Ingredient> ingredientList, Context context,
                                      OnIngredientListener onIngredientListener) {
        _ingredientList = ingredientList;
        _context = context;
        _onIngredientListener = onIngredientListener;
    }

    @NonNull
    @Override
    public RecyclerIngredientsAdapter.
            IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.ingredient_picker_item, parent, false);
        return new RecyclerIngredientsAdapter.
                IngredientViewHolder(itemView, _onIngredientListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerIngredientsAdapter.
            IngredientViewHolder holder, int position) {
        Ingredient _ingredient=_ingredientList.get(position);
        holder.bind(_ingredient);
    }

    @Override
    public int getItemCount() {
        return _ingredientList==null? 0:_ingredientList.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        ImageView _imgVIngredient;
        ImageView _imgVTick;
        TextView _txtVIngredient;

        OnIngredientListener _onIngredientListener;

        public IngredientViewHolder(@NonNull View itemView,OnIngredientListener onIngredientListener) {
            super(itemView);
            _imgVIngredient=(ImageView)itemView.findViewById(R.id.imgVPickerItem);
            _imgVTick=(ImageView)itemView.findViewById(R.id.imgViewTickPoint);
            _txtVIngredient=(TextView)itemView.findViewById(R.id.txtVPickerName);
            _onIngredientListener=onIngredientListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            _onIngredientListener.onIngredientItemClick(view,getAdapterPosition(),_imgVTick);
        }

        void bind(final Ingredient ingredient){
            _imgVTick.setVisibility(ingredient.isChecked()? View.VISIBLE:View.GONE);
            String _name=ingredient.getName();
            int _imgID= ingredient.getPhotoId();
            _imgVIngredient.setImageResource(_imgID);
            _txtVIngredient.setText(_name);
        }
    }

    public interface OnIngredientListener{
        void onIngredientItemClick(View view, int position,ImageView imageView);
    }
    public List<Ingredient> getSelected() {
        List<Ingredient> selected = new ArrayList<>();
        for (int i = 0; i < _ingredientList.size(); i++) {
            if (_ingredientList.get(i).isChecked()) {
                selected.add(_ingredientList.get(i));
            }
        }
        return selected;
    }
    public List<String> getSelectedName() {
        List<String> selected = new ArrayList<>();

        for (int i = 0; i < _ingredientList.size(); i++) {
            if (_ingredientList.get(i).isChecked()) {
                selected.add(_ingredientList.get(i).getName());
            }
        }
        return selected;
    }
}
