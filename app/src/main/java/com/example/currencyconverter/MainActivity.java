package com.example.currencyconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // UI elements
    EditText usernameEt, passwordEt;
    CheckBox rememberMe;
    SharedPreferences sp;
    Button loginBtn, signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // linking views with ids
        usernameEt = findViewById(R.id.usernameET);
        passwordEt = findViewById(R.id.passwordET);
        rememberMe = findViewById(R.id.rememberMe);
        loginBtn   = findViewById(R.id.loginBtn);
        signUpBtn  = findViewById(R.id.signupBtn);

        sp = getSharedPreferences("login_prefs",MODE_PRIVATE);

        //login the user if he already logged in and checked "remember me"
        String username = sp.getString("username",null);
        boolean stayLogged = sp.getBoolean("stayLogged",false);

        if(stayLogged && username!=null){
            Intent go = new Intent(MainActivity.this, CurrencyConverterActivity.class);
            go.putExtra("username", username);
            startActivity(go);

            // close this activity so user can't go back
            finish();

        }

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

                //save the username if "remember me" is checked
                SharedPreferences.Editor editor = sp.edit();
                if(rememberMe.isChecked()){
                    editor.putString("username",uname);
                    editor.putBoolean("stayLogged",true);
                }else{
                    editor.remove("username");
                    editor.putBoolean("stayLogged",false);
                }
                editor.apply();

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
