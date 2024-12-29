package modele;

import java.util.HashMap;
import java.util.Observable;

public class Environnement extends Observable implements Runnable {
    private Case[][] tab;
    private HashMap<Case, Point> caseMap;
    private int sizeX, sizeY;

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public boolean getState(int x, int y) {
        return tab[x][y].getState();
    }

    // Récupère la cellule voisine dans la direction spécifiée
    public Case getCase(Case source, Direction d) {
        Point position = caseMap.get(source);
        if (position == null) {
            return null;
        }
        int dx = 0, dy = 0;
        switch (d) {
            case h: dx = -1; dy = 0; break;
            case hd: dx = -1; dy = 1; break;
            case d: dx = 0; dy = 1; break;
            case db: dx = 1; dy = 1; break;
            case b: dx = 1; dy = 0; break;
            case bg: dx = 1; dy = -1; break;
            case g: dx = 0; dy = -1; break;
            case gh: dx = -1; dy = -1; break;
        }
        int newX = position.getX() + dx;
        int newY = position.getY() + dy;
        if (newX >= 0 && newX < sizeX && newY >= 0 && newY < sizeY) {
            return tab[newX][newY];
        } else {
            return null;
        }
    }

    // Modifie l'état de la cellule à une position spécifique
    public void setState(int x, int y, boolean nouvelEtat) {
        tab[x][y].setState(nouvelEtat);
    }

    // Constructeur : initialise l'environnement avec des cellules vides
    public Environnement(int _sizeX, int _sizeY) {
        sizeX = _sizeX;
        sizeY = _sizeY;
        tab = new Case[sizeX][sizeY];
        caseMap = new HashMap<>();
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j] = new Case();
                Point point = new Point(i, j);
                caseMap.put(tab[i][j], point);
            }
        }
    }

    // Initialise l'état de toutes les cellules de manière aléatoire
    public void rndState() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].rndState();
            }
        }
    }

    // Calcule l'état suivant de toutes les cellules
    public void calculateNextState() {
        // Propager l'infection avant de calculer l'état suivant
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].infectNeighbors(this); // Infecte les voisins si nécessaire
                tab[i][j].nextState(this);       // Calcule l'état suivant
            }
        }
    }

    // Met à jour l'état de chaque cellule après avoir calculé son état suivant
    public void env_updateState() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].updateState(); // Met à jour l'état de la cellule
            }
        }
    }

    // Réinitialise l'ensemble de l'environnement en mettant toutes les cellules à l'état "mort"
    public void resetEnvironment() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                tab[i][j].resetState();
            }
        }
    }

    // Infecte une cellule spécifique
    public void setVirus(int x, int y) {
        if (x >= 0 && x < sizeX && y >= 0 && y < sizeY) {
            tab[x][y].setInfected(true); // Marque la cellule comme infectée
        }
    }

    // Vérifie si une cellule spécifique est infectée
    public boolean isInfected(int x, int y) {
        return tab[x][y].isInfected();
    }

    // Méthode principale du thread : calcule l'état suivant, met à jour l'environnement et notifie les observateurs
    @Override
    public void run() {
        calculateNextState();  // Calcule l'état suivant de chaque cellule
        env_updateState();     // Met à jour l'état de chaque cellule
        setChanged();          // Marque l'objet comme ayant changé
        notifyObservers();     // Notifie les observateurs
        try {
            Thread.sleep(50);  // Attente de 50 ms avant le prochain cycle
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
