package com.example.a10021740.jaskoo;


import android.widget.ImageView;

public class Card {

    private String calories;
    private String protein;
    private String carbs;
    private String fat;
    private String productName;
    private String ImageURL;
    private int id;
    private int guidingStarPoints;

    public Card(String productName, String calories, String protein, String carbs, String fat, String ImageURL, int guidingStarPoints) {
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.ImageURL = ImageURL;
        this.guidingStarPoints = guidingStarPoints;
        this.productName = productName;
    }

    public String getCalories() {
        return calories;
    }
    public String getProductName() {
        return productName;
    }


    public String getProtein() {
        return protein;
    }

    public String getCarbs() {
        return carbs;
    }

    public String getFat() {
        return fat;
    }

    public String getImageURL() { return ImageURL;
    }

    public int getGuidingStarPoints() { return guidingStarPoints; }

}
