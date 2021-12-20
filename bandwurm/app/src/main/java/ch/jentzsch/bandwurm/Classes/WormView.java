package ch.jentzsch.bandwurm.Classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ch.jentzsch.bandwurm.Activities.GameActivity;
import ch.jentzsch.bandwurm.Helpers.Directions;
import ch.jentzsch.bandwurm.Helpers.SharedPref;
import ch.jentzsch.bandwurm.R;

public class WormView extends View {
    //ArrayList welche der Verwaltung der Segmente des Wurms dienen soll
    private ArrayList<Rect> worm = new ArrayList<>();

    //Variablen zwecks direkter Adressierung des Kopfes und des Apfels
    private Rect apple, head;

    // Breite und Hoehe der Segmente
    private int wormWidth = 40;
    private int wormHeight = 40;

    //Handler welcher der Verwaltung des Runnable threads dienen soll
    private Handler handler = new Handler();

    //Instanzierung eines Zufallsgenerators zwecks Platzierung der Äpfel auf dem Spielfeld
    private Random rnd = new Random();

    //Variable zwecks Speicherung der aktuellen Richtung der Bewegung des Wurmes
    private int numDirection = 1;

    //Hashmap welche den Richtungswerten des Enums Directions einen Integerwert zuordnet
    private HashMap<Integer, Directions> directionMap = new HashMap<>();

    //Variable zwecks der Verwaltung der Richtungswerte
    private Directions direction = Directions.LEFT;

    //Variable zwecks Ansprechbarkeit der Shared Preferences welche als minimalistischer persistenter Speicher fungieren
    private SharedPref sharedPref;

    //Kontextvariable der aktuellen übergeordneten Activity
    private Context context;

    //Referenzvariable zwecks direkten Ansprechens der Übergeordneten Activity
    private GameActivity gameActivity;

    //boolean zwecks Verwaltung des Pausezustands
    private boolean isPaused = true;

    //boolean zwecks Prüfung ob ein Spiel erstmals gestartet wurde
    private boolean gameStarted = false;

    //boolean zwecks Pruefung ob Spiel vorbei
    private boolean gameOver = false;

    //Breite vom Rand des Spielfelds als Integerwert
    private int borderThicknessInPixel;


    //Konstruktormethode der Wormview
    public WormView(Context context, GameActivity gameActivity) {
        super(context);
        this.context = context;
        Resources re = getResources();

        borderThicknessInPixel = re.getDimensionPixelOffset(R.dimen.border_thickness);
        this.gameActivity = gameActivity;
        //Hashmap zwecks Richtungswechsel wird generiert
        //Zu diesem Zweck wird jeder Richtung der Enum struktur ein Integer-Wert zugeordnet
        directionMap.put(1, Directions.LEFT);
        directionMap.put(2, Directions.UP);
        directionMap.put(3, Directions.RIGHT);
        directionMap.put(4, Directions.DOWN);
        preparePlayground();
    }

    //Mit dieser Methode wird der Kopf des Wurm zu Beginn des Spiels auf dem Spielfeld platziert
    private void preparePlayground()
    {
        head = new Rect(10 * wormWidth + borderThicknessInPixel,15 * wormHeight + borderThicknessInPixel,11 * wormWidth + borderThicknessInPixel,16 * wormHeight + borderThicknessInPixel);
        worm.add(head);
    }

    //Diese Methode dient dem erzeugen neuer Äpfel mit randomisierter Position
    private void setApple(Canvas canvas)
    {
        int appleXPos = rnd.nextInt((canvas.getWidth() - 2 * borderThicknessInPixel) / 40);
        int appleYPos = rnd.nextInt((canvas.getHeight() - 2 * borderThicknessInPixel) / 40);
        apple = new Rect(appleXPos * 40 + borderThicknessInPixel, appleYPos * 40 + borderThicknessInPixel, appleXPos * 40 + 40 + borderThicknessInPixel, appleYPos * 40 + 40 + borderThicknessInPixel);
    }

    //Wurm Bewegungslogik
    //Diese Methode koordiniert die Bewegung des Wurmes, und wird während der Lebensdauer des Spiels einmal pro Timerzyklus aufgerufen
    private void moveWorm()
    {
        Rect head = worm.get(0);
        int oldX = head.left;
        int oldY = head.top;
        int oldX2 = head.right;
        int oldY2 = head.bottom;

        worm.get(0).left += wormWidth * direction.getX();
        worm.get(0).top += wormHeight * direction.getY();
        worm.get(0).right += wormWidth * direction.getX();
        worm.get(0).bottom += wormHeight * direction.getY();
        if(worm.size() > 1)
        {
            worm.add(1, worm.get(worm.size() - 1));
            worm.remove(worm.size() - 1);
            worm.get(1).left = oldX;
            worm.get(1).top = oldY;
            worm.get(1).right = oldX2;
            worm.get(1).bottom = oldY2;
        }
    }

    //Prüft ob alle Bedingungen erfüllt sind, so dass ein Apfel gegessen werden kann
    //Sollte dies der Fall sein, so wird der Wurm um ein Segment verlängert
    private boolean eatApple()
    {
        if(head.left == apple.left && head.bottom == apple.bottom)
        {
            apple = null;
            worm.add(new Rect(head.left, head.top, head.right, head.bottom));
            gameActivity.increaseScore(10);
            return true;
        }
        return false;
    }

    //OnDraw Methode
    //Override Methode der Superklasse View, dient dem Zweck das Spielfeld und dessen Dynamik graphisch darzustellen
    //Wird zwecks spiel mittels des Timers regelmäsig aufgerufen
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGame(canvas);
        if(!gameOver) {

            sharedPref = new SharedPref(context);
            //Schwierigkeitsgrad wird aus den Shared Preferences geladen und als Timer-intervall gesetzt
            if (gameStarted) handler.postDelayed(r, sharedPref.getDifficulty());
        }
    }

    //Runnable
    //Diese Komponente fungiert als Timer des Spiels
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            moveWorm();
            //Invalidate bewirkt ein erneutes Zeichnen des Spielfelds
            invalidate();
        }
    };

    //Methode zwecks Zeichnung des Spielfeldes
    //Benötigt Leinwand (Canvas) objekt
    private void drawGame(Canvas canvas)
    {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.GRAY);
        p.setStrokeWidth(5);

        //Prüft ob Bedingungen für Game-Over erfüllt sind
        gameOver = checkGameOver();
        if(gameOver){
            handler.removeCallbacks(r);
            gameActivity.showGameOverDialog();
        }

        //Zeichnet den Wurm auf das Spielfeld
        //Verleit dem ersten Segment des Wurmes (Kopf) eine Farbe, welche es von den restlichen Segmenten abhebt
        for (int i = worm.size() - 1; i >= 0; i--){
            if(i == 0) p.setColor(ContextCompat.getColor(getContext(), R.color.wormHead));
            else p.setColor(ContextCompat.getColor(getContext(), R.color.wormBody));
            canvas.drawRect(worm.get(i), p);
        }

        //Prüft ob sich ein Apfel auf dem Spielfeld befindet und ob der Apfel gegessen und anschliessend ersetzt werden muss
        //In beiden Fällen wird ein Apfel gezeichnet
        if(apple == null || eatApple())
        {
            //Ein neuer Apfel wird auf dem Spielfeld platziert
            setApple(canvas);
            p.setColor(ContextCompat.getColor(getContext(), R.color.apple));
            canvas.drawRect(apple, p);
        }
        else {
            p.setColor(ContextCompat.getColor(getContext(), R.color.apple));
            canvas.drawRect(apple, p);
        }
    }

    //Richtungswechsel Methode
    public void changeDirection(int dir)
    {
        //Kreisartige Navigation zwischen vier Richtungen
        //Wird eins unterschritten wird die Richtungsnummer auf vier gesetzt
        //Wird vier überschritten wird die Richtungsnummer auf eins gesetzt
        if(gameStarted && !isPaused) {
            numDirection += dir;
            if (numDirection > 4) numDirection = 1;
            if (numDirection < 1) numDirection = 4;
            direction = directionMap.get(numDirection);
        }
    }

    //Pause Button Klick Methode
    public void pressPauseBtn()
    {
        //Änderung des Zustands Pause
        isPaused = !isPaused;
        if(isPaused) {
            gameActivity.btnPause.setBackgroundResource(R.drawable.btn_play);
            handler.removeCallbacks(r);
        }
        else {
            gameActivity.btnPause.setBackgroundResource(R.drawable.btn_pause);
            //Setzt den Zustand des Spiels auf gestartet
            if(!gameStarted) {
                gameStarted = true;
                gameActivity.btnPause.setBackgroundResource(R.drawable.btn_pause);
                //Invalidate bewirkt ein erneutes Zeichnen des Spielfelds
                invalidate();
            }
            else invalidate();
        }
    }

    //Prüft ob das Spiel vorbei ist Methode
    private boolean checkGameOver()
    {
        //Überprüft Kollision mit der Spielfeldbegrenzung
        if(head.left < borderThicknessInPixel ||
                head.top < borderThicknessInPixel ||
                head.right > getWidth() - borderThicknessInPixel ||
                head.bottom > getHeight() - borderThicknessInPixel)
        {
            return true;
        }
        //Überprüft Kollision mit anderen Segmenten
        for(int i = 1; i < worm.size(); i++)
        {
            if(head.left == worm.get(i).left && head.top == worm.get(i).top) return true;
        }
        return false;
    }
}
