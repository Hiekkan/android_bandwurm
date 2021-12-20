package ch.jentzsch.bandwurm.DB;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import ch.jentzsch.bandwurm.Classes.HighScore;

//Repository Klasse für das LiveData ViewModel
public class HighScoreRepository {
    private HighScoreDao scoreDao;
    private LiveData<List<HighScore>> highScoresList;


    //Konstruktor Methode der Repository Klasse
    public HighScoreRepository(Application application) {
        WormDatabase db = WormDatabase.getDatabase(application);
        scoreDao = db.highScoreDao();
        highScoresList = scoreDao.getAllHighScores();
    }

    //Punktzahl wird mittels asynchronem Methodenaufruf in nebenläufigem Thread gesetzt
    public void insert(HighScore highScore) {
        new InsertHighScoreAsyncTask(scoreDao).execute(highScore);
    }

    //BestenListe wird mittels asynchronem Methodenaufruf in nebenläufigem Thread gelöscht
    public void deleteAllHighScores() {
        new DeleteAllHighScoresAsyncTask(scoreDao).execute();
    }

    //Bestenliste wird mittels asynchronem Methodenaufruf in nebenläufigem Thread ausgelesen
    public LiveData<List<HighScore>> getHighScoresList() {
        return highScoresList;
    }

    //Verschachtelte AsyncTask Thread Klasse zwecks Insert
    private static class InsertHighScoreAsyncTask extends AsyncTask<HighScore, Void, Void> {
        private HighScoreDao dao;

        private InsertHighScoreAsyncTask(HighScoreDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(HighScore... highScores) {
            dao.insert(highScores[0]);
            return null;
        }
    }

    //Verschachtelte AsyncTask Thread Klasse Zwecks Bestenliste löschen
    private static class DeleteAllHighScoresAsyncTask extends AsyncTask<Void, Void, Void> {
        private HighScoreDao dao;

        private DeleteAllHighScoresAsyncTask(HighScoreDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }
}
