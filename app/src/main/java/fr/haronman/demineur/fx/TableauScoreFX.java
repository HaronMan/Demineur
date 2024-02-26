package fr.haronman.demineur.fx;

import fr.haronman.demineur.model.Difficulte;
import fr.haronman.demineur.model.Partie;
import fr.haronman.demineur.model.TableauxScores;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

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
        diabolique.setId("6"); diabolique.setClosable(false);

        TabPane tp = new TabPane(
            facile, intermediaire, difficile, expert, impossible, hardcore, diabolique
        );

        for(Tab t : tp.getTabs()){
            VBox scores;
            try {
                scores = scoreTab(t);
                t.setContent(scores);
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        }

        Scene scene = new Scene(tp);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

    private static VBox scoreTab(Tab t) throws NumberFormatException, IOException{
        HashMap<Integer, Partie> tableau = TableauxScores.getTableau(
            Difficulte.getById(Integer.valueOf(t.getId())).get()
        );
        VBox scores = new VBox(5);
        tableau.forEach((key, val) -> {
            String text = key+". ";
            if(val != null){
                text += val.getNomSave()+" ("+Partie.convertTime(val)+")";
            }
            scores.getChildren().add(new Text(text));
        });
        scores.setPadding(new Insets(10));
        return scores;
    }
}
