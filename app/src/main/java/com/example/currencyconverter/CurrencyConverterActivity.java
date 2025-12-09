package com.example.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class CurrencyConverterActivity extends AppCompatActivity {

    EditText etAmount;
    Spinner spFrom, spTo;
    Button btnConvert;
    TextView tvResult;

    JSONObject savedRates = null;   // used to store the first fetch result

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);

        // toolbar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // linking views
        etAmount   = findViewById(R.id.etAmount);
        spFrom     = findViewById(R.id.spinnerFrom);
        spTo       = findViewById(R.id.spinnerTo);
        btnConvert = findViewById(R.id.btnConvert);
        tvResult   = findViewById(R.id.tvResult);

        // load currency list once using USD
        fetchRates("USD", new RateCallback() {
            @Override
            public void onSuccess(JSONObject rates) {
                savedRates = rates;
                loadCurrencies(rates);
            }

            @Override
            public void onError(String msg) {
                tvResult.setText("Couldn't load currencies");
            }
        });

        // convert button
        btnConvert.setOnClickListener(v -> {

            if(etAmount.getText().toString().trim().isEmpty()){
                etAmount.setError("Enter amount");
                return;
            }

            if(savedRates == null){
                tvResult.setText("Rates not ready");
                return;
            }

            double amount = Double.parseDouble(etAmount.getText().toString());
            String from = spFrom.getSelectedItem().toString();
            String to   = spTo.getSelectedItem().toString();

            // fetch the rate for selected base
            fetchRates(from, new RateCallback() {
                @Override
                public void onSuccess(JSONObject rates) {
                    try {
                        double r = rates.getDouble(to);
                        double result = amount * r;

                        tvResult.setText(
                                String.format("%.2f %s = %.2f %s", amount, from, result, to)
                        );

                    } catch (JSONException e) {
                        tvResult.setText("Invalid currency");
                    }
                }

                @Override
                public void onError(String msg) {
                    tvResult.setText("Error converting");
                }
            });

        });
    }


    // toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_resource, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String username = getIntent().getStringExtra("username");

        int id = item.getItemId();
        if(id == R.id.nav_profile){
            Intent i = new Intent(this, UserProfileActivity.class);
            i.putExtra("username", username);
            startActivity(i);
            return true;
        }

        if(id == R.id.nav_chat){
            startActivity(new Intent(this, ChatActivity.class));
            return true;
        }

        if(id == R.id.nav_logout){
            //show an alert dialog for the user to confirm logout
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                //go back to login page
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
            builder.setNegativeButton("No", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    // fill the spinners
    private void loadCurrencies(JSONObject rates) {
        try {
            Iterator<String> keys = rates.keys();
            ArrayList<String> list = new ArrayList<>();

            while (keys.hasNext()) {
                list.add(keys.next());
            }

            Collections.sort(list);

            ArrayAdapter<String> adp = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    list
            );

            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spFrom.setAdapter(adp);
            spTo.setAdapter(adp);

            spFrom.setSelection(list.indexOf("USD"));
            spTo.setSelection(list.indexOf("EUR"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // callback interface
    interface RateCallback {
        void onSuccess(JSONObject rates);
        void onError(String msg);
    }

    // fetch function
    private void fetchRates(String base, RateCallback cb) {

        String url = "https://open.er-api.com/v6/latest/" + base;

        RequestQueue q = Volley.newRequestQueue(this);

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                res -> {
                    try {
                        JSONObject r = res.getJSONObject("rates");
                        cb.onSuccess(r);
                    } catch (Exception e) {
                        cb.onError("Parsing error");
                    }
                },
                err -> cb.onError("Network error")
        );

        q.add(req);
    }
}
