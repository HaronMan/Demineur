package fr.haronman.demineur.model;

import fr.haronman.demineur.Sauvegarde;
import fr.haronman.demineur.fx.JeuFX;

import java.io.IOException;
import java.util.Optional;

public class Jeu {
    private static Jeu instance;
    private Partie partie;
    private JeuFX jeuFX;
    private boolean fin;

    protected Jeu(){
    }

    public void start(Difficulte difficulte) throws Exception{
        partie = new Partie(difficulte);
        fin = false;
        jeuFX.jouer();
    }

    public void start(Partie partie) throws Exception{
        this.partie = partie;
        fin = false;
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

    public boolean getFin(){
        return fin;
    }

    public void setFin(boolean fin){
        this.fin = fin;
    }

    public void save(Partie partie, String nom) throws IOException, ClassNotFoundException{
        new Sauvegarde().save(partie, nom);
    }

    public Optional<Partie> load(String nom) throws IOException, ClassNotFoundException{
        return new Sauvegarde().load(nom);

    }


}
