package com.krealif.beritaku.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.krealif.beritaku.R;

public class NewsDetailActivity extends AppCompatActivity {

    ImageView imgNews;
    TextView txtTitle, txtPublished, txtAgeRating, txtBody;
    String title, published, ageRating, body;
    int imgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        imgNews = findViewById(R.id.image_news);
        txtTitle = findViewById(R.id.txt_title);
        txtPublished = findViewById(R.id.txt_published);
        txtAgeRating = findViewById(R.id.txt_age);
        txtBody = findViewById(R.id.txt_body);

        Intent intent = getIntent();
        imgId = getResources().getIdentifier("placeholder", "drawable", getPackageName());
        title = intent.getStringExtra("title");
        published = intent.getStringExtra("published");
        ageRating = intent.getStringExtra("ageRating");
        body = intent.getStringExtra("body");

        imgNews.setImageResource(imgId);
        txtTitle.setText(title);
        txtPublished.setText(published);
        txtAgeRating.setText("Age " + ageRating + "+");
        txtBody.setText(body);
    }
}