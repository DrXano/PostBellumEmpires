package com.example.postbellumempires.utils;

import android.os.CountDownTimer;

import java.util.HashMap;
import java.util.Map;

public class CollectionCooldown {

    private static final int COOLDOWN_TIME = 180000; //3 minutes
    private static CollectionCooldown instance = null;

    private Map<String, CountDownTimer> timers;

    private CollectionCooldown(){
        this.timers = new HashMap<>();
    }

    public static CollectionCooldown getInstance(){
        if(instance == null)
            instance = new CollectionCooldown();

        return instance;
    }

    public boolean isCoolDownOn(String id){
        if(timers.containsKey(id)){
            return true;
        }else{
            final String tid = id;
            CountDownTimer timer = new CountDownTimer(COOLDOWN_TIME, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    timers.remove(tid);
                }
            }.start();
            this.timers.put(tid,timer);
            return false;
        }
    }
}
