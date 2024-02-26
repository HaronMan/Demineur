package fr.haronman.demineur.fx;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Classe gérant le tableau des scores
 * TODO
 * @author HaronMan
 */
public class TableauScoreFX {
    
    public static void show(){
        Stage stage = new Stage();
        stage.setTitle("Tableau des scores");

        Tab facile = new Tab("Facile");
        facile.setId("0"); facile.setClosable(false);
        Tab intermediaire = new Tab("Intermédiaire");
        intermediaire.setId("1"); intermediaire.setClosable(false);
        Tab difficile = new Tab("Difficile");
        difficile.setId("2"); difficile.setClosable(false);
        Tab expert = new Tab("Expert");
        expert.setId("3"); expert.setClosable(false);
        Tab impossible = new Tab("Impossible");
        impossible.setId("4"); impossible.setClosable(false);
        Tab hardcore = new Tab("Hardcore");
        hardcore.setId("5"); hardcore.setClosable(false);
        Tab diabolique = new Tab("Diabolique");
        diabolique.setId("5"); diabolique.setClosable(false);

        TabPane tp = new TabPane(
            facile, intermediaire, difficile, expert, impossible, hardcore, diabolique
        );

        Scene scene = new Scene(tp);
        
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }
}
