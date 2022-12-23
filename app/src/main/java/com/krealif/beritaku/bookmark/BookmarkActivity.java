package com.krealif.beritaku.bookmark;

import static com.krealif.beritaku.news.NewsActivity.newsDao;
import static com.krealif.beritaku.news.NewsActivity.executor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.krealif.beritaku.R;
import com.krealif.beritaku.model.News;

import java.util.ArrayList;
import java.util.List;


public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static List<News> newsList = new ArrayList<>();
    private static BookmarkAdapter bookmarkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        bookmarkAdapter = new BookmarkAdapter(this, newsList);
        recyclerView = findViewById(R.id.rv_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookmarkAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        getAllNews();
    }

    public  static void deleteData(News news) {
        executor.execute(()->newsDao.delete(news));
    }

    private void getAllNews() {
        newsDao.getAllNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> list) {
                newsList.clear();
                newsList.addAll(list);
                bookmarkAdapter.notifyDataSetChanged();
            }
        });
    }
}