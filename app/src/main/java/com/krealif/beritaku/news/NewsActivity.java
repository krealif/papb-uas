package com.krealif.beritaku.news;

import static com.krealif.beritaku.auth.LoginActivity.DOB;
import static com.krealif.beritaku.auth.LoginActivity.USERNAME;
import static com.krealif.beritaku.auth.LoginActivity.sharedPrefFile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.krealif.beritaku.R;
import com.krealif.beritaku.auth.LoginActivity;
import com.krealif.beritaku.bookmark.BookmarkActivity;
import com.krealif.beritaku.database.NewsDao;
import com.krealif.beritaku.database.NewsFirebase;
import com.krealif.beritaku.database.NewsRoom;
import com.krealif.beritaku.model.News;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static List<News> newsList = new ArrayList<>();
    private static NewsAdapter newsAdapter;
    public static SharedPreferences sharedPref;
    private Spinner spinnerCategory;
    public static ExecutorService executor;
    public static NewsDao newsDao;

    public static final String CATEGORY_SELECTED = "CATEGORY_SELECTED";
    public static String[] listCategory;
    public static int categorySelected;
    public static int userAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        sharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        userAge = calculateAge(sharedPref.getString(DOB, "1970-1-1"));
        Log.d("DEBUG", String.valueOf(userAge));
        listCategory = getResources().getStringArray(R.array.list_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        String toolbarTitle = "Hello, " + sharedPref.getString(USERNAME, "User");
        toolbar.setTitle(toolbarTitle);
        toolbar.inflateMenu(R.menu.menu_main);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_bookmarks:
                        Intent intent = new Intent(NewsActivity.this, BookmarkActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_logout:
                        LoginActivity.setLoggedOut();
                        intent = new Intent(NewsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });

        newsAdapter = new NewsAdapter(this, newsList);
        recyclerView = findViewById(R.id.rv_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        spinnerCategory = findViewById(R.id.spinner_category);
        loadSpinnerCategory();

        categorySelected = sharedPref.getInt(CATEGORY_SELECTED, 0);
        if (categorySelected != 0) {
            spinnerCategory.setSelection(categorySelected);
        }

        executor = Executors.newSingleThreadExecutor();
        NewsRoom db = NewsRoom.getDatabase(this);
        newsDao = db.newsDao();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FormNewsActivity.class);
                intent.putExtra("MODE", "MODE_ADD");
                startActivity(intent);
            }
        });
    }

    public static void refreshList(List<News> list) {
        newsList.clear();
        newsList.addAll(list);
        newsAdapter.notifyDataSetChanged();
    }

    //function insert data ke room
    public static void insertData(News news) {
        executor.execute(()->newsDao.insert(news));
    }


    private void loadSpinnerCategory() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list_category, android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        if (spinnerCategory != null) {
            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (categorySelected != i) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(CATEGORY_SELECTED, i);
                        categorySelected = i;
                        editor.apply();
                    }
                    NewsFirebase.getAllNewsByCategory(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

        }
    }

    private int calculateAge(String dateOfBirth) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar dob = Calendar.getInstance();
        dob.setTime(date);

        // www.javatpoint.com/java-calculate-age
        Calendar currentDate = new GregorianCalendar();
        int age = currentDate.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if ((dob.get(Calendar.MONTH) > currentDate.get(Calendar.MONTH)) || (dob.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) && dob.get(Calendar.DAY_OF_MONTH) > currentDate.get(Calendar.DAY_OF_MONTH)))
        {
            age--;
        }
        return age;
    }

}