package jeu;


/**
 *
 * Classe repr�sentant un joueur du jeu des guerriers
 */

public class Joueur {
	
	private Guerrier[] guerriers ; // tableau contenant les guerriers du joueur
	private int numJoueur ; // num�ro du joueur
	private String nom; //nom du joueur
	
	/**
	 * Cr�e un joueur du jeu des guerriers 
	 * @param nom : le nom du joueur
	 * @param nbGuerriers : le nombre de guerriers du joueur
	 * @param ptsVie : le nombre de points de vie de d�part des guerriers du joueur
	 * @param numJoueur : num�ro du joueur
	 */
	public Joueur(String nom, int nbGuerriers, int ptsVie, int numJoueur) {
		//TODO
		this.nom=nom;
		this.numJoueur=numJoueur;
		this.guerriers= new Guerrier[nbGuerriers];
		
		for(int i = 1; i <= nbGuerriers; i++) {
			this.guerriers[i-1] = new Guerrier(numJoueur, ptsVie, i);
		}
	}
	
	/**
	 * renvoie le nom du joueur
	 * @return le nom du joueur
	 */
	public String getNom() {
		return nom;
	}
	
	/**
	 * renvoie le num�ro du joueur
	 * @return le num�ro du joueur
	 */
	public int getNumJoueur() {
		return numJoueur ;
	}
	
	
	/**
	 * Renvoie le guerrier num�ro numGuerrier du joueur
	 * @param numGuerrier : le num�ro du guerrier
	 * @return le guerrier num�ro numGuerrier du joueur
	 */
	public Guerrier getGuerrier(int numGuerrier) {
		//TODO
		if (numGuerrier > this.guerriers.length || numGuerrier <= 0) {
			System.out.println("jai pas ce guerrier");
			return null;
		}
		return this.guerriers[numGuerrier-1];
	}
	
	/** 
	 * D�termine le nombre de guerrier encore en vie du joueur
	 * @return le nombre de guerrier encore en vie du joueur
	 */
	public int nombreDeGuerriersEnVie() {
		//TODO
		int cpt = 0;
		for (int i = 0; i < this.guerriers.length; i++) {
			if (this.guerriers[i].getPtsVie() > 0) {
				cpt++;
			}	
		}
		
		return cpt ;
	}

}
