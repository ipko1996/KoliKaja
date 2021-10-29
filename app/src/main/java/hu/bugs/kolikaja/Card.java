package hu.bugs.kolikaja;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

public class Card {
    private String foodName;
    private int foodPrice;
    private Uri foodImage;
    private String foodLocation;

    public Card(String foodName, int foodPrice, Uri foodImage, String foodLocation) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImage = foodImage;
        this.foodLocation = foodLocation;
    }

    public Card(Map<String, Object> map) {
        this.foodName = (String) map.get("name");
        Long l= Long.valueOf((Long) map.get("price"));
        this.foodPrice = l.intValue();
        this.foodImage = Uri.parse((String) map.get("imageUrl"));
        this.foodLocation = (String) map.get("foodLocation");
    }

    @NonNull
    @Override
    public String toString() {
        return this.foodName + " " + this.foodLocation;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public Uri getFoodImage() {
        return foodImage;
    }

    public String getFoodLocation() {
        return foodLocation;
    }
}