package fr.haronman.demineur.model.Plateau.Case;

import java.io.Serial;
import java.io.Serializable;

import fr.haronman.demineur.fx.CaseImageBuilder;
import javafx.scene.image.Image;


/**
 * Classe abstraite correspondant a une case
 * @author HaronMan
 */
public abstract class Case implements Serializable{
    // Id pour sauvegarde (Serializable)
    @Serial
    private static final long serialVersionUID = 1L;
    // True si découverte
    private boolean decouvert;
    // True si un drapeau est placé dessus
    private boolean drapeau;
    // Coordonnées x et y
    private final int row, column;

    /**
     * Constructeur
     * @param row Ligne de la matrice
     * @param column Colonne de la matrice
     */
    public Case(int row, int column){
        decouvert = false;
        drapeau = false;
        this.row = row;
        this.column = column;
    }

    /**
     ** Méthode abstraite
     * Renvoie l'image de la case
     * @return l'image de la case
     */
    public abstract Image getImage();

    /**
     * Renvoie l'état de découverte
     * @return état de découverte
     */
    public boolean getDecouvert(){
        return decouvert;
    }

    /**
     * Attribut "decouvert" à true
     * @return état de découverte
     */
    public void decouvrir(){
        decouvert = true;
    }

    public boolean getDrapeau(){
        return drapeau;
    }

    /**
     * Attribut "drapeau" à true SI non découvert
     */
    public void insererDrapeau(){
        if(!decouvert){
            drapeau = true;
        }
    }

    /**
     * Attribut "decouvert" à false SI non découvert
     */
    public void retirerDrapeau(){
        if(drapeau){
            drapeau = false;
        }
    }

    /**
     * Renvoie le numéro de la ligne de la matrice
     * @return numéro de ligne
     */
    public int getRow() {
        return row;
    }

    /**
     * Renvoie le numéro de la colonne de la matrice
     * @return numéro de colonne
     */
    public int getColumn() {
        return column;
    }

    /**
     * Renvoie l'image ONCLICK (CaseImageBuilder)
     * @return image ONCLICK
     */
    public Image onClickImage(){
        return CaseImageBuilder.ONCLICK.image;
    }
}
