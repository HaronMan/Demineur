package fr.haronman.demineur.controller.jeu;

import java.io.IOException;

import fr.haronman.demineur.fx.JeuFX;
import fr.haronman.demineur.fx.NomSauvegardeFX;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;

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
    
    public void sauvegarder() throws ClassNotFoundException, IOException{
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

    public void pause(){
        if(!jeuFX.getJeu().getPartie().getPremierClic()){
            jeuFX.setPause(true);
            jeuFX.getChrono().stop();
            jeuFX.getPlateauFX().setOpacity(.2);
        }
    }

    public void reprendre(){
        jeuFX.setPause(false);
        jeuFX.getChrono().play();
        jeuFX.getPlateauFX().setOpacity(1);
    }
}
