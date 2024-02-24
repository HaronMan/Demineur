package fr.haronman.demineur.controller.jeu;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import fr.haronman.demineur.Sauvegarde;
import fr.haronman.demineur.fx.JeuFX;
import fr.haronman.demineur.fx.NomSauvegardeFX;
import fr.haronman.demineur.fx.Visage;
import fr.haronman.demineur.model.Difficulte;
import fr.haronman.demineur.model.Partie;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

/**
 * Classe gérant les évenements de la barre de menu dans "JeuFX"
 * @author HaronMan
 */
public class MenuController implements EventHandler<ActionEvent>{
    // Vue
    private JeuFX jeuFX;

    /**
     * Constructeur
     * @param jeuFX Vue du jeu
     */
    public MenuController(JeuFX jeuFX){
        this.jeuFX = jeuFX;
    }

    
    @Override
    public void handle(ActionEvent event){
        if(event.getSource() instanceof MenuItem){
            // Récupération du MenuItem selectionné
            MenuItem choix = (MenuItem) event.getSource();
            String id = choix.getId();
            switch (id) {
                // Si le menu sélectionné est "Sauvegarde"
                case "save":
                    try {
                        sauvegarder();
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                    break;
                // Si le menu sélectionné est "Charger"
                case "load":
                    try {
                        charger();
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                    break;
                // Si le menu sélectionné est "Pause"
                case "pause":
                    if(!jeuFX.getJeu().getFin()){
                        if(!jeuFX.getPause()) {
                            pause();
                        }else{
                            reprendre();
                        }
                    }
                    break;
                // Si le menu sélectionné est "Recommencé"
                case "restart":
                    recommencer();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Gestion de la sauvegarde d'une partie
     * @throws ClassNotFoundException
     * @throws IOException
     */
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
            if(!jeuFX.getPause()){
                jeuFX.getChrono().play();
            }
        }
    }
    
    /**
     * Gestion du chargement d'une partie
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private void charger() throws ClassNotFoundException, IOException{
        jeuFX.getChrono().stop();
        FileChooser fc = new FileChooser();
        fc.setTitle("Choississez une sauvegarde");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
            "Sauvegarde",
            "*.save" //Voir uniquement les fichiers .save
            );
        fc.getExtensionFilters().add(extFilter);
        fc.setInitialDirectory(new File(Sauvegarde.CHEMIN_SAUVEGARDE));

        File sauvegarde = fc.showOpenDialog(jeuFX.getStage());
        if(sauvegarde != null){
            try{
                Optional<Partie> loadPartie = jeuFX.getJeu().load(sauvegarde);
                loadPartie.ifPresent(p -> {
                    try {
                        jeuFX.getJeu().start(p);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }catch(Exception e){
                Alert erreur = new Alert(AlertType.ERROR);
                erreur.setTitle("Chargement impossible");
                erreur.setHeaderText("Erreur : Chargement echoué");
                erreur.setContentText(
                    "Une erreur est survenue lors du chargement de la partie, " 
                    + "veuillez en sélectionner une autre"
                );
                erreur.showAndWait();
                if(!jeuFX.getPause()){
                    jeuFX.getChrono().play();
                }
            }
        }else{
            if(!jeuFX.getPause()){
                jeuFX.getChrono().play();
            }
        }
    }

    /**
     * Met le jeu en pause
     */
    private void pause(){
        if(!jeuFX.getJeu().getPartie().getPremierClic()){
            jeuFX.setPause(true);
            jeuFX.getChrono().stop();
            jeuFX.getPlateauFX().setOpacity(.2);
        }
    }

    /**
     * Reprend le jeu
     */
    private void reprendre(){
        jeuFX.setPause(false);
        jeuFX.getChrono().play();
        jeuFX.getPlateauFX().setOpacity(1);
    }

    /**
     * Recrée une partie et réinitialise le timer
     */
    private void recommencer(){
        if(!jeuFX.getJeu().getPartie().getPremierClic()){
            Difficulte courant = jeuFX.getJeu().getPartie().getDifficulte();
            try {
                jeuFX.updateVisage(Visage.IDLE);
                jeuFX.getChrono().stop();
                jeuFX.getJeu().getPartie().setMillis(0);
                jeuFX.getJeu().start(courant);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
