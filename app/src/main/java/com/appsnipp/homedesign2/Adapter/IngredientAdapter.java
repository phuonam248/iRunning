package com.appsnipp.homedesign2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.appsnipp.homedesign2.Entity.Ingredient;
import com.appsnipp.homedesign2.R;

import java.util.List;


public class IngredientAdapter extends ArrayAdapter<Ingredient> {
    List<Ingredient> _ingredients;
    Context _context;
    private static class ViewHolder{
        ImageView _imageIngredient;
        TextView _nameIngredient;
        TextView _contentIngredient;
        int _position;
    }

    public IngredientAdapter(@NonNull Context context, int resource, @NonNull List<Ingredient> ingredients) {
        super(context,0,ingredients);
        _ingredients=ingredients;
        _context=context;
    }

    @Override
    public int getCount() {
        return _ingredients.size();
    }

    @Override
    public Ingredient getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).
                    inflate(R.layout.ingredient_item_layout,null);
            viewHolder=new ViewHolder();
            initViewHolder(viewHolder,convertView,position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        //convertView = createRow(position, (ListView)parent);
        setRow(viewHolder,position);
        return  convertView;
    }

    private void initViewHolder(ViewHolder viewHolder,View view,int position){
        viewHolder._imageIngredient=(ImageView)view.findViewById(R.id.imgViewIngredientOfMeal);
        viewHolder._contentIngredient=(TextView)view.findViewById(R.id.txtViewContentIngredientOfMeal);
        viewHolder._nameIngredient=(TextView)view.findViewById(R.id.txtViewNameIngredientOfMeal);
        viewHolder._position=position;
    }

    private void setRow(ViewHolder viewHolder,int position) {
        Ingredient ingredient= _ingredients.get(position);
        String quantityUnit= ingredient.getQuantity()+ingredient.getUnit();
        viewHolder._imageIngredient.setImageResource(ingredient.getPhotoId());
        viewHolder._nameIngredient.setText(ingredient.getName());
        viewHolder._contentIngredient.setText(quantityUnit);
        viewHolder._position=position;
    }

}
