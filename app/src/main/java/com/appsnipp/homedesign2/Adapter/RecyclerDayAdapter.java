package com.appsnipp.homedesign2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.homedesign2.Entity.DayOfWeek;
import com.appsnipp.homedesign2.R;

import java.util.List;

public class RecyclerDayAdapter extends RecyclerView.Adapter<RecyclerDayAdapter.DayViewHolder> {
    private static final String TAG="RecyclerDayAdapter";
    private int _selectedPosition=0;
    private final List<DayOfWeek> _dayOfWeeks;
    private Context _context;
    private RecyclerDayAdapter.OnDayListener _onDayListener;
    private View _selectedView=null;

    public RecyclerDayAdapter(List<DayOfWeek> dayOfWeeks, Context context, RecyclerDayAdapter.OnDayListener onDayListener) {
        _dayOfWeeks = dayOfWeeks;
        _context = context;
        _onDayListener = onDayListener;
    }

    @NonNull
    @Override
    public RecyclerDayAdapter.
            DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.day_of_week_item_layout, parent, false);
        return new RecyclerDayAdapter.DayViewHolder(itemView, _onDayListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerDayAdapter.DayViewHolder holder, int position) {
        String _day=_dayOfWeeks.get(position).getDayOfWeek();
        int _color=0;
        switch (_day){
            case "MON":
                _selectedView=holder.itemView;
                _color=_context.getColor(R.color.colorOrange);
                break;
            case "TUE":
                _color=_context.getColor(R.color.colorOrangeLight1);
                break;
            case "WED":
                _color=_context.getColor(R.color.colorOrangeLight2);
                break;
            case "THU":
                _color=_context.getColor(R.color.colorOrangeLight3);
                break;
            case "FRI":
                _color=_context.getColor(R.color.colorOrangeLight4);
                break;
            case "SAT":
                _color=_context.getColor(R.color.colorOrangeLight5);
                break;
            default:
                break;
        }
        holder._txtVDayOfWeek.setBackgroundColor(_color);
        holder._txtVDayOfWeek.setText(_day);
        if (position==0){
            _selectedView.setScaleY(1.2f);
            _selectedView.setScaleX(1.2f);
        }
    }

    public void setSelectedPosition(View view, int position){
        _selectedView=view;
        _selectedView.setScaleX(1.2f);
        _selectedView.setScaleY(1.2f);
        _selectedPosition=position;
    }

    @Override
    public int getItemCount() {
        return _dayOfWeeks.size();
    }

    public void resetSelected() {
        if (_selectedView==null) return;
        _selectedView.setScaleX(1f);
        _selectedView.setScaleY(1f);
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView _txtVDayOfWeek;
        OnDayListener _onDayListener;

        public DayViewHolder(@NonNull View itemView, OnDayListener onDayListener) {
            super(itemView);
            _txtVDayOfWeek =(TextView) itemView.findViewById(R.id.txtVDayOfWeek);
            this._onDayListener=onDayListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            _onDayListener.onDayItemListener(view, getAdapterPosition());
        }
    }

    public interface OnDayListener {
        void onDayItemListener(View view, int position);
    }

}
