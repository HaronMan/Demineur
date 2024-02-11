package fr.haronman.demineur.fx;

import javafx.scene.image.Image;

public enum Visage {
    IDLE("img/face/idle.png"),
    ONCLICK("img/face/onclick.png"),
    WIN("img/face/win.png"),
    LOSE("img/face/lose.png");

    private final String url;

    private Visage(String url){
        this.url = url;
    }

    public Image getImage() {
        return new Image(url);
    }

    public String getUrl(){
        return url;
    }
}
