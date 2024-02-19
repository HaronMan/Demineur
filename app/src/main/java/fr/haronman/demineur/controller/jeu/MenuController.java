package fr.haronman.demineur.controller.jeu;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import fr.haronman.demineur.Sauvegarde;
import fr.haronman.demineur.fx.JeuFX;
import fr.haronman.demineur.fx.NomSauvegardeFX;
import fr.haronman.demineur.model.Jeu;
import fr.haronman.demineur.model.Partie;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

public class MenuController implements EventHandler<ActionEvent>{
    private JeuFX jeuFX;

    public MenuController(JeuFX jeuFX){
        this.jeuFX = jeuFX;
    }

    @Override
    public void handle(ActionEvent event){
        if(event.getSource() instanceof MenuItem){
            MenuItem choix = (MenuItem) event.getSource();
            String id = choix.getId();
            switch (id) {
                case "save":
                    try {
                        sauvegarder();
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "load":
                    try {
                        charger();
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "pause":
                    if(!jeuFX.getJeu().getFin()){
                        if(!jeuFX.getPause()) {
                            pause();
                        }else{
                            reprendre();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    private void sauvegarder() throws ClassNotFoundException, IOException{
        if(!jeuFX.getJeu().getPartie().getPremierClic()){
            jeuFX.getChrono().stop();
            NomSauvegardeFX sauvegardeFX = new NomSauvegardeFX();
            if(jeuFX.getJeu().getPartie().getNomSave() == null){
                sauvegardeFX.show();
                if(sauvegardeFX.getNom() != null){
                    jeuFX.getJeu().getPartie().setNomSave(sauvegardeFX.getNom().toLowerCase());
                    jeuFX.getJeu().save(jeuFX.getJeu().getPartie());

                    Alert confirmation = new Alert(AlertType.INFORMATION, 
                "La partie a bien été sauvegardée"
                    );
                    confirmation.setTitle("Sauvegarde");
                    confirmation.setHeaderText("Sauvegarde effectué");
                    confirmation.showAndWait();
                }
            }else{
                jeuFX.getJeu().save(jeuFX.getJeu().getPartie());
                Alert confirmation = new Alert(AlertType.INFORMATION, 
                "La partie a bien été sauvegardée"
                    );
                confirmation.setTitle("Sauvegarde");
                confirmation.setHeaderText("Sauvegarde effectué");
                confirmation.showAndWait();
            }
            jeuFX.getChrono().play();
        }
    }

    private void charger() throws ClassNotFoundException, IOException{
        jeuFX.getChrono().stop();
        FileChooser fc = new FileChooser();
        fc.setTitle("Choississez une sauvegarde");
        //Voir uniquement les fichiers .save
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Sauvegardes", "*.save");
        fc.getExtensionFilters().add(extFilter);
        fc.setInitialDirectory(new File(Sauvegarde.CHEMIN_SAUVEGARDE));

        File sauvegarde = fc.showOpenDialog(jeuFX.getStage());
        if(sauvegarde != null){
            Optional<Partie> loadPartie = jeuFX.getJeu().load(sauvegarde);
            loadPartie.ifPresent(p -> {
                try {
                    jeuFX.getJeu().start(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }else{
            jeuFX.getChrono().play();
        }
    }

    private void pause(){
        if(!jeuFX.getJeu().getPartie().getPremierClic()){
            jeuFX.setPause(true);
            jeuFX.getChrono().stop();
            jeuFX.getPlateauFX().setOpacity(.2);
        }
    }

    private void reprendre(){
        jeuFX.setPause(false);
        jeuFX.getChrono().play();
        jeuFX.getPlateauFX().setOpacity(1);
    }
}
