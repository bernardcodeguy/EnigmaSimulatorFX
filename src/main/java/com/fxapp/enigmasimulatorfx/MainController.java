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
    private Button btnClear;

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
    private HBox motherPane;

    @FXML
    private ListView<Plugboard.Plug> plugList;

    @FXML
    private Button rightRotorDown;

    @FXML
    private Button rightRotorUp;

    @FXML
    private TextArea taEncrytedWriterMsg;


    private HashMap<String, StackPane> lightMap;
    private HashMap<String, Button> buttonMap;

    Stage stage;

    private EnigmaDevice enigmaDevice;

    private String leftRotorStartPos;
    private String midRotorStartPos;
    private String rightRotorStartPos;

    private String content = "";

    Popup info;
    int count = 0;

    File file;
    AudioClip typesound;
    AudioClip spinsound;
    AudioClip plug_in;
    AudioClip plug_out;

    AudioClip button_click;

    Boolean unmuted = true;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enigmaDevice = new EnigmaDevice();
        stage = new Stage();
        info = new Popup();

        if(count < 1){
            leftRotorStartPos = enigmaDevice.getLeftRotor().alphabet.get(0);
            midRotorStartPos = enigmaDevice.getMiddleRotor().alphabet.get(0);
            rightRotorStartPos = enigmaDevice.getRightRotor().alphabet.get(0);
        }




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


        lightMap = new HashMap<String, StackPane>();
        buttonMap = new HashMap<String, Button>();

        String[] keys = {
                "QWERTYUIOP",
                "ASDFGHJKL",
                "ZXCVBNM"
        };

        taEncrytedWriterMsg.setWrapText(true);

        Font font = new Font(16);

        for (int r = 0; r < keys.length; r++){
            HBox row = new HBox(5);
            HBox lightRow = new HBox();
            for(int c = 0; c < keys[r].length(); c++){

                Button btn = new Button(keys[r].substring(c, c + 1));
                btn.setOnAction(this::keyboardPressedAction);
                btn.setFont(font);

                row.getChildren().add(btn);

                buttonMap.put(keys[r].substring(c, c + 1), btn);

                StackPane light = new StackPane();
                light.setPrefSize(30, 30);

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
            row.setAlignment(Pos.CENTER);
            keyboard.getChildren().add(row);

            lightRow.setAlignment(Pos.CENTER);
            lightRow.setSpacing(5);
            lightboard.getChildren().add(lightRow);
        }

        lightboard.setSpacing(5);
        lightboard.setPadding(new Insets(5));

        keyboard.setSpacing(5);
        keyboard.setPadding(new Insets(5));

        plugList.setItems(enigmaDevice.getPlugboard().getPlugs());

        leftRotor.setItems(enigmaDevice.getLeftRotor().alphabet);
        middleRotor.setItems(enigmaDevice.getMiddleRotor().alphabet);
        rightRotor.setItems(enigmaDevice.getRightRotor().alphabet);


        btnMuteUnmute.setOnAction(e ->{
            if(unmuted){
                imvMuteUnMute.setImage((new Image(getClass().getResource("mute.png").toExternalForm())));
                btnMuteUnmute.setGraphic(imvMuteUnMute);
                unmuted = false;
            }else{
                button_click.play();
                imvMuteUnMute.setImage((new Image(getClass().getResource("unmute.png").toExternalForm())));
                btnMuteUnmute.setGraphic(imvMuteUnMute);
                unmuted = true;
            }
        });

        // Left rotor
        leftRotorUp.setOnAction(e ->{
            e.consume();
            if(unmuted){
                spinsound.play();
            }
            enigmaDevice.getLeftRotor().advance();
            if(count < 1){
                leftRotorStartPos = enigmaDevice.getLeftRotor().alphabet.get(0);
            }
        });

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

        // Middle rotor
        midRotorUp.setOnAction(e ->{
            e.consume();
            if(unmuted){
                spinsound.play();
            }
            enigmaDevice.getMiddleRotor().advance();
            if(count < 1){
                midRotorStartPos = enigmaDevice.getMiddleRotor().alphabet.get(0);
            }
        });

        midRotorDown.setOnAction(e ->{
            e.consume();
            if(unmuted){
                spinsound.play();
            }
            enigmaDevice.getMiddleRotor().advanceBack();
            if(count < 1){
                midRotorStartPos = enigmaDevice.getMiddleRotor().alphabet.get(0);
            }
        });

        // Right rotor
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


       btnAddPlug.setOnAction(e -> addPlug(e));
       btnRemovePlug.setOnAction(e -> removePlug(e));

        btnExport.setOnAction(e ->{
            if(unmuted){
                button_click.play();
            }
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


        btnClear.setOnAction(e -> {
            if(unmuted){
                button_click.play();
            }
            count = 0;
            content = "";
            leftRotorStartPos = enigmaDevice.getLeftRotor().alphabet.get(0);
            midRotorStartPos = enigmaDevice.getMiddleRotor().alphabet.get(0);
            rightRotorStartPos = enigmaDevice.getRightRotor().alphabet.get(0);

            taEncrytedWriterMsg.clear();
            plugList.getItems().clear();
            e.consume();
        });

        parent.setOnKeyPressed(event -> {
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
                    String encoded = enigmaDevice.switchLetter(key);
                    turnOnLight(encoded);
                    buttonMap.get(key).setStyle("-fx-background-color:rgb(93, 77, 58);");
                    taEncrytedWriterMsg.appendText(encoded);
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


        btnHelp.setOnAction(e ->{
            if(unmuted){
                button_click.play();
            }
            if(!info.isShowing()){
                VBox pane = new VBox();
                pane.setStyle("-fx-background-color: #000;");
                Button btnOk = new Button("Ok");
                Label lbl = null;
                try {
                    File file = new File(getClass().getResource("instructions.txt").toURI());
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String st;
                    while ((st = br.readLine()) != null) {
                        pane.getChildren().add(new Label(st));
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
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

    private void SaveFile(String text, File file) throws IOException {

        FileWriter fileWriter;
        fileWriter = new FileWriter(file);
        fileWriter.write("Settings\n");
        fileWriter.write("================================\n");
        fileWriter.write("Rotor Starting Positions\n");
        fileWriter.write("Left Rotor Start Position -> "+leftRotorStartPos+"\n");
        fileWriter.write("Middle Rotor Start Position -> "+midRotorStartPos+"\n");
        fileWriter.write("Right Rotor Start Position -> "+rightRotorStartPos+"\n");
        fileWriter.write("\n");

        if(plugList.getItems().size()>0 ){
            fileWriter.write("Plugs\n");
            for(int i=0;i<plugList.getItems().size();i++){
                fileWriter.write(plugList.getItems().get(i)+"\n");
            }
            fileWriter.write("\n");
        }

        fileWriter.write("================================\n");
        fileWriter.write("Encrypted Message\n");
        fileWriter.write("================================\n");
        fileWriter.write(text+"\n");
        fileWriter.write("================================\n");
        fileWriter.close();
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

    private void keyboardPressedAction(ActionEvent e) {
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
        if(unmuted){
            typesound.play();
        }
        Button btn = (Button) e.getSource();
        String key = btn.getText();
        content += key;
        String encodedLetter = enigmaDevice.switchLetter(key);
        turnOnLight(encodedLetter);
        taEncrytedWriterMsg.appendText(encodedLetter);
        e.consume();
    }

    private void turnOnLight(String encoded) {
        StackPane light = lightMap.get(encoded);

        if (light != null) {
            light.getChildren().get(0).setStyle("-fx-fill: white");
            light.getChildren().get(1).setStyle("-fx-fill: black");
            Task<Void> sleeper = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
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

    private void addPlug(ActionEvent e) {
        e.consume();
        TextInputDialog dialog = new TextInputDialog();
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        dialog.setTitle("Add Plug");
        dialog.setGraphic(null);
        dialog.setHeaderText("Enter first plus letter");

        String firstLetter = plugDialog(dialog);

        if (firstLetter == null) {
            return;
        }

        dialog.getEditor().setText("");
        dialog.setHeaderText("Enter second plug letter");

        String secondLetter = plugDialog(dialog);

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
