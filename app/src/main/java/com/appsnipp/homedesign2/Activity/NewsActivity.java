package com.appsnipp.homedesign2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appsnipp.homedesign2.Adapter.NewsArrayAdapter;
import com.appsnipp.homedesign2.Entity.News;

import com.appsnipp.homedesign2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    private static final int MODE_DARK = 0;
    private static final int MODE_LIGHT = 1;

    BottomNavigationView bottomNavigationView;

    private ArrayList<News> _newsList;
    private NewsArrayAdapter _newsArrayAdapter;
    News _selectedNews;

    private ListView _newsListView;
    private TextView _selectedNewsDate;
    private TextView _selectedNewsTitle;
    private TextView _selectedNewsShortDes;
    private TextView _selectedNewsAuthorName;
    private ImageView _selectedNewsAuthorImage;
    private BottomNavigationView _bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initComponents();
        loadData();
        setUpBottomNav();
        if (_newsList.size() > 0)
            setUpStartingSelectedNews(_selectedNews);
        setUpNewsListView();
    }
    private void setUpBottomNav() {
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation1);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation1:
                    return true;

                case R.id.navigation2:
                    Intent news = new Intent(NewsActivity.this, DietActivity.class);
                    startActivity(news);
                    return true;

                case R.id.navigation3:
                    Intent home = new Intent(NewsActivity.this, MainActivity.class);
                    startActivity(home);
                    return true;

                case R.id.navigation4:
                    Intent songs = new Intent(NewsActivity.this, ListSongActivity.class);
                    startActivity(songs);
                    return true;

                case R.id.navigation5:
                    Intent setting = new Intent(NewsActivity.this, SettingActivity.class);
                    startActivity(setting);
                    return true;
            }
            return false;
        }
    };

    private void loadData() {
        _newsList.add(new News("Aug. 20, 2020, at 12:06 p.m",
                "Orlando Hospital Cook Honors Health Care Heroes With Portraits",
                "K. Aleisha Fetters",
                "The self-taught artist used a special technique to burn the images of six health workers into plywood.\n",
                R.drawable.author_2,
                R.drawable.background_2,
                "https://health.usnews.com/hospital-heroes/articles/orlando-hospital-cook-honors-health-care-heroes-with-portraits"));

        _newsList.add(new News("Aug. 19, 2020, at 2:52 p.m.",
                "Noom vs. Mediterranean Diet: What's the Difference?",
                "Elaine K. Howley",
                "Both take a holistic approach to being and eating healthy.",
                R.drawable.author_3,
                R.drawable.background_3,
                "https://health.usnews.com/wellness/compare-diets/articles/noom-vs-mediterranean-diet-whats-the-difference"));

        _newsList.add(new News("Aug. 17, 2020, at 2:23 p.m.",
                "8 Healthy Drinks Rich in Electrolytes",
                "Ruben Castaneda",
                "The self-taught artist used a special technique to burn the images of six health workers into plywood.\n",
                R.drawable.author_4,
                R.drawable.background_4,
                "https://health.usnews.com/wellness/slideshows/healthy-drinks-rich-in-electrolytes"));

        _newsList.add(new News("Feb. 20, 2020, at 12:06 p.m",
                "Does coffee raise blood pressure?",
                "Louisa Richards",
                "Research about coffee and blood pressure is conflicting. It seems that how often a person drinks coffee could influence its effect on blood pressure",
                R.drawable.author_1,
                R.drawable.background_1,
                "https://www.medicalnewstoday.com/articles/does-coffee-raise-blood-pressure#does-it"));

        _newsList.add(new News("Aug. 20, 2020, at 4:02 p.m",
                "Good Fats vs. Bad Fats for Healthy Heart",
                "David Levine",
                "Knowing the difference can make all the difference in cardiovascular health.",
                R.drawable.author_7,
                R.drawable.background_7,
                "https://health.usnews.com/wellness/articles/good-fats-vs-bad-fats-healthy-heart"));

        _newsList.add(new News("Aug. 14, 2020, at 11:58 a.m.",
                "Is Your Quarantine Diet Hurting Your Health?",
                "Heidi Godman",
                "Eating the wrong foods can increase your risks for weight gain and chronic disease.",
                R.drawable.author_5,
                R.drawable.background_5,
                "https://health.usnews.com/wellness/food/articles/is-your-quarantine-diet-hurting-your-health"));

        _newsList.add(new News("Aug. 14, 2020, at 12:37 p.m",
                "Keto-Friendly Sweeteners",
                "Elaine K. Howley",
                "These sugar-alternatives are compatible with the ketogenic diet.",
                R.drawable.author_6,
                R.drawable.background_6,
                "https://health.usnews.com/wellness/food/articles/keto-friendly-sweeteners"));



        _selectedNews = _newsList.get(0);
    }

    private void setUpNewsListView() {
        _newsListView = findViewById(R.id.newsListView);
        _newsArrayAdapter = new NewsArrayAdapter(this, R.layout.news_row_layout, _newsList);
        _newsArrayAdapter.setOnNewsClickInterface(new NewsArrayAdapter.OnNewsClickInterface() {
            @Override
            public void onNewsAdapterClick(News selectedNews) {
                _selectedNews = selectedNews;
                setUpStartingSelectedNews(selectedNews);
            }
        });
        _newsListView.setAdapter(_newsArrayAdapter);
    }

    private void setUpStartingSelectedNews(News selectedNews) {
        _selectedNewsShortDes.setText(selectedNews.getShortDes());
        _selectedNewsAuthorImage.setImageResource(selectedNews.getAuthorPhotoId());
        _selectedNewsAuthorName.setText(selectedNews.getAuthor());
        _selectedNewsDate.setText(selectedNews.getDate());
        _selectedNewsTitle.setText(selectedNews.getTitle());

        LinearLayout selectedNewsWrapper = findViewById(R.id.selectedNewsWrapper);
        selectedNewsWrapper.setBackgroundResource(selectedNews.getBackgroundPhotoId());
    }

    private void initComponents() {
        _selectedNewsAuthorImage = findViewById(R.id.selectedNewsAuthorImage);
        _selectedNewsAuthorName = findViewById(R.id.selectedNewsAuthorName);
        _selectedNewsTitle = findViewById(R.id.selectedNewsTitle);
        _selectedNewsDate = findViewById(R.id.selectedNewsDate);
        _selectedNewsShortDes = findViewById(R.id.selectedNewsShortDes);
        _newsList = new ArrayList<>();
    }

    public void startingNews_onClick(View view) {
        Intent intent = new Intent(NewsActivity.this, WebViewActivity.class);
        intent.putExtra("url", _selectedNews.getUri());
        startActivity(intent);
    }
}