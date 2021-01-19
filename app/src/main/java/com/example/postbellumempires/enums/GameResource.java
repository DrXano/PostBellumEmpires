package com.example.postbellumempires.enums;

import com.example.postbellumempires.gameobjects.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public enum GameResource {
    //Abstract item
    BP(0, "Structure Blueprint", 1000, 1, 1, true, true),

    //Resources
    FOOD(1, "Food", 1000, 30, 50, false, false),
    WOOD(2, "Wood", 1000, 25, 40, false, false),
    IRON(3, "Iron", 1000, 15, 25, false, false),
    STONE(4, "Stone", 1000, 20, 30, false, false),
    KNOWLEDGE(5, "Knowledge", 1000, 12, 28, false, false),
    MEDICINE(6, "Medicine", 1000, 15, 25, false, false),
    URANIUM(7, "Uranium", 1000, 1, 6, false, false),

    //Blueprints
    BARRACK_BP(8, "Barrack Blueprint", 1000, 1, 1, true, false),
    STORAGE_BP(9, "Storage Blueprint", 1000, 1, 1, true, false),
    VAULT_BP(10, "Vault Blueprint", 1000, 1, 1, true, false),
    ARMORY_BP(11, "Armory Blueprint", 1000, 1, 1, true, false),
    MEDBAY_BP(12, "Medbay Blueprint", 1000, 1, 1, true, false),
    LABORATORY_BP(13, "Laboratory Blueprint", 1000, 1, 1, true, false);

    private static final List<GameResource> bps = new ArrayList<>();
    private static final Map<Integer, GameResource> BY_ID = new HashMap<>();

    static {
        for (GameResource gr : values()) {
            BY_ID.put(gr.id, gr);

            if (gr.isBp && !gr.isAbstract)
                bps.add(gr);
        }
    }

    public final int id;
    public final String name;
    public final int image;
    public final int min;
    public final int max;
    public final boolean isBp;
    public final boolean isAbstract;

    GameResource(int id, String name, int image, int min, int max, boolean isBp, boolean isAbstract) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.min = min;
        this.max = max;
        this.isBp = isBp;
        this.isAbstract = isAbstract;
    }

    public static GameResource valueOfID(int id) {
        return BY_ID.get(id);
    }

    public Item getReward(double multiplier) {
        Item reward;
        if (isAbstract) {
            Random r = new Random();
            reward = new Item(bps.get(r.nextInt(bps.size())), 1);
            return reward;
        } else {
            if (isBp) {
                reward = new Item(this, 1);
            } else {
                Random r = new Random();
                int value = (int) ((r.nextInt(max - min) + min) * multiplier);
                reward = new Item(this, value);
            }
            return reward;
        }
    }
}
