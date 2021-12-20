package ch.jentzsch.bandwurm.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import ch.jentzsch.bandwurm.Classes.WormView;
import ch.jentzsch.bandwurm.Helpers.SharedPref;
import ch.jentzsch.bandwurm.R;

public class GameActivity extends AppCompatActivity {

    //Referenz auf persistenten Speicher 'Shared Preferences'
    private SharedPref sharedPref;

    private Context context;

    private TextView tvScore;
    private ImageButton btnLeft;
    public ImageButton btnPause;
    private ImageButton btnRight;
    private View contentView;

    //Referenzvariable des Spielfeldes
    private WormView wormView;

    //Spielstand zwecks Anzeige
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);

        //Überprüft ob Dark Theme angewendet werden soll
        if (sharedPref.loadNightModeState())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setzen des entsprechenden Layouts
        setContentView(R.layout.activity_game);

        //Applikationskontext wird mit Instanzvariable referenziert
        context = getApplicationContext();

        //Setzen der Steuerelemente
        contentView = findViewById(R.id.frameGame);
        tvScore = findViewById(R.id.tvScore);
        btnLeft = findViewById(R.id.btnLeft);
        btnPause = findViewById(R.id.btnPause);
        btnRight = findViewById(R.id.btnRight);

        //Ausgangspielstand wird in der Anzeige gesetzt
        tvScore.setText(Integer.toString(score));

        //Generiert Spielsteuerung
        btnPause.setBackgroundResource(R.drawable.btn_play);

        //View des Spielfeldes wird instanziert und gesetzt
        wormView = new WormView(context, this);
        ((FrameLayout) contentView).addView(wormView);

        //Button Links Klick Methode
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wormView.changeDirection(-1);
            }
        });

        //Button Rechts Klick Methode
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wormView.changeDirection(1);
            }
        });

        //Button Pause Klick Methode
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wormView.pressPauseBtn();
            }
        });
    }

    //Methode zwecks Anzeigen des Game-Over Dialogs
    public void showGameOverDialog()
    {
        //Aktueller Punktestand wird innerhalb der 'Shared Preferences' zwischengespeichert
        sharedPref.setHighScore(score);

        //Instanzierung und Aufruf des Game-Over Dialogs
        GameOverDialog gdd = new GameOverDialog(GameActivity.this);
        gdd.getWindow().setBackgroundDrawableResource(R.color.game_over_background);
        gdd.show();
    }

    //Punktzahl addieren und anzeigen
    public void increaseScore(int increase)
    {
        score += increase;
        tvScore.setText(Integer.toString(score));
    }

    //Verschachtelte Klasse des Game-Over Dialogs
    //Verschachtelung zwecks vereinfachter Kommunikation mit der Klasse GameActivity
    protected class GameOverDialog extends Dialog implements View.OnClickListener {

        //Steuerelemente des Game-Over Dialogs
        private Button btnNew;
        private Button btnEnd;
        private Button btnScore;
        private TextView tvOverScore;

        public GameOverDialog(@NonNull Context context) {
            //Aufruf des Superkonstruktors zwecks graphischer Darstellung
            super(context, android.R.style.Theme_NoTitleBar_Fullscreen);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            //Setzen des entsprechenden Layouts
            //In diesem Falle Dialog
            setContentView(R.layout.gameover);


            //Zuweisung der Steuerelemente
            btnNew = findViewById(R.id.btnNew);
            btnEnd = findViewById(R.id.btnEnd);
            btnScore = findViewById(R.id.btnScore);
            tvOverScore = findViewById(R.id.tvOverScore);
            tvOverScore.setText(Integer.toString(score));

            btnNew.setOnClickListener(this);
            btnEnd.setOnClickListener(this);
            btnScore.setOnClickListener(this);
        }

        //Eventlistener zwecks Steuerelemente des Game-Over Dialogs
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnScore:
                    //Weiterleitung zur Activity des Highscores zwecks Eintragen des Spielstandes
                    Intent i = new Intent(getContext(), HighScoreActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    break;
                case R.id.btnNew:
                    //Weiterleitung zur Activity des Spiels zwecks dem Starten eines neuen Spiels
                    i = new Intent(context, GameActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    break;
                case R.id.btnEnd:
                    //Weiterleitung zum Startmenu
                    i = new Intent(context, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    break;
                default:
                    break;
            }
        }
    }
}
