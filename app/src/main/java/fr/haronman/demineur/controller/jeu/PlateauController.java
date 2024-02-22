package fr.haronman.demineur.controller.jeu;

import fr.haronman.demineur.fx.JeuFX;
import fr.haronman.demineur.fx.Visage;
import fr.haronman.demineur.model.Plateau.Case.Case;
import fr.haronman.demineur.model.Plateau.Case.Mine;
import fr.haronman.demineur.model.Plateau.Case.Terrain;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PlateauController implements EventHandler<MouseEvent>{
    private JeuFX jeuFX;
    private Case c;
    private ImageView iv;
    private boolean mouseInsideBP;

    public PlateauController(Case c, ImageView iv, JeuFX jeuFX){
        this.c = c;
        this.jeuFX = jeuFX;
        this.iv = iv;
    }

    @Override
    public void handle(MouseEvent event) {
        // Permet de vérifier si la souris est toujours sur la case
        // Un peu comme un droit a l'erreur
        if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            mouseInsideBP = true;
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            mouseInsideBP = false;
        }

        if(event.getEventType() == MouseEvent.MOUSE_PRESSED){
            if(event.getButton() == MouseButton.PRIMARY){
                if(!c.getDecouvert() && !c.getDrapeau() && !jeuFX.getPause()){
                    try {
                        jeuFX.updateVisage(Visage.ONCLICK);                                
                    } catch (Exception e) {
                            e.printStackTrace();
                    }
                    iv.setImage(c.onClickImage());
                }
            }
        }
        if(event.getEventType() == MouseEvent.MOUSE_RELEASED){
            int x = c.getRow(), y = c.getColumn();
            if(event.getButton() == MouseButton.PRIMARY){
                try {
                    jeuFX.updateVisage(Visage.IDLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(mouseInsideBP){
                    //Lancement du minuteur
                    if(jeuFX.getJeu().getPartie().getPremierClic() && !jeuFX.getPause()){
                        jeuFX.getJeu().getPartie().premierClicEffectue();
                        jeuFX.getChrono().play();
                        if(c instanceof Mine){
                            c.decouvrir();
                            Terrain t = jeuFX.getJeu().getPartie().replacerMine(c);
                            jeuFX.getJeu().getPartie().getMatricePlateau()[x][y] = t;
                            if(!t.getDecouvert() && !t.getDrapeau()){ // Si on souhaite la découvrir                                        
                                // Condtions : caché et pas de drapeau
                                jeuFX.devoiler(t);
                                iv.setImage(t.getImage());
                            }
                        }
                    }if(!c.getDecouvert() && !c.getDrapeau()){ // Si on souhaite la découvrir
                        // Condtions : caché et pas de drapeau
                        jeuFX.devoiler(c);
                        iv.setImage(c.getImage());
                    }
                }else{                            
                    iv.setImage(c.getImage());
                }
            }
        }

        if(event.getEventType() == MouseEvent.MOUSE_CLICKED){
            if(event.getButton() == MouseButton.SECONDARY) {
                if(jeuFX.getJeu().getPartie().getPremierClic()){
                    jeuFX.getJeu().getPartie().premierClicEffectue();
                    jeuFX.getChrono().play();
                }
                // Si on souhaite manipuler les drapeaux
                if(!c.getDecouvert() && !jeuFX.getPause()){
                    if(!c.getDrapeau()){
                        // Placer un drapeau
                        if(jeuFX.getJeu().getPartie().getNbrDrapeaux() > 0){                                        
                            // Si il reste au moins un drapeau en stock
                            c.insererDrapeau();
                            jeuFX.getJeu().getPartie().retirerDrapeaux();
                            jeuFX.getJeu().getPartie().addEmplacementsDrapeaux(new Integer[]{c.getRow(), c.getColumn()});
                        }
                    }else{
                        // Retirer un drapeau
                        c.retirerDrapeau();
                        jeuFX.getJeu().getPartie().removeEmplacementsDrapeaux(new Integer[]{c.getRow(), c.getColumn()});
                        jeuFX.getJeu().getPartie().ajouterDrapeaux();
                    }
                    jeuFX.updateDrapeauxFX();
                    iv.setImage(c.getImage());
                }
            }
        }
    }

    public ImageView getImageView() {
        return iv;
    }
}
