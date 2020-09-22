package com.appsnipp.homedesign2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appsnipp.homedesign2.Entity.History;
import com.appsnipp.homedesign2.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends ArrayAdapter<History> {

    Context _context;
    int _resource;
    ArrayList<History> _historyList;

    public HistoryAdapter(@NonNull Context context, int resource, @NonNull ArrayList<History> objects) {
        super(context, resource, objects);
        _context = context;
        _resource = resource;
        _historyList = objects;
    }

    @Override
    public int getCount() {
        return _historyList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(_context);
            convertView = layoutInflater.inflate(_resource, null);
        }
        TextView dateTextView = convertView.findViewById(R.id.hisDateTextView);
        dateTextView.setText(_historyList.get(position).getDate());
        TextView distanceTextView = convertView.findViewById(R.id.hisDistanceTextView);
        distanceTextView.setText(_historyList.get(position).getDistance());
        TextView durationTextView = convertView.findViewById(R.id.hisDurationTextView);
        durationTextView.setText(_historyList.get(position).getDuration());
        TextView caloriesTextView = convertView.findViewById(R.id.hisCaloriesTextView);
        caloriesTextView.setText(_historyList.get(position).getCalories());
        TextView scoreTextView = convertView.findViewById(R.id.hisScoreTextView);
        scoreTextView.setText(String.valueOf(_historyList.get(position).getScore()));
        return convertView;
    }
}
