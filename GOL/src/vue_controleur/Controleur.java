package vue_controleur;

import modele.Environnement;
import modele.Ordonnanceur;
import modele.Motif;


import javax.swing.*;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Point;


public class Controleur {

    private final Environnement environnement;
    private final FenetrePrincipale vue;
    private final Ordonnanceur ordonnanceur;

    private Timer timer;
    private int vitessemin = 800;
    private int vitessemax = 1;

    public boolean modeVirus = false;
    private boolean modeDessin = false; // Variable pour savoir si le mode dessin est activé
    private boolean[][] motifActuel;
    private boolean modePlacementMotif; //motif est prêt à être placé

    public Controleur(Environnement env, FenetrePrincipale fenetre, Ordonnanceur ordonnanceur) {
        environnement = env;
        vue = fenetre;
        this.ordonnanceur = ordonnanceur;
        this.modePlacementMotif = false;
        initialiserListeners();
        initialiserTimer();
    }

    private void initialiserListeners() {
        vue.ajouterEcouteurCellule(new MouseAdapter() {
            private boolean modeDragActif = false;

            @Override
            public void mousePressed(MouseEvent e) {
                if (modeDessin) {
                    modeDragActif = true; // Active le mode de drag pour dessiner
                    gererClicOuDrag(e); // Gère le clic pour dessiner
                } else {
                    // Même si le mode dessin est désactivé, un simple clic peut changer l'état d'une cellule
                    gererClicOuDrag(e);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (modeDessin && modeDragActif) {
                    gererClicOuDrag(e); // Permet de dessiner pendant un drag si le mode dessin est activé
                }
                // Si le mode dessin est désactivé, rien ne se passe pendant un drag
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Si le mode dessin est activé, on peut arrêter le drag
                if (modeDessin) {
                    modeDragActif = false;
                }
            }

            private void gererClicOuDrag(MouseEvent e) {
                Point positionGrille = vue.getPositionGrille();
                // Positions X et Y relative à la grille
                int xSourisRelatif = e.getXOnScreen() - positionGrille.x;
                int ySourisRelatif = e.getYOnScreen() - positionGrille.y;

                // coordonnées de la cellule à partir des coordonnées de la souris
                int[] coords = vue.obtenirCoordonneesDepuisSouris(xSourisRelatif, ySourisRelatif);

                if (coords != null) {
                    int x = coords[0];
                    int y = coords[1];

                    // Si le mode dessin est activé, on dessine dans la grille
                    if (modeDessin) {
                        environnement.setState(x, y, true);
                        vue.rafraichir();
                    }
                    // Si le mode virus est activé
                    else if (modeVirus) {
                        environnement.setVirus(x, y);
                        vue.rafraichir();
                        modeVirus = false;
                    }
                    // Si un motif est prêt à être placé
                    else if (modePlacementMotif && motifActuel != null) {
                        placerMotif(x, y); // Place le motif à la position du clic
                        modePlacementMotif = false;
                        motifActuel = null;
                    }
                    else { // Mode normal ou clic
                        boolean nouvelEtat = !environnement.getState(x, y);
                        environnement.setState(x, y, nouvelEtat);
                        vue.rafraichir();
                    }
                }
            }
        });


        vue.ajouterEcouteurBoutonEffacer(e -> {
            arreterSimulation();
            environnement.resetEnvironment();
            vue.rafraichir();
        });

        vue.ajouterEcouteurBoutonReinitialiser(e -> {
            arreterSimulation();
            environnement.rndState();
            vue.rafraichir();
        });

        vue.ajouterEcouteurBoutonDessiner(e -> {
            modeDessin = !modeDessin; // Active le mode dessin
            System.out.println("Mode dessin activé.");
        });

        vue.ajouterEcouteurBoutonPause(e -> {
            boolean nouvelEtatPause = !ordonnanceur.enPause;
            ordonnanceur.setPause(nouvelEtatPause);
            vue.getBoutonPause().setText(nouvelEtatPause ? "Reprendre" : "Pause");
        });

        vue.ajouterEcouteurSliderVitesse(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int valeurBrute = vue.getSliderVitesse().getValue();
                int nouvelleVitesse = vitessemax - valeurBrute + vitessemin;
                ordonnanceur.sleepTime = nouvelleVitesse;
                if (nouvelleVitesse == vitessemin) {
                    vue.getLabelVitesse().setText("Vitesse: min");
                } else if (nouvelleVitesse == vitessemax) {
                    vue.getLabelVitesse().setText("Vitesse: max");
                } else {
                    vue.getLabelVitesse().setText("Vitesse: " + nouvelleVitesse);
                }
            }
        });

        vue.ajouterEcouteurMouseMotion(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (modePlacementMotif && motifActuel != null) {
                    JPanel celluleSurvolee = (JPanel) e.getSource();
                    int[] coords = vue.obtenirCoordonneesCellule(celluleSurvolee);
                    if (coords != null) {
                        int x = coords[0];
                        int y = coords[1];
                        afficherSurbrillanceMotif(x, y); // Afficher prévisualisation
                    }
                }
            }
        });

        vue.ajouterEcouteurBoutonVirus(e -> {
            modeVirus = true;
            System.out.println("mode virus = true");
        });

        // Gestion de la sélection des motifs
        vue.ajouterEcouteurBoutonGlider(e -> selectionnerMotif(Motif.GLIDER));
        vue.ajouterEcouteurBoutonBlock(e -> selectionnerMotif(Motif.BLOCK));
        vue.ajouterEcouteurBoutonBlinker(e -> selectionnerMotif(Motif.BLINKER));
        vue.ajouterEcouteurBoutonToad(e -> selectionnerMotif(Motif.TOAD));
        vue.ajouterEcouteurBoutonBeacon(e -> selectionnerMotif(Motif.BEACON));
    }

    private void initialiserTimer() {
        timer = new Timer(50, e -> {
            environnement.calculateNextState();
            environnement.env_updateState();
            vue.rafraichir();
        });
    }

    private void arreterSimulation() {
        timer.stop();
    }

    private void selectionnerMotif(boolean[][] motif) {
        motifActuel = motif;
        modePlacementMotif = true;
        System.out.println("Motif sélectionné : prêt à être placé.");
    }


    private void placerMotif(int startX, int startY) {
        for (int i = 0; i < motifActuel.length; i++) {
            for (int j = 0; j < motifActuel[i].length; j++) {
                int x = startX + i;
                int y = startY + j;
                // Vérifie si le motif peut être placé dans la zone
                if (x < environnement.getSizeX() && y < environnement.getSizeY()) {
                    environnement.setState(x, y, motifActuel[i][j]);
                }
            }
        }
        vue.rafraichir();
        System.out.println("Motif placé à la position (" + startX + ", " + startY + ").");
    }

    private void afficherSurbrillanceMotif(int startX, int startY) {
        vue.nettoyerSurbrillance(); // Nettoie l'ancienne prévisualisation
        for (int i = 0; i < motifActuel.length; i++) {
            for (int j = 0; j < motifActuel[i].length; j++) {
                int x = startX + i;
                int y = startY + j;

                // Vérifie si le motif reste dans les limites de la grille
                if (x < environnement.getSizeX() && y < environnement.getSizeY()) {
                    vue.afficherSurbrillanceCellule(x, y, motifActuel[i][j]);
                }
            }
        }
    }


}
