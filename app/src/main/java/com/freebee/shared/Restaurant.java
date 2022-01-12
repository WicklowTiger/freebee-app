package com.freebee.shared;

import android.graphics.Color;

public class Restaurant {
    public final String name;
    public final Double pandaPrice;
    public final Double tazzPrice;

    public Restaurant(String name, String pandaPrice, String tazzPrice) {
        this.name = name;
        double tempPandaPrice = 0d;
        double tempTazzPrice = 0d;
        try {
            tempPandaPrice = Double.parseDouble(pandaPrice);
            tempTazzPrice = Double.parseDouble(tazzPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.pandaPrice = tempPandaPrice;
        this.tazzPrice = tempTazzPrice;
    }

    public int getPandaColor() {
        if (Double.compare(0d, this.pandaPrice) == 0) {
            return Color.GREEN;
        } else if (this.pandaPrice < 0) {
            return Color.RED;
        }
        if(Double.compare(0d, this.tazzPrice) >= 0) { return Color.GREEN;}
        int compareResult = Double.compare(this.pandaPrice, this.tazzPrice);
        if (compareResult > 0) {
            return Color.GREEN;
        } else if (compareResult == 0) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }

    public int getTazzColor() {
        if (Double.compare(0d, this.tazzPrice) == 0) {
            return Color.GREEN;
        } else if (this.tazzPrice < 0) {
            return Color.RED;
        }
        if(Double.compare(0d, this.pandaPrice) >= 0) { return Color.GREEN;}
        int compareResult = Double.compare(this.tazzPrice, this.pandaPrice);
        if (compareResult > 0) {
            return Color.GREEN;
        } else if (compareResult == 0) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }

    public static String priceToString(Double price) {
        if (price == 0) return "Freebee!";
        if (price < 0) return "No delivery";
        return price.toString();
    }


}
