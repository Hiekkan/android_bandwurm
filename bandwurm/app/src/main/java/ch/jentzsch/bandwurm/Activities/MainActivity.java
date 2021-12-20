package ch.jentzsch.bandwurm.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import ch.jentzsch.bandwurm.Helpers.SharedPref;
import ch.jentzsch.bandwurm.R;

public class MainActivity extends AppCompatActivity {
    private SharedPref sharedPref;
    private Context context;

    private Button btnStart;
    private Button btnHighscore;
    private Button btnExit;
    private ImageButton btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);

        //Überprüft ob Dark Theme angewendet werden soll
        if (sharedPref.loadNightModeState())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.menu);
        }

        context = getApplicationContext();

        btnStart = findViewById(R.id.btnStart);
        btnHighscore = findViewById(R.id.btnHighScore);
        btnExit = findViewById(R.id.btnExit);
        btnSettings = findViewById(R.id.btnSettings);

        if(sharedPref.loadNightModeState())
            btnSettings.setBackgroundResource(R.drawable.settings_white_24dp);
        else
            btnSettings.setBackgroundResource(R.drawable.settings_black_24dp);

        //Start Button Klick Methode
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Weiterleitung Spiel starten
                Intent i = new Intent(context, GameActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //HighScore Button Klick Methode
        btnHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Weiterleitung Bestenliste
                Intent i = new Intent(context, HighScoreActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //Exit Button Klick Methode
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Applikation wird beendet
                finishAffinity();
                System.exit(0);
            }
        });

        //Settings Button Klick Methode
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SettingsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
    }
}
