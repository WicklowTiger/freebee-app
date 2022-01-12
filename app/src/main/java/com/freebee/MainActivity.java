package com.freebee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.freebee.firebase.FirebaseManager;
import com.freebee.shared.Callback;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        try {
//            FirebaseManager.runFunction("InitDatabase");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        this.initializeValues();
    }

    private void initializeValues() {
        FirebaseManager.getCollection("countries", (Callback<QuerySnapshot>) result -> {
            String[] countriesToLoad = Lists.newArrayList(result).stream().map(DocumentSnapshot::getId).toArray(String[]::new);
            Collections.reverse(Arrays.asList(countriesToLoad));
            loadCountries(countriesToLoad);
        });
    }

    private void selectCountry(String countryName) {
        FirebaseManager.getCollection("countries", (Callback<QuerySnapshot>) result -> {
            String[] countriesToLoad = Lists.newArrayList(result).stream().map(DocumentSnapshot::getId).toArray(String[]::new);
            Collections.reverse(Arrays.asList(countriesToLoad));
            loadCountries(countriesToLoad);
        });
    }

    private void loadCountries(String[] countries) {
        Spinner countrySpinner = findViewById(R.id.countrySpinner);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countries);
        countrySpinner.setAdapter(countryAdapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectCountry(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void selectCity(String cityName) {

    }

    private void loadCities(String[] cities) {
        Spinner countrySpinner = findViewById(R.id.citySpinner);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        countrySpinner.setAdapter(countryAdapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectCountry(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

}
