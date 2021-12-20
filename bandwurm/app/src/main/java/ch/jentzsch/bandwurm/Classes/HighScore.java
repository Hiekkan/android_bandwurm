package ch.jentzsch.bandwurm.Classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Klasse für Datenbank Tabelle
//Diese Klasse kann mittels Room direkt als Datenbanktabelle umgesetzt werden
@Entity(tableName = "highscore_table")
public class HighScore {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "hid")
    private int id;
    private String name;
    private int score;

    public HighScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
