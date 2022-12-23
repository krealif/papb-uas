package com.krealif.beritaku.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krealif.beritaku.R;
import com.krealif.beritaku.model.User;
import com.krealif.beritaku.news.NewsActivity;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText inputUsername, inputPassword;
    private static String username, password;
    public static SharedPreferences sharedPref;
    public static DatabaseReference userReference;

    public static final String sharedPrefFile = "com.krealif.beritaku";
    public static final String LOGIN_STATUS = "LOGIN_STATUS";
    public static final String USERNAME = "USERNAME";
    public static final String DOB = "DOB";
    private boolean isLoggedIn;

    // notification
    private NotificationManager notificationManager;
    private final static String CHANNEL_ID = "primary-channel";
    private int NOTIFICATION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        userReference = FirebaseDatabase.getInstance().getReference().child("User");

        inputUsername = findViewById(R.id.input_username);
        inputPassword = findViewById(R.id.input_password);

        sharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        isLoggedIn = sharedPref.getBoolean(LOGIN_STATUS, false);

        if (isLoggedIn) {
            Intent intent = new Intent(this, NewsActivity.class);
            startActivity(intent);
            finish();
        }

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = inputUsername.getText().toString();
                password = inputPassword.getText().toString();
                validate(username, password);
            }
        });

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void validate(String username, String password) {
        userReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null && password.equals(user.getPassword())) {
                    Intent intent = new Intent(LoginActivity.this, NewsActivity.class);
                    startActivity(intent);
                    setLoggedIn(username, user.getDob());
                    sendNotification();
                    finish();
                } else {
                    showAlertDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // notification
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "App Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

    private void setLoggedIn(String username, String dob) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(LOGIN_STATUS, true);
        editor.putString(USERNAME, username);
        editor.putString(DOB, dob);
        editor.apply();
    }

    public static void setLoggedOut() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(LOGIN_STATUS, false);
        editor.apply();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Error");
        alertBuilder.setMessage("Pastikan Anda sudah mengisikan username dan password dengan benar");

        alertBuilder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertBuilder.show();
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Hello")
                .setContentText("Welcome to Beritaku App")
                .setSmallIcon(R.drawable.ic_baseline_newspaper_24);
        return notifyBuilder;
    }

    private void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

}