package hu.bugs.kolikaja;

import com.google.firebase.Timestamp;

import java.net.URI;

public class Food {
    private String name;
    private int price;
    private String userId;
    private Timestamp date;
    private URI imageUrl;
    private String foodLocation;
    private String description;
    private Timestamp expiration;

    public Food(String name, int price,String foodLocation, String userId, URI imageUrl, String description, Timestamp expiration) {
        this.name = name;
        this.price = price;
        this.foodLocation = foodLocation;
        this.userId = userId;
        this.date = Timestamp.now();
        this.imageUrl = imageUrl;
        this.description = description;
        this.expiration = expiration;
    }
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public URI getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(URI imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getExpiration() {
        return expiration;
    }

    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }

    public String getFoodLocation() {
        return foodLocation;
    }

    public void setFoodLocation(String foodLocation) {
        this.foodLocation = foodLocation;
    }
}
