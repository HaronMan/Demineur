package fr.haronman.demineur.model.Plateau.Case;

import fr.haronman.demineur.fx.CaseImageBuilder;
import javafx.scene.image.Image;

public class Terrain extends Case{
    private int bombesProches;
    private boolean fausse_mine;

    public Terrain(int row, int column){
        super(row, column);
        bombesProches = 0;
        fausse_mine = false;
    }

    public int getBombesProches() {
        return bombesProches;
    }

    public void setBombesProches(int bombesProches) {
        this.bombesProches = bombesProches;
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

    public void fausse_mine() {
        decouvrir();
        fausse_mine = true;
    }
}
