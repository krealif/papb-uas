package com.krealif.beritaku.database;

import android.app.Application;
import android.util.Log;

import static com.krealif.beritaku.news.NewsActivity.userAge;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.krealif.beritaku.model.News;
import com.krealif.beritaku.news.NewsActivity;

import java.util.ArrayList;
import java.util.List;

public class NewsFirebase extends Application {
    private FirebaseDatabase database;
    public static DatabaseReference newsReference;
    private String debug;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        newsReference = database.getReference(News.class.getSimpleName());
    }

    public static void insertNews(News news) {
        newsReference.push().setValue(news);
    }

    public static void updateNews(News news, String newsId) {
        newsReference.child(newsId).setValue(news);
    }

    public static void removeNews(String newsId) {
        newsReference.child(newsId).removeValue();
    }

    public static void getAllNewsByCategory(int category) {
        Query query = newsReference.orderByChild("category").equalTo(category);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<News> newsList = new ArrayList<>();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot currentData : dataSnapshot.getChildren()){
                        News news = currentData.getValue(News.class);
                        news.setId(currentData.getKey());

                        // filter umur
                        if (userAge >= news.getAgeRating()) {
                            newsList.add(news);
                        }
                    }
                }
                NewsActivity.refreshList(newsList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", error.toString());
            }
        });
    }
}
