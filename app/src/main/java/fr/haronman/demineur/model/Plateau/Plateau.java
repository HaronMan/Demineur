package fr.haronman.demineur.model.Plateau;

import java.util.ArrayList;
import java.util.Random;

import java.io.Serializable;

import fr.haronman.demineur.model.Plateau.Case.Case;
import fr.haronman.demineur.model.Plateau.Case.Mine;
import fr.haronman.demineur.model.Plateau.Case.Terrain;

public class Plateau implements Serializable {
    private static final long serialVersionUID = 10L;
    private Case[][] matrice;
    private final int nbrBombe;
    private ArrayList<Integer[]> emplacementsMines;
    private ArrayList<Integer[]> emplacementsDrapeaux;

    public Plateau(int row, int column, int nbrBombe){
        matrice = new Case[row][column];
        this.nbrBombe = nbrBombe;
        emplacementsMines = new ArrayList<Integer[]>(); 
        emplacementsDrapeaux = new ArrayList<Integer[]>(); 
        initTerrains();
        placerMines();
    }

    public Case getCase(int row, int column) {
        return matrice[row][column];
    }

    private void initTerrains(){
        for(int i = 0; i < matrice.length; i++){
            for(int j = 0; j < matrice[0].length; j++){
                matrice[i][j] = new Terrain(i, j);
            }
        }
    }

    public void placerMines(){
        Random r = new Random();
        int cpt_bombes = 0;
        while(cpt_bombes < nbrBombe){
            int x = r.nextInt(matrice.length);
            int y = r.nextInt(matrice[0].length);
            if(! (matrice[x][y] instanceof Mine)){
                matrice[x][y] = new Mine(x, y);
                emplacementsMines.add(new Integer[]{x, y});
                cpt_bombes++;
            }
        }
    }

    public void calculerBombesProches(Terrain t){
        int cpt = 0, row = t.getRow(), column = t.getColumn();
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

    public Case[][] getMatrice() {
        return matrice;
    }

    public ArrayList<Integer[]> getEmplacementsMines() {
        return emplacementsMines;
    }

    public ArrayList<Integer[]> getEmplacementsDrapeaux() {
        return emplacementsDrapeaux;
    }

    @Override
    public String toString() {
        String texte = "";
        for(Case[] lig : matrice){
            for(Case c : lig){
                if(c instanceof Terrain t){
                    texte += t.toString()+" ";
                }else if(c instanceof Mine m){
                    texte += m.toString()+" ";
                }
            }
            texte += "\n";
        }
        return texte;
    }
}
