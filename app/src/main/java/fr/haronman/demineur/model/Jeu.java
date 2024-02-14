package fr.haronman.demineur.model;

import fr.haronman.demineur.Sauvegarde;
import fr.haronman.demineur.fx.JeuFX;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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

    public void save() throws IOException{
        Sauvegarde save = new Sauvegarde("TEST", getPartie());

        
        // Sauvegarde dans un fichier (resources/save)
        FileOutputStream fos = new FileOutputStream(Sauvegarde.CHEMIN_SAUVEGARDE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(save);
        oos.close();
    }

    public void load(){
        // TODO Chargement
    }


}
