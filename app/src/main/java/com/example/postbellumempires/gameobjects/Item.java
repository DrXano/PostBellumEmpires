package com.example.postbellumempires.gameobjects;

import com.google.firebase.database.Exclude;

public class Item {
    private String resource;
    private int quantity;

    public Item(){}

    public Item(String resource){
        this.resource = resource;
        this.quantity = 0;
    }

    public Item(String resource, int quantity){
        this.resource = resource;
        this.quantity = quantity;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Exclude
    public boolean isExhausted(){
        return this.quantity <= 0;
    }

    @Exclude
    public void increase(int quantity){
        this.quantity += quantity;
    }

    @Exclude
    public void decrease(int quantity){
        this.quantity -= quantity;
    }
}
