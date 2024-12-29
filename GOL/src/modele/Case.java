package modele;

import java.util.Random;

public class Case {
    private static final Random rnd = new Random();
    private boolean state;      // L'état de la cellule (vivante ou morte)
    private boolean nextState;  // L'état suivant de la cellule (vivante ou morte)
    private boolean infected;   // Indique si la cellule est infectée

    // Récupère l'état actuel de la cellule
    public boolean getState() {
        return state;
    }

    // Définit l'état de la cellule
    public void setState(boolean newState) {
        this.state = newState;
    }

    // Initialisation aléatoire de l'état de la cellule (vivante ou morte)
    public void rndState() {
        state = rnd.nextBoolean();
    }

    // Méthode pour déterminer l'état suivant de la cellule en fonction du voisinage
    public void nextState(Environnement env) {
        int liveNeighbors = 0;

        // Compte les voisins vivants
        for (Direction dir : Direction.values()) {
            Case neighbor = env.getCase(this, dir);
            if (neighbor != null && neighbor.getState()) {
                liveNeighbors++;
            }
        }

        // Règles du jeu de la vie pour une cellule vivante ou infectée
        if (state) {
            // Une cellule vivante ou infectée suit les mêmes règles
            if (liveNeighbors < 2 || liveNeighbors > 3) {
                nextState = false;  // La cellule meurt
            } else {
                nextState = true;   // La cellule reste vivante
            }
        } else {
            // Une cellule morte devient vivante si elle a exactement 3 voisins vivants
            if (liveNeighbors == 3) {
                nextState = true;  // La cellule devient vivante
            } else {
                nextState = false; // La cellule reste morte
            }
        }

        // Si la cellule est infectée et morte, elle disparait
        if (!state && infected) {
            infected = false;
        }
    }

    // Met à jour l'état de la cellule en fonction du calcul précédent
    public void updateState() {
        state = nextState;
    }

    // Réinitialise l'état de la cellule à "morte"
    public void resetState() {
        state = false;
    }

    // Vérifie si la cellule est infectée
    public boolean isInfected() {
        return infected;
    }

    // Définit si la cellule est infectée
    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    // Méthode pour propager l'infection à un voisin si la cellule est infectée
    public void infectNeighbors(Environnement env) {
        if (infected) {
            for (Direction dir : Direction.values()) {
                Case neighbor = env.getCase(this, dir);
                if (neighbor != null && !neighbor.isInfected() && neighbor.nextState) { // Utilisation de nextState ici
                    neighbor.setInfected(true); // Le voisin devient infecté dans le prochain état

                }
            }
        }
    }


}

