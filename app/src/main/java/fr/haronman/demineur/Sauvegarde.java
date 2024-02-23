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

/**
 * Classe contenant les méthodes statiques qui gèrent tout le système de sauvegarde du jeu
 *? Lire le Readme pour plus d'informations sur le système de sauvegarde
 * @author HaronMan
 */
public class Sauvegarde implements Serializable{
    // Chemin du dossier de sauvegardes 
    public static final String CHEMIN_SAUVEGARDE = "C:\\Users\\hkoch\\Documents\\demineur\\saves";

    /**
     * Effectue une sauvegarde de la partie donnée
     * @param partie partie donnée
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void save(Partie partie) throws IOException, ClassNotFoundException{
        // Sauvegarde dans un fichier (resources/save)
        File dossier = new File(CHEMIN_SAUVEGARDE);

        if(!dossier.exists()){dossier.mkdirs();}
        // Si le dossier de sauvegarde n'existe pas

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

    /**
     * Effectue un chargement d'une sauvegarde à partir d'un fichier
     * @param fichier fichier à récupérer
     * @return Partie contenu dans la sauvegarde
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Optional<Partie> load(File fichier) throws IOException, ClassNotFoundException{
        // Chargement d'une partie dans un fichier
        File dossier = new File(CHEMIN_SAUVEGARDE);
        if(!dossier.exists()){
            // Si le dossier de sauvegarde n'existe pas
            dossier.mkdirs();
            return Optional.empty();
        }else{
            FileInputStream fis = new FileInputStream(fichier);
            try(ObjectInputStream ois = new ObjectInputStream(fis)){
                return Optional.ofNullable((Partie) ois.readObject());
            }
        }
    }

    /**
     * Supprime un fichier de sauvegarde
     * @param nom nom du fichier
     * @throws IOException
     */
    public static void delete(String nom) throws IOException{
        //Supression d'un fichier à partir de son nom
        File dossier = new File(CHEMIN_SAUVEGARDE);
        if(!dossier.exists()){
            dossier.mkdirs();
        }else{
            File[] fichiers = new File(CHEMIN_SAUVEGARDE).listFiles();
            // Récupère tous les fichiers de sauvegarde et les stocke dans un tableau
            if(fichiers != null && fichiers.length > 0){
                for(File f : fichiers){
                    if(f.isFile() && f.getName().endsWith(".save")){
                        // Si le fichier possède l'extension .save
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

    /**
     * Vérifie si un nom de sauvegarde existe déjà,
     * respecte la contrainte unique de la sauvegarde
     * @param nom
     * @return
     * @throws IOException
     */
    public static boolean nomDejaExistant(String nom) throws IOException{
        //Vérification de l'existence d'un fichier à partir d'un nom
        File dossier = new File(CHEMIN_SAUVEGARDE);
        if(!dossier.exists()){
            dossier.mkdirs();
            return false;
        }else{
            File[] fichiers = new File(CHEMIN_SAUVEGARDE).listFiles();
            // Récupère tous les fichiers de sauvegarde et les stocke dans un tableau
            if(fichiers != null && fichiers.length > 0){
                for(File f : fichiers){
                    if(f.isFile() && f.getName().endsWith(".save")){
                        // Si le fichier possède l'extension .save
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