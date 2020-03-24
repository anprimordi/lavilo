package com.makaryostudio.lavilo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Drink implements Parcelable {

    public static final Creator<Drink> CREATOR = new Creator<Drink>() {
        @Override
        public Drink createFromParcel(Parcel in) {
            return new Drink(in);
        }

        @Override
        public Drink[] newArray(int size) {
            return new Drink[size];
        }
    };
    private String imageUrl;
    private String name;
    private String price;
    private String stock;
    private String key;

    public Drink() {
    }

    private Drink(Parcel in) {
        imageUrl = in.readString();
        name = in.readString();
        price = in.readString();
        stock = in.readString();
    }

    public Drink(String imageUrl, String name, String price, String stock) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public static Creator<Drink> getCREATOR() {
        return CREATOR;
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
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
