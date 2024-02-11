package fr.haronman.demineur.model.Plateau.Case;

import javafx.scene.image.Image;

public class Terrain extends Case{
    private int bombesProches;

    public Terrain(int row, int column){
        super(row, column);
        bombesProches = 0;
    }

    public int getBombesProches() {
        return bombesProches;
    }

    public void setBombesProches(int bombesProches) {
        this.bombesProches = bombesProches;
    }

    public void updateImage() {
        setImage(new Image("img/box/terrain_" + bombesProches + ".png"));
    }

    public void fausse_mine() {
        decouvrir();
        setImage(new Image("img/box/mine_incorrect.png"));
    }

    @Override
    public String toString() {
        return String.valueOf(bombesProches);
    }
}
