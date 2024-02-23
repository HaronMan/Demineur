package fr.haronman.demineur.model;

import fr.haronman.demineur.Sauvegarde;
import fr.haronman.demineur.fx.JeuFX;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Classe qui permet de créer un jeu qui sera créé dans le main
 * Agissant ocmme un siglelton, il est considéré comme la classe principale
 * @author HaronMan
 */
public class Jeu {
    // Instance de la classe Jeu
    private static Jeu instance;
    // Partie en cours
    private Partie partie;
    // Vue du jeu
    private JeuFX jeuFX;
    // Fin de partie
    private boolean fin;

    /**
     * Constructeur
     */
    protected Jeu(){
    }

    /**
     * Création et/ou récupération de l'instance de la classe Jeu
     * @return instance du jeu
     */
    public static Jeu getInstance() {
        if(instance == null){
            instance = new Jeu();
        }
        return instance;
    }

    /**
     * Lancement d'une partie à partir d'une difficulté donné
     * (par défaut : Intermédiaire)
     * @param difficulte difficulté souhaité
     * @throws Exception
     */
    public void start(Difficulte difficulte) throws Exception{
        partie = new Partie(difficulte);
        fin = false;
        // Lancement de la vue
        jeuFX.jouer();
    }

    /**
     * Lancement d'une partie à partir d'une autre existante
     * Voir le système de sauvegarde pour plus d'information
     * @param partie partie récupérée
     * @throws Exception
     */
    public void start(Partie partie) throws Exception{
        this.partie = partie;
        fin = false;
        // Lancement de la vue
        jeuFX.jouer();
    }

    /**
     * Renvoie la partie en cours
     * @return partie en cours
     */
    public Partie getPartie() {
        return partie;
    }

    /**
     * Définit la vue du jeu
     * @param jeuFX vue du jeu
     */
    public void setJeuFX(JeuFX jeuFX) {
        this.jeuFX = jeuFX;
    }

    /**
     * Renvoie l'état de fin du jeu
     * @return fin du jeu
     */
    public boolean getFin(){
        return fin;
    }

    /**
     * Définit l'état de fin du jeu
     * @param fin état de fin du jeu
     */
    public void setFin(boolean fin){
        this.fin = fin;
    }

    /**
     * Lancement du programme de sauvegarde
     * @param partie partie à sauvegarder
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void save(Partie partie) throws IOException, ClassNotFoundException{
        Sauvegarde.save(partie);
    }

    /**
     * Lancement du programme de chargement d'une 
     * partie à partir d'un fichier récupéré
     * @param file fichier de sauvegarde à récupérer
     * @return partie à récupérer
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Optional<Partie> load(File file) throws IOException, ClassNotFoundException{
        return Sauvegarde.load(file);
    }


}
