package fr.haronman.demineur;

import fr.haronman.demineur.model.Partie;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

public class Sauvegarde implements Serializable{
    private static final String CHEMIN_SAUVEGARDE = System.getProperty("user.dir")+"/src/main/resources/saves/test.save";

    public Sauvegarde(){}

    public void save(Partie partie, String nom) throws IOException, ClassNotFoundException{
        // Sauvegarde dans un fichier (resources/save)
        FileOutputStream fos = new FileOutputStream(Sauvegarde.CHEMIN_SAUVEGARDE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(partie);
        oos.close();
    }

    public Optional<Partie> load(String nom) throws IOException, ClassNotFoundException{
        // Chargement d'une partie dans un fichier
        FileInputStream fis = new FileInputStream(Sauvegarde.CHEMIN_SAUVEGARDE);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return Optional.ofNullable((Partie) ois.readObject());
    }

    public static void delete(String nom){
        // TODO Supprimer une partie après victoire
    }
}