package com.freebee.shared;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.freebee.R;

import java.util.ArrayList;

public class RestaurantArrayAdapter extends ArrayAdapter<Restaurant> {
    private final Context context;
    private final ArrayList<Restaurant> data;
    private final int layoutResourceId;

    public RestaurantArrayAdapter(Context context, int layoutResourceId, ArrayList<Restaurant> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RestaurantItemViews itemViews;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            itemViews = new RestaurantItemViews();
            itemViews.restaurantNameView = row.findViewById(R.id.restaurantName);
            itemViews.foodPandaPriceView = row.findViewById(R.id.foodPandaPrice);
            itemViews.tazzPriceView = row.findViewById(R.id.tazzPrice);

            row.setTag(itemViews);
        }
        else {
            itemViews = (RestaurantItemViews) row.getTag();
        }

        Restaurant restaurant = data.get(position);

        itemViews.restaurantNameView.setText(restaurant.name);
        itemViews.foodPandaPriceView.setText(Restaurant.priceToString(restaurant.pandaPrice));
        itemViews.tazzPriceView.setText(Restaurant.priceToString(restaurant.tazzPrice));
        itemViews.foodPandaPriceView.setTextColor(restaurant.getPandaColor());
        itemViews.tazzPriceView.setTextColor(restaurant.getTazzColor());

        return row;
    }

    static class RestaurantItemViews
    {
        TextView restaurantNameView;
        TextView foodPandaPriceView;
        TextView tazzPriceView;
    }
}
