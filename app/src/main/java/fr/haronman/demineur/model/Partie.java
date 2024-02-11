package fr.haronman.demineur.model;

import fr.haronman.demineur.model.Plateau.Plateau;
import fr.haronman.demineur.model.Plateau.Case.Case;
import fr.haronman.demineur.model.Plateau.Case.Mine;
import fr.haronman.demineur.model.Plateau.Case.Terrain;

public class Partie {
    private Plateau plateau;
    private int drapeaux;
    private int cases_restantes;
    private boolean premier_clic;
    private final Difficulte difficulte;
    
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
        plateau.getEmplacementsDrapeaux().remove(pos);
    }

    public void devoilerMines(){
        for(Integer[] pos : plateau.getEmplacementsMines()){
            Mine m = (Mine) plateau.getCase(pos[0], pos[1]);
            if(!m.getDrapeau()){
                plateau.getCase(pos[0], pos[1]).decouvrir();
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
}
