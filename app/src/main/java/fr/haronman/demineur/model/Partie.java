package fr.haronman.demineur.model;

import java.util.Arrays;
import java.util.Random;
import java.io.Serializable;

import fr.haronman.demineur.model.Plateau.Plateau;
import fr.haronman.demineur.model.Plateau.Case.Case;
import fr.haronman.demineur.model.Plateau.Case.Mine;
import fr.haronman.demineur.model.Plateau.Case.Terrain;

/**
 * Classe correspondante à une partie classqiue du jeu
 * @author HaronMan
 */
public class Partie implements Serializable{
    // Id pour sauvegarde (Serializable)
    public static final long serialVersionUID = 2273883084829754458L;
    // Nom de la sauvegarde
    private String nomSave;
    // Plateau de la partie
    private Plateau plateau;
    // Nombre de drapeaux restants
    private int drapeaux;
    // Nombre de cases restantes à découvrir 
    // (bombes non comprises)
    private int cases_restantes;
    // Premier clic de la partie
    private boolean premier_clic;
    // Difficulté de la partie
    private final Difficulte difficulte;
    // Temps écoulé depuis le début de la partie (en ms)
    private static long millis;
    
    /**
     * Constructeur
     * @param difficulte difficulté souhaité
     */
    public Partie(Difficulte difficulte){
        this.difficulte = difficulte;
        plateau = new Plateau(difficulte.getRow(), difficulte.getColumn(), difficulte.getNbrBombe());
        drapeaux = difficulte.getNbrBombe();
        cases_restantes = difficulte.getRow() * difficulte.getColumn() - difficulte.getNbrBombe();
        premier_clic = true;
    }

    /**
     * Renvoie le plateau de la partie
     * @return plateau de partie
     */
    public Plateau getPlateau() {
        return plateau;
    }

    /**
     * Renvie directement la matrice du plateau de la partie
     * @return matrice du plateau
     */
    public Case[][] getMatricePlateau(){
        return plateau.getMatrice();
    }

    /**
     * Récupére la case correspondante de la matrice du plateau
     * à partir d'un numéro de ligne et de colonne donnée
     * @param row numéro de ligne
     * @param column numéro de colonne
     * @return case correspondante
     */
    public Case getCaseMatrice(int row, int column){
        return plateau.getCase(row, column);
    }

    /**
     * Ajoute un emplacement de drapeau dans la liste correspondante
     * @param pos tableau d'entier correspondant aux coordonnées
     */
    public void addEmplacementsDrapeaux(Integer[] pos){
        plateau.getEmplacementsDrapeaux().add(pos);
    }

    /**
     * Retire un emplacement de drapeau dans la liste correspondante
     * @param pos tableau d'entier correspondant aux coordonnées
     */
    public void removeEmplacementsDrapeaux(Integer[] pos){
        plateau.getEmplacementsDrapeaux().removeIf(coordonnees -> Arrays.equals(coordonnees, pos));
    }

    /**
     * Replace une mine sur la matrice du terrain, ceci peut être
     * du au fait qu'au premier clic, le joueur à cliqué sur une mine
     * @param c case du clic
     * @return terrain qui sera remplacé
     */
    public Terrain replacerMine(Case c){
        Terrain t = null;
        Case[][] matrice = getMatricePlateau();
        Random r = new Random();
        boolean placee = false;
        while(!placee){
            int x = r.nextInt(matrice.length);
            int y = r.nextInt(matrice[0].length);
            if( !(matrice[x][y] instanceof Mine) && matrice[x][y] != c){
                // Placement de la mine
                matrice[x][y] = new Mine(x, y);
                plateau.getEmplacementsMines().removeIf(coordonnees -> Arrays.equals(coordonnees, new Integer[]{c.getRow(), c.getColumn()}));
                plateau.getEmplacementsMines().add(new Integer[]{x, y});
                t = new Terrain(c.getRow(), c.getColumn());
                placee = true;
            }
        }
        return t;
    }

    /**
     * Dévoile toutes les mines du plateau, 
     * cela se produit à la défaite de la partie
     */
    public void devoilerMines(){
        for(Integer[] pos : plateau.getEmplacementsMines()){
            Mine m = (Mine) plateau.getCase(pos[0], pos[1]);
            if(!m.getDrapeau()){
                // Si la mine n'a pas de drapeau
                m.decouvrir();
            }else{
                // Si un drapeau est posé sur une mine
                m.bonne_mine();
            }
            // Position de drapeau retiré
            removeEmplacementsDrapeaux(pos);
        }
    }

    /**
     * Dévoile tous les drapeaux mal placés, elle sera
     * appelé après avoir dévoilé les mines
     */
    public void devoilerFauxDrapeaux(){
        for(Integer[] pos : plateau.getEmplacementsDrapeaux()){
            Case c = plateau.getCase(pos[0], pos[1]);
            if(c instanceof Terrain t){
                t.fausse_mine();
            }
        }   
    }

    /**
     * Renvoie le nombre de drapeaux restants
     * @return nombre de drapeau
     */
    public int getDrapeaux() {
        return drapeaux;
    }

    /**
     * Renvoie le nombre de cases restantes à dévoiler
     * @return nombre de cases restantes
     */
    public int getCasesRestantes() {
        return cases_restantes;
    }

    /**
     * Décremente le nombre de cases restantes
     */
    public void retirerCaseRestante(){
        cases_restantes--;
    }

    /**
     * Décremente le nombre de drapeaux restants (s'il en reste)
     */
    public void retirerDrapeaux() {
        if (drapeaux > 0){
            this.drapeaux--;
        }
    }

    /**
     * Incrémente le nombre de drapeaux restants
     */
    public void ajouterDrapeaux() {
        this.drapeaux++;
    }

    /**
     * Renvoie le nom de la sauvegarde
     * @return nom de la sauvegarde
     */
    public String getNomSave() {
        return nomSave;
    }

    /**
     * Définit le nom de la sauvegarde
     * @param nomSave nom de la sauvegarde
     */
    public void setNomSave(String nomSave) {
        this.nomSave = nomSave;
    }

    /**
     * Renvoie le nombre de drapeaux restants
     * @return nombre de drapeaux restants
     */
    public int getNbrDrapeaux(){
        return drapeaux;
    }
    
    /**
     * Renvoie la difficulté de la partie
     * @return difficulté de la partie
     */
    public Difficulte getDifficulte(){
        return difficulte;
    }

    /**
     * Renvoie l'état du premier clic de la partie
     * @return état du premier clic de la partie
     */
    public boolean getPremierClic(){
        return premier_clic;
    }

    /**
     * Redéfinit l'état du premier clic de la partie
     */
    public void premierClicEffectue(){
        premier_clic = false;
    }

    /**
     * Renvoie le nombre de millisecondes qui s'est écoulé depuis le début de la partie
     * @return nombre de millisecondes
     */
    public long getMillis() {
        return millis;
    }

    /**
     * Définit le nombre de millisecondes
     * @param millis nombre de millisecondes
     */
    public void setMillis(long millis) {
        this.millis = millis;
    }

    /**
     * Incrémente le compteur de millisecondes
     */
    public void incrementMillis(){
        millis++;
    }

    public static String convertTime(Partie p){
        int m = 0, h = 0, s = 0;
        long ms = p.getMillis();
        String text = "";
        while(ms >= 1000){
            s++;
            ms -= 1000;
        }
        while(s >= 60){
            m++;
            s -= 60;
        }
        while(m >= 60){
            h++;
            m -= 60;
        }
        text += (h > 0) ? h+"h" : "";
        text += (m > 0) ? m+"m" : "";
        text += s+"s";
        return text;
    }
}
