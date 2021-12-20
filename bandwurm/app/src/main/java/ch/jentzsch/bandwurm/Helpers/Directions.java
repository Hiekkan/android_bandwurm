package ch.jentzsch.bandwurm.Helpers;

//Enum-Struktur welche der Speicherung der Richtungen des Wurmes dient
public enum Directions {

    //Sammlung sämtlicher Richtungsinstanzen
    LEFT(-1,0),
    RIGHT(1,0),
    UP(0,-1),
    DOWN(0,1);

    //Richtungsvariablen
    private int x,y;

    //Konstruktor der einzelnen Werte des Enums
    //Dient der Zuordnung von x und y
    Directions(int x, int y)
    {
        this.x = x;
        this.y=y;
    }


    //Gette-Methoden für x und y
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
