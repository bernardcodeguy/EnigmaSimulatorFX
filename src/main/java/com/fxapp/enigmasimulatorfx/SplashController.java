package com.fxapp.enigmasimulatorfx;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML
    private VBox parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new SplashScreen().start();
    }


    class SplashScreen extends Thread{

        @Override
        public void run(){
            try {
                Thread.sleep(5000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Scene scene = new Scene(root);
                        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
                        Image img = new Image(getClass().getResource("enigma_logo.png").toExternalForm());
                        Stage stage = new Stage();
                        stage.getIcons().add(img);
                        MainController mainController = (MainController)loader.getController();
                        mainController.setPrimaryStage(stage);

                        scene.addEventFilter(KeyEvent.ANY, e -> {
                            if (e.getText().isBlank()) {
                                e.consume();
                            }
                        });

                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent windowEvent) {

                            }
                        });

                        stage.setScene(scene);
                        stage.show();
                        parent.getScene().getWindow().hide();
                    }
                });

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
