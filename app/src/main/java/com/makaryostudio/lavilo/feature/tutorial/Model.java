package com.makaryostudio.lavilo.feature.tutorial;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Model implements Parcelable {
    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };
    String image;
    String name;
    String price;
    String quantity;
    String threshold;
    String key;

    protected Model(Parcel in) {
        image = in.readString();
        name = in.readString();
        price = in.readString();
        quantity = in.readString();
        threshold = in.readString();
    }

    public Model() {
    }

    public Model(String image, String name, String price, String quantity, String threshold) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.threshold = threshold;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(quantity);
        dest.writeString(threshold);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
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
