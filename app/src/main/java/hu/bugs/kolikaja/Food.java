package hu.bugs.kolikaja;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class Food implements Parcelable {
    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
    private String userName;
    private Timestamp date;
    private String description;
    private Timestamp expiration;
    private String name;
    private int price;
    private Uri imageUrl;
    private String foodLocation;
    private String fileName;
    private String userId;
    private String message;

    public Food(Food food) {
        this.name = food.getName();
        this.price = food.getPrice();
        this.imageUrl = food.getImageUrl();
        this.foodLocation = food.getFoodLocation();
        this.userName = food.getName();
        this.date = food.getDate();
        this.description = food.getDescription();
        this.expiration = food.getExpiration();
        this.fileName = food.getFileName();
        this.userId = food.getUserId();
        this.message = food.getMessage();
    }

    public Food(String name, int price, Uri imageUrl, String foodLocation) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.foodLocation = foodLocation;
    }

    protected Food(Parcel in) {
        userName = in.readString();
        date = in.readParcelable(Timestamp.class.getClassLoader());
        description = in.readString();
        expiration = in.readParcelable(Timestamp.class.getClassLoader());
        name = in.readString();
        price = in.readInt();
        imageUrl = in.readParcelable(Uri.class.getClassLoader());
        foodLocation = in.readString();
        fileName = in.readString();
        userId = in.readString();
        message = in.readString();
    }

    public Food(Map<String, Object> map) {
        this.name = (String) map.get("name");
        Long l = (Long) map.get("price");
        this.price = l.intValue();
        this.imageUrl = Uri.parse((String) map.get("imageUrl"));
        this.foodLocation = (String) map.get("foodLocation");
        this.userName = (String) map.get("userName");
        this.date = (Timestamp) map.get("date");
        this.description = (String) map.get("description");
        this.expiration = (Timestamp) map.get("expiration");
        this.fileName = (String) map.get("fileName");
        this.userId = (String) map.get("userId");
        this.message = (String) map.get("message");
    }

    public HashMap<String, Object> getHashMap(){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("name", name);
        temp.put("price", price);
        temp.put("imageUrl", imageUrl);
        temp.put("foodLocation", foodLocation);
        temp.put("userName", userName);
        temp.put("date", date);
        temp.put("description", description);
        temp.put("expiration", expiration);
        temp.put("fileName", fileName);
        temp.put("userId", userId);
        temp.put("message", message);

        return temp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUserName() {
        return userName;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getExpiration() {
        return expiration;
    }

    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }

    @Override
    public String toString() {
        return "Food{" +
                "userName='" + userName + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", expiration=" + expiration +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl=" + imageUrl +
                ", foodLocation='" + foodLocation + '\'' +
                ", fileName='" + fileName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFoodLocation() {
        return foodLocation;
    }

    public void setFoodLocation(String foodLocation) {
        this.foodLocation = foodLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeParcelable(date, i);
        parcel.writeString(description);
        parcel.writeParcelable(expiration, i);
        parcel.writeString(name);
        parcel.writeInt(price);
        parcel.writeParcelable(imageUrl, i);
        parcel.writeString(foodLocation);
        parcel.writeString(fileName);
        parcel.writeString(userId);
        parcel.writeString(message);
    }
}