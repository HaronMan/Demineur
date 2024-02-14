package fr.haronman.demineur.model.Plateau.Case;

import java.io.Serial;
import java.io.Serializable;

import fr.haronman.demineur.fx.CaseImageBuilder;
import javafx.scene.image.Image;

public abstract class Case implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private boolean decouvert;
    private boolean drapeau;
    private final int row, column;

    public Case(int row, int column){
        decouvert = false;
        drapeau = false;
        this.row = row;
        this.column = column;
    }

    public boolean getDecouvert(){
        return decouvert;
    }

    public void decouvrir(){
        decouvert = true;
    }

    public boolean getDrapeau(){
        return drapeau;
    }

    public void insererDrapeau(){
        if(!decouvert){
            drapeau = true;
        }
    }

    public void retirerDrapeau(){
        if(drapeau){
            drapeau = false;
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public abstract Image getImage();

    public Image onClickImage(){
        return CaseImageBuilder.ONCLICK.image;
    }
}
