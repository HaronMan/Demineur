package fr.haronman.demineur.model.Plateau.Case;

import fr.haronman.demineur.fx.CaseImageBuilder;
import javafx.scene.image.Image;

/**
 * Classe héritant de Case qui agit comme un simple terrain
 * @author HaronMan
 */
public class Terrain extends Case{
    // Nombre de bombes proches de this
    private int bombesProches;
    // Drapeau posé (mine incorrect)
    private boolean fausse_mine;

    /**
     * Constructeur
     * @param row Ligne de la matrice
     * @param column Colonne de la matrice
     */
    public Terrain(int row, int column){
        super(row, column);
        bombesProches = 0;
        fausse_mine = false;
    }

    /**
     * Renvoie le nombre de bombes proches
     * @return nombre de bombes proches
     */
    public int getBombesProches() {
        return bombesProches;
    }

    /**
     *
     * Définit le nombre de bombes proches
     * @param bombesProches nombre de bombes proches
     */
    public void setBombesProches(int bombesProches) {
        this.bombesProches = bombesProches;
    }

    /**
     * Si drapeau placé donc fausse mine
     */
    public void fausse_mine() {
        decouvrir();
        fausse_mine = true;
    }

    @Override
    public Image getImage() {
        if(!getDecouvert()){
            if(getDrapeau()){
                return CaseImageBuilder.HIDDEN_FLAG.image;
            }
            return CaseImageBuilder.HIDDEN.image;
        }
        if(fausse_mine){
            return CaseImageBuilder.FLAG_INCORRECT.image;
        }
        return new Image("img/box/terrain_" + bombesProches + ".png");
    }
}
