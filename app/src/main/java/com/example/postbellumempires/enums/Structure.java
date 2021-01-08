package com.example.postbellumempires.enums;

import com.example.postbellumempires.R;
import com.example.postbellumempires.gameobjects.Item;
import com.example.postbellumempires.gameobjects.PlaceBonuses;

import java.util.HashMap;
import java.util.Map;

public enum Structure {
    NONE("None", R.drawable.empty, new Item[]{}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
        }
    },
    BARRACK("Barrack", R.drawable.barrack, new Item[]{new Item(GameResource.BARRACK_BP, 1)}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
            bonuses.addCapacity(5); //The place gains 5 more capacity to hold more units
        }
    },
    STORAGE("Storage", R.drawable.storage, new Item[]{new Item(GameResource.STORAGE_BP, 1)}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
            bonuses.addResourceBonus(0.2); //The players whose faction owns the place get +20% of resources when collecting them;
        }
    },
    VAULT("Vault", R.drawable.vault, new Item[]{new Item(GameResource.VAULT_BP, 1)}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
            bonuses.addResourcePenalty(0.1); //The players will get -10% of resources when stealing from a place owned by the enemy;
        }
    },
    ARMORY("Armory", R.drawable.armory, new Item[]{new Item(GameResource.ARMORY_BP, 1)}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
            bonuses.addAttack(50);
            bonuses.addArmor(30); //Units will get +50 attack points and +30 armor points
        }
    },
    MEDBAY("Medbay", R.drawable.medbay, new Item[]{new Item(GameResource.MEDBAY_BP, 1)}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
            bonuses.addHealth(100); //Units will get +100 health points
        }
    },
    LABORATORY("Laboratory", R.drawable.laboratory, new Item[]{new Item(GameResource.LABORATORY_BP, 1)}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
            bonuses.addSpeed(100); //Units will get +100 speed points
        }
    };

    private static final Map<String, Structure> BY_NAME = new HashMap<>();

    static {
        for (Structure s : values()) {
            BY_NAME.put(s.name, s);
        }
    }

    public final String name;
    public final int imageId;
    public final Item[] cost;

    Structure(String name, int imageId, Item[] cost) {
        this.name = name;
        this.imageId = imageId;
        this.cost = cost;
    }

    public static Structure valueOfName(String name) {
        return BY_NAME.get(name);
    }

    public abstract void applyBonus(PlaceBonuses bonuses);
}
