package fr.istic.project.application;

import fr.istic.project.controller.Controller;
import fr.istic.project.model.Board;
import fr.istic.project.model.JSynBoard;
import fr.istic.project.view.JavaFXController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private Board model;
    private JavaFXController view;
    private Controller controller;
    private Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        createModel();
        createView();
        createController();
        display(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void createModel() {
        try {
            model = new JSynBoard();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void createView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL location = getClass().getResource("/fxml/main.fxml");
        root = fxmlLoader.load(location.openStream());
        view = fxmlLoader.getController();
    }

    private void createController() {
        controller = new Controller(model, view);
    }

    private void display(Stage primaryStage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        // Main CSS stylesheet
        scene.getStylesheets().add("/styles/application.css");

        primaryStage.setTitle("SynTardOS");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(ev -> model.setActivated(false));
        primaryStage.show();

        model.notifyObservers();
    }
}
