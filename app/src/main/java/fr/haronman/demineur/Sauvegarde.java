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
    public static final String CHEMIN_SAUVEGARDE = "C:\\Users\\hkoch\\Documents\\demineur\\saves";

    public Sauvegarde(){}

    public void save(Partie partie) throws IOException, ClassNotFoundException{
        // Sauvegarde dans un fichier (resources/save)
        File dossier = new File(CHEMIN_SAUVEGARDE);
        if(!dossier.exists()){dossier.mkdirs();}

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
        File dossier = new File(CHEMIN_SAUVEGARDE);
        if(!dossier.exists()){
            dossier.mkdirs();
            return Optional.empty();
        }else{
            FileInputStream fis = new FileInputStream(fichier);
            try(ObjectInputStream ois = new ObjectInputStream(fis)){
                return Optional.ofNullable((Partie) ois.readObject());
            }
        }
    }

    public static void delete(String nom) throws IOException{
        //Supression d'un fichier à partir de son nom
        File dossier = new File(CHEMIN_SAUVEGARDE);
        if(!dossier.exists()){
            dossier.mkdirs();
        }else{
            File[] fichiers = new File(CHEMIN_SAUVEGARDE).listFiles();
            if(fichiers != null && fichiers.length > 0){
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
    }

    public static boolean nomDejaExistant(String nom) throws IOException{
        //Vérificationd e l'existence d'un fichier à partir d'un nom
        File dossier = new File(CHEMIN_SAUVEGARDE);
        if(!dossier.exists()){
            dossier.mkdirs();
            return false;
        }else{
            File[] fichiers = new File(CHEMIN_SAUVEGARDE).listFiles();
            if(fichiers != null && fichiers.length > 0){
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
}