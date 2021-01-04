package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.GameResource;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Inventory {

    private String invOwner;
    private HashMap<String, Item> inv;

    public Inventory() {
    }

    public Inventory(String owner) {
        this.inv = new HashMap<>();
        this.invOwner = owner;
    }

    public HashMap<String, Item> getInv() {
        return inv;
    }

    public void setInv(HashMap<String, Item> inv) {
        this.inv = inv;
    }

    @Exclude
    public List<Item> getInventory() {
        if (this.inv != null) {
            return new ArrayList<>(this.inv.values());
        } else {
            return null;
        }
    }

    @Exclude
    public void addItem(GameResource resource, int quantity) {
        if (this.inv == null) {
            this.inv = new HashMap<>();
        }
        String resName = resource.name();
        if (this.inv.containsKey(resName)) {
            Item i = this.inv.get(resName);
            i.increase(quantity);
            this.inv.put(resName, i);
        } else {
            this.inv.put(resName, new Item(resource, quantity));
        }
    }

    @Exclude
    public void removeItem(GameResource resource, int quantity) {
        if (this.inv == null) {
            this.inv = new HashMap<>();
        }
        String resName = resource.name();
        if (this.inv.containsKey(resName)) {
            Item i = this.inv.get(resName);
            i.decrease(quantity);
            this.inv.put(resName, i);

            if (i.isExhausted()) {
                this.inv.remove(resName);
            }
        }
    }

    @Exclude
    public boolean hasItem(GameResource resource) {
        return this.inv.containsKey(resource.name());
    }

    public String getInvOwner() {
        return invOwner;
    }

    public void setInvOwner(String invOwner) {
        this.invOwner = invOwner;
    }

    @Exclude
    public boolean hasEnough(Item i) {
        if (this.inv == null)
            return false;

        String resName = i.getResourceItem().name();

        if (this.inv.containsKey(resName)) {
            Item itm = this.inv.get(resName);

            return i.getQuantity() <= itm.getQuantity();
        } else {
            return false;
        }
    }

    @Exclude
    public boolean hasEnough(Item[] items) {
        for (Item i : items) {
            if (!this.hasEnough(i))
                return false;
        }
        return true;
    }

    @Exclude
    public void remove(Item[] items) {
        for (Item i : items) {
            this.removeItem(i.getResourceItem(), i.getQuantity());
        }
    }
}
