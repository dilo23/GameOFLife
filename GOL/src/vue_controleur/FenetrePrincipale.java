package vue_controleur;

import modele.Environnement;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import java.net.URL;


import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import modele.XMLHandler;

public class FenetrePrincipale extends JFrame implements Observer {

    private JPanel[][] grille;
    private Environnement environnement;
    private JButton boutonReinitialiser;
    private JButton boutonEffacer;
    private JButton boutonPause;
    private JButton boutonVirus;
    private JButton boutonDessiner;
    private JLabel labelVitesse;
    private JSlider sliderVitesse;
    private JPanel panneauMotifs;
    private JButton boutonGlider;
    private JButton boutonBlock;
    private JButton boutonBlinker;
    private JButton boutonToad;
    private JButton boutonBeacon;
    private int vitessemin = 1;
    private int vitessemax = 800;
    private int vitesseinit = 400;

    public FenetrePrincipale(Environnement env) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        environnement = env;
        environnement.addObserver(this);  // Assurez-vous que la fenêtre principale est ajoutée comme observateur
        construireInterface();
    }

    public void construireInterface() {
        setTitle("Jeu de la Vie");
        setSize(1000, 900); // Taille ajustée pour inclure le panneau des motifs
        JPanel panneauPrincipal = new JPanel(new BorderLayout());
        JComponent panneauGrille = new JPanel(new GridLayout(environnement.getSizeX(), environnement.getSizeY()));
        grille = new JPanel[environnement.getSizeX()][environnement.getSizeY()];
        Border bordureNoire = BorderFactory.createLineBorder(Color.DARK_GRAY);

        // Initialisation de la grille
        for (int i = 0; i < environnement.getSizeX(); i++) {
            for (int j = 0; j < environnement.getSizeY(); j++) {
                grille[i][j] = new JPanel();
                grille[i][j].setBackground(Color.WHITE);  // Par défaut, les cellules sont blanches
                grille[i][j].setBorder(bordureNoire);
                panneauGrille.add(grille[i][j]);
            }
        }

        // Menu "Fichier"
        JMenuBar jm = new JMenuBar();
        JMenu m = new JMenu("Fichier");

        JMenuItem miCharger = new JMenuItem("Charger");
        JMenuItem miSauvegarder = new JMenuItem("Sauvegarder");
        m.add(miCharger);
        m.add(miSauvegarder);
        jm.add(m);
        setJMenuBar(jm);
        miCharger.addActionListener(this::chargerEtat);
        miSauvegarder.addActionListener(this::sauvegarderEtat);
        panneauPrincipal.add(panneauGrille, BorderLayout.CENTER);

        // Boutons de contrôle (en bas)
        boutonReinitialiser = new JButton("Réinitialiser");
        boutonEffacer = new JButton("Effacer");
        boutonDessiner = new JButton("Dessiner");
        boutonVirus = new JButton("   Virus   ");
        boutonPause = new JButton("Pause");
        labelVitesse = new JLabel("Vitesse", SwingConstants.CENTER);

        // Slider de vitesse
        sliderVitesse = new JSlider(JSlider.HORIZONTAL, vitessemin, vitessemax, vitesseinit);
        sliderVitesse.setMajorTickSpacing(200);
        sliderVitesse.setMinorTickSpacing(50);
        sliderVitesse.setSnapToTicks(false); // Permet un déplacement fluide
        sliderVitesse.setPreferredSize(new Dimension(200, 50));


        JPanel panneauControle = new JPanel();
        panneauControle.add(boutonReinitialiser);
        panneauControle.add(boutonEffacer);
        panneauControle.add(boutonDessiner);
        panneauControle.add(boutonVirus);
        panneauControle.add(boutonPause);
        panneauControle.add(labelVitesse);
        panneauControle.add(labelVitesse);
        panneauControle.add(sliderVitesse); // Ajouter uniquement le slider pour gérer la vitesse

        panneauPrincipal.add(panneauControle, BorderLayout.SOUTH);

        panneauMotifs = new JPanel();
        panneauMotifs.setLayout(new BoxLayout(panneauMotifs, BoxLayout.Y_AXIS));
        panneauMotifs.setBorder(BorderFactory.createTitledBorder("Motifs"));

        // Charger les icônes
        URL gliderURL = getClass().getResource("/data/glider.png");
        URL blockURL = getClass().getResource("/data/block.png");
        URL blinkerURL = getClass().getResource("/data/blinker.png");
        URL toadURL = getClass().getResource("/data/toad.png");
        URL beaconURL = getClass().getResource("/data/beacon.png");

        // Redimensionner les icônes
        int tailleimg = 90;
        ImageIcon resizedGlider = new ImageIcon(new ImageIcon(gliderURL).getImage().getScaledInstance(tailleimg, tailleimg, Image.SCALE_SMOOTH));
        ImageIcon resizedBlock = new ImageIcon(new ImageIcon(blockURL).getImage().getScaledInstance(tailleimg, tailleimg, Image.SCALE_SMOOTH));
        ImageIcon resizedBlinker = new ImageIcon(new ImageIcon(blinkerURL).getImage().getScaledInstance(tailleimg, tailleimg, Image.SCALE_SMOOTH));
        ImageIcon resizedToad = new ImageIcon(new ImageIcon(toadURL).getImage().getScaledInstance(tailleimg, tailleimg, Image.SCALE_SMOOTH));
        ImageIcon resizedBeacon = new ImageIcon(new ImageIcon(beaconURL).getImage().getScaledInstance(tailleimg, tailleimg, Image.SCALE_SMOOTH));

        // Créer les boutons avec les icônes
        boutonGlider = new JButton(resizedGlider);
        boutonBlock = new JButton(resizedBlock);
        boutonBlinker = new JButton(resizedBlinker);
        boutonToad = new JButton(resizedToad);
        boutonBeacon = new JButton(resizedBeacon);

        // Créer les labels pour les textes sous les boutons
        JLabel labelGlider = new JLabel("               Glider");
        JLabel labelBlock = new JLabel("               Block");
        JLabel labelBlinker = new JLabel("              Blinker");
        JLabel labelToad = new JLabel("                Toad");
        JLabel labelBeacon = new JLabel("              Beacon");

        // Utiliser un BoxLayout pour disposer les boutons et les labels sous chaque autre dans le même panneau
        panneauMotifs.setLayout(new BoxLayout(panneauMotifs, BoxLayout.Y_AXIS)); // Disposition verticale

        // Ajouter les boutons et les labels directement sans JPanel supplémentaire
        int spacing = 30;
        panneauMotifs.add(boutonGlider);
        panneauMotifs.add(labelGlider);
        panneauMotifs.add(Box.createVerticalStrut(spacing));

        panneauMotifs.add(boutonBlock);
        panneauMotifs.add(labelBlock);
        panneauMotifs.add(Box.createVerticalStrut(spacing));

        panneauMotifs.add(boutonBlinker);
        panneauMotifs.add(labelBlinker);
        panneauMotifs.add(Box.createVerticalStrut(spacing));

        panneauMotifs.add(boutonToad);
        panneauMotifs.add(labelToad);
        panneauMotifs.add(Box.createVerticalStrut(spacing));

        panneauMotifs.add(boutonBeacon);
        panneauMotifs.add(labelBeacon);
        panneauMotifs.add(Box.createVerticalStrut(spacing));

        // Ajouter le panneauMotifs au panneau principal
        panneauPrincipal.add(panneauMotifs, BorderLayout.EAST);


        this.setContentPane(panneauPrincipal);
    }

    @Override
    public void update(Observable o, Object arg) {
        rafraichir();  // Rafraîchit l'interface lorsque l'état change dans l'environnement
    }

    public void rafraichir() {
        // Mise à jour de chaque cellule en fonction de son état
        for (int i = 0; i < environnement.getSizeX(); i++) {
            for (int j = 0; j < environnement.getSizeY(); j++) {
                // Si la cellule est infectée, elle devient rouge
                if (environnement.isInfected(i, j)) {
                    grille[i][j].setBackground(Color.RED);
                }
                // Si la cellule est vivante, elle devient verte
                else if (environnement.getState(i, j)) {
                    grille[i][j].setBackground(Color.GREEN);
                }
                // Si la cellule est morte, elle devient noire
                else {
                    grille[i][j].setBackground(Color.BLACK);
                }
            }
        }
    }

    public void ajouterEcouteurCellule(MouseAdapter mouseAdapter) {
        for (int i = 0; i < environnement.getSizeX(); i++) {
            for (int j = 0; j < environnement.getSizeY(); j++) {
                grille[i][j].addMouseListener(mouseAdapter);
                grille[i][j].addMouseMotionListener(mouseAdapter); // Ajout pour détecter les drags
            }
        }
    }


    public JSlider getSliderVitesse() {
        return sliderVitesse;
    }

    public JLabel getLabelVitesse() {
        return labelVitesse;
    }

    public void ajouterEcouteurBoutonReinitialiser(ActionListener actionListener) {
        boutonReinitialiser.addActionListener(actionListener);
    }

    public void ajouterEcouteurBoutonEffacer(ActionListener actionListener) {
        boutonEffacer.addActionListener(actionListener);
    }

    public void ajouterEcouteurBoutonDessiner(ActionListener actionListener) {
        boutonDessiner.addActionListener(actionListener);
    }

    public void ajouterEcouteurBoutonPause(ActionListener actionListener) {
        boutonPause.addActionListener(actionListener);
    }

    public void ajouterEcouteurSliderVitesse(ChangeListener changeListener) {
        sliderVitesse.addChangeListener(changeListener);
    }

    public void ajouterEcouteurBoutonGlider(ActionListener actionListener) {
        boutonGlider.addActionListener(actionListener);
    }

    public void ajouterEcouteurBoutonBlock(ActionListener actionListener) {
        boutonBlock.addActionListener(actionListener);
    }

    public void ajouterEcouteurBoutonBlinker(ActionListener actionListener) {
        boutonBlinker.addActionListener(actionListener);
    }

    public void ajouterEcouteurBoutonToad(ActionListener actionListener) {
        boutonToad.addActionListener(actionListener);
    }

    public void ajouterEcouteurBoutonBeacon(ActionListener actionListener) {
        boutonBeacon.addActionListener(actionListener);
    }
    public int[] obtenirCoordonneesDepuisSouris(int xSouris, int ySouris) {
        int cellWidth = grille[0][0].getWidth();
        int cellHeight = grille[0][0].getHeight();

        int x = ySouris / cellHeight; // Calcul de la ligne
        int y = xSouris / cellWidth;  // Calcul de la colonne

        if (x >= 0 && x < environnement.getSizeX() && y >= 0 && y < environnement.getSizeY()) {
            return new int[] {x, y};
        }
        return null; // coordonnées en dehors des limites
    }


    public void ajouterEcouteurBoutonVirus(ActionListener actionListener) {
        boutonVirus.addActionListener(actionListener);
    }

    public void ajouterEcouteurMouseMotion(MouseMotionAdapter mouseMotionAdapter) {
        for (int i = 0; i < environnement.getSizeX(); i++) {
            for (int j = 0; j < environnement.getSizeY(); j++) {
                grille[i][j].addMouseMotionListener(mouseMotionAdapter);
            }
        }
    }



    public int[] obtenirCoordonneesCellule(JPanel cellule) {
        for (int i = 0; i < environnement.getSizeX(); i++) {
            for (int j = 0; j < environnement.getSizeY(); j++) {
                if (grille[i][j] == cellule) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
    public void afficherSurbrillanceCellule(int x, int y, boolean etat) {
        if (etat) {
            grille[x][y].setBackground(new Color(173, 216, 230)); // Couleur bleu clair
        }
    }

    public void nettoyerSurbrillance() {
        // Réinitialise la couleur des cellules à leur état réel
        for (int i = 0; i < environnement.getSizeX(); i++) {
            for (int j = 0; j < environnement.getSizeY(); j++) {
                if (environnement.isInfected(i, j)) {
                    grille[i][j].setBackground(Color.RED);
                } else if (environnement.getState(i, j)) {
                    grille[i][j].setBackground(Color.GREEN);
                } else {
                    grille[i][j].setBackground(Color.BLACK);
                }
            }
        }
    }

    public void chargerEtat(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers XML", "xml");
        fileChooser.setFileFilter(filter);
        int retour = fileChooser.showOpenDialog(this);  // 'this' fait référence à FenetrePrincipale
        if (retour == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XMLHandler.loadState(environnement, file.getAbsolutePath());
            rafraichir();  // Met à jour l'affichage après le chargement
        }
    }

    public void sauvegarderEtat(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers XML", "xml");
        fileChooser.setFileFilter(filter);
        int retour = fileChooser.showSaveDialog(this);  // 'this' fait référence à FenetrePrincipale
        if (retour == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XMLHandler.saveState(environnement, file.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "État sauvegardé avec succès !");
        }
    }

    public Point getPositionGrille() {
        return this.getContentPane().getComponent(0).getLocationOnScreen();
    }


    public JButton getBoutonPause() {
        return boutonPause;
    }
}