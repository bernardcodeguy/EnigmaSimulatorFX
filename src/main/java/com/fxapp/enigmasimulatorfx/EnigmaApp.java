package com.fxapp.enigmasimulatorfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class EnigmaApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        Image img = new Image(getClass().getResource("enigma_logo.png").toExternalForm());
        stage.getIcons().add(img);
        Controller controller = (Controller)loader.getController();
        controller.setPrimaryStage(stage);

        scene.addEventFilter(KeyEvent.ANY, e -> {
            if (e.getText().isBlank()) {
                e.consume();
            }
        });

        stage.setTitle("Enigma Simulator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}