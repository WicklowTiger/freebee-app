package com.freebee;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.freebee.firebase.FirebaseManager;
import com.freebee.shared.Callback;
import com.freebee.shared.Restaurant;
import com.freebee.shared.RestaurantArrayAdapter;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentSnapshot;

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
        FirebaseManager.getCollection("countries", result -> {
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
        FirebaseManager.getCollection("countries/" + countryName + "/cities", result -> {
            String[] citiesToLoad = Lists.newArrayList(result).stream().map(x -> (String) x.getData().get("name")).toArray(String[]::new);
            Collections.reverse(Arrays.asList(citiesToLoad));
            setCities(citiesToLoad);
        });
    }

    /**
     * Loads restaurants for selected city from firebase
     */
    private void selectCity(String cityName) {
        FirebaseManager.getDocumentByName("countries/" + this.selectedCountry + "/cities", cityName, result -> {
            @SuppressWarnings("unchecked")
            Map<String, ArrayList<?>> restaurants = (HashMap<String, ArrayList<?>>) result.get("restaurants");
            if(restaurants != null) {
                setRestaurants(restaurants);
            } else {
                ((ListView) findViewById(R.id.restaurantsView)).setAdapter(null);
            }
        });
    }

    private void setCountries(String[] countries) {
        Spinner countrySpinner = findViewById(R.id.countrySpinner);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries);
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
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
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

    private void setRestaurants(Map<String, ArrayList<?>> restaurants) {
        ListView restaurantsView = findViewById(R.id.restaurantsView);
        ArrayList<Restaurant> dataSource = new ArrayList<>();
        for(String key: restaurants.keySet()) {
            ArrayList<?> prices = restaurants.get(key);
            if(prices != null && prices.size() == 2) {
                dataSource.add(new Restaurant(key, prices.get(1).toString(), prices.get(0).toString()));
            }
        }
        RestaurantArrayAdapter restaurantsAdapter = new RestaurantArrayAdapter(this, R.layout.restaurant_item, dataSource);
        restaurantsView.setAdapter(restaurantsAdapter);
    }

}
