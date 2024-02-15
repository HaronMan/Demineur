package fr.haronman.demineur.model;

import java.util.Arrays;
import java.util.Random;
import java.io.Serializable;

import fr.haronman.demineur.model.Plateau.Plateau;
import fr.haronman.demineur.model.Plateau.Case.Case;
import fr.haronman.demineur.model.Plateau.Case.Mine;
import fr.haronman.demineur.model.Plateau.Case.Terrain;

public class Partie implements Serializable{
    public static final long serialVersionUID = 2273883084829754458L;
    private String nomSave;
    private Plateau plateau;
    private int drapeaux;
    private int cases_restantes;
    private boolean premier_clic;
    private final Difficulte difficulte;
    private int secondes;
    
    public Partie(Difficulte difficulte){
        this.difficulte = difficulte;
        plateau = new Plateau(difficulte.getRow(), difficulte.getColumn(), difficulte.getNbrBombe());
        drapeaux = difficulte.getNbrBombe();
        cases_restantes = difficulte.getRow() * difficulte.getColumn() - difficulte.getNbrBombe();
        premier_clic = true;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Case[][] getMatricePlateau(){
        return plateau.getMatrice();
    }

    public Case getCaseMatrice(int row, int column){
        return plateau.getCase(row, column);
    }

    public void addEmplacementsDrapeaux(Integer[] pos){
        plateau.getEmplacementsDrapeaux().add(pos);
    }

    public void removeEmplacementsDrapeaux(Integer[] pos){
        plateau.getEmplacementsDrapeaux().removeIf(coordonnees -> Arrays.equals(coordonnees, pos));
    }

    public Terrain replacerMine(Case c){
        Terrain t = null;
        Case[][] matrice = getMatricePlateau();
        Random r = new Random();
        boolean placee = false;
        while(!placee){
            int x = r.nextInt(matrice.length);
            int y = r.nextInt(matrice[0].length);
            if( !(matrice[x][y] instanceof Mine) && matrice[x][y] != c){
                matrice[x][y] = new Mine(x, y);
                plateau.getEmplacementsMines().removeIf(coordonnees -> Arrays.equals(coordonnees, new Integer[]{c.getRow(), c.getColumn()}));
                plateau.getEmplacementsMines().add(new Integer[]{x, y});
                t = new Terrain(c.getRow(), c.getColumn());
                placee = true;
            }
        }
        return t;
    }

    public void devoilerMines(){
        for(Integer[] pos : plateau.getEmplacementsMines()){
            Mine m = (Mine) plateau.getCase(pos[0], pos[1]);
            if(!m.getDrapeau()){
                m.decouvrir();
            }else{
                m.bonne_mine();
            }
            removeEmplacementsDrapeaux(pos);
        }
    }

    public void devoilerFauxDrapeaux(){
        for(Integer[] pos : plateau.getEmplacementsDrapeaux()){
            Case c = plateau.getCase(pos[0], pos[1]);
            if(c instanceof Terrain t){
                t.fausse_mine();
            }
        }   
    }

    public int getDrapeaux() {
        return drapeaux;
    }

    public int getCasesRestantes() {
        return cases_restantes;
    }

    public void retirerCaseRestante(){
        cases_restantes--;
    }

    public void retirerDrapeaux() {
        if (drapeaux > 0){
            this.drapeaux--;
        }
    }

    public void ajouterDrapeaux() {
        this.drapeaux++;
    }

    public String getNomSave() {
        return nomSave;
    }

    public void setNomSave(String nomSave) {
        this.nomSave = nomSave;
    }

    public int getNbrDrapeaux(){
        return drapeaux;
    }
    
    public Difficulte getDifficulte(){
        return difficulte;
    }

    public boolean getPremierClic(){
        return premier_clic;
    }

    public void premierClicEffectue(){
        premier_clic = false;
    }

    public int getSecondes() {
        return secondes;
    }

    public void setSecondes(int secondes) {
        this.secondes = secondes;
    }

    public void incrementSecondes(){
        secondes++;
    }
}
