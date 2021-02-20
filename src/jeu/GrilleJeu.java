package jeu;

/**
 * 
 *         Classe représentant l'état du jeu des guerriers
 * 
 */

public class GrilleJeu {

	private Joueur[] tableJoueurs; // tableau des joueurs
	private Guerrier[] cases; // cases du jeu
	private int nombreDeTours; // nombre de tours à faire pour gagner

	/**
	 * initialise et construit la table des joueurs initialise et construit la table
	 * représentant les cases du jeu initialise le nombre de tour à faire pour
	 * gagner
	 * 
	 * @param nombreJoueurs              : le nombre de joueurs participants
	 * @param nombreDeCases              : le nombre de cases du plateau de jeu
	 * @param nombreDeGuerriersParJoueur : le nombre de guerriers par joueurs
	 * @param nombreDeTours              : le nombre de tours à effectuer pour
	 *                                   gagner
	 * @param ptsVieDeDepart             : le nombre de points de vie de départ des
	 *                                   guerriers
	 * @param nomDesJoueurs              : tableau contenant le nom des joueurs
	 * 
	 */

	public GrilleJeu(int nombreJoueurs, int nombreDeCases, int nombreDeGuerriersParJoueur, int nombreDeTours,
			int ptsVieDeDepart, String[] nomDesJoueurs) {
		// TODO
		if (nombreDeGuerriersParJoueur * nombreJoueurs > nombreDeCases) {
			System.out.println("trop de joueurs");
		} else {
			this.nombreDeTours = nombreDeTours;
			this.tableJoueurs = new Joueur[nombreJoueurs];

			for (int i = 0; i < nombreJoueurs; i++) {
				this.tableJoueurs[i] = new Joueur(nomDesJoueurs[i], nombreDeGuerriersParJoueur, ptsVieDeDepart, i+1);
			}

			this.cases = new Guerrier[nombreDeCases];
			int cpt = 0;

			for (int i = 1; i <= nombreDeGuerriersParJoueur; i++) {
				for (int j = 0; j < nombreJoueurs; j++) {
					this.cases[cpt] = this.tableJoueurs[j].getGuerrier(i);
					cpt++;
				}
			}

		}
	}

	/**
	 * renvoie le joueur dont le numero est passe en parametre
	 * 
	 * @param numJoueur : le numero d'ordre du joueur dans le jeu
	 * @return le joueur
	 */
	public Joueur donnerJoueur(int numJoueur) {
		// TODO
		if (numJoueur > this.tableJoueurs.length || numJoueur <= 0) {
			System.out.println("je n'ai pas ce joueur");
			return null;
		}
		return this.tableJoueurs[numJoueur - 1];
	}

	/**
	 * Renvoie le guerrier se trouvant à la case dont le numéro est numCase
	 * 
	 * @param numCase : le numéro de la case
	 * @return le guerrier se trouvant à la case dont le numéro est numCase s'il y
	 *         en a un null sinon
	 */
	public Guerrier donnerPion(int numCase) {
		// donner pion commence à 1...
		if (numCase > this.cases.length || numCase <= 0) {
			System.out.println("je n'ai pas ce guerrier");
			return null;
		}
		return this.cases[numCase - 1];
	}

	/**
	 * Bouge le guerrier se trouvant à la case numéro caseDepart et le met à la case
	 * numéro caseArrivée
	 * 
	 * @param caseDepart  : numéro de la case où se trouve le guerrier à bouger
	 * @param caseArrivee : numéro de la case où il faut mettre le guerrier
	 *
	 */

	public void bougerPion(int caseDepart, int caseArrivee) {
		// TODO
		// Les cases commencent à 1...
		cases[caseArrivee - 1] = cases[caseDepart - 1];
		this.supprimerPion(caseDepart);
	}

	/**
	 * Détermine si le guerrier se trouvant sur la case numéro numCase appartient au
	 * joueur joueur
	 * 
	 * @param numCase : numéro de la case
	 * @param joueur  : le joueur
	 * @return true si le guerrier se trouvant à la case numéro numCase appartient
	 *         au joueur Joueur false sinon
	 */
	public boolean estUnPionDuJoueur(int numCase, Joueur joueur) {
		// numCase commence à 1...
		if (this.cases[numCase - 1] != null && this.cases[numCase - 1].getNumJoueur() == joueur.getNumJoueur()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * supprime le guerrier se trouvant à la case numéro numCase (vide la case)
	 * 
	 * @param numCase : numéro de la case
	 */

	public void supprimerPion(int numCase) {
		// numCase commence à 1...
		this.cases[numCase - 1] = null;
	}

	/**
	 * Classe les guerriers encore un jeu d'abord selon le nombre tour déjà effectué
	 * (du plus grand au plus petit) et ensuite (si dans le même tour) par numéro de
	 * case occupée (du plus grand au plus petit)
	 * 
	 * @return un tableau de pion représentant le classement des pions selon les
	 *         critères ci-dessus
	 */
	public Guerrier[] classerGuerriers() {
		// TODO
		int nbGuerriersVivant = 0;
		// on compte le nombre de guerriers vivants sur le plateau
		for (int i = 0; i < tableJoueurs.length; i++) {
			nbGuerriersVivant += tableJoueurs[i].nombreDeGuerriersEnVie();
		}

		Guerrier[] guerriers = new Guerrier[nbGuerriersVivant];
		int cpt = 0;

		// on met tous ces guerriers vivants dans une table
		for (int i = 0; i < this.cases.length; i++) {
			if (this.cases[i] != null && this.cases[i].getPtsVie() > 0) {
				guerriers[cpt] = this.cases[i];
				cpt++;
			}
		}

		// Tri Selection
		for (int i = 0; i < guerriers.length; i++) {
			int iMin = i;

			for (int j = i + 1; j < guerriers.length; j++) {
				// D'abord, celui qui a le plus de tour en priorité
				if (guerriers[j].getNombreDeTours() > guerriers[iMin].getNombreDeTours()) {
					iMin = j;
				}
				// Sinon, si les deux guerriers ont le même nombre de tours, celui le plus proche de la fin en priorité
				else if (guerriers[j].getNombreDeTours() == guerriers[iMin].getNombreDeTours()) {
					if (this.getNbCasesAvantAvantFin(guerriers[j]) < this.getNbCasesAvantAvantFin(guerriers[iMin])) {
						iMin = j;
					}
				}
			}

			Guerrier temp = guerriers[i];
			guerriers[i] = guerriers[iMin];
			guerriers[iMin] = temp;
		}

		return guerriers;
	}
	
	// Cette méthode nous indique combien de cases il reste avant de 
	// terminer le plateau. Ca retourne -1 si le guerrier n'a pas été trouvé. 
	public int getNbCasesAvantAvantFin(Guerrier guerrier) {
		for (int i = 0; i < this.cases.length; i++) {
			if (this.cases[i] == guerrier) {
				return this.cases.length - i - 1;
			}
		}
		return -1;
	}

	public boolean partieFinie() {
		// si un guerrier a gagne (point 11)
		for (int i = 0; i < this.cases.length; i++) {
			if (this.cases[i] != null && this.cases[i].getNombreDeTours() >= this.nombreDeTours) {
				return true;
			}
		}

		// on compte nb joueurs vivants
		int cpt = 0;
		for (int i = 0; i < this.tableJoueurs.length; i++) {
			if (this.tableJoueurs[i].nombreDeGuerriersEnVie() > 0) {
				cpt++;
			}
		}

		// sil n y a plus que 0 ou 1 joueur ayant des guerriers vivants, la partie est
		// finie (11, 12)
		if (cpt == 1 || cpt == 0) {
			return true;
		}

		return false;
	}
}
