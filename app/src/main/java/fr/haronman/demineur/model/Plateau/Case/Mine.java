package fr.haronman.demineur.model.Plateau.Case;

import javafx.scene.image.Image;

public class Mine extends Case{

    public Mine(int row, int column){
        super(row, column);
        setImage(new Image("img/box/mine.png"));
    }

    public void touchee(){
        decouvrir();
        setImage(new Image("img/box/mine_clicked.png"));
    }

    public void bonne_mine(){
        setImage(new Image("img/flag/flag_correct.png"));
    }

    @Override
    public String toString() {
        return "M";
    }
}
