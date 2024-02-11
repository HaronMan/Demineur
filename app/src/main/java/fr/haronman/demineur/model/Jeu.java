package fr.haronman.demineur.model;

import fr.haronman.demineur.fx.JeuFX;

public class Jeu {
    private static Jeu instance;
    private Partie partie;
    private JeuFX jeuFX;

    protected Jeu(){
    }

    public void start(Difficulte difficulte) throws Exception{
        partie = new Partie(difficulte);
        jeuFX.jouer();
    }

    public static Jeu getInstance() {
        if(instance == null){
            instance = new Jeu();
        }
        return instance;
    }

    public Partie getPartie() {
        return partie;
    }

    public void setJeuFX(JeuFX jeuFX) {
        this.jeuFX = jeuFX;
    }

    public void save(){
        // TODO Sauvegarde
    }

    public void load(){
        // TODO Chargement
    }


}
