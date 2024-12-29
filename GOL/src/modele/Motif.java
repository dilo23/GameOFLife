package modele;

/**
 * Classe utilitaire contenant des motifs prédéfinis pour le Jeu de la Vie.
 */
public class Motif {

    // Un motif "Glider" (vaisseau léger)
    public static final boolean[][] GLIDER = {
            {false, true, false},
            {false, false, true},
            {true, true, true}
    };

    // Un motif "Block" (motif stable)
    public static final boolean[][] BLOCK = {
            {true, true},
            {true, true}
    };

    // Un motif "Blinker" (oscillateur de période 2)
    public static final boolean[][] BLINKER = {
            {false, false, false},
            {true, true, true},
            {false, false, false}
    };

    // Un motif "Toad" (oscillateur de période 2)
    public static final boolean[][] TOAD = {
            {false, false, false, false},
            {false, true, true, true},
            {true, true, true, false},
            {false, false, false, false}
    };

    // Un motif "Beacon" (oscillateur de période 2)
    public static final boolean[][] BEACON = {
            {true, true, false, false},
            {true, true, false, false},
            {false, false, true, true},
            {false, false, true, true}
    };

    // Constructeur privé pour empêcher l'instanciation
    private Motif() {
        throw new UnsupportedOperationException("classe ne doit pas être instanciee.");
    }
}
