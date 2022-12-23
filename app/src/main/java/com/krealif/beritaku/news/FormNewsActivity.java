package com.krealif.beritaku.news;

import static com.krealif.beritaku.auth.LoginActivity.USERNAME;
import static com.krealif.beritaku.auth.LoginActivity.sharedPrefFile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.krealif.beritaku.R;
import com.krealif.beritaku.database.NewsFirebase;
import com.krealif.beritaku.model.News;
import com.krealif.beritaku.utils.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormNewsActivity extends AppCompatActivity implements DatePickerFragment.DatePickerFragmentListener{

    TextInputEditText inputTitle, inputAge, inputDate, inputBody;
    Button btnSave;
    private Spinner spinnerCategory;
    public static SharedPreferences sharedPref;

    // News
    private String newsId, title, body, author, published;
    private int ageRating, category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        inputTitle = findViewById(R.id.input_title);
        inputAge = findViewById(R.id.input_age);
        inputDate = findViewById(R.id.input_date);
        inputBody = findViewById(R.id.input_body);
        spinnerCategory = findViewById(R.id.spinner_category);
        loadSpinnerCategory();
        btnSave = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        String mode = intent.getStringExtra("MODE");

        sharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        author = sharedPref.getString(USERNAME, "user");


        if (mode.equals("MODE_ADD")) {
            toolbar.setTitle("Add News");
            spinnerCategory.setSelection(NewsActivity.categorySelected);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insertNews();
                }
            });
        } else if (mode.equals("MODE_EDIT")) {
            toolbar.setTitle("Edit News");
            newsId = intent.getStringExtra("id");
            inputTitle.setText(intent.getStringExtra("title"));
            spinnerCategory.setSelection(intent.getIntExtra("category", 0));
            inputAge.setText(String.valueOf(intent.getIntExtra("ageRating", 0)));
            inputDate.setText(intent.getStringExtra("published"));
            inputBody.setText(intent.getStringExtra("body"));

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateNews();
                }
            });
        }

        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        inputDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "date-picker");
    }

    private void loadSpinnerCategory() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list_category, android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        if (spinnerCategory != null) {
            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    category = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

        }
    }

    @Override
    public void onDateSet(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        inputDate.setText(sdf.format(calendar.getTime()));
        inputDate.setError(null);
    }

    public void getAllInput() {
        title = inputTitle.getText().toString();
        published = inputDate.getText().toString();
        body = inputBody.getText().toString();
        ageRating = Integer.parseInt(inputAge.getText().toString());
    }

    public void insertNews() {
        getAllInput();
        News news = new News(title, body, author, published, ageRating, category);
        NewsFirebase.insertNews(news);
        Toast.makeText(FormNewsActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
        NewsFirebase.getAllNewsByCategory(NewsActivity.categorySelected);
        super.onBackPressed();
    }


    private void updateNews() {
        getAllInput();
        News news = new News(title, body, author, published, ageRating, category);
        NewsFirebase.updateNews(news, newsId);
        Toast.makeText(FormNewsActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
        NewsFirebase.getAllNewsByCategory(NewsActivity.categorySelected);
        super.onBackPressed();
    }
}