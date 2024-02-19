package fr.haronman.demineur.controller.jeu;

import fr.haronman.demineur.fx.JeuFX;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    
    public void pause(){
        jeuFX.setPause(true);
        jeuFX.getChrono().stop();
        jeuFX.getPlateauFX().setOpacity(.2);
    }

    public void reprendre(){
        jeuFX.setPause(false);
        jeuFX.getChrono().play();
        jeuFX.getPlateauFX().setOpacity(1);
    }
}
