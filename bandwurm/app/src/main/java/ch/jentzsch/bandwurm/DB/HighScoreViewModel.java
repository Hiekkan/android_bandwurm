package ch.jentzsch.bandwurm.DB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ch.jentzsch.bandwurm.Classes.HighScore;

//LiveData ViewModel Klasse um Daten in echtzeit an zu zeigen
public class HighScoreViewModel extends AndroidViewModel {
    private HighScoreRepository repository;
    private LiveData<List<HighScore>> highScoresLiveData;

    public HighScoreViewModel(@NonNull Application application) {
        super(application);
        repository = new HighScoreRepository(application);
        highScoresLiveData = repository.getHighScoresList();
    }

    //Aus der Selektion der Bestenliste wird ein LiveData Objekt generiert
    public LiveData<List<HighScore>> getHighScoresList() {
        return highScoresLiveData;
    }

    //Mit LiveData Punktzahl einsetzen
    public void insert(HighScore highScore) {
        repository.insert(highScore);
    }
}
