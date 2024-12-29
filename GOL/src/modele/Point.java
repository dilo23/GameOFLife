package modele;

public class Point {
    private int x;
    private int y;

    // Constructeur pour initialiser les coordonnées
    public Point(int _x, int _y) {
        this.x = _x;
        this.y = _y;
    }

    // Méthode pour obtenir la coordonnée x
    public int getX() {
        return x;
    }

    // Méthode pour obtenir la coordonnée y
    public int getY() {
        return y;
    }
}
