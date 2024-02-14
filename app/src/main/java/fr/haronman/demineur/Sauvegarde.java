package fr.haronman.demineur;

import fr.haronman.demineur.model.Partie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

public class Sauvegarde implements Serializable{
    private static final String CHEMIN_SAUVEGARDE = "C:\\Users\\hkoch\\Documents\\demineur_saves";

    public Sauvegarde(){}

    public void save(Partie partie, String nom) throws IOException, ClassNotFoundException{
        // Sauvegarde dans un fichier (resources/save)
        String nomFichier = nom+".save";
        File fichier = new File(Sauvegarde.CHEMIN_SAUVEGARDE+"\\"+nomFichier);
        if(!fichier.exists()){
            fichier.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(fichier);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(partie);
        oos.close();
    }

    public Optional<Partie> load(String nom) throws IOException, ClassNotFoundException{
        // Chargement d'une partie dans un fichier
        FileInputStream fis = new FileInputStream(Sauvegarde.CHEMIN_SAUVEGARDE+"\\"+nom+".save");
        ObjectInputStream ois = new ObjectInputStream(fis);
        ois.close();
        return Optional.ofNullable((Partie) ois.readObject());
    }

    public static void delete(String nom){
        // TODO Supprimer une partie après victoire
    }
}