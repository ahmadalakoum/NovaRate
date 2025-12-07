package com.example.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class UserProfileActivity extends AppCompatActivity {

    TextView tvUser, tvEmail, tvCountry;
    Button btnEdit;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_user_profile);

        // toolbar setup
        MaterialToolbar tb = findViewById(R.id.profileToolbar);
        setSupportActionBar(tb);
        tb.setNavigationOnClickListener(v -> finish());

        // init views
        tvUser = findViewById(R.id.username);
        tvEmail = findViewById(R.id.email);
        tvCountry = findViewById(R.id.country);
        btnEdit = findViewById(R.id.btnEdit);

        // getting username from the previous screen
        String uName = getIntent().getStringExtra("username");

        // database stuff
        AppDatabase db = DatabaseClient.getDatabase(this);
        UserDao dao = db.userDao();
        User u = dao.getUserByUsername(uName);

        // fill fields if user exists
        if (u != null) {
            tvUser.setText(u.username);
            tvEmail.setText(u.email);
            tvCountry.setText(u.country);
        }

        // edit button
        btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(UserProfileActivity.this, EditProfileActivity.class);
            i.putExtra("username", uName);
            startActivity(i);
        });
    }
}
