package fr.haronman.demineur;

import fr.haronman.demineur.fx.JeuFX;
import fr.haronman.demineur.model.Difficulte;
import fr.haronman.demineur.model.Jeu;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe principale de l'application, lance le main
 * @author HaronMan
 */
public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Jeu jeu = Jeu.getInstance();
        jeu.setJeuFX(new JeuFX(primaryStage, jeu));
        jeu.start(Difficulte.INTERMEDIAIRE);
    }
    
}
