package com.example.project_app;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Product implements Parcelable {
    private String name;
    private String description;
    private double price;
    private int imageResourceId;
    private String category;
    private String color;
    private String gender;
    private String size;


    public Product(String name, String description, double price, int imageResourceId, String category, String color, String gender) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResourceId = imageResourceId;
        this.category = category;
        this.color = color;
        this.gender = gender;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }

    public String getGender() {
        return gender;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) &&
                Objects.equals(description, product.description) &&
                Objects.equals(category, product.category) &&
                Objects.equals(color, product.color) &&
                Objects.equals(gender, product.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, category, color, gender);
    }

    // Parcelable
    protected Product(Parcel in) {
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        imageResourceId = in.readInt();
        category = in.readString();
        color = in.readString();
        gender = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeDouble(price);
        parcel.writeInt(imageResourceId);
        parcel.writeString(category);
        parcel.writeString(color);
        parcel.writeString(gender);
    }
    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }
}

