/*
    This class controls the main window
 */

package com.fxapp.enigmasimulatorfx;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    // Calling the controls from the scenebuider
    @FXML
    private VBox parent;

    @FXML
    private Button btnAddPlug;
    @FXML
    private Button btnMuteUnmute;

    @FXML
    private ImageView imvMuteUnMute;

    @FXML
    private Button btnRemovePlug;

    @FXML
    private Button btnNewMsg;

    @FXML
    private Button btnExport;

    @FXML
    private Button btnHelp;

    @FXML
    private VBox keyboard;

    @FXML
    private ListView<String> leftRotor;

    @FXML
    private ListView<String> middleRotor;

    @FXML
    private ListView<String> rightRotor;

    @FXML
    private Button leftRotorDown;

    @FXML
    private Button leftRotorUp;

    @FXML
    private VBox lightboard;

    @FXML
    private Button midRotorDown;

    @FXML
    private Button midRotorUp;

    @FXML
    private ListView<Plugboard.Plug> plugList;

    @FXML
    private Button rightRotorDown;

    @FXML
    private Button rightRotorUp;

    @FXML
    private TextArea taEncrytedWriterMsg;

    // Hashmap for keyboard and lightboard
    private HashMap<String, StackPane> lightMap;
    private HashMap<String, Button> buttonMap;

    // A stage to help display the popups
    Stage stage;

    // Defining the enigma object
    private EnigmaDevice enigmaDevice;

    // Starting positions of the rotors
    private String leftRotorStartPos;
    private String midRotorStartPos;
    private String rightRotorStartPos;

    // To help us define the length of the message
    private String content = "";

    // Popup definition
    Popup info;

    // To help us define the length of the message
    int count = 0;

    // Sound usage definition
    AudioClip typesound;
    AudioClip spinsound;
    AudioClip plug_in;
    AudioClip plug_out;
    AudioClip button_click;

    // Boolean to help mute and unmute the app
    Boolean unmuted = true;



    // Initialization method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enigmaDevice = new EnigmaDevice();
        stage = new Stage();
        info = new Popup();

        // whiles there has not been any keyboard press, we get the starting postion of rotors
        if(count < 1){
            leftRotorStartPos = enigmaDevice.getLeftRotor().alphabet.get(0);
            midRotorStartPos = enigmaDevice.getMidRotor().alphabet.get(0);
            rightRotorStartPos = enigmaDevice.getRightRotor().alphabet.get(0);
        }



            // Initializing the sounds used in the application
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                    typesound = new AudioClip(getClass().getResource("typesound.mp3").toURI().toString());
                    spinsound = new AudioClip(getClass().getResource("rotor_spin.mp3").toURI().toString());
                    plug_in = new AudioClip(getClass().getResource("plug_in.mp3").toURI().toString());
                    plug_out = new AudioClip(getClass().getResource("plug_out.mp3").toURI().toString());
                    button_click = new AudioClip(getClass().getResource("button_click.mp3").toURI().toString());

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            });

        // Initialising the j=hashmap that maps the keybaord and lightboard buttons
        lightMap = new HashMap<String, StackPane>();
        buttonMap = new HashMap<String, Button>();

        // Keys to be used as keyboard and lightboard keys
        String[] keys = {
                "QWERTYUIOP",
                "ASDFGHJKL",
                "ZXCVBNM"
        };

        // Wrap content in the writer textarea
        taEncrytedWriterMsg.setWrapText(true);

        // Setting the font size of the keys
        Font font = new Font(16);
        // Looping through the keys string
        for (int r = 0; r < keys.length; r++){
            HBox row = new HBox(5);
            HBox lightRow = new HBox();
            for(int c = 0; c < keys[r].length(); c++){

                Button btn = new Button(keys[r].substring(c, c + 1));
                btn.setOnAction(this::keyboardPressedAction);
                btn.setFont(font);
                //Adding the buttons to the horizontal boxes
                row.getChildren().add(btn);

                buttonMap.put(keys[r].substring(c, c + 1), btn);

                StackPane light = new StackPane();
                light.setPrefSize(30, 30);

                // A circle around the lightboard to make it look realistic
                Circle circ = new Circle();
                circ.setRadius(15);
                circ.setFill(Color.web("#ABA19F"));

                Text letter = new Text(keys[r].substring(c, c + 1));
                letter.setFont(font);
                letter.setTextAlignment(TextAlignment.CENTER);
                letter.setFill(Color.BLACK);

                light.getChildren().addAll(circ, letter);
                lightRow.getChildren().add(light);

                lightMap.put(keys[r].substring(c, c + 1), light);
            }
            // Centering all the horizontal boxes
            row.setAlignment(Pos.CENTER);
            keyboard.getChildren().add(row);

            lightRow.setAlignment(Pos.CENTER);
            lightRow.setSpacing(5);
            lightboard.getChildren().add(lightRow);
        }

        // Spacing lightboard
        lightboard.setSpacing(5);
        lightboard.setPadding(new Insets(5));

        keyboard.setSpacing(5);
        keyboard.setPadding(new Insets(5));


        plugList.setItems(enigmaDevice.getPlugboard().getPlugs());

        // Setting the alphabets to each rotor
        leftRotor.setItems(enigmaDevice.getLeftRotor().alphabet);
        middleRotor.setItems(enigmaDevice.getMidRotor().alphabet);
        rightRotor.setItems(enigmaDevice.getRightRotor().alphabet);

        // This method is called when the mute/unmute button is clicked
        btnMuteUnmute.setOnAction(e ->{
            if(unmuted){
                // Muting the app if it unmuted
                // Image of button changes after a click
                imvMuteUnMute.setImage((new Image(getClass().getResource("mute.png").toExternalForm())));
                btnMuteUnmute.setGraphic(imvMuteUnMute);
                unmuted = false;
            }else{
                // Unmuting the app if it muted
                button_click.play();
                // Image of button changes after a click
                imvMuteUnMute.setImage((new Image(getClass().getResource("unmute.png").toExternalForm())));
                btnMuteUnmute.setGraphic(imvMuteUnMute);
                unmuted = true;
            }
        });

        // This method is called if the left rotor is rotated up using the button up
        leftRotorUp.setOnAction(e ->{
            e.consume();
            if(unmuted){
                spinsound.play();
            }
            enigmaDevice.getLeftRotor().advance();
            // Updating the settings if the keyboard has not been pressed
            if(count < 1){
                leftRotorStartPos = enigmaDevice.getLeftRotor().alphabet.get(0);
            }
        });

        // This method is called if the left rotor is rotated down using the button down
        leftRotorDown.setOnAction(e ->{
            e.consume();
            if(unmuted){
                spinsound.play();
            }
            enigmaDevice.getLeftRotor().advanceBack();
            if(count < 1){
                leftRotorStartPos = enigmaDevice.getLeftRotor().alphabet.get(0);
            }

        });

        // This method is called if the middle rotor is rotated up using the button up
        midRotorUp.setOnAction(e ->{
            e.consume();
            if(unmuted){
                spinsound.play();
            }
            enigmaDevice.getMidRotor().advance();
            if(count < 1){
                midRotorStartPos = enigmaDevice.getMidRotor().alphabet.get(0);
            }
        });

        // This method is called if the middle rotor is rotated down using the button down
        midRotorDown.setOnAction(e ->{
            e.consume();
            if(unmuted){
                spinsound.play();
            }
            enigmaDevice.getMidRotor().advanceBack();
            if(count < 1){
                midRotorStartPos = enigmaDevice.getMidRotor().alphabet.get(0);
            }
        });

        // This method is called if the right rotor is rotated up using the button up
        rightRotorUp.setOnAction(e ->{
            e.consume();
            if(unmuted){
                spinsound.play();
            }
            enigmaDevice.getRightRotor().advance();
            if(count < 1){
                rightRotorStartPos = enigmaDevice.getRightRotor().alphabet.get(0);
            }
        });

        // This method is called if the right rotor is rotated down using the button down
        rightRotorDown.setOnAction(e ->{
            e.consume();
            if(unmuted){
                spinsound.play();
            }
            enigmaDevice.getRightRotor().advanceBack();
            if(count < 1){
                rightRotorStartPos = enigmaDevice.getRightRotor().alphabet.get(0);
            }
        });

       // This event is called when the add plug button is pressed
       btnAddPlug.setOnAction(e -> addPlug(e));

       // This event is called when the remove plug button is pressed
       btnRemovePlug.setOnAction(e -> removePlug(e));

        // This event is called when the export message button is pressed
        btnExport.setOnAction(e ->{
            if(unmuted){
                button_click.play();
            }
            // Using the filechooser control to help user save file at his prefrered location
            FileChooser fileChooser = new FileChooser();
            //Set the extension
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            //Showing dialog
            File file = fileChooser.showSaveDialog(stage);

            if(file != null){
                try {
                    SaveFile(taEncrytedWriterMsg.getText(), file);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // This event is called when the clear  button is pressed
        btnNewMsg.setOnAction(e -> {
            if(unmuted){
                button_click.play();
            }

            btnAddPlug.setDisable(false);
            btnRemovePlug.setDisable(false);
            leftRotorUp.setDisable(false);
            leftRotorDown.setDisable(false);
            midRotorUp.setDisable(false);
            midRotorDown.setDisable(false);
            rightRotorUp.setDisable(false);
            rightRotorDown.setDisable(false);

            // Revert the count to zero for new settings
            count = 0;
            content = "";
            // Maintaining the current rotor positions as the thier new starting positions
            leftRotorStartPos = enigmaDevice.getLeftRotor().alphabet.get(0);
            midRotorStartPos = enigmaDevice.getMidRotor().alphabet.get(0);
            rightRotorStartPos = enigmaDevice.getRightRotor().alphabet.get(0);

            taEncrytedWriterMsg.clear();
            plugList.getItems().clear();
            e.consume();
        });

        // This even occurs when you press the physical keyboard
        parent.setOnKeyPressed(event -> {
            btnAddPlug.setDisable(true);
            btnRemovePlug.setDisable(true);
            leftRotorUp.setDisable(true);
            leftRotorDown.setDisable(true);
            midRotorUp.setDisable(true);
            midRotorDown.setDisable(true);
            rightRotorUp.setDisable(true);
            rightRotorDown.setDisable(true);

            // Checks if the length of the message has reached 200
            if(content.length() == 200){
               if(!info.isShowing()){
                // If only the pop up is not already showing
                VBox pane = new VBox();
                pane.setStyle("-fx-background-color: #ABA19F;");
                Label lbl = new Label("Maximum message length reached");
                Button btnOk = new Button("Ok");
                pane.getChildren().addAll(lbl,btnOk);
                pane.setAlignment(Pos.CENTER);
                info.getContent().add(pane);
                info.show(stage);
                info.centerOnScreen();
                btnOk.setOnAction(e -> info.hide());
               }
                return;
            }
            count +=1;
            if(unmuted){
                typesound.play();
            }

            String key = event.getText().toUpperCase();

            content += key;
            for(String row : keys) {
                if (row.indexOf(key) != -1 && !key.isBlank()) {
                    // Switching the letter before the writer can see and right in text area
                    String cypherTextLetter = enigmaDevice.switchLetter(key);
                    turnOnLight(cypherTextLetter);
                    buttonMap.get(key).setStyle("-fx-background-color:rgb(93, 77, 58);");
                    taEncrytedWriterMsg.appendText(cypherTextLetter);
                    Task<Void> sleeper = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            try {Thread.sleep(500);}
                            catch (InterruptedException e) {}
                            return null;
                        }
                    };
                    sleeper.setOnSucceeded(e -> {
                        buttonMap.get(key).setStyle("-fx-background-color: rgb(43, 35, 26);");
                    });
                    new Thread(sleeper).start();
                }
            }
            event.consume();
        });

        // When the help button is pressed
        btnHelp.setOnAction(e ->{
            if(unmuted){
                button_click.play();
            }
            if(!info.isShowing()){
                VBox pane = new VBox();
                pane.setStyle("-fx-background-color: #000;");
                Button btnOk = new Button("OK");
                btnOk.setStyle("-fx-background-color: #443627;");
                String ins = "===================================================================================\n" +
                        "  INSTRUCTIONS\n" +
                        "===================================================================================\n" +
                        "1. Use the arrows of the rotors to configure their starting positions\n" +
                        "2. Use the plug in and out icons to configure plugboard\n" +
                        "4. Make sure to configure your settings before typing in the messages\n"+
                        "5. To export the encrypted message as a text file, click on the 'Export Message' button and select location to export\n" +
                        "6. To decrypt a message, open the encrypted text file and set settings before typing to decode message\n" +
                        "7. Use the mute and unmute button to turn on/of sound\n" +
                        "===================================================================================";

                pane.getChildren().add(new Label(ins));
                pane.getChildren().add(btnOk);
                pane.setAlignment(Pos.CENTER);



                info.getContent().add(pane);
                info.show(stage);
                info.centerOnScreen();
                btnOk.setOnAction(ev -> {
                    if(unmuted){
                        button_click.play();
                    }
                    info.hide();
                });
            }




        });
    }

    // Save method called in the save message button event
    private void SaveFile(String text, File file) throws IOException {
        // Calling the file writer since we are going to write and save a file
        FileWriter fileWriter;
        // Write to the text file
        fileWriter = new FileWriter(file);
        fileWriter.write("Settings\n");
        fileWriter.write("================================\n");
        fileWriter.write("Rotor Starting Positions\n");
        // Adding the rotor starting positions settings to the exported message
        fileWriter.write("Left Rotor Start Position -> "+leftRotorStartPos+"\n");
        fileWriter.write("Middle Rotor Start Position -> "+midRotorStartPos+"\n");
        fileWriter.write("Right Rotor Start Position -> "+rightRotorStartPos+"\n");
        fileWriter.write("\n");
        // dding plug settings if any
        if(plugList.getItems().size()>0 ){
            fileWriter.write("Plugs\n");
            for(int i=0;i<plugList.getItems().size();i++){
                fileWriter.write(plugList.getItems().get(i)+"\n");
            }
            fileWriter.write("\n");
        }

        fileWriter.write("================================\n");
        fileWriter.write("Encrypted Message\n");
        // Adding the actual encrypted message
        fileWriter.write("================================\n");
        fileWriter.write(text+"\n");
        fileWriter.write("================================\n");
        fileWriter.close();
        // Showing a popup after done saving the file
        Popup info = new Popup();
        VBox pane = new VBox();
        pane.setStyle("-fx-background-color: #ABA19F;");
        Label lbl = new Label("File saved successfully");
        Button btnOk = new Button("Ok");
        pane.getChildren().addAll(lbl,btnOk);
        pane.setPrefWidth(200);
        pane.setPrefHeight(50);
        pane.setAlignment(Pos.CENTER);
        info.getContent().add(pane);
        info.show(stage);
        info.centerOnScreen();
        btnOk.setOnAction(event -> {
            if(unmuted){
                button_click.play();
            }
            info.hide();
        });
    }

    // Virtual keyboard press called
    private void keyboardPressedAction(ActionEvent e) {

        btnAddPlug.setDisable(true);
        btnRemovePlug.setDisable(true);
        leftRotorUp.setDisable(true);
        leftRotorDown.setDisable(true);
        midRotorUp.setDisable(true);
        midRotorDown.setDisable(true);
        rightRotorUp.setDisable(true);
        rightRotorDown.setDisable(true);

        // Checks to see if the maximum message length is reached
        if(content.length() == 200){
            if(!info.isShowing()){
                VBox pane = new VBox();
                pane.setStyle("-fx-background-color: #ABA19F;");
                Label lbl = new Label("Maximum message length reached");
                Button btnOk = new Button("Ok");
                pane.getChildren().addAll(lbl,btnOk);
                pane.setAlignment(Pos.CENTER);
                info.getContent().add(pane);
                info.show(stage);
                info.centerOnScreen();
                btnOk.setOnAction(ev -> info.hide());
            }
            return;
        }
        count +=1;
        // When not muted a sound plays
        if(unmuted){
            typesound.play();
        }
        Button btn = (Button) e.getSource();
        String key = btn.getText();
        content += key;
        String cypherTextLetter = enigmaDevice.switchLetter(key);
        // Calling the light on method to emulate the enigma lightboard light switch
        turnOnLight(cypherTextLetter);
        
        // Writing the encoded letter to the writer text area
        taEncrytedWriterMsg.appendText(cypherTextLetter);
        e.consume();
    }

    // Calling the turnlight method
    private void turnOnLight(String cypherTextLetter) {
        StackPane light = lightMap.get(cypherTextLetter);

        if (light != null) {
            // Light display
            light.getChildren().get(0).setStyle("-fx-fill: white");
            light.getChildren().get(1).setStyle("-fx-fill: black");
            Task<Void> sleeper = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    // Shows for about 0.5 seconds
                    try {Thread.sleep(500);}
                    catch (InterruptedException e) {}
                    return null;
                }
            };
            sleeper.setOnSucceeded(event -> {
                light.getChildren().get(0).setStyle("-fx-fill: #ABA19F");
                light.getChildren().get(1).setStyle("-fx-fill: black");
            });
            new Thread(sleeper).start();
        }
    }

    // The removing a plug method when its button is pressed
    private void removePlug(ActionEvent e) {
        e.consume();

        MultipleSelectionModel<Plugboard.Plug> select = plugList.getSelectionModel();
        if (select.getSelectedIndex() >= 0) {
            enigmaDevice.getPlugboard().removePlug(select.getSelectedIndex());
            if(unmuted){
                plug_out.play();
            }
        }
    }

    // The adding a plug method when its button is pressed
    private void addPlug(ActionEvent e) {
        e.consume();
        TextInputDialog dialog = new TextInputDialog();
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        dialog.setTitle("Add Plug");
        dialog.setGraphic(null);
        // Showing first letter to be entered
        dialog.setHeaderText("Enter  first plus letter");
        //
        String firstLetter = plugDialog(dialog);

        if (firstLetter == null) {
            return;
        }
        // Showing for second letter to be entered
        dialog.getEditor().setText("");
        dialog.setHeaderText("Enter second plug letter");

        String secondLetter = plugDialog(dialog);

        // Shows when same letter is entered twice to be plugged
        while (firstLetter.equals(secondLetter)) {
            dialog.setHeaderText("Cannot plug firstLetter letter into itself. Please choose another letter.");
            secondLetter = plugDialog(dialog);
        }

        if (secondLetter == null) {
            return;
        };

        enigmaDevice.getPlugboard().addPlug(firstLetter, secondLetter);
        if(unmuted){
            plug_in.play();
        }
    }


    // A dialog that shows for the user to configure plugs
    private String plugDialog(TextInputDialog dialog) {
        String res = "";

        while (res.length() != 1) {
            Optional<String> result = dialog.showAndWait();

            if (!result.isPresent()) {
                return null;
            }

            if (result.get().length() < 1 || result.get().length() > 1) {
                dialog.setHeaderText("Please enter a single letter");
            } else if (enigmaDevice.getPlugboard().isPlugged(result.get().toUpperCase())) {
                // Shows when the input is already plugged
                dialog.setHeaderText("'" + result.get().toUpperCase() + "' is already plugged. Please choose another letter.");
            } else {
                res = result.get().toUpperCase();
            }
        }
        return res;
    }

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }



}
