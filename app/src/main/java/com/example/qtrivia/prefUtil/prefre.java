package com.example.qtrivia.prefUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class prefre {
    public static final String HIGHEST_SCORE = "highestScore";
    public static final String APP_STATE = "app_state";
    private SharedPreferences preferences;


    public prefre(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveScore(int score){
        int recentScore = preferences.getInt(HIGHEST_SCORE, 0);


        if(score >recentScore){
             preferences.edit().putInt(HIGHEST_SCORE, score).apply();
        }
    }

    public int getHighest(){
        return preferences.getInt(HIGHEST_SCORE,0);
    }

    public void setState(int index){
        preferences.edit().putInt(APP_STATE,index).apply();
    }
    public int getState(){
        return preferences.getInt(APP_STATE,0);
    }
}
