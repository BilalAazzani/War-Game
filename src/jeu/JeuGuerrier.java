package jeu;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * 
 *         Classe d'ex�cution du jeu
 *
 */

public class JeuGuerrier {

	private static Scanner scanner = new Scanner(System.in);
	private static GrilleJeu grille; // gestion des donn�es du jeu
	private static PlateauDeJeu plateau; // panneau graphique du jeu
	private static De de = new DeJeu();

	public static void main(String[] args) {
		
		System.out.println("Bienvenue au jeu des guerriers !");

		// configuration du jeu
		// A ne pas modifier

		System.out.print("Entrez le nombre de cases : ");
		int nbrCases = UtilitairesJeux.lireEntierPositif("Le nombre de cases doit �tre pair");
		System.out.print("Entrez le nombre de tours : ");
		int nbrTours = UtilitairesJeux.lireEntierPositif("Le nombre de tours est de minimum 1");
		System.out.print("Entrez le nombre de joueurs : ");
		int nbreJoueurs = UtilitairesJeux.lireEntierPositif("Le nombre de joueurs est de minimum 2");
		System.out.print("Entrez le nombre de guerriers par joueurs : ");
		int nbreJetons = UtilitairesJeux.lireEntierPositif("Le nombre de guerriers est de minimum 1");
		System.out.print("Entrez le nombre de points de vie des guerriers : ");
		int ptsVie = UtilitairesJeux.lireEntierPositif("Le nombre de points de vie est de minimum 1");

		String[] nomJoueurs = new String[nbreJoueurs];
		System.out.println("Entrez les noms des joueurs selon l'ordre du jeu : ");
		for (int numJoueur = 1; numJoueur <= nbreJoueurs; numJoueur++) {
			System.out.print("Entrez le nom du joueur " + numJoueur + " : ");
			nomJoueurs[numJoueur - 1] = UtilitairesJeux.lireStringNonVide("Le nom doit contenir au moins une lettre");
		}

		grille = new GrilleJeu(nbreJoueurs, nbrCases, nbreJetons, nbrTours, ptsVie, nomJoueurs);
		plateau = new PlateauDeJeu(nbrCases, nbreJoueurs, nbreJetons, grille);
		// TODO
		
		// numero du joueur qui joue
		int numTourJoueur = 1;
		int lancerDe, caseDepart, caseArrive;
		plateau.afficherGuerriers(grille.classerGuerriers());
		//WHILE
		while (!grille.partieFinie()) {
			Joueur joueur = grille.donnerJoueur(numTourJoueur);
			lancerDe = de.lancer();
			plateau.afficherInformation(joueur.getNom() + " (N� "+joueur.getNumJoueur()+") doit faire avancer un guerrier de " + lancerDe + " cases.");
			plateau.afficherResultatDe(lancerDe);
			plateau.afficherJoueur(joueur);
			
			caseDepart = plateau.jouer();
			caseArrive = caseDepart + lancerDe;
			// on recommence au d�but si on d�passe le tableau.
			if (caseArrive > nbrCases) {
				caseArrive = caseArrive - nbrCases;
			}
			
			
			// Un joueur ne peut pas jouer avec le guerrier d�un autre joueur. S�il essaie de jouer avec
			// le guerrier d�un autre joueur, il perd son tour de jeu.
			// et Si un joueur clique sur case o� il n�y a pas de guerrier, il perd son tour de jeu
			if (grille.estUnPionDuJoueur(caseDepart, joueur)) {
				
				Guerrier joueurDepart = grille.donnerPion(caseDepart);
				Guerrier joueurArrive = grille.donnerPion(caseArrive);
			
				int caseDarriveDuGuerrier;
					
				// le joueur arrive sur un endroit vide
				if (joueurArrive == null) {
					grille.bougerPion(caseDepart, caseArrive);
					plateau.afficherInformation2("Le guerrier est arriv� sur la case vide n�" + caseArrive + ".");
					// Un guerrier a fait un tour quand il repasse sur la case 1. 
					if (caseArrive < caseDepart) {
						joueurDepart.ajouterUnTour();
					}
				}
				// il y a qqun, donc bagarre
				else {
					int degatGuerrierDepart = de.lancer();
					int degatGuerrierArrive = de.lancer();
					// bataille
					joueurArrive.setPtsVie(joueurArrive.getPtsVie() - degatGuerrierDepart);
					joueurDepart.setPtsVie(joueurDepart.getPtsVie() - degatGuerrierArrive);
					plateau.afficherInformation("Combat ! Guerrier de la case " + caseDepart + " VS Guerrier de la case " + caseArrive);

					// 1. Les deux guerriers n�ont plus de points de vie et sont �limin�s du jeu
					if (joueurArrive.getPtsVie() <= 0 && joueurDepart.getPtsVie() <= 0 ) {
						grille.supprimerPion(caseDepart);
						grille.supprimerPion(caseArrive);
						plateau.afficherInformation2("Les deux s'infligent " + degatGuerrierDepart + "points de d�gats, ils meurent ensemble !");
					}
					
					// 2. L�un des guerriers n�a plus de point de vie, il est �limin� et c�est l�autre qui
					//    reste sur la case.
					else if (joueurArrive.getPtsVie() <= 0 && joueurDepart.getPtsVie() > 0 ) {
						grille.bougerPion(caseDepart, caseArrive);
						if (caseArrive < caseDepart) {
							joueurDepart.ajouterUnTour();
						}
						plateau.afficherInformation2("Le guerrier qui a boug� a tu� celui qui se trouvait sur la case d'arriv�e !");
					}
					// 2. L�un des guerriers n�a plus de point de vie, il est �limin� et c�est l�autre qui
					//    reste sur la case.
					else if ((joueurArrive.getPtsVie() > 0 && joueurDepart.getPtsVie() <= 0 )) {
						grille.supprimerPion(caseDepart);
						plateau.afficherInformation2("Le guerrier qui a boug� est mort face � son opposant !");
					}
					
					 /* 3. Les deux guerriers sont en vie et le guerrier qui est jou� a fait perdre plus de
						points de vie que le guerrier attaqu�. Alors le guerrier attaqu� reste sur la
						case qu�il occupait et l�autre avance jusqu�� la premi�re case disponible. La
						recherche commen�ant � la case apr�s celle du guerrier attaqu�
					 */
					else if (joueurArrive.getPtsVie() > 0 && joueurDepart.getPtsVie() > 0  && degatGuerrierDepart > degatGuerrierArrive ) {
						caseArrive = (caseArrive + 1) % nbrCases;
						while (grille.donnerPion(caseArrive) != null ) {
							caseArrive++;
							if (caseArrive == nbrCases + 1) {
								caseArrive = 1;	
							}
						}
						
						grille.bougerPion(caseDepart, caseArrive);
						
						if (caseArrive <  caseDepart) {
							joueurDepart.ajouterUnTour();
						}
						plateau.afficherInformation2("Le guerrier qui a boug� n'a pas pu tuer son ennemi, il s'enfuit donc � la case " + caseArrive + ".");
					}
					else {
						// 4. Les deux guerriers sont en vie et le guerrier qui est jou� a fait perdre le m�me
						//    nombre ou moins de points de vie que le guerrier attaqu�. Alors les 2
						//    guerriers restent sur leur case respective.
						plateau.afficherInformation2("Le guerrier qui a boug� n'a pas pu faire davantage de d�gats que son adversaire, il reste o� il est !");
					}
				}
			}
			else {
				plateau.afficherInformation2("Oups, " + joueur.getNom() + " perd son tour car il a cliqu� sur une case vide ou un guerrier ennemi.");
			}
			
			plateau.afficherGuerriers(grille.classerGuerriers());
			
			// on passe au tour suivant (et on saute ceux qui sont d�j� morts)
			do {
				numTourJoueur++;
				if (numTourJoueur == nbreJoueurs + 1) {
					numTourJoueur = 1;	
				}
			} while (grille.donnerJoueur(numTourJoueur).nombreDeGuerriersEnVie() == 0);
			
		
			plateau.actualiser(grille);
		}
		
		plateau.afficherGuerriers(grille.classerGuerriers());
		plateau.afficherInformation("Le jeu est fini !");
	}
}
