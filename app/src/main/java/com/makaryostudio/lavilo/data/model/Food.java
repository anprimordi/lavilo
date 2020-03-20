package com.makaryostudio.lavilo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Food implements Parcelable {

    String imageUrl;
    String name;
    String price;
    String stock;
    String key;

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

    public Food() {
    }

    public Food(String imageUrl, String name, String price, String stock) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    protected Food(Parcel in) {
        imageUrl = in.readString();
        name = in.readString();
        price = in.readString();
        stock = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(stock);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @Exclude
    @com.google.firebase.firestore.Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    @com.google.firebase.firestore.Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
