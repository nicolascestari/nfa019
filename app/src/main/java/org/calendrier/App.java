package org.calendrier;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.calendrier.dao.ActiviteDao;
import org.calendrier.database.DatabaseManager;
import org.calendrier.model.Activite;
import org.calendrier.view.MainView;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Exécuter les migrations Flyway
            DatabaseManager.migrate();

            // Initialiser l'EntityManagerFactory
            DatabaseManager.getEntityManagerFactory();

            // Ajouter une activité bidon
            ActiviteDao activiteDao = new ActiviteDao();
            Activite activite = new Activite("2024-06-23", "Commentaire bidon", "Statut bidon");
            activiteDao.saveActivite(activite);

            // Afficher la MainView
            MainView mainView = new MainView();
            mainView.start(primaryStage);

        } catch (Exception e) {
            e.printStackTrace();
            Stage errorStage = new Stage();
            Label label = new Label("Échec de la mise à jour de l'activité.");
            Scene scene = new Scene(label, 400, 200);
            errorStage.setScene(scene);
            errorStage.setTitle("Le Calendrier des Telecoms");
            errorStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
