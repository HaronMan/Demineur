package fr.haronman.demineur;

import fr.haronman.demineur.model.Partie;

import java.io.Serializable;

public class Sauvegarde implements Serializable{
    public static final String CHEMIN_SAUVEGARDE = System.getProperty("user.dir")+"/src/main/resources/saves/test.save";
    private final String nom;
    private Partie partie;

    public Sauvegarde(String nom, Partie partie){
        this.nom = nom;
        this.partie = partie;
    }

    public String getNom() {
        return nom;
    }

    public Partie getPartie() {
        return partie;
    }
}