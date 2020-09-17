package com.appsnipp.homedesign2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appsnipp.homedesign2.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends ArrayAdapter<File> {
    Context _context;
    int _layoutID;
    ArrayList<File> _songsList;
    public SongsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<File> objects) {
        super(context, resource, objects);
        _context = context;
        _layoutID = resource;
        _songsList = objects;
    }

    @Override
    public int getCount() {
        return _songsList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(_context);
            convertView = layoutInflater.inflate(_layoutID, null);
        }
        TextView songNameTextView = convertView.findViewById(R.id.songNameTextView);
        songNameTextView.setText(_songsList.get(position).getName().replace(".mp3", ""));
        return convertView;
    }
}