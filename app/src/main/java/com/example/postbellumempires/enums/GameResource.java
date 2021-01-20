package com.example.postbellumempires.enums;

import com.example.postbellumempires.R;
import com.example.postbellumempires.gameobjects.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public enum GameResource {
    //Abstract item
    BP(0, "Structure Blueprint", R.drawable.empty, 1, 1, true, true, R.string.ok),

    //Resources
    FOOD(1, "Food", R.drawable.food, 30, 50, false, false, R.string.foodDesc),
    WOOD(2, "Wood", R.drawable.wood, 25, 40, false, false, R.string.woodDesc),
    IRON(3, "Iron", R.drawable.iron, 15, 25, false, false, R.string.ironDesc),
    STONE(4, "Stone", R.drawable.stone, 20, 30, false, false, R.string.stoneDesc),
    KNOWLEDGE(5, "Knowledge", R.drawable.knowledge, 12, 28, false, false, R.string.knowledgeDesc),
    MEDICINE(6, "Medicine", R.drawable.medicine, 15, 25, false, false, R.string.medicineDesc),
    URANIUM(7, "Uranium", R.drawable.uranium, 1, 6, false, false, R.string.uraniumDesc),

    //Blueprints
    BARRACK_BP(8, "Barrack Blueprint", R.drawable.barrack, 1, 1, true, false, R.string.barrackDesc),
    STORAGE_BP(9, "Storage Blueprint", R.drawable.storage, 1, 1, true, false, R.string.storageDesc),
    VAULT_BP(10, "Vault Blueprint", R.drawable.vault, 1, 1, true, false, R.string.vaultDesc),
    ARMORY_BP(11, "Armory Blueprint", R.drawable.armory, 1, 1, true, false, R.string.armoryDesc),
    MEDBAY_BP(12, "Medbay Blueprint", R.drawable.medbay, 1, 1, true, false, R.string.medbayDesc),
    LABORATORY_BP(13, "Laboratory Blueprint", R.drawable.laboratory, 1, 1, true, false, R.string.laboratoryDesc);

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
    public final int description;

    GameResource(int id, String name, int image, int min, int max, boolean isBp, boolean isAbstract, int description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.min = min;
        this.max = max;
        this.isBp = isBp;
        this.isAbstract = isAbstract;
        this.description = description;
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
