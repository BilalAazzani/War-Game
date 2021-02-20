package jeu;

/**
 * 
 *         Classe repr�sentant l'�tat du jeu des guerriers
 * 
 */

public class GrilleJeu {

	private Joueur[] tableJoueurs; // tableau des joueurs
	private Guerrier[] cases; // cases du jeu
	private int nombreDeTours; // nombre de tours � faire pour gagner

	/**
	 * initialise et construit la table des joueurs initialise et construit la table
	 * repr�sentant les cases du jeu initialise le nombre de tour � faire pour
	 * gagner
	 * 
	 * @param nombreJoueurs              : le nombre de joueurs participants
	 * @param nombreDeCases              : le nombre de cases du plateau de jeu
	 * @param nombreDeGuerriersParJoueur : le nombre de guerriers par joueurs
	 * @param nombreDeTours              : le nombre de tours � effectuer pour
	 *                                   gagner
	 * @param ptsVieDeDepart             : le nombre de points de vie de d�part des
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
	 * Renvoie le guerrier se trouvant � la case dont le num�ro est numCase
	 * 
	 * @param numCase : le num�ro de la case
	 * @return le guerrier se trouvant � la case dont le num�ro est numCase s'il y
	 *         en a un null sinon
	 */
	public Guerrier donnerPion(int numCase) {
		// donner pion commence � 1...
		if (numCase > this.cases.length || numCase <= 0) {
			System.out.println("je n'ai pas ce guerrier");
			return null;
		}
		return this.cases[numCase - 1];
	}

	/**
	 * Bouge le guerrier se trouvant � la case num�ro caseDepart et le met � la case
	 * num�ro caseArriv�e
	 * 
	 * @param caseDepart  : num�ro de la case o� se trouve le guerrier � bouger
	 * @param caseArrivee : num�ro de la case o� il faut mettre le guerrier
	 *
	 */

	public void bougerPion(int caseDepart, int caseArrivee) {
		// TODO
		// Les cases commencent � 1...
		cases[caseArrivee - 1] = cases[caseDepart - 1];
		this.supprimerPion(caseDepart);
	}

	/**
	 * D�termine si le guerrier se trouvant sur la case num�ro numCase appartient au
	 * joueur joueur
	 * 
	 * @param numCase : num�ro de la case
	 * @param joueur  : le joueur
	 * @return true si le guerrier se trouvant � la case num�ro numCase appartient
	 *         au joueur Joueur false sinon
	 */
	public boolean estUnPionDuJoueur(int numCase, Joueur joueur) {
		// numCase commence � 1...
		if (this.cases[numCase - 1] != null && this.cases[numCase - 1].getNumJoueur() == joueur.getNumJoueur()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * supprime le guerrier se trouvant � la case num�ro numCase (vide la case)
	 * 
	 * @param numCase : num�ro de la case
	 */

	public void supprimerPion(int numCase) {
		// numCase commence � 1...
		this.cases[numCase - 1] = null;
	}

	/**
	 * Classe les guerriers encore un jeu d'abord selon le nombre tour d�j� effectu�
	 * (du plus grand au plus petit) et ensuite (si dans le m�me tour) par num�ro de
	 * case occup�e (du plus grand au plus petit)
	 * 
	 * @return un tableau de pion repr�sentant le classement des pions selon les
	 *         crit�res ci-dessus
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
				// D'abord, celui qui a le plus de tour en priorit�
				if (guerriers[j].getNombreDeTours() > guerriers[iMin].getNombreDeTours()) {
					iMin = j;
				}
				// Sinon, si les deux guerriers ont le m�me nombre de tours, celui le plus proche de la fin en priorit�
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
	
	// Cette m�thode nous indique combien de cases il reste avant de 
	// terminer le plateau. Ca retourne -1 si le guerrier n'a pas �t� trouv�. 
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
