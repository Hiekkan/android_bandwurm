package ch.jentzsch.bandwurm.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import ch.jentzsch.bandwurm.DB.HighScoreRepository;
import ch.jentzsch.bandwurm.Helpers.SharedPref;
import ch.jentzsch.bandwurm.R;

public class SettingsActivity extends AppCompatActivity {

    //Referenz auf persistenten Speicher Shared Preferences
    private SharedPref sharedPref;

    //Referenz auf Applikationskontext
    private Context context;

    //Steuerelemente der Activity
    private Switch swDarkTheme;
    private Spinner spDifficulty;
    private Button btnClear;

    //Repository zwecks Datenbankzugriff
    private HighScoreRepository repository;

    private int spTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Überprüft ob Dark Theme angewendet werden soll
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //Setzen des Layouts
        setContentView(R.layout.activity_settings);

        //Zuweisung des Applikationskontext
        context = getApplicationContext();

        //Toolbar wird generiert
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_white_24dp);
            getSupportActionBar().setTitle(R.string.settings);
        }

        //Zuweisung der Steuerelemente
        swDarkTheme = findViewById(R.id.switchDarkTheme);
        spDifficulty = findViewById(R.id.spDifficulty);
        btnClear = findViewById(R.id.btnClear);

        TextView tvDarkTheme = findViewById(R.id.tvDarkTheme);

        //Überprüft gewünschten Zustand bzgl. des Dark Theme Schalters
        if (sharedPref.loadNightModeState()) {
            swDarkTheme.setChecked(true);
            spTextColor = tvDarkTheme.getCurrentTextColor();
        }
        else {
            swDarkTheme.setChecked(false);
            spTextColor = tvDarkTheme.getCurrentTextColor();
        }

        //Switch Methode um Theme zu wechseln
        swDarkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPref.setNightModeState(false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedPref.setNightModeState(true);
                }
                startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                finish();
            }
        });

        //Array an Spinner setzen
        //Erfolgt zwecks Auswahl des Schwierigkeitsgrades
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.difficulty_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDifficulty.setAdapter(adapter);

        //Spinner Auswahl setzen
        if (sharedPref.getDifficulty() != 1000) {
            switch (sharedPref.getDifficulty()) {
                case 1200:
                    spDifficulty.setSelection(0);
                    break;
                case 800:
                    spDifficulty.setSelection(1);
                    break;
                case 400:
                    spDifficulty.setSelection(2);
                    break;
            }
        }

        //Spinner Auswahl speichern
        //Hinterlegt den gewählten Schwierigkeitsgrad in den Shared Preferences
        spDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(spTextColor);
                switch (spDifficulty.getSelectedItem().toString()) {
                    //Ordnet jedem Schwierigkeitsgrad ein Zeitintervall zu
                    case "Einfach":
                        sharedPref.setDifficulty(1200);
                        break;
                    case "Mittel":
                        sharedPref.setDifficulty(800);
                        break;
                    case "Schwierig":
                        sharedPref.setDifficulty(400);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView)parent.getChildAt(0)).setTextColor(spTextColor);
                sharedPref.setDifficulty(1200);
            }
        });

        //Bestenliste Daten löschen Klick Methode
        repository = new HighScoreRepository(getApplication());
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this).setTitle(R.string.clearData).setMessage(R.string.clearMessage)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                repository.deleteAllHighScores();
                            }
                        }).setNegativeButton(R.string.no, null).setIcon(R.drawable.warning_yellow_24dp).show();
            }
        });
    }

    //Eventlistener des Zurück Knopfes
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Weiterleitung zum Hauptmenu
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
