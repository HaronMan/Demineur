package fr.haronman.demineur;

import fr.haronman.demineur.model.Partie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

public class Sauvegarde implements Serializable{
    public static final String CHEMIN_SAUVEGARDE = "C:\\Users\\hkoch\\Documents\\demineur_saves";

    public Sauvegarde(){}

    public void save(Partie partie) throws IOException, ClassNotFoundException{
        // Sauvegarde dans un fichier (resources/save)
        String nomFichier = partie.getNomSave()+".save";
        File fichier = new File(Sauvegarde.CHEMIN_SAUVEGARDE+"\\"+nomFichier);
        if(!fichier.exists()){
            fichier.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(fichier);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(partie);
        oos.close();
    }

    public Optional<Partie> load(File fichier) throws IOException, ClassNotFoundException{
        // Chargement d'une partie dans un fichier
        FileInputStream fis = new FileInputStream(fichier);
        try(ObjectInputStream ois = new ObjectInputStream(fis)){
            return Optional.ofNullable((Partie) ois.readObject());
        }
    }

    public static void delete(String nom){
        File[] fichiers = new File(CHEMIN_SAUVEGARDE).listFiles();
        if(fichiers != null || fichiers.length > 0){
            for(File f : fichiers){
                if(f.isFile() && f.getName().endsWith(".save")){
                    String nomFichier = f.getName();
                    nomFichier = nomFichier.substring(0, nomFichier.lastIndexOf("."));
                    if(nomFichier.equals(nom)){
                        f.delete();
                        break;
                    }
                }
            }
        }
    }

    public static boolean nomDejaExistant(String nom){
        File[] fichiers = new File(CHEMIN_SAUVEGARDE).listFiles();
        if(fichiers != null || fichiers.length > 0){
            for(File f : fichiers){
                if(f.isFile() && f.getName().endsWith(".save")){
                    String nomFichier = f.getName();
                    nomFichier = nomFichier.substring(0, nomFichier.lastIndexOf("."));
                    if(nomFichier.equals(nom)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}