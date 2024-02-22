package fr.haronman.demineur.fx;

import javafx.scene.image.Image;

/**
 * Enumération permettant de stocker l'url des images du visage.
 * L'URL sera gardé en paramètre
 * @author HaronMan
 */
public enum Visage {
    IDLE("img/face/idle.png"),
    ONCLICK("img/face/onclick.png"),
    WIN("img/face/win.png"),
    LOSE("img/face/lose.png");

    //URL de l'image
    private final String url;

    /**
     * Constructeur
     * @param url URL de l'image
     */
    private Visage(String url){
        this.url = url;
    }

    /**
     * Renvoie l'image generé à partir de l'URL en paramètre
     * @return l'image en question
     */
    public Image getImage() {
        return new Image(url);
    }

    /**
     * Renvoie l'URL en paramètre
     * @return l'URL en paramètre
     */
    public String getUrl(){
        return url;
    }
}
