package com.fxapp.enigmasimulatorfx;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML
    private VBox parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Starting the splash screen on app startup
        new SplashScreen().start();
    }

    // Using a thread to start application
    class SplashScreen extends Thread{

        @Override
        public void run(){
            try {
                // Showing it for 7 seconds
                // You can change it to your prefrered seconds
                Thread.sleep(7000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        // Loading the main screen
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
                        stage.setTitle("Enigma Simulator");
                        stage.getIcons().add(img);
                        MainController mainController = (MainController)loader.getController();
                        mainController.setPrimaryStage(stage);
                        // Making sure to escape same keyboard presses on the physical keyboard
                        scene.addEventFilter(KeyEvent.ANY, e -> {
                            if (e.getText().isBlank()) {
                                e.consume();
                            }
                        });

                        // Confirmation dialog when the close button is called or pressed
                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent windowEvent) {
                                Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
                                dialog.getDialogPane().getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
                                dialog.setTitle("Close Confirmation");
                                dialog.setHeaderText("Confirm close application");
                                dialog.setContentText("Closing application will mean that all unexported messages will be lost.\nProceed?");
                                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                                dialog.getButtonTypes().setAll(yesButton, noButton);

                                Optional<ButtonType> result = dialog.showAndWait();
                                if(result.get() != yesButton){
                                    windowEvent.consume();
                                }

                            }
                        });

                        stage.setScene(scene);
                        stage.show();
                        // When the splash screen 5 seconds is up, it hides it to show main screen
                        parent.getScene().getWindow().hide();
                    }
                });

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
