package fr.haronman.demineur.fx;

import java.util.Optional;

import fr.haronman.demineur.Sauvegarde;
import fr.haronman.demineur.TableauxScores;
import fr.haronman.demineur.controller.jeu.MenuController;
import fr.haronman.demineur.controller.jeu.PlateauController;
import fr.haronman.demineur.model.Difficulte;
import fr.haronman.demineur.model.Jeu;
import fr.haronman.demineur.model.Partie;
import fr.haronman.demineur.model.Plateau.Case.Case;
import fr.haronman.demineur.model.Plateau.Case.Mine;
import fr.haronman.demineur.model.Plateau.Case.Terrain;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Classe permettant de gerer l'affichage du jeu et du plateau
 * * Plateau = Cases à cliquer pour jouer
 * @author HaronMan
 */
public class JeuFX {
    // Jeu (modele)
    private Jeu jeu;
    // Plateau de jeu
    private VBox plateauFX;
    // Fenêtre d'affichage
    private Stage stage;
    // Texte affichant le nombre de drapeaux restant
    private Label nbrDrapeauFX;
    // Visage (en haut au centre)
    private ImageView visage;
    // Texte affichant le chronomètre
    private Label timerFX;
    // Chronomètre
    private Timeline chrono;
    // Définit si le jeu est en pause ou non
    private boolean pause;

    /**
     * Constructeur
     * @param stage Fenêtre du jeu
     * @param jeu Jeu (modèle)
     */
    public JeuFX(Stage stage, Jeu jeu){
        this.jeu = jeu;
        this.stage = stage;
        this.nbrDrapeauFX = new Label();
        nbrDrapeauFX.setFont(Font.font(20));
        this.visage = new ImageView(Visage.IDLE.getImage());
        this.timerFX = new Label();
        timerFX.setFont(Font.font(20));
        pause = false;
        initTimer();
    }

    /**
     * Lancement du jeu (affichage)
     * @throws Exception
     */
    public void jouer() throws Exception{
        show(AffichagePlateau());
    }

    /**
     * Affichage de la fenêtre de jeu avec le plateau en paramètre
     * @param partie Plateau à afficher
     * @throws Exception
     */
    private void show(VBox partie) throws Exception{
        updateTimerFX();
        StackPane root = new StackPane();

        // Barre de menu
        MenuBar menuBarre = new MenuBar();

        // Première colonne (Jeu)
        Menu jeuBarre = new Menu("Jeu");

        MenuItem setPause = new MenuItem("Pause / Reprendre");
        setPause.setId("pause");
        setPause.setAccelerator(new KeyCodeCombination(KeyCode.P));
        setPause.setOnAction(new MenuController(this));

        MenuItem recommencer = new MenuItem("Recommencer");
        recommencer.setId("restart");
        recommencer.setAccelerator(new KeyCodeCombination(KeyCode.R));
        recommencer.setOnAction(new MenuController(this));

        SeparatorMenuItem ligne = new SeparatorMenuItem();

        MenuItem sauvegarder = new MenuItem("Sauvegarder");
        sauvegarder.setId("save");
        sauvegarder.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        if(jeu.getFin()){
            sauvegarder.setDisable(true);
        }
        sauvegarder.setOnAction(new MenuController(this));

        MenuItem charger = new MenuItem("Charger");
        charger.setId("load");
        charger.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
        charger.setOnAction(new MenuController(this));

        MenuItem quitter = new MenuItem("Quitter");
        quitter.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        quitter.setOnAction(action -> {
            stage.close();
        });

        // Deuxième colonne (Choix difficulté)
        Menu choix_difficulte = new Menu("Difficulté");
        ToggleGroup tg = new ToggleGroup();
        RadioMenuItem facile = new RadioMenuItem("Facile"); facile.setId("0");
        RadioMenuItem intermediaire = new RadioMenuItem("Intermediaire"); intermediaire.setId("1");
        RadioMenuItem difficile = new RadioMenuItem("Difficile"); difficile.setId("2");
        RadioMenuItem expert = new RadioMenuItem("Expert"); expert.setId("3");
        RadioMenuItem impossible = new RadioMenuItem("Impossible"); impossible.setId("4");
        RadioMenuItem hardcore = new RadioMenuItem("Hardcore"); hardcore.setId("5");
        RadioMenuItem diabolique = new RadioMenuItem("Diabolique"); diabolique.setId("6");
        facile.setToggleGroup(tg);
        intermediaire.setToggleGroup(tg);
        difficile.setToggleGroup(tg);
        expert.setToggleGroup(tg);
        impossible.setToggleGroup(tg);
        hardcore.setToggleGroup(tg);
        diabolique.setToggleGroup(tg);
        tg.getToggles().get(jeu.getPartie().getDifficulte().getId()).setSelected(true);

        tg.selectedToggleProperty().addListener(
            (obs, oldT, newT) -> {
                if(newT != null){
                    RadioMenuItem rmi = (RadioMenuItem) newT;
                    Optional<Difficulte> d = Difficulte.getById(Integer.valueOf(rmi.getId()));
                    if(d.isPresent()){
                        try {
                            updateVisage(Visage.IDLE);
                            chrono.stop();
                            jeu.start(d.get());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        );

        // TODO Troisième colonne
        Menu plus = new Menu("Plus");

        MenuItem tableauScore = new MenuItem("Tableau des scores");
        tableauScore.setId("tabScore");
        tableauScore.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN));
        tableauScore.setOnAction(action -> {
            TableauScoreFX.show();
        });

        MenuItem cmds = new MenuItem("Commandes");
        cmds.setId("cmds");
        cmds.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCodeCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN));
        
        jeuBarre.getItems().addAll(setPause, recommencer, ligne, sauvegarder, charger, quitter);
        choix_difficulte.getItems().addAll(facile, intermediaire, difficile, expert, impossible, hardcore, diabolique);
        plus.getItems().addAll(cmds, tableauScore);

        menuBarre.getMenus().addAll(jeuBarre, choix_difficulte, plus);

        root.getChildren().add(menuBarre);
        StackPane.setAlignment(menuBarre, Pos.TOP_CENTER);

        VBox affichageJeu = new VBox();

        root.setBackground(
            new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY))
        );
        StackPane.setMargin(affichageJeu, new Insets(10));

        // Compteur de drapeaux
        HBox drapeaux = new HBox();
        updateDrapeauxFX();

        drapeaux.getChildren().addAll(
            new ImageView(new Image("img/flag.png")),
            nbrDrapeauFX
        );
        
        // Visage
        HBox img = new HBox();
        visage.setOnMouseClicked(event -> {
            if(!jeu.getPartie().getPremierClic()){
                Difficulte courant = jeu.getPartie().getDifficulte();
                try {
                    updateVisage(Visage.IDLE);
                    chrono.stop();
                    jeu.getPartie().setMillis(0);
                    jeu.start(courant);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        img.getChildren().add(visage);

        HBox infos = new HBox();
        infos.setPadding(new Insets(20, 0, 10, 0));
        infos.setPrefWidth(HBox.USE_COMPUTED_SIZE);

        HBox.setHgrow(drapeaux, Priority.ALWAYS);
        HBox.setHgrow(img, Priority.ALWAYS);
        HBox.setHgrow(timerFX, Priority.ALWAYS);

        infos.getChildren().addAll(
            drapeaux,
            img,
            timerFX
        );
        infos.setAlignment(Pos.CENTER);
        
        StackPane.setMargin(infos, new Insets(10));

        plateauFX = partie;
        BorderPane bp = new BorderPane(plateauFX);
        BorderStroke bs = new BorderStroke(
            Color.BLACK, 
            BorderStrokeStyle.SOLID, 
            CornerRadii.EMPTY, 
            new BorderWidths(2)
        );
        bp.setBorder(new Border(bs));
        affichageJeu.getChildren().addAll(infos, bp);

        root.getChildren().add(affichageJeu);

        if(jeu.getPartie().getDifficulte().getId() >= 4){
            ScrollPane sp = new ScrollPane(root);
            sp.setFitToWidth(true);
            sp.setFitToHeight(true);
            stage.setScene(new Scene(sp, 1200, 925));
        }else{
            stage.setScene(new Scene(root));
        }

        stage.setTitle("Démineur ["+jeu.getPartie().getDifficulte().getNom()+"]");
        stage.getIcons().add(new Image("img/icon.png"));
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Affichage du plateau du jeu
     * @return VBox stockant le plateau du jeu
     */
    public VBox AffichagePlateau(){
        VBox lignes = new VBox();

        Case[][] mat = jeu.getPartie().getMatricePlateau();

        for(int row = 0; row < mat.length; row++){
            HBox col = new HBox();
            for(int column = 0; column < mat[row].length; column++){
                Case c = jeu.getPartie().getCaseMatrice(row, column);

                ImageView iv = new ImageView(c.getImage());
                if(jeu.getPartie().getDifficulte().getId() == 3){
                    iv.setFitWidth(20);
                    iv.setFitHeight(20);
                } else if(jeu.getPartie().getDifficulte().getId() >= 4){
                    iv.setFitWidth(15);
                    iv.setFitHeight(15);
                }

                // Création d'une bordure gros pour bien distinguer les cases
                BorderPane bp = new BorderPane(iv);
                BorderStroke bs = new BorderStroke(
                    Color.GREY, 
                    BorderStrokeStyle.SOLID, 
                    CornerRadii.EMPTY, 
                    new BorderWidths(1)
                );

                if(!jeu.getFin()){
                    // Gestion du controller si jeu non fini
                    PlateauController pc = new PlateauController(c, iv, this);
                    bp.setOnMouseEntered(pc);
                    bp.setOnMouseExited(pc);
                    bp.setOnMousePressed(pc);
                    bp.setOnMouseReleased(pc);
                    bp.setOnMouseClicked(pc);
                }

                bp.setBorder(new Border(bs));
                col.getChildren().add(bp);
            }
            lignes.getChildren().add(col);
        }

        return lignes;
    }

    /**
     * Dévoile la case tout en gérant la victoire et la défaite
     * @param c Case à dévoiler
     */
    public void devoiler(Case c){
        Case matCase = jeu.getPartie().getCaseMatrice(c.getRow(), c.getColumn());
        if(matCase instanceof Mine m){
            // Si cliqué sur une mine
            m.touchee();
            try {
                // Gestion de la défaite
                jeu.setFin(true);
                updateVisage(Visage.LOSE);
                defaite();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(matCase instanceof Terrain t){
            jeu.getPartie().getPlateau().calculerBombesProches(t);
            t.decouvrir();
            jeu.getPartie().retirerCaseRestante();
            
            if(jeu.getPartie().getCasesRestantes() == 0){
                try {
                    // Gestion de la victoire
                    jeu.setFin(true);
                    updateVisage(Visage.WIN);
                    victoire();
                    // Pop-Up de victoire
                    Alert victoire = new Alert(AlertType.INFORMATION, 
                        "Vous avez gagné une partie "+jeu.getPartie().getDifficulte().getNom()+" en "+timerFX.getText()
                    );
                    victoire.setTitle("Victoire");
                    victoire.setHeaderText("Vous avez gagné !!!");
                    victoire.showAndWait();

                    if(jeu.getPartie().getNomSave() == null){
                        NomSauvegardeFX sauvegardeFX = new NomSauvegardeFX();
                        sauvegardeFX.show();
                        if(sauvegardeFX.getNom() != null){
                            jeu.getPartie().setNomSave(sauvegardeFX.getNom());
                        }
                    }
                    TableauxScores.addToTableau(jeu.getPartie());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if(t.getBombesProches() == 0){
                // Si la case n'a aucune bombe a proximité
                decouvrirZoneSure(t);
            }
        }
    }

    /**
     * Fonction qui révèlera toutes les cases aux alentours d'un terrain donné
     * Appel récursif si un terrain voisin ne possède également pas de bombe a proximité
     * @param t Terrain en question
     */
    public void decouvrirZoneSure(Terrain t){
        Case[][] mat = jeu.getPartie().getMatricePlateau();

        int row = t.getRow(), column = t.getColumn();
        for(int i = row - 1; i <= row + 1; i++){
            if(i >= 0 && i < mat.length){
                for(int j = column - 1; j <= column + 1; j++){
                    if(j >= 0 && j < mat[i].length){
                        if(!(mat[i][j] instanceof Mine)){
                            Terrain newTerrain = (Terrain) mat[i][j];
                            
                            if(!newTerrain.getDecouvert() && !newTerrain.getDrapeau()){
                                jeu.getPartie().getPlateau().calculerBombesProches(newTerrain);
                                newTerrain.decouvrir();
                                
                                HBox hb = (HBox) plateauFX.getChildren().get(i);
                                BorderPane bp = (BorderPane) hb.getChildren().get(j);
                                ImageView iv = (ImageView) bp.getChildren().get(0);
                                iv.setImage(newTerrain.getImage());

                                if(newTerrain.getBombesProches() == 0){
                                    decouvrirZoneSure(newTerrain);
                                }
                                jeu.getPartie().retirerCaseRestante();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Gestion de la défaite
     * @throws Exception
     */
    public void defaite() throws Exception{
        chrono.stop();
        jeu.getPartie().devoilerMines();
        jeu.getPartie().devoilerFauxDrapeaux();
        show(AffichagePlateau());
    }

    /**
     * Gestion de la victoire
     * @throws Exception
     */
    public void victoire() throws Exception{
        chrono.stop();
        if(jeu.getPartie().getNomSave() != null){
            // Suppression de la sauvegarde si existante
            Sauvegarde.delete(jeu.getPartie().getNomSave());
        }
        show(AffichagePlateau());
    }

    /**
     * Met à jour l'affichage du drapeau
     */
    public void updateDrapeauxFX(){
        int nbr = jeu.getPartie().getNbrDrapeaux();
        nbrDrapeauFX.setText(" : "+String.valueOf(nbr));
        // Pour afficher le texte en rouge, il faut que le nombre de drapeaux
        // restant inférieur à 30% du nombre de drapeau (nombre de bombe) initiale
        if(nbr <= Math.round(jeu.getPartie().getDifficulte().getNbrBombe() * 30 / 100)){
            nbrDrapeauFX.setTextFill(Color.rgb(180, 0, 0));
        }else{
            nbrDrapeauFX.setTextFill(Color.BLACK);
        }
    }

    /**
     * Met à jour le visage en le remplacant par un autre donné
     * @param v Visage à placer
     * @throws Exception
     */
    public void updateVisage(Visage v) throws Exception{
        this.visage.setImage(v.getImage());
    }

    /**
     * Met à jour l'affichage du timer
     * @throws Exception
     */
    public void updateTimerFX() throws Exception{
        String text = Partie.convertTime(jeu.getPartie());
        timerFX.setText(text);
    }

    /**
     * Initialise le timer
     */
    public void initTimer(){
        chrono = new Timeline();
        chrono.getKeyFrames().add(new KeyFrame(Duration.millis(1), event -> {
            jeu.getPartie().incrementMillis();
            try {
                updateTimerFX();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        chrono.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Renvoie le jeu (modèle)
     * @return le jeu
     */
    public Jeu getJeu() {
        return jeu;
    }

    /**
     * Renvoie le chrono
     * @return le chrono
     */
    public Timeline getChrono() {
        return chrono;
    }

    /**
     * Renvoie le label du compteur de drapeau
     * @return le label
     */
    public Label getNbrDrapeauFX() {
        return nbrDrapeauFX;
    }

    /**
     * Renvoie le plateau
     * @return le plateau
     */
    public VBox getPlateauFX() {
        return plateauFX;
    }

    /**
     * Renvoie la stage
     * @return la stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Renvoie le label du timer
     * @return le label
     */
    public Label getTimerFX() {
        return timerFX;
    }

    /**
     * Renvoie le visage actuelle
     * @return l'imageView
     */
    public ImageView getVisage() {
        return visage;
    }

    /**
     * Renvoie si le jeu est en pause ou non
     * @return le booléen pause
     */
    public boolean getPause(){
        return pause;
    }

    /**
     * Définit la pause par un autre booléen
     * @param pause L'état de la pause
     */
    public void setPause(boolean pause) {
        this.pause = pause;
    }
}