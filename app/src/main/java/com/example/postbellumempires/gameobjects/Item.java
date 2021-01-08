package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.GameResource;
import com.google.firebase.database.Exclude;

public class Item {
    private GameResource resource;
    private int quantity;

    public Item() {
    }

    public Item(GameResource resource) {
        this.resource = resource;
        this.quantity = 0;
    }

    public Item(GameResource resource, int quantity) {
        this.resource = resource;
        this.quantity = quantity;
    }

    public String getResource() {
        return resource.name();
    }

    public void setResource(String resource) {
        this.resource = GameResource.valueOf(resource);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Exclude
    public String getName() {
        return resource.name;
    }

    @Exclude
    public boolean isExhausted() {
        return this.quantity <= 0;
    }

    @Exclude
    public GameResource getResourceItem() {
        return this.resource;
    }

    @Exclude
    public void increase(int quantity) {
        this.quantity += quantity;
    }

    @Exclude
    public void decrease(int quantity) {

        this.quantity -= quantity;
        if (this.quantity < 0)
            this.quantity = 0;
    }
}
