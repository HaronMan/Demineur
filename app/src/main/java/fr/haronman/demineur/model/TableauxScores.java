package fr.haronman.demineur.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Classe permettant de manipuler le fichier de tableau des scores
 * Si non existant, il sera automatiquement créé au lancement du jeu
 * @author HaronMan
 */
public class TableauxScores {
    private static final String CHEMIN_TABLEAU = "C:\\Users\\hkoch\\Documents\\demineur\\scoreboard";

    /**
     * Récupère le tableau depuis le fichier concernant une difficulté donnée.
     * Si non lisible, réinitialise le ficher en question
     * @param difficulte difficulté souhaité
     * @return le tableau des scores sous forme de HashMap
     * @throws IOException
     */
    public static HashMap<Integer, Partie> getTableau(Difficulte difficulte) throws IOException{
        HashMap<Integer, Partie> tab = new HashMap<Integer, Partie>();
        if(!creerFichierTableau(difficulte)){
            File tabFile = new File(CHEMIN_TABLEAU+"\\"+difficulte.getNom().toLowerCase()+".sb");
            FileInputStream fis = new FileInputStream(tabFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                tab = (HashMap<Integer, Partie>) ois.readObject();
            }catch (Exception e){
                recreerFichierTableau(difficulte);
            }finally{
                ois.close();
            }
        }
        return tab;
    }

    /**
     * Ajoute le nouveau score au tableau si dans les 10 premiers
     * Si pas encore de score sur ce tableau, ajoute automatiquement
     * à la première place
     * @param p
     * @throws IOException 
     */
    public static void addToTableau(Partie p) throws IOException{
        HashMap<Integer, Partie> tab = getTableau(p.getDifficulte());
        boolean placee = false;
        for(Partie val : tab.values()){
            if(val == null){
                val = p;
                placee = true;
            }
        }

        if(!placee){
            // TODO
        }
    }

    /**
     * Crée le fichier du tableau des scores à la 
     * difficulté souhaité si non existant.
     * Appelé à l'ajout d'un score
     * @param difficulte difficulte souhaité
     * @return true si fichier créé, false sinon
     * @throws IOException 
     */
    private static boolean creerFichierTableau(Difficulte difficulte) throws IOException{
        File tableau = new File(CHEMIN_TABLEAU+"\\"+difficulte.getNom().toLowerCase()+".sb");
        if(!tableau.exists()){
            HashMap<Integer, Partie> tab = new HashMap<Integer, Partie>();
            for(int i = 1; i <= 10; i++){
                tab.put(i, null);
            }
            // Si le fichier de sauvegarde n'existe pas
            // Creation du fichier
            FileOutputStream fos = new FileOutputStream(tableau);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tab);
            oos.close();
            return true;
        }
        return false;
    }

    /**
     * Crée le dossier du tableau des scores si non existant
     */
    public static void creerDossier(){
        File dossier = new File(CHEMIN_TABLEAU);
        if(!dossier.exists()){
            // Si le dossier de sauvegarde n'existe pas
            dossier.mkdirs();
        }
    }

    /**
     * Supprime le fichier de tableau des scores dans la difficulté souhaité.
     * Cette fonction est uniquement appelé si un fichier de tableau de score est
     * modifié d'une quelconque manière autre que ce programme
     * @param difficulte difficulté en question
     * @throws IOException
     */
    private static void recreerFichierTableau(Difficulte difficulte) throws IOException{
        //Supression d'un fichier à partir de son nom
        File fichier = new File(CHEMIN_TABLEAU+"\\"+difficulte.getNom().toLowerCase()+".sb");
        fichier.delete();
        creerFichierTableau(difficulte);
    }


}
