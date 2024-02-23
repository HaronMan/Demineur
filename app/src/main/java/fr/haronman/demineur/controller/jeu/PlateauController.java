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

/**
 * Classe qui gère les actions sur le plateau du jeu
 * @author HaronMan
 */
public class PlateauController implements EventHandler<MouseEvent>{
    // Vue du jeu
    private JeuFX jeuFX;
    // Case cliquée
    private Case c;
    // Image de la case cliquée
    private ImageView iv;
    // Définit si la souris est toujours dans la case au maintient de souris
    private boolean mouseInsideBP;

    /**
     * Constructeur
     * @param c Case cliquée
     * @param iv Image de la case cliquée
     * @param jeuFX Vue du jeu
     */
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

        // Clic souris maintenu (clic gauche)
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
        // Clic souris relaché (clic gauche)
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
                    }if(!c.getDecouvert() && !c.getDrapeau() && !jeuFX.getPause()){ // Si on souhaite la découvrir
                        // Condtions : caché et pas de drapeau
                        jeuFX.devoiler(c);
                        iv.setImage(c.getImage());
                    }
                }else{                            
                    iv.setImage(c.getImage());
                }
            }
        }
        // Si clic souris (clic droit)
        if(event.getEventType() == MouseEvent.MOUSE_CLICKED){
            if(event.getButton() == MouseButton.SECONDARY) {
                if(jeuFX.getJeu().getPartie().getPremierClic() && !jeuFX.getPause()){
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

    /**
     * Renvoie l'image de la case
     * @return l'image de la case
     */
    public ImageView getImageView() {
        return iv;
    }
}
