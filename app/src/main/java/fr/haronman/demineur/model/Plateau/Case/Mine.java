package fr.haronman.demineur.model.Plateau.Case;

import fr.haronman.demineur.fx.CaseImageBuilder;
import javafx.scene.image.Image;

public class Mine extends Case{
    private boolean touche;
    private boolean drapeau_pose;

    public Mine(int row, int column){
        super(row, column);
        touche = false;
        drapeau_pose = false;
    }

    public void touchee(){
        decouvrir();
        touche = true;
    }

    public void bonne_mine(){
        decouvrir();
        drapeau_pose = true;
    }

    @Override
    public Image getImage() {
        if(!getDecouvert()){
            if(getDrapeau()){
                return CaseImageBuilder.HIDDEN_FLAG.image;
            }
            return CaseImageBuilder.HIDDEN.image;
        }
        if(touche){
            return CaseImageBuilder.MINE_CLICKED.image;
        }
        if(drapeau_pose){
            return CaseImageBuilder.FLAG_CORRECT.image;
        }
        return CaseImageBuilder.MINE.image;
    }
}
