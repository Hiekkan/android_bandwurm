package ch.jentzsch.bandwurm.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ch.jentzsch.bandwurm.Classes.HighScore;

//Datenbank Query Interface
//Mittels der Room basierten Annotationen können Queries als Methoden aufgerufen werden
//Datenbankzugriff weist Objektrelationale Züge auf. Objekte können mittels Room direkt in die Datenbank übertragen werden
@Dao
public interface HighScoreDao {

    //Dient dem Speichern neuer Einträge
    @Insert
    void insert(HighScore highScore);

    //Dient dem Löschen sämtlicher Einträge
    @Query("DELETE FROM highscore_table")
    void deleteAll();

    //Dient dem Abrufen sämtlicher Einträge in geordneter Abfolge
    @Query("SELECT * FROM highscore_table ORDER BY score DESC")
    LiveData<List<HighScore>> getAllHighScores();

}
