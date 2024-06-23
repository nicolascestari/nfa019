package org.calendrier.view;

import org.calendrier.dao.ActiviteDao;
import org.calendrier.model.Activite;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditActiviteView extends Application {

    public interface SaveCallback {
        void onSave();
    }

    private Activite activite;
    private ActiviteDao activiteDao;
    private SaveCallback callback;

    public EditActiviteView(SaveCallback callback) {
        this.activiteDao = new ActiviteDao();
        this.activite = new Activite();
        this.callback = callback;
    }

    public EditActiviteView(Activite activite, SaveCallback callback) {
        this.activiteDao = new ActiviteDao();
        this.activite = activite;
        this.callback = callback;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Créer/Éditer Activité");

        // Création des champs de saisie
        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker();
        if (activite.getDate() != null && !activite.getDate().isEmpty()) {
            datePicker.setValue(LocalDate.parse(activite.getDate()));
        }

        Label commentaireLabel = new Label("Commentaire:");
        TextField commentaireField = new TextField();
        commentaireField.setText(activite.getCommentaire());

        Label statutLabel = new Label("Statut:");
        TextField statutField = new TextField();
        statutField.setText(activite.getStatut());

        Button saveButton = new Button("Sauvegarder");
        Button cancelButton = new Button("Annuler");

        // Ajouter la logique pour le bouton Sauvegarder
        saveButton.setOnAction(event -> {
            activite.setDate(datePicker.getValue().toString());
            activite.setCommentaire(commentaireField.getText());
            activite.setStatut(statutField.getText());
            activiteDao.saveActivite(activite);
            if (callback != null) {
                callback.onSave();
            }
            primaryStage.close(); // Fermer la fenêtre après sauvegarde
        });

        // Ajouter la logique pour le bouton Annuler
        cancelButton.setOnAction(event -> primaryStage.close());

        // Disposition des éléments dans une grille
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(dateLabel, 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(commentaireLabel, 0, 1);
        grid.add(commentaireField, 1, 1);
        grid.add(statutLabel, 0, 2);
        grid.add(statutField, 1, 2);
        grid.add(saveButton, 0, 3);
        grid.add(cancelButton, 1, 3);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
