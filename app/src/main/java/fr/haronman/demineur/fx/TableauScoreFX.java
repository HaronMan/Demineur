package fr.haronman.demineur.fx;

import javafx.stage.Stage;

/**
 * Classe gérant le tableau des scores
 * TODO
 * @author HaronMan
 */
public class TableauScoreFX {
    
    public static void show(){
        // TODO
        Stage stage = new Stage();
        stage.setTitle("Tableau des scores");

        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }
}
