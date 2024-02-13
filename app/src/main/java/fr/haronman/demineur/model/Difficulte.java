package fr.haronman.demineur.model;

import java.util.Optional;

public enum Difficulte {
    FACILE(0, "Facile", 10, 10, 10),
    INTERMEDIAIRE(1, "Intermédiaire", 16, 16, 40),
    DIFFICILE(2, "Difficile", 16, 31, 99),
    EXPERT(3, "Expert", 30, 50, 300),
    IMPOSSIBLE(4, "Impossible", 75, 80, 1000),
    HARDCORE(5, "HARDCORE", 100, 150, 3000);

    private final int id;
    private final String nom;
    private final int row, column, nbrBombe;

    private Difficulte(int id, String nom, int row, int column, int nbrBombe){
        this.id = id;
        this.nom = nom;
        this.row = row;
        this.column = column;
        this.nbrBombe = nbrBombe;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getNbrBombe() {
        return nbrBombe;
    }

    public static Optional<Difficulte> getById(int id){
        Optional<Difficulte> difficulte = Optional.empty();
        for(Difficulte d: values()){
            if(d.getId() == id){
                difficulte = Optional.of(d);
            }
        }
        return difficulte;
    }
}
