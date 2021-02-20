package jeu;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.Queue;

import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import javax.swing.plaf.metal.MetalLookAndFeel ;


/**
 * 
 * Classe gérant la version graphique du jeu des guerriers
 *
 */

/**
 * @author user
 *
 */
public class PlateauDeJeu extends JFrame implements ActionListener, WindowListener {

	public static java.util.Scanner scanner = new java.util.Scanner(System.in);

	private int nombreJoueurs; // nombre de Joueurs
	private int nombreDePionsParJoueur; // nombre de pions par Joueurs
	private int nombreDeCases; // nombre de cases
	private JPanel principal, damier, deEtJoueur, boutons, donneesP;
	private JButton[] cases;
	private JButton stop;
	private JLabel joueur, faceDe, titre;
	private ImageIcon[] fDe;
	private ImageIcon[] coulJoueur ;
	private ImageIcon[][] coul;
	private ImageIcon arrivee ;
	private Color couleurDeBase ;
	private boolean partieEnCours = false;
	private Queue<Integer> coupQueue = new LinkedList<Integer>();
	private Thread listener;
	private JLabel information;
	private JLabel infoGagnante;
	private JTable classement;

	/**
	 * Construit la version graphique du jeu des guerriers en le mettant dans l'état de départ défini par grille 
	 * @param nombreDeCases : le nombre de cases du jeu
	 * @param nombreJoueurs : le nombre de joueur
	 * @param nombreDePionsParJoueur : le nombre de pions par joueur
	 * @param grille : l'état de départ du jeu
	 * 
	 */
	public PlateauDeJeu(int nombreDeCases,int nombreJoueurs, int nombreDePionsParJoueur, GrilleJeu grille) {
		this.nombreDeCases = nombreDeCases;
		this.nombreJoueurs = nombreJoueurs ;
		this.nombreDePionsParJoueur = nombreDePionsParJoueur ;
		partieEnCours = true;

		listener = Thread.currentThread();
		try {
			UIManager.setLookAndFeel(new MetalLookAndFeel());
		} catch (Exception e) {
			System.out.println(e.getMessage()) ;
		}
		initialiserLesIcones();
		this.setTitle("Jeu des guerriers");

		this.setSize(1500, 1000);
		this.setLocation(30, 30);
		principal = new JPanel();
		principal.setLayout(new BorderLayout());
		donneesP = construirePanneauDeDonnees();
		damier = construirePlateau(this.nombreDeCases, grille);

		principal.add(damier, BorderLayout.CENTER);
		principal.add(construirePanneauClassement(), BorderLayout.EAST);
		this.setContentPane(principal);
		this.addWindowListener(this);
		this.setVisible(true);
	}
	
	
	/**
	 * Construit les cases du jeu
	 * @param nbJoueurs : nombres de joueurs
	 * @param nbCases : nombres de cases
	 * @param nbPions : nombres de pions par joueurs
	 * @return
	 */
	private JPanel construirePlateau(int nbCases, GrilleJeu grille) {
		
		JPanel plateauP = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		plateauP.setLayout(gridbag);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		cases = new JButton[nbCases];
		Font font = new Font("Arial", Font.BOLD, 18);
		cases[nbCases - 1]=new JButton("<html><center>Arrivée<br> " + nbCases + " </center></html>");
		couleurDeBase = cases[nbCases-1].getBackground() ;
		cases[nbCases - 1].setIcon(arrivee) ; 
		cases[nbCases - 1].setVerticalTextPosition(AbstractButton.BOTTOM);
		cases[nbCases - 1].setHorizontalTextPosition(AbstractButton.CENTER);
		for (int i = 0; i < nbCases; i++) {
			Guerrier guerrier = grille.donnerPion(i+1) ;
			if (guerrier!=null) {
				cases[i] = new JButton(coul[guerrier.getNumJoueur()-1][guerrier.getNumGuerrier()-1]);
				cases[i].setText(" " + (i + 1) + " ");
				cases[i].setVerticalTextPosition(AbstractButton.BOTTOM);
				cases[i].setHorizontalTextPosition(AbstractButton.CENTER);
			} else if (i<nbCases-1) {
				cases[i] = new JButton(" " + (i + 1) + " ");
			}
			cases[i].setFont(font);
			// cases[i].setIconTextGap(-42);
			cases[i].setForeground(Color.BLUE);
			cases[i].setBorder(new LineBorder(Color.black, 1, false));
			cases[i].setPreferredSize(new Dimension(100, 100));
			cases[i].addActionListener(this);
		}
		int largeur = (nbCases / 2) / 2;
		int longueur = (nbCases / 2) - largeur;
		int numCase = 0;
		gbc.gridy = 0;
		gbc.gridx = 0;
		for (int i = 0; i < longueur; i++) {
			gbc.gridx = i;
			gridbag.setConstraints(cases[numCase], gbc);
			plateauP.add(cases[numCase]);
			numCase++;
		}
		for (int i = 1; i <= largeur; i++) {
			gbc.gridy = i;
			gridbag.setConstraints(cases[numCase], gbc);
			plateauP.add(cases[numCase]);
			numCase++;
		}
		gbc.gridy = largeur + 1;
		for (int i = longueur - 1; i >= 0; i--) {
			gbc.gridx = i;
			gridbag.setConstraints(cases[numCase], gbc);
			plateauP.add(cases[numCase]);
			numCase++;
		}
		for (int i = largeur; i >= 1; i--) {
			gbc.gridy = i;
			gridbag.setConstraints(cases[numCase], gbc);
			plateauP.add(cases[numCase]);
			numCase++;
		}

		gbc.gridheight = largeur;
		gbc.gridwidth = longueur - 2;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gridbag.setConstraints(donneesP, gbc);
		plateauP.add(donneesP);

		return plateauP;
	}

	
	/**
	 * Construit le panneau qui va contenir les labels de commentaires ainsi que les boutons de début et de fermeture du jeu
	 * @return le panneau
	 */
	private JPanel construirePanneauDeDonnees() {
		JPanel panneauDonnees = new JPanel();
		panneauDonnees.setLayout(new GridLayout(5, 1));
		titre = new JLabel("Bienvenue dans le jeu des guerriers !");
		titre.setHorizontalAlignment(SwingConstants.CENTER);
		titre.setVerticalAlignment(SwingConstants.CENTER);
		Font font = new Font("Arial", Font.BOLD, 24);
		titre.setFont(font);
		titre.setForeground(Color.BLUE);
		panneauDonnees.add(titre);
		deEtJoueur = new JPanel();
		deEtJoueur.setLayout(new GridLayout(1, 4));
		deEtJoueur.add(new JLabel("Joueur :"));
		joueur = new JLabel("");
		deEtJoueur.add(joueur);
		deEtJoueur.add(new JLabel("Résultat du dé ;"));
		faceDe = new JLabel("");
		deEtJoueur.add(faceDe);
		panneauDonnees.add(deEtJoueur);
		Font font2 = new Font("Arial", Font.BOLD, 24);
		information = new JLabel();
		information.setFont(font2);
		information.setForeground(Color.RED);
		information.setHorizontalAlignment(SwingConstants.CENTER);
		information.setVerticalAlignment(SwingConstants.CENTER);
		panneauDonnees.add(information);
		infoGagnante = new JLabel();
		infoGagnante.setFont(font);
		infoGagnante.setForeground(Color.GREEN);
		infoGagnante.setHorizontalAlignment(SwingConstants.CENTER);
		infoGagnante.setVerticalAlignment(SwingConstants.CENTER);
		panneauDonnees.add(infoGagnante);
		boutons = new JPanel();
		boutons.setLayout(new GridLayout(1, 5));
		boutons.add(new JLabel(""));
		boutons.add(new JLabel(""));
		boutons.add(new JLabel(""));
		boutons.add(new JLabel(""));
		stop = new JButton("Exit");
		stop.addActionListener(this);
		boutons.add(stop);
		boutons.add(new JLabel(""));
		panneauDonnees.add(boutons);
		panneauDonnees.setBorder(new LineBorder(Color.black, 1, false));
		return panneauDonnees;
	}
	
	

	
	
	/**
	 * Construit le panneau qui va contenir le classement des guerriers
	 * @return le panneau
	 */
	private JPanel construirePanneauClassement() {
		JPanel panneauClassement = new JPanel();
		panneauClassement.setLayout(new BorderLayout());
		JLabel titreClassement = new JLabel("  Classement des guerriers");
		titreClassement.setFont(new Font("Arial", Font.BOLD, 24));
		titreClassement.setHorizontalTextPosition(SwingConstants.CENTER);
		panneauClassement.add(titreClassement, BorderLayout.NORTH);
		panneauClassement.add(new JLabel("  "), BorderLayout.WEST);
		String[] titre = new String[4];
		titre[0] = "N°";
		titre[1] = "Guerrier";
		titre[2] = "Tours";
		titre[3] = "Pts Vie";
		int nbJetons = this.nombreJoueurs * this.nombreDePionsParJoueur;
		Object[][] donneesGuerriers = new Object[nbJetons][4];
		for (int i = 0; i < nbJetons; i++) {
			donneesGuerriers[i][0] = "" + (i + 1);
		}
		for (int i = 0; i < this.nombreJoueurs; i++) {
			for (int j = 0; j < this.nombreDePionsParJoueur; j++) {
				donneesGuerriers[i + j * this.nombreJoueurs][1] = coul[this.nombreJoueurs - 1 - i][this.nombreDePionsParJoueur-1-j];
			}
		}
		for (int i = 0; i < nbJetons; i++) {
			for (int j = 2; j < 4; j++) {
				donneesGuerriers[i][j] = "";
			}
		}

		classement = new JTable(donneesGuerriers, titre);
		classement.setRowHeight(75);

		DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
		custom.setHorizontalAlignment(JLabel.CENTER);
		custom.setFont(new Font("Arial", Font.BOLD, 24));// centre les données de ton tableau
		classement.getColumnModel().getColumn(0).setCellRenderer(custom);
		classement.getColumnModel().getColumn(1).setCellRenderer(new IconCellRenderer());
		classement.getColumnModel().getColumn(2).setCellRenderer(custom);
		classement.getColumnModel().getColumn(3).setCellRenderer(custom);
		classement.setPreferredScrollableViewportSize(classement.getPreferredSize());

		panneauClassement.add(new JScrollPane(classement), BorderLayout.CENTER);
		return panneauClassement;
	}
	
	
	/**
	 * Affiche le classement des guerriers représenté par le tableau guerriers
	 * @param guerriers : tableau de pions représentant le classement des guerriers
	 */
	public void afficherGuerriers(Guerrier[] guerriers) {
		for (int i=0 ; i<guerriers.length ; i++) {
			classement.setValueAt(""+(i+1), i,0);
			classement.setValueAt(coul[guerriers[i].getNumJoueur()-1][guerriers[i].getNumGuerrier()-1],i,1) ;
			classement.setValueAt(guerriers[i].getNombreDeTours(), i, 2);
			classement.setValueAt(guerriers[i].getPtsVie(), i, 3);
		}
		int nbMaxPions = this.nombreJoueurs*this.nombreDePionsParJoueur ;
		for (int i=guerriers.length ; i<nbMaxPions ; i++) {
			classement.setValueAt("", i, 0);
			classement.setValueAt(null,i,1) ;
			classement.setValueAt("", i, 2);
			classement.setValueAt("", i, 3);
		}
		redessiner() ;
	}

	private void initialiserLesIcones() {
		fDe = new ImageIcon[6];
		coul = new ImageIcon[8][10];
		coulJoueur = new ImageIcon[8];
		arrivee = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/Arrivee.png")).getImage().getScaledInstance(125, 50, Image.SCALE_DEFAULT)) ;
		fDe[0] = new ImageIcon(PlateauDeJeu.class.getResource("/un.png"));
		fDe[1] = new ImageIcon(PlateauDeJeu.class.getResource("/deux.png"));
		fDe[2] = new ImageIcon(PlateauDeJeu.class.getResource("/trois.png"));
		fDe[3] = new ImageIcon(PlateauDeJeu.class.getResource("/quatre.png"));
		fDe[4] = new ImageIcon(PlateauDeJeu.class.getResource("/cinq.png"));
		fDe[5] = new ImageIcon(PlateauDeJeu.class.getResource("/six.png"));
		coulJoueur[0] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierBlanc.png")).getImage()
				.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
		coulJoueur[1] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierNoir.png")).getImage()
				.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
		coulJoueur[2] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierRouge.png")).getImage()
				.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
		coulJoueur[3] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierBleu.png")).getImage()
				.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
		coulJoueur[4] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierVert.png")).getImage()
				.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
		coulJoueur[5] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierCyan.png")).getImage()
				.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
		coulJoueur[6] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierJaune.png")).getImage()
				.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
		coulJoueur[7] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierMauve.png")).getImage()
				.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
		for (int j = 1; j < 11; j++) {
			coul[0][j-1] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierBlanc"+j+".png")).getImage()
					.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
			coul[1][j-1] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierNoir"+j+".png")).getImage()
					.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
			coul[2][j-1] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierRouge"+j+".png")).getImage()
					.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
			coul[3][j-1] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierBleu"+j+".png")).getImage()
					.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
			coul[4][j-1] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierVert"+j+".png")).getImage()
					.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
			coul[5][j-1] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierCyan"+j+".png")).getImage()
					.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
			coul[6][j-1] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierJaune"+j+".png")).getImage()
					.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
			coul[7][j-1] = new ImageIcon(new ImageIcon(PlateauDeJeu.class.getResource("/GuerrierMauve"+j+".png")).getImage()
					.getScaledInstance(75, 75, Image.SCALE_DEFAULT));
		}
	}

	
	/**
	 * Affiche le résutat du lancer du dé
	 * @param i : le numéro affiché par le dé
	 */
	public void afficherResultatDe(int i) {
		faceDe.setIcon(fDe[i - 1]);
		redessiner() ;
	}

	private void redessiner() {
		principal.invalidate();
		principal.repaint();
	}

	

	/**
	 * Affiche le joueur dont c'est le tour de jeu
	 * @param jou : le joueur dont c'est le tour de jeu
	 */
	public void afficherJoueur(Joueur jou) {
		joueur.setIcon(coulJoueur[jou.getNumJoueur() - 1]);
		joueur.setText(jou.getNom());
		redessiner();
	}

	
	/**
	 * Affiche le joueur ayant gagné ou "pas de gagnant" si jou==null
	 * @param jou : le jouer qui a gagné
	 */
	public void afficherGagnant(Joueur jou) {
		Font font = new Font("Arial", Font.BOLD, 16);
		joueur.setFont(font);
		if (jou==null) {
			joueur.setIcon(null) ;
			joueur.setForeground(Color.ORANGE);
			joueur.setText("Pas de gagnant !");
		} else {
			joueur.setIcon(coulJoueur[jou.getNumJoueur() - 1]);
			joueur.setForeground(Color.GREEN);
			joueur.setText("Gagné " + jou.getNom() + "!");
		}
		partieEnCours = false;
		for (int j = 0; j < cases.length; j = j + 1) {
			cases[j].setEnabled(false);
		}
		redessiner() ;
	}
	
	/**
	 * Affiche la fin du jeu
	 */
	public void afficherFinDuJeu() {
		titre.setForeground(Color.MAGENTA);
		titre.setText("Fin du Jeu ! Merci d'avoir joué !") ;
		partieEnCours = false;
		for (int j = 0; j < cases.length; j = j + 1) {
			cases[j].setEnabled(false);
		}
		redessiner() ;
	}
	

	/**
	 * Affiche Un information sur la première ligne prévue à cet effet
	 * @param texte : le texte à afficher
	 */
	public void afficherInformation(String texte) {
		information.setText(texte);
	}

	/**
	 * Affiche Un information sur la seconde ligne prévue à cet effet
	 * @param texte : le texte à afficher
	 */
	public void afficherInformation2(String texte) {
		infoGagnante.setText(texte);
	}
	
	/**
	 * Affiche l'état du jeu définit par grille et mets en vert les cases qui ont été modifiée depuis l'état précédent
	 * @param grille : l'état du jeu
	 */
	public void actualiser(GrilleJeu grille) {
		Font font = new Font("Arial", Font.BOLD, 18);
		Icon iconeOld = cases[this.nombreDeCases-1].getIcon() ;
		Guerrier guerrierI = grille.donnerPion(this.nombreDeCases) ;
 		ImageIcon iconeNew = arrivee ;
 		cases[this.nombreDeCases-1].setText("<html><center>Arrivée<br> " + this.nombreDeCases + " </center></html>");
 		if (guerrierI!=null) {
 			iconeNew = coul[guerrierI.getNumJoueur()-1][guerrierI.getNumGuerrier()-1] ;
 			cases[this.nombreDeCases-1].setIcon(iconeNew) ;
 		} else {
 			cases[this.nombreDeCases-1].setIcon(arrivee) ; 
 			cases[this.nombreDeCases-1].setVerticalTextPosition(AbstractButton.BOTTOM);
 			cases[this.nombreDeCases-1].setHorizontalTextPosition(AbstractButton.CENTER);
 		}
 		if (iconeNew!=iconeOld) {
 			cases[this.nombreDeCases-1].setBackground(Color.GREEN);
 		} else {
 			cases[this.nombreDeCases-1].setBackground(couleurDeBase);
 		}
		
		for (int i = 0; i < this.nombreDeCases-1; i++) {
			Guerrier guerrier = grille.donnerPion(i+1) ;
			iconeOld = cases[i].getIcon() ;
			iconeNew = null ;
			if (guerrier!=null) {
				iconeNew = coul[guerrier.getNumJoueur()-1][guerrier.getNumGuerrier()-1] ;
				cases[i].setIcon(iconeNew);
				cases[i].setText(" " + (i + 1) + " ");
				cases[i].setVerticalTextPosition(AbstractButton.BOTTOM);
				cases[i].setHorizontalTextPosition(AbstractButton.CENTER);
			} else if (i<this.nombreDeCases-1){
				cases[i].setIcon(null);
				cases[i].setText(" " + (i + 1) + " ");
			}
			if (iconeNew!=iconeOld) {
	 			cases[i].setBackground(Color.GREEN);
	 		} else {
	 			cases[i].setBackground(couleurDeBase);
	 		}
			cases[i].setFont(font);
			// cases[i].setIconTextGap(-42);
			cases[i].setForeground(Color.BLUE);
			cases[i].setBorder(new LineBorder(Color.black, 1, false));
			cases[i].setPreferredSize(new Dimension(100, 100));
		}
		redessiner() ;
	}

	private int coupSuivant() {
		synchronized (coupQueue) {
			while (coupQueue.isEmpty())
				try {
					coupQueue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			return coupQueue.remove();

		}
	}
	
	/**
	 * Renvoie le dernier coup joué
	 * @return le dernier coup joué
	 */
	public int jouer() {
		return coupSuivant();
	}

	private void addCoup(int numCase) {
		synchronized (coupQueue) {
			coupQueue.add(numCase);
			if (listener != null)
				coupQueue.notifyAll();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton jb = (JButton) e.getSource();
			if (jb == stop) {
				System.exit(0);
			} else if (partieEnCours) {
				this.information.setText("");
				this.infoGagnante.setText("");
				int i = 0;
				while ((i < cases.length) && (cases[i] != jb)) {
					i = i + 1;
				}
				addCoup(i + 1);
			}
		}
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		e.getWindow().dispose();
		System.exit(0);
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	private class IconCellRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			JLabel label = (JLabel) component;
			label.setIcon((ImageIcon) value);

			return component;
		}

	}

}