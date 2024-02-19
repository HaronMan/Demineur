package fr.haronman.demineur.fx;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import fr.haronman.demineur.Sauvegarde;
import fr.haronman.demineur.controller.jeu.MenuController;
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
import javafx.scene.input.MouseButton;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JeuFX {
    private Jeu jeu;
    private MenuBar menuBarre;
    private VBox plateauFX;
    private Stage stage;
    private Label nbrDrapeauFX;
    private ImageView visage;
    private Label timerFX;
    private Timeline chrono;
    private boolean mouseInsideBP;
    private boolean pause;

    public JeuFX(Stage stage, Jeu jeu){
        this.jeu = jeu;
        this.stage = stage;
        this.menuBarre = new MenuBar();
        this.nbrDrapeauFX = new Label();
        nbrDrapeauFX.setFont(Font.font(20));
        this.visage = new ImageView(Visage.IDLE.getImage());
        this.timerFX = new Label();
        timerFX.setFont(Font.font(20));
        pause = false;
        initTimer();
    }

    public void jouer() throws Exception{
        show(AffichagePlateau());
    }

    public void show(VBox partie) throws Exception{

        updateTimerFX();
        StackPane root = new StackPane();

        createMenuBarre();

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

    public VBox AffichagePlateau(){
        VBox lignes = new VBox();

        Case[][] mat = jeu.getPartie().getMatricePlateau();

        for(int row = 0; row < mat.length; row++){
            HBox col = new HBox();
            for(int column = 0; column < mat[row].length; column++){
                int x = row, y = column;
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

                // Permet de vérifier si la souris est toujours sur la case
                // Un peu comme un droit a l'erreur
                bp.setOnMouseEntered(event -> mouseInsideBP = true);
                bp.setOnMouseExited(event -> mouseInsideBP = false);

                // Clic sur une case
                bp.setOnMousePressed(event -> {
                    if(event.getButton() == MouseButton.PRIMARY){
                        if(!c.getDecouvert() && !c.getDrapeau() && !pause){
                            try {
                                updateVisage(Visage.ONCLICK);                                } catch (Exception e) {
                                    e.printStackTrace();
                            }
                            iv.setImage(c.onClickImage());
                        }
                    }
                });
                bp.setOnMouseReleased(event -> {
                    if(event.getButton() == MouseButton.PRIMARY){
                        try {
                            updateVisage(Visage.IDLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(mouseInsideBP){
                            //Lancement du minuteur
                            if(jeu.getPartie().getPremierClic() && !pause){
                                jeu.getPartie().premierClicEffectue();
                                chrono.play();
                                if(c instanceof Mine){
                                    c.decouvrir();
                                    Terrain t = jeu.getPartie().replacerMine(c);
                                    jeu.getPartie().getMatricePlateau()[x][y] = t;
                                    if(!t.getDecouvert() && !t.getDrapeau()){ // Si on souhaite la découvrir
                                        // Condtions : caché et pas de drapeau
                                    devoiler(t);
                                    iv.setImage(t.getImage());
                                    }
                                }
                            }if(!c.getDecouvert() && !c.getDrapeau() && !pause){ // Si on souhaite la découvrir
                                // Condtions : caché et pas de drapeau
                                devoiler(c);
                                iv.setImage(c.getImage());
                            }
                        }else{
                            iv.setImage(c.getImage());
                        }
                    }
                });
                bp.setOnMouseClicked(event -> {
                    if(jeu.getPartie().getPremierClic() && !pause){
                        jeu.getPartie().premierClicEffectue();
                        chrono.play();
                    }
                    if(event.getButton() == MouseButton.SECONDARY) {
                        // Si on souhaite manipuler les drapeaux
                        if(!c.getDecouvert() && !pause){
                            if(!c.getDrapeau()){
                                // Placer un drapeau
                                if(jeu.getPartie().getNbrDrapeaux() > 0){                                        
                                    // Si il reste au moins un drapeau en stock
                                    c.insererDrapeau();
                                    jeu.getPartie().retirerDrapeaux();
                                    jeu.getPartie().addEmplacementsDrapeaux(new Integer[]{c.getRow(), c.getColumn()});
                                }
                            }else{
                                // Retirer un drapeau
                                c.retirerDrapeau();
                                jeu.getPartie().removeEmplacementsDrapeaux(new Integer[]{c.getRow(), c.getColumn()});
                                jeu.getPartie().ajouterDrapeaux();
                            }
                            updateDrapeauxFX();
                            iv.setImage(c.getImage());
                        }
                    }
                });
                bp.setBorder(new Border(bs));
                col.getChildren().add(bp);
            }
            lignes.getChildren().add(col);
        }

        return lignes;
    }

    public void devoiler(Case c){
        Case matCase = jeu.getPartie().getCaseMatrice(c.getRow(), c.getColumn());
        if(matCase instanceof Mine m){
            // Si cliqué sur une mine
            m.touchee();
            try {
                jeu.setFin(true);
                updateVisage(Visage.LOSE);
                defaite(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(matCase instanceof Terrain t){
            jeu.getPartie().getPlateau().calculerBombesProches(t);
            t.decouvrir();
            jeu.getPartie().retirerCaseRestante();
            
            if(jeu.getPartie().getCasesRestantes() == 0){
                try {
                    jeu.setFin(true);
                    updateVisage(Visage.WIN);
                    victoire();
                    Alert victoire = new Alert(AlertType.INFORMATION, 
                        "Vous avez gagné une partie "+jeu.getPartie().getDifficulte().getNom()+" en "+timerFX.getText()
                    );
                    victoire.setTitle("Victoire");
                    victoire.setHeaderText("Vous avez gagné !!!");
                    victoire.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if(t.getBombesProches() == 0){
                decouvrirZoneSure(t);
            }
        }
    }

    public Image getImage(Case c){
        if(!c.getDecouvert()){
            if(!c.getDrapeau()){
                if(c instanceof Terrain){

                }else if(c instanceof Mine){

                }
            }
            return new Image("resources/flag/hidden_flag.png");
        }
        return new Image("resources/box/hidden.png");
    }

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

    public void defaite(Mine touchee) throws Exception{
        jeu.getPartie().devoilerMines();
        jeu.getPartie().devoilerFauxDrapeaux();
        VBox lignes = new VBox();

        Case[][] mat = jeu.getPartie().getMatricePlateau();

        for(int row = 0; row < mat.length; row++){
            HBox col = new HBox();
            for(int column = 0; column < mat[row].length; column++){
                ImageView iv = new ImageView();
                if(jeu.getPartie().getDifficulte().getId() == 3){
                    iv.setFitWidth(20);
                    iv.setFitHeight(20);
                } else if(jeu.getPartie().getDifficulte().getId() >= 4){
                    iv.setFitWidth(15);
                    iv.setFitHeight(15);
                }

                Case c = jeu.getPartie().getCaseMatrice(row, column);
                iv.setImage(c.getImage());
                // Création d'une bordure gros pour bien distinguer les cases
                BorderPane bp = new BorderPane(iv);
                BorderStroke bs = new BorderStroke(
                    Color.GREY, 
                    BorderStrokeStyle.SOLID, 
                    CornerRadii.EMPTY, 
                    new BorderWidths(1, 1, 1, 1)
                );
    
                bp.setBorder(new Border(bs));
                col.getChildren().add(bp);
            }
            lignes.getChildren().add(col);
        }
        chrono.stop();
        show(lignes);
    }

    public void victoire() throws Exception{
        VBox lignes = new VBox();

        Case[][] mat = jeu.getPartie().getMatricePlateau();

        for(int row = 0; row < mat.length; row++){
            HBox col = new HBox();
            for(int column = 0; column < mat[row].length; column++){
                ImageView iv = new ImageView();

                Case c = jeu.getPartie().getCaseMatrice(row, column);
                iv.setImage(c.getImage());
                if(jeu.getPartie().getDifficulte().getId() == 3){
                    iv.setFitWidth(20);
                    iv.setFitHeight(20);
                } else if(jeu.getPartie().getDifficulte().getId() >= 4){
                    iv.setFitWidth(15);
                    iv.setFitHeight(15);
                }

                // Création d'une bordure gros pour bien distunguer les cases
                BorderPane bp = new BorderPane(iv);
                BorderStroke bs = new BorderStroke(
                    Color.GREY, 
                    BorderStrokeStyle.SOLID, 
                    CornerRadii.EMPTY, 
                    new BorderWidths(1, 1, 1, 1)
                );
    
                bp.setBorder(new Border(bs));
                col.getChildren().add(bp);
            }
            lignes.getChildren().add(col);
        }
        if(jeu.getPartie().getNomSave() != null){
            Sauvegarde.delete(jeu.getPartie().getNomSave());
        }
        chrono.stop();
        show(lignes);
    }

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

    public void updateVisage(Visage v) throws Exception{
        this.visage.setImage(v.getImage());
    }

    public void updateTimerFX() throws Exception{
        int m = 0, h = 0, s = 0;
        long ms = jeu.getPartie().getMillis();
        String text = "";
        while(ms >= 1000){
            s++;
            ms -= 1000;
        }
        while(s >= 60){
            m++;
            s -= 60;
        }
        while(m >= 60){
            h++;
            m -= 60;
        }
        text += (h > 0) ? h+"h" : "";
        text += (m > 0) ? m+"m" : "";
        text += s+"s";
        timerFX.setText(text);
    }

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

    public void createMenuBarre(){
        Menu jeuBarre = new Menu("Jeu");

        MenuItem sauvegarder = new MenuItem("Sauvegarder");
        sauvegarder.setId("save");
        sauvegarder.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        if(jeu.getFin()){
            sauvegarder.setDisable(true);
        }
        sauvegarder.setOnAction(action -> {
            try {
                sauvegarder();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem charger = new MenuItem("Charger");
        charger.setId("load");
        charger.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
        charger.setOnAction(action -> {
            try {
                charger();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem setPause = new MenuItem("Pause / Reprendre");
        setPause.setId("pause");
        setPause.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
        setPause.setOnAction(new MenuController(this));

        SeparatorMenuItem ligne = new SeparatorMenuItem();

        MenuItem tableauScore = new MenuItem("Tableau des scores");
        tableauScore.setId("tabScore");
        tableauScore.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN));
        tableauScore.setOnAction(action -> {
            TableauScoreFX.show();
            System.err.println("Tableau des scores non implémenté");
        });

        MenuItem quitter = new MenuItem("Quitter");
        quitter.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        quitter.setOnAction(action -> {
            stage.close();
        });

        // Choix difficulté
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

        Menu plus = new Menu("Plus");
        MenuItem regles = new MenuItem("Règles");
        
        jeuBarre.getItems().addAll(sauvegarder, charger, setPause, ligne, tableauScore, quitter);
        choix_difficulte.getItems().addAll(facile, intermediaire, difficile, expert, impossible, hardcore, diabolique);
        plus.getItems().addAll(regles);

        menuBarre.getMenus().addAll(jeuBarre, choix_difficulte, plus);
    }

    public void sauvegarder() throws ClassNotFoundException, IOException{
        chrono.stop();
        NomSauvegardeFX sauvegardeFX = new NomSauvegardeFX();
        if(jeu.getPartie().getNomSave() == null){
            sauvegardeFX.show();
            if(sauvegardeFX.getNom() != null){
                jeu.getPartie().setNomSave(sauvegardeFX.getNom().toLowerCase());
                jeu.save(jeu.getPartie());

                Alert confirmation = new Alert(AlertType.INFORMATION, 
            "La partie a bien été sauvegardée"
                );
                confirmation.setTitle("Sauvegarde");
                confirmation.setHeaderText("Sauvegarde effectué");
                confirmation.showAndWait();
            }
        }else{
            jeu.save(jeu.getPartie());
            Alert confirmation = new Alert(AlertType.INFORMATION, 
            "La partie a bien été sauvegardée"
                );
            confirmation.setTitle("Sauvegarde");
            confirmation.setHeaderText("Sauvegarde effectué");
            confirmation.showAndWait();
        }
        chrono.play();
    }

    private void charger() throws ClassNotFoundException, IOException{
        chrono.stop();
        FileChooser fc = new FileChooser();
        fc.setTitle("Choississez une sauvegarde");
        //Voir uniquement les fichiers .save
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Sauvegardes", "*.save");
        fc.getExtensionFilters().add(extFilter);
        fc.setInitialDirectory(new File(Sauvegarde.CHEMIN_SAUVEGARDE));

        File sauvegarde = fc.showOpenDialog(stage);
        if(sauvegarde != null){
            Optional<Partie> loadPartie = jeu.load(sauvegarde);
            loadPartie.ifPresent(p -> {
                try {
                    jeu.start(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }else{
            chrono.play();
        }
    }

    public Jeu getJeu() {
        return jeu;
    }

    public Timeline getChrono() {
        return chrono;
    }

    public Label getNbrDrapeauFX() {
        return nbrDrapeauFX;
    }

    public MenuBar getMenuBarre() {
        return menuBarre;
    }

    public VBox getPlateauFX() {
        return plateauFX;
    }

    public Stage getStage() {
        return stage;
    }

    public Label getTimerFX() {
        return timerFX;
    }

    public ImageView getVisage() {
        return visage;
    }

    public boolean getPause(){
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }
}