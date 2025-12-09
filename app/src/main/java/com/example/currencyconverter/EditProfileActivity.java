package com.example.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class EditProfileActivity extends AppCompatActivity {

    EditText etUser, etMail, etCountry;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_edit_profile);

        // toolbar stuff
        MaterialToolbar tb = findViewById(R.id.editToolbar);
        setSupportActionBar(tb);
        tb.setNavigationOnClickListener(v -> finish());

        // init views
        etUser = findViewById(R.id.etEditUsername);
        etMail = findViewById(R.id.etEditEmail);
        etCountry = findViewById(R.id.etEditCountry);
        btnSave = findViewById(R.id.btnSaveProfile);

        // get the username passed from the profile page
        String uName = getIntent().getStringExtra("username");

        // database things
        AppDatabase db = DatabaseClient.getDatabase(this);
        UserDao dao = db.userDao();
        User u = dao.getUserByUsername(uName);

        // fill fields if the user exists
        if (u != null) {
            etUser.setText(u.username);
            etMail.setText(u.email);
            etCountry.setText(u.country);
        }

        // save button listener
        btnSave.setOnClickListener(v -> {

            // update user values
            String newU = etUser.getText().toString().trim();
            String newE = etMail.getText().toString().trim();
            String newC = etCountry.getText().toString().trim();

            // quick validation
            if (newU.isEmpty() || newE.isEmpty() || newC.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }
            //show an alert dialog for the user to confirm changes
            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
            builder.setTitle("Save Changes");
            builder.setMessage("Are you sure you want to save changes?");
            builder.setPositiveButton("Yes", (dialog, which) -> {

                // update data inside object
                u.username = newU;
                u.email = newE;
                u.country = newC;

                // update in DB
                dao.updateUser(u);

                Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();

                // go back to main converter screen with updated username
                Intent i = new Intent(EditProfileActivity.this, CurrencyConverterActivity.class);
                i.putExtra("username", u.username);
                startActivity(i);
                finish();
            });
            builder.setNegativeButton("No", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }
}
