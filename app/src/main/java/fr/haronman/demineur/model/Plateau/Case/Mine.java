package fr.haronman.demineur.model.Plateau.Case;

import java.io.Serial;

import fr.haronman.demineur.fx.CaseImageBuilder;
import javafx.scene.image.Image;

/**
 * Classe héritant de Case qui agit comme une mine
 * @author HaronMan
 */
public class Mine extends Case{
    // Id pour sauvegarde (Serializable)
    @Serial
    private static final long serialVersionUID = -8558941516743330845L;
    // Si il a été cliqué
    private boolean touche;

    /**
     * Constructeur
     * @param row Ligne de la matrice
     * @param column Colonne de la matrice
     */
    public Mine(int row, int column){
        super(row, column);
        touche = false;
    }

    /**
     * Si cliqué
     */
    public void touchee(){
        decouvrir();
        touche = true;
    }

    /**
     * Si un drapeau est placé dessus (drapeau correct)
     */
    public void bonne_mine(){
        decouvrir();
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
        if(getDrapeau()){
            return CaseImageBuilder.FLAG_CORRECT.image;
        }
        return CaseImageBuilder.MINE.image;
    }
}
