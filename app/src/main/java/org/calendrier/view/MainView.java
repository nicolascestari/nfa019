package org.calendrier.view;

import org.calendrier.dao.ActiviteDao;
import org.calendrier.model.Activite;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainView extends Application {

    private ActiviteDao activiteDao;
    private YearMonth currentYearMonth;
    private GridPane calendar;
    private Label currentYearMonthLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Calendrier");

        activiteDao = new ActiviteDao();
        currentYearMonth = YearMonth.now();

        // Initialiser les composants UI
        initializeUI(primaryStage);
        updateCalendar();

        // Mettre la vue en plein écran par défaut
        primaryStage.setMaximized(true);
    }

    private void initializeUI(Stage primaryStage) {
        // Label pour afficher le mois et l'année courants
        currentYearMonthLabel = new Label(getFormattedYearMonth(currentYearMonth));
        currentYearMonthLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center;");

        // Créer le GridPane pour afficher le calendrier
        calendar = new GridPane();
        calendar.setGridLinesVisible(true);
        calendar.setAlignment(Pos.CENTER);

        // Boutons de navigation pour changer de mois
        Button previousMonthButton = new Button("Mois Précédent");
        previousMonthButton.setOnAction(event -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        });

        Button nextMonthButton = new Button("Mois Suivant");
        nextMonthButton.setOnAction(event -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        });

        // Bouton pour ajouter une nouvelle activité
        Button addActivityButton = new Button("Ajouter une activité");
        addActivityButton.setOnAction(event -> {
            try {
                new EditActiviteView(this::updateCalendar).start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Créer un layout et ajouter les composants
        VBox vbox = new VBox(previousMonthButton, nextMonthButton, addActivityButton, calendar, currentYearMonthLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        Scene scene = new Scene(vbox, 1200, 800);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateCalendar() {
        calendar.getChildren().clear();

        // Ajouter les en-têtes des jours de la semaine
        String[] daysOfWeek = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center;");
            calendar.add(dayLabel, i, 0);
        }

        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        int dayOfWeekOfFirstDay = (firstDayOfMonth.getDayOfWeek().getValue() % 7) - 1;
        if (dayOfWeekOfFirstDay < 0) {
            dayOfWeekOfFirstDay = 6; // Ajustement pour que Dimanche soit à la fin de la semaine
        }
        int daysInMonth = currentYearMonth.lengthOfMonth();

        // Charger les activités pour le mois courant
        Map<LocalDate, List<Activite>> activiteMap = new HashMap<>();
        List<Activite> activites = activiteDao.getAllActivites();
        for (Activite activite : activites) {
            LocalDate date = LocalDate.parse(activite.getDate());
            activiteMap.computeIfAbsent(date, k -> new ArrayList<>()).add(activite);
        }

        int row = 1;
        int col = dayOfWeekOfFirstDay;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            VBox dayBox = new VBox(new Label(String.valueOf(day)));
            dayBox.setStyle("-fx-border-color: black; -fx-alignment: center;");
            dayBox.setMinSize(100, 100);  // Augmenté pour améliorer l'affichage
            GridPane.setMargin(dayBox, new Insets(5));

            // Ajouter les informations de l'activité s'il y en a une pour ce jour
            if (activiteMap.containsKey(date)) {
                List<Activite> activitesDuJour = activiteMap.get(date);
                Button showAllButton = new Button("Afficher tous");
                showAllButton.setMaxWidth(Double.MAX_VALUE);  // Le bouton prend toute la largeur de la colonne
                showAllButton.setOnAction(event -> showAllEventsPopup(activitesDuJour));
                dayBox.getChildren().add(showAllButton);
            }

            calendar.add(dayBox, col, row);

            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }

        // Mettre à jour le label du mois et de l'année
        currentYearMonthLabel.setText(getFormattedYearMonth(currentYearMonth));
    }

    private void showAllEventsPopup(List<Activite> activites) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Tous les événements");

        ListView<String> listView = new ListView<>();
        for (Activite activite : activites) {
            listView.getItems().add(activite.getDate() + ": " + activite.getCommentaire());
        }
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Button deleteButton = new Button("X");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setOnAction(event -> {
            String selectedActiviteString = listView.getSelectionModel().getSelectedItem();
            if (selectedActiviteString != null) {
                Alert alert = new Alert(AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer cet événement ?");
                alert.showAndWait().ifPresent(response -> {
                    Activite selectedActivite = activites.stream()
                            .filter(a -> (a.getDate() + ": " + a.getCommentaire()).equals(selectedActiviteString))
                            .findFirst().orElse(null);
                    if (selectedActivite != null) {
                        activiteDao.deleteActivite(selectedActivite.getId());
                        listView.getItems().remove(selectedActiviteString);
                        updateCalendar();
                    }
                });
            }
        });

        HBox hbox = new HBox(deleteButton);
        hbox.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(listView, hbox);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        Scene scene = new Scene(vbox, 300, 200);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    private void loadActivites() {
        List<Activite> activites = activiteDao.getAllActivites();
        if (activites != null) {
            ObservableList<Activite> data = FXCollections.observableArrayList(activites);
            // Vous pouvez utiliser ces données pour mettre à jour un TableView ou d'autres composants
        }
    }

    private String getFormattedYearMonth(YearMonth yearMonth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return yearMonth.atDay(1).format(formatter);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
