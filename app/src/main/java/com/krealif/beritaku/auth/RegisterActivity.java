package com.krealif.beritaku.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krealif.beritaku.model.User;
import com.krealif.beritaku.utils.DatePickerFragment;
import com.krealif.beritaku.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements DatePickerFragment.DatePickerFragmentListener {

    public static DatabaseReference userReference;
    private TextInputEditText inputUsername, inputPassword, inputDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userReference = FirebaseDatabase.getInstance().getReference().child("User");

        inputUsername = findViewById(R.id.input_username);
        inputPassword = findViewById(R.id.input_password);
        inputDate = findViewById(R.id.input_date);

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

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.super.onBackPressed();
            }
        });

    }

    private void registerUser() {
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();
        String dob = inputDate.getText().toString();

        if (username.isEmpty() || password.isEmpty() || dob.isEmpty()) {
            showAlertDialog("Pastikan Anda sudah mengisikan semua kolom");
        } else {
            register(new User(username, password, dob));
        }
    }

    private void register(User user) {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = user.getUsername();
                if (snapshot.hasChild(username)) {
                    showAlertDialog("Username telah tersedia");
                } else {
                    user.setUsername(null);
                    userReference.child(username).setValue(user);
                    Toast.makeText(RegisterActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Error");
        alertBuilder.setMessage(message);

        alertBuilder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertBuilder.show();
    }

    private void showDatePicker() {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "date-picker");
    }

    @Override
    public void onDateSet(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        inputDate.setText(sdf.format(calendar.getTime()));
        inputDate.setError(null);
    }
}