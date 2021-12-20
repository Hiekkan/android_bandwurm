package ch.jentzsch.bandwurm.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ch.jentzsch.bandwurm.Classes.HighScore;

//Datenbank Instanz Klasse
@Database(entities = HighScore.class, version = 1, exportSchema = false)
public abstract class WormDatabase extends RoomDatabase {
    private static WormDatabase INSTANCE;
    private static final String DB_NAME = "tapeworm.db";

    public abstract HighScoreDao highScoreDao();

    //Datenbank Instanz holen
    //Singleton-Pattern
    public static WormDatabase getDatabase(final Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WormDatabase.class, DB_NAME).allowMainThreadQueries().build();
        return INSTANCE;
    }
}
