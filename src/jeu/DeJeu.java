package jeu;

/**
 *
 * Classe représentant un dé à 6 face non pipé
 * 
 */
public class DeJeu implements De{
	
	public DeJeu() {
		super() ;
	}
		
	/**
	 * Simule le lancer du dé
	 * @return le résutat (entre 1 et 6) du lancé du dé
	 */
	public int lancer() {
		return unEntierAuHasardEntre(1,6) ;
	}
	
	private int unEntierAuHasardEntre(int valeurMinimale, int valeurMaximale) {
		double nombreReel;
		int resultat;

		nombreReel = Math.random();
		resultat = (int) (nombreReel * (valeurMaximale - valeurMinimale + 1)) + valeurMinimale;
		return resultat;
	}
}
