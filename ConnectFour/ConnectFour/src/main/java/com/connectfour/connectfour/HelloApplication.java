package com.connectfour.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private HelloController controller;
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game.fxml"));
        GridPane rootGridPane = fxmlLoader.load();
        controller= fxmlLoader.getController();
        controller.createPlayground();

        MenuBar menuBar = createMenu();
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);
        Scene scene = new Scene(rootGridPane);
        stage.setTitle("Connect Four ");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public  MenuBar createMenu(){
        Menu fileMenu = new Menu("File");
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(actionEvent -> {
            controller.resetGame();
        });
        MenuItem resetGame = new MenuItem("Reset Game");
        resetGame.setOnAction(actionEvent -> {
            controller.resetGame();
        });
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem exitGame = new MenuItem("Exit Game ");
        exitGame.setOnAction(actionEvent -> {
            exitGame();
        });
        fileMenu.getItems().addAll(newGame,resetGame,separatorMenuItem,exitGame);

        Menu helpMenu  = new Menu("Help");
        MenuItem aboutGame = new MenuItem("About Connect4");
        aboutGame.setOnAction(actionEvent -> {
            aboutConnect4();
        });
        SeparatorMenuItem separatorMenuItem1 = new SeparatorMenuItem();
        MenuItem aboutMe = new MenuItem("About Me");
        aboutMe.setOnAction(actionEvent -> {
            aboutDeveloper();
        });
        helpMenu.getItems().addAll(aboutGame,separatorMenuItem1,aboutMe);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,helpMenu);
        return menuBar;
    }

    private void aboutDeveloper() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the Developer");
        alert.setHeaderText("Radhe Ravi");
        alert.setContentText("I am 3rd Year Cse Student from Chandigarh university." +
                "I developed this programme by the help of Coursera " +
                             "Hope you enjoy this Game ! " +
                             "Thank You ");
        alert.show();
    }

    private void aboutConnect4() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect4");
        alert.setHeaderText("How to Play ?");
        alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs\n" +
                "from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space\n" +
                "within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs.\n" +
                "Connect Four is a solved game. The first player can always win by playing the right moves.");
        alert.show();
    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }


    public static void main(String[] args) {
        launch();
    }
}