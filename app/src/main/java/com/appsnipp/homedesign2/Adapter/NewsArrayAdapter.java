package com.appsnipp.homedesign2.Adapter;

import android.content.Context;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appsnipp.homedesign2.Entity.News;
import com.appsnipp.homedesign2.R;
import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class NewsArrayAdapter extends ArrayAdapter<News> {
    Context _context;
    int _newsRowLayoutId;
    ArrayList<News> _newsList;
    OnNewsClickInterface _myListener;

    public NewsArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<News> objects) {
        super(context, resource, objects);
        _context = context;
        _newsRowLayoutId = resource;
        _newsList = objects;
    }

    public interface OnNewsClickInterface {
        void onNewsAdapterClick(News selectedNews);
    }

    public void setOnNewsClickInterface(OnNewsClickInterface callback) {
        _myListener = callback;
    }

    @Override
    public int getCount() {
        return _newsList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(_context);
            convertView = layoutInflater.inflate(_newsRowLayoutId, null);
        }
        final News curNews = getItem(position);
        ImageView newsImage = convertView.findViewById(R.id.newsImage);
        TextView newsTitle = convertView.findViewById(R.id.newsTitle);
        TextView newsDate = convertView.findViewById(R.id.newsDate);


        Glide.with(_context)
                .load(curNews.getBackgroundPhotoId())
                .error(R.mipmap.ic_launcher_load)
                .override(200, 200)
                .centerCrop()
                .into(newsImage);

        newsTitle.setText(curNews.getTitle());
        newsDate.setText(curNews.getDate());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _myListener.onNewsAdapterClick(curNews);
            }
        });


        return convertView;
    }
}
