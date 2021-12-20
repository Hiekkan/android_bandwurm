package ch.jentzsch.bandwurm.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

//SharedPreferences Klasse um Daten zwischen zu speichern
public class SharedPref {
    private SharedPreferences sharedPref;

    public SharedPref(Context context) {
        sharedPref = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    //Speichere Dark Theme Status Methode
    public void setNightModeState(boolean state) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Nightmode", state);
        editor.apply();
    }

    //Lade Dark Theme Status Methode
    public boolean loadNightModeState() {
        return sharedPref.getBoolean("Nightmode", false);
    }

    //Speichere Punktzahl Methode
    public void setHighScore(int score) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("HighScore", score);
        editor.apply();
    }

    //Lade Punktzahl Methode
    public int getHighScore() {
        return sharedPref.getInt("HighScore", 0);
    }

    //Speichere Schwierigkeit Methode
    public void setDifficulty(int time) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Difficulty", time);
        editor.apply();
    }

    //Lade Schwierigkeit Methode
    public int getDifficulty() {
        return sharedPref.getInt("Difficulty", 1000);
    }
}
