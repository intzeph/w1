package com.example.wildtech;

public class item {
    private String productID;
    private String name;
    private String location;
    private String price;
    private String imageURL;
    private int rating;

    public item() {
        // Default constructor required for Firebase
    }

    public item(String productID, String name, String location, String price, String imageURL, int rating) {
        this.productID = productID;
        this.name = name;
        this.location = location;
        this.price = price;
        this.imageURL = imageURL;
        this.rating = rating;
    }
    public String getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getRating() {
        return rating;
    }
}
