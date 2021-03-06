package com.example.postbellumempires.enums;

import com.example.postbellumempires.R;
import com.example.postbellumempires.gameobjects.Item;
import com.example.postbellumempires.gameobjects.PlaceBonuses;

import java.util.HashMap;
import java.util.Map;

public enum Structure {
    NONE("None", true, R.drawable.empty, new Item[]{}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
        }
    },
    BARRACK("Barrack", false, R.drawable.barrack, new Item[]{new Item(GameResource.BARRACK_BP, 1), new Item(GameResource.WOOD, 100), new Item(GameResource.STONE, 100)}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
            bonuses.addCapacity(5); //The place gains 5 more capacity to hold more units
        }
    },
    STORAGE("Storage", false, R.drawable.storage, new Item[]{new Item(GameResource.STORAGE_BP, 1), new Item(GameResource.WOOD, 100), new Item(GameResource.STONE, 50), new Item(GameResource.IRON, 50)}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
            bonuses.addResourceBonus(0.2); //The players whose faction owns the place get +20% of resources when collecting them;
        }
    },
    VAULT("Vault", false, R.drawable.vault, new Item[]{new Item(GameResource.VAULT_BP, 1), new Item(GameResource.IRON, 100), new Item(GameResource.WOOD, 100), new Item(GameResource.STONE, 50)}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
            bonuses.addResourcePenalty(0.1); //The players will get -10% of resources when stealing from a place owned by the enemy;
        }
    },
    ARMORY("Armory", false, R.drawable.armory, new Item[]{new Item(GameResource.ARMORY_BP, 1), new Item(GameResource.IRON, 200), new Item(GameResource.WOOD, 100)}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
            bonuses.addAttack(20);
            bonuses.addArmor(10); //Units will get +20 attack points and +10 armor points
        }
    },
    MEDBAY("Medbay", false, R.drawable.medbay, new Item[]{new Item(GameResource.MEDBAY_BP, 1), new Item(GameResource.MEDICINE, 150), new Item(GameResource.STONE, 100), new Item(GameResource.WOOD, 50)}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
            bonuses.addHealth(50); //Units will get +50 health points
        }
    },
    LABORATORY("Laboratory", false, R.drawable.laboratory, new Item[]{new Item(GameResource.LABORATORY_BP, 1), new Item(GameResource.URANIUM, 20), new Item(GameResource.IRON, 100), new Item(GameResource.STONE, 100)}) {
        @Override
        public void applyBonus(PlaceBonuses bonuses) {
            bonuses.addSpeed(10); //Units will get +10 speed points
        }
    };

    private static final Map<String, Structure> BY_NAME = new HashMap<>();

    static {
        for (Structure s : values()) {
            BY_NAME.put(s.name, s);
        }
    }

    public final String name;
    public final boolean isEmpty;
    public final int imageId;
    public final Item[] cost;

    Structure(String name, boolean isEmpty, int imageId, Item[] cost) {
        this.name = name;
        this.isEmpty = isEmpty;
        this.imageId = imageId;
        this.cost = cost;
    }

    public static Structure valueOfName(String name) {
        return BY_NAME.get(name);
    }

    public abstract void applyBonus(PlaceBonuses bonuses);
}
