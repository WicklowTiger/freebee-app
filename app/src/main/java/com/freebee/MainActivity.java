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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private String selectedCountry;

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

    /**
     * Loads initial values from firestore
     */
    private void initializeValues() {
        FirebaseManager.getCollection("countries", (Callback<QuerySnapshot>) result -> {
            String[] countriesToLoad = Lists.newArrayList(result).stream().map(DocumentSnapshot::getId).toArray(String[]::new);
            Collections.reverse(Arrays.asList(countriesToLoad));
            setCountries(countriesToLoad);
        });
    }

    /**
     * Load cities for selected country from firestore
     */
    private void selectCountry(String countryName) {
        this.selectedCountry = countryName;
        FirebaseManager.getCollection("countries/" + countryName + "/cities", (Callback<QuerySnapshot>) result -> {
            String[] citiesToLoad = Lists.newArrayList(result).stream().map(x -> (String) x.getData().get("name")).toArray(String[]::new);
            Collections.reverse(Arrays.asList(citiesToLoad));
            setCities(citiesToLoad);
        });
    }

    /**
     * Loads restaurants for selected city from firebase
     */
    private void selectCity(String cityName) {
        FirebaseManager.getDocumentByName("countries/" + this.selectedCountry + "/cities", cityName, (Callback<DocumentSnapshot>) result -> {
            @SuppressWarnings("unchecked")
            Map<String, ArrayList<Integer>> restaurants = (HashMap<String, ArrayList<Integer>>) result.get("restaurants");
            if(restaurants != null) {
                setRestaurants(restaurants);
            }
        });
    }

    private void setCountries(String[] countries) {
        Spinner countrySpinner = findViewById(R.id.countrySpinner);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countries);
        countrySpinner.setAdapter(countryAdapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectCountry(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setCities(String[] cities) {
        Spinner countrySpinner = findViewById(R.id.citySpinner);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        countrySpinner.setAdapter(countryAdapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectCity(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setRestaurants(Map<String, ArrayList<Integer>> restaurants) {
        for(String key: restaurants.keySet()) {
            ArrayList<Integer> prices = restaurants.get(key);
            if(prices != null && prices.size() == 2) {
                System.out.println("Panda price: " + prices.get(0));
                System.out.println("Tazz price: " + prices.get(1));
            }
        }
    }

}
