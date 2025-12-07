package com.example.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etPassword, etCountry;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // connecting xml to java
        etUsername = findViewById(R.id.signupUsername);
        etEmail    = findViewById(R.id.signupEmail);
        etPassword = findViewById(R.id.signupPassword);
        etCountry  = findViewById(R.id.signupCountry);
        btnCreate  = findViewById(R.id.btnCreateAccount);

        // database reference
        AppDatabase db = DatabaseClient.getDatabase(this);
        UserDao dao = db.userDao();

        // create new account
        btnCreate.setOnClickListener(v -> {

            String u = etUsername.getText().toString().trim();
            String e = etEmail.getText().toString().trim();
            String p = etPassword.getText().toString().trim();
            String c = etCountry.getText().toString().trim();

            // simple checks
            if(TextUtils.isEmpty(u) || TextUtils.isEmpty(e) ||
                    TextUtils.isEmpty(p) || TextUtils.isEmpty(c)) {

                Toast.makeText(SignupActivity.this,
                        "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // check if username already taken
            if(dao.getUserByUsername(u) != null){
                etUsername.setError("This username already exists");
                return;
            }

            // hash the password before saving it
            String hashedPw = SecurityUtils.hashPassword(p);

            // create user object
            User usr = new User();
            usr.username = u;
            usr.email = e;
            usr.password = hashedPw;
            usr.country = c;

            // save it
            dao.insertUser(usr);

            Toast.makeText(SignupActivity.this,
                    "Account created successfully", Toast.LENGTH_SHORT).show();

            // go back to login
            Intent goLogin = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(goLogin);
            finish();
        });
    }
}
