/*
    This is the entry point of the enigma application
    SInce we are using javafx library, we need to extend the Application class
 */


package com.fxapp.enigmasimulatorfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class EnigmaApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("splash.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        Image img = new Image(getClass().getResource("enigma_logo.png").toExternalForm());
        stage.getIcons().add(img);
        stage.setScene(scene);
        stage.show();
    }

    // This method launches the desktop application
    public static void main(String[] args) {
        launch();
    }
}