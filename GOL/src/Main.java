import modele.Environnement;
import modele.Ordonnanceur;
import vue_controleur.FenetrePrincipale;
import vue_controleur.Controleur;

import javax.swing.SwingUtilities;

/**
 * Classe principale pour lancer le jeu.
 * @author frederic
 */
public class Main {

	/**
	 * Méthode principale pour lancer le programme.
	 * @param args les arguments de la ligne de commande
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				// Initialisation de l'environnement taille
				Environnement e = new Environnement(50, 50);

				// Création de la fenêtre principale et passage de l'environnement
				FenetrePrincipale fenetre = new FenetrePrincipale(e);
				fenetre.setVisible(true);

				// Ajout de l'environnement comme observateur de la fenêtre
				e.addObserver(fenetre);

				// Initialisation d'un ordonnanceur qui met à jour l'environnement toutes les 60ms
				Ordonnanceur o = new Ordonnanceur(50, e);
				o.start();  // Démarre le thread de l'ordonnanceur
                // Initialisation du contrôleur en passant l'environnement et la fenêtre
                Controleur controleur = new Controleur(e, fenetre,o);


				// Initialisation de l'état aléatoire des cases dans l'environnement
				e.rndState();
			}
		});
	}
}
