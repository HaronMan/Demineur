package fr.haronman.demineur.fx;

import javafx.scene.image.Image;

/**
 * Enumération permettant de stocker les images constantes du jeu.
 * L'URL indiqué dans le constructeur sera utilisé pour créer une image et
 * ce dernier sera gardé en paramètre
 * @author HaronMan
 */
public enum CaseImageBuilder {
    FLAG_CORRECT("img/flag/flag_correct.png"),
    FLAG_INCORRECT("img/flag/flag_incorrect.png"),
    HIDDEN("img/box/hidden.png"),
    HIDDEN_FLAG("img/flag/hidden_flag.png"),
    MINE("img/box/mine.png"),
    MINE_CLICKED("img/box/mine_clicked.png"),
    ONCLICK("img/box/terrain_0.png");

    // Image de case
    public final Image image;

    /**
     * Constructeur
     * @param url URL de l'image (package resources)
     */
    private CaseImageBuilder(String url){
        image = new Image(url);
    }
}
