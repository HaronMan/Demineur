package fr.haronman.demineur.model;

import java.util.Optional;

/**
 * Enumération permettant de créer des difficultés
 * Leurs paramètres seront utilisés pour créer une partie
 * @author HaronMan
 */
public enum Difficulte {
    // Listes des difficultés disponibles
    FACILE(0, "Facile", 10, 10, 10),
    INTERMEDIAIRE(1, "Intermédiaire", 16, 16, 40),
    DIFFICILE(2, "Difficile", 16, 31, 99),
    EXPERT(3, "Expert", 30, 50, 300),
    IMPOSSIBLE(4, "Impossible", 75, 80, 1000),
    HARDCORE(5, "Hardcore", 100, 150, 3000),
    DIABOLIQUE(6, "Diabolique", 200, 300, 12000);

    // Id de la difficulté
    private final int id;
    // Nom de la difficulté
    private final String nom;
    // Paramètres utilisables pour la création de la partie
    private final int row, column, nbrBombe;

    /**
     * Constructeur
     * @param id Id de la difficulté
     * @param nom Nom de la difficulté
     * @param row Nombre de lignes
     * @param column Nombre de colonnes
     * @param nbrBombe Nombre de bombes
     */
    private Difficulte(int id, String nom, int row, int column, int nbrBombe){
        this.id = id;
        this.nom = nom;
        this.row = row;
        this.column = column;
        this.nbrBombe = nbrBombe;
    }

    /**
     * Permet de renvoyer la difficulté correspondante à l'id (statique)
     * Ici, on renvoit un optional, au cas ou la difficulté rensigné n'existe pas
     * @param id Id de la difficulté
     * @return difficulté en question
     */
    public static Optional<Difficulte> getById(int id){
        Optional<Difficulte> difficulte = Optional.empty();
        for(Difficulte d: values()){
            if(d.getId() == id){
                difficulte = Optional.of(d);
            }
        }
        return difficulte;
    }

    /**
     * Renvoie l'id de la difficulté
     * @return id de la difficulté
     */
    public int getId() {
        return id;
    }

    /**
     * Renvoie le nom de la difficulté
     * @return nom de la difficulté
     */
    public String getNom() {
        return nom;
    }

    /**
     * Renvoie le nombre de lignes de la difficulté
     * @return nombre de lignes
     */
    public int getRow() {
        return row;
    }

    /**
     * Renvoie le nombre de colonnes de la difficulté
     * @return nombre de colonnes
     */
    public int getColumn() {
        return column;
    }

    /**
     * Renvoie le nombre de bombes
     * @return nombre de bombes
     */
    public int getNbrBombe() {
        return nbrBombe;
    }
}
