package fr.haronman.demineur.model.Plateau;

import java.util.ArrayList;
import java.util.Random;

import java.io.Serializable;

import fr.haronman.demineur.model.Plateau.Case.Case;
import fr.haronman.demineur.model.Plateau.Case.Mine;
import fr.haronman.demineur.model.Plateau.Case.Terrain;

/**
 * Classe agissant comme un plateau de jeu
 * @author HaronMan
 */
public class Plateau implements Serializable {
    // Id pour sauvegarde (Serializable)
    private static final long serialVersionUID = 10L;
    // Matrice du plateau
    private Case[][] matrice;
    // Nombre de bombes
    private final int nbrBombe;
    // Liste des emplacements des mines
    private ArrayList<Integer[]> emplacementsMines;
    // Liste des emplacements de drapeaux
    private ArrayList<Integer[]> emplacementsDrapeaux;

    /**
     * Constructeur
     * @param row Nombre de lignes
     * @param column Nombre de colonnes
     * @param nbrBombe Nombre de bombes
     */
    public Plateau(int row, int column, int nbrBombe){
        matrice = new Case[row][column];
        this.nbrBombe = nbrBombe;
        emplacementsMines = new ArrayList<Integer[]>(); 
        emplacementsDrapeaux = new ArrayList<Integer[]>(); 
        initTerrains();
        placerMines();
    }

    /**
     * Renvoie la case correspondante à l'emplacement de la ligne et de la colonne cibles
     * @param row Numéro de ligne
     * @param column Numéro de colonne
     * @return case correspondante
     */
    public Case getCase(int row, int column) {
        return matrice[row][column];
    }

    /**
     * Initialisation du plateau
     * Au début, tout est un terrain
     */
    private void initTerrains(){
        for(int i = 0; i < matrice.length; i++){
            for(int j = 0; j < matrice[0].length; j++){
                matrice[i][j] = new Terrain(i, j);
            }
        }
    }

    /**
     * Placement des mines de manière aléatoire
     */
    private void placerMines(){
        Random r = new Random();
        int cpt_bombes = 0;
        while(cpt_bombes < nbrBombe){
            int x = r.nextInt(matrice.length);
            int y = r.nextInt(matrice[0].length);
            if( !(matrice[x][y] instanceof Mine)){
                matrice[x][y] = new Mine(x, y);
                emplacementsMines.add(new Integer[]{x, y});
                cpt_bombes++;
            }
        }
    }

    /**
     * Permet de calculer les bombes se trouvant
     * à proximité d'un terrain donné
     * @param t terrain donné
     */
    public void calculerBombesProches(Terrain t){
        int cpt = 0, row = t.getRow(), column = t.getColumn();
        //cpt = compteur de bombes
        for(int i = row - 1; i <= row + 1; i++){
            if(i >= 0 && i < matrice.length){
                for(int j = column - 1; j <= column + 1; j++){
                    if(j >= 0 && j < matrice[i].length){
                        if(matrice[i][j] instanceof Mine){
                            cpt++;
                        }
                    }
                }
            }
        }
        t.setBombesProches(cpt);
    }

    /**
     * Renvoie la matrice du plateau
     * @return matrice
     */
    public Case[][] getMatrice() {
        return matrice;
    }

    /**
     * Renvoie la liste des emplacements des mines
     * @return liste en question
     */
    public ArrayList<Integer[]> getEmplacementsMines() {
        return emplacementsMines;
    }

    /**
     * Renvoie la liste des emplacements des drapeaux
     * @return liste en question
     */
    public ArrayList<Integer[]> getEmplacementsDrapeaux() {
        return emplacementsDrapeaux;
    }
}
