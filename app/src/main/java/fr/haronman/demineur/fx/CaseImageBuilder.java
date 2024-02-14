package fr.haronman.demineur.fx;

import javafx.scene.image.Image;

public enum CaseImageBuilder {
    FLAG_CORRECT("img/flag/flag_correct.png"),
    FLAG_INCORRECT("img/flag/flag_incorrect.png"),
    HIDDEN("img/box/hidden.png"),
    HIDDEN_FLAG("img/flag/hidden_flag.png"),
    MINE("img/box/mine.png"),
    MINE_CLICKED("img/box/mine_clicked.png"),
    ONCLICK("img/box/terrain_0.png");

    public final Image image;

    private CaseImageBuilder(String url){
        image = new Image(url);
    }
}
