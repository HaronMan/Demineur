package fr.haronman.demineur.fx;

import fr.haronman.demineur.Sauvegarde;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NomSauvegardeFX {
    private String nom;

    public NomSauvegardeFX(){
    }

    public void show(){
        Stage stage = new Stage();
        stage.setTitle("Nom sauvegarde");

        VBox contenu = new VBox(20);
        contenu.setAlignment(Pos.CENTER);

        Text text = new Text("Entrez un nom de sauvegarde (10 caractères max) :");
        text.setFont(new Font(13));
        TextField inputNom = new TextField();
        inputNom.setPromptText("Nom");
        inputNom.setAlignment(Pos.CENTER);
        inputNom.setMaxWidth(200);
        inputNom.setFont(new Font(15));
    
        //Contrainte 10 caractères max
        TextFormatter<String> textFormatter = new TextFormatter<>(change ->
                change.getControlNewText().length() <= 10 ? change : null);
        inputNom.setTextFormatter(textFormatter);

        Text message = new Text("Ce nom existe déjà !");
        message.setFont(new Font(13));
        message.setFill(Color.RED);
        message.setVisible(false);
        inputNom.textProperty().addListener((observable, oldV, newV) -> {
            if(message.isVisible()){
                message.setVisible(false);
            }
        });

        HBox boutons = new HBox(10);
        boutons.setAlignment(Pos.CENTER);

        Button valider = new Button("Ajouter sauvegarde");
        //Bouton "Valider" bloqué tant que inputNom vide
        BooleanProperty bp = new SimpleBooleanProperty(true);
        bp.bind(inputNom.textProperty().isEmpty());
        valider.disableProperty().bind(bp);
        

        valider.setOnMouseClicked(event -> {
            if(Sauvegarde.nomDejaExistant(inputNom.getText().toLowerCase())){
                message.setVisible(true);
            }else{
                nom = inputNom.getText();
                stage.close();
            }
        });
        Button annuler = new Button("Annuler");
        annuler.setOnMouseClicked(event -> stage.close());

        boutons.getChildren().addAll(valider, annuler);
        contenu.getChildren().addAll(text, inputNom, message, boutons);

        Scene scene = new Scene(contenu, 350, 200);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    public String getNom() {
        return nom;
    }
    
}
