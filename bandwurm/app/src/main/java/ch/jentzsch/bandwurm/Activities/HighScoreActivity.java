package ch.jentzsch.bandwurm.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.jentzsch.bandwurm.Classes.HighScore;
import ch.jentzsch.bandwurm.DB.HighScoreViewModel;
import ch.jentzsch.bandwurm.Helpers.HighScoreListAdapter;
import ch.jentzsch.bandwurm.Helpers.SharedPref;
import ch.jentzsch.bandwurm.R;

public class HighScoreActivity extends AppCompatActivity {

    //Referenz auf persistenten Speicher 'Shared Preferences'
    private SharedPref sharedPref;

    //Instanzvariable zwecks Referenzierung des Applikationskontexts
    private Context context;

    //Recyclerview zwecks Anzeige der Bestenliste
    private RecyclerView recListView;

    //Adapter zwecks Befüllung der Recyclerview
    private HighScoreListAdapter listAdapter;

    //Viewmodel zwecks Datenbankzugriff
    private HighScoreViewModel viewModel;

    //Steuerelemente des Layouts Highscore
    private EditText txtName;
    private Button btnSave;
    private TextView tvShowHighscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);

        //Überprüft ob Dark Theme angewendet werden soll
        if (sharedPref.loadNightModeState())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setzen des Layouts
        setContentView(R.layout.activity_high_score);

        //Erzeugen des Toolbars
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_white_24dp);
            getSupportActionBar().setTitle(R.string.highScore);
        }

        context = getApplicationContext();

        txtName = findViewById(R.id.txtName);
        btnSave = findViewById(R.id.btnSave);
        tvShowHighscore = findViewById(R.id.tvShowPoints);

        //Wenn Punktzahl gleich null ist dann wird EditText und Button deaktiviert
        if (sharedPref.getHighScore() == 0) {
            txtName.setEnabled(false);
            btnSave.setEnabled(false);
            tvShowHighscore.setText("");
            sharedPref.setHighScore(0);

        } else {
            txtName.setEnabled(true);
            btnSave.setEnabled(true);
            tvShowHighscore.setText(Integer.toString(sharedPref.getHighScore()));
        }

        //HighScore Liste Anzeigen
        recListView = findViewById(R.id.recScoreList);
        recListView.setHasFixedSize(true);
        recListView.setLayoutManager(new LinearLayoutManager(context));
        listAdapter = new HighScoreListAdapter(context);
        recListView.setAdapter(listAdapter);

        viewModel = new ViewModelProvider(this).get(HighScoreViewModel.class);
        viewModel.getHighScoresList().observe(this, new Observer<List<HighScore>>() {
            @Override
            public void onChanged(List<HighScore> highScores) {
                listAdapter.setHighScoreList(highScores);
            }
        });

        //Button Speichern Klick Methode
        btnSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int score = sharedPref.getHighScore();
               String name = txtName.getText().toString();
               if (name.isEmpty()) name = "No Name";
               HighScore highScore = new HighScore(name, score);
               viewModel.insert(highScore);
               txtName.setText("");
               tvShowHighscore.setText("");
               sharedPref.setHighScore(0);
               txtName.setEnabled(false);
               btnSave.setEnabled(false);
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

    //Override Methode onDestroy zwecks Entfernen des Zwischenspeichers
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPref.setHighScore(0);
    }
}
