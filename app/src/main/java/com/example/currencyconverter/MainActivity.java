package com.example.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // UI elements
    EditText usernameEt, passwordEt;
    Button loginBtn, signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // linking views with ids
        usernameEt = findViewById(R.id.usernameET);
        passwordEt = findViewById(R.id.passwordET);
        loginBtn   = findViewById(R.id.loginBtn);
        signUpBtn  = findViewById(R.id.signupBtn);

        // LOGIN BUTTON ACTION
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the user input
                String uname = usernameEt.getText().toString();
                String pw = passwordEt.getText().toString();

                // basic checks
                if(TextUtils.isEmpty(uname)){
                    usernameEt.setError("Please enter username");
                    return;
                }

                if(TextUtils.isEmpty(pw)){
                    passwordEt.setError("Please enter password");
                    return;
                }

                // database stuff
                AppDatabase db = DatabaseClient.getDatabase(MainActivity.this);
                UserDao dao = db.userDao();

                // check if user exists
                User currentUser = dao.getUserByUsername(uname);

                if(currentUser == null){
                    Toast.makeText(MainActivity.this,
                            "User not found. Please sign up first.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // hashing the password to compare
                String hashed = SecurityUtils.hashPassword(pw);

                // check password
                if(!hashed.equals(currentUser.password)){
                    passwordEt.setError("Wrong password");
                    return;
                }

                // login success → go to converter page
                Intent go = new Intent(MainActivity.this, CurrencyConverterActivity.class);
                go.putExtra("username", uname);
                startActivity(go);

                // close this activity so user can't go back
                finish();
            }
        });

        // SIGNUP BUTTON ACTION
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(s);
            }
        });
    }
}
