package com.connectfour.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloController implements Initializable {
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static final int DIAMETER = 80;
    private static final String discColor1 = "#24303E";
    private static final String discColor2 = "#4CAA88";

    private static String PLAYER_ONE = " Player One ";
    private static String PLAYER_TWO = " Player Two ";
    @FXML
    public TextField playerTwoTextField;
    @FXML
    public TextField playerOneTextField;
    @FXML
    public Button setNamesButton;

    private boolean isPlayerOneTurn = true;
    private Disc[][] insertedDiscArray = new Disc[ROWS][COLUMNS];// For Developers

    @FXML
    public GridPane rootGridPane;
    @FXML
    public Pane insertedDiscPane;
    @FXML
    public Label playerNameLabel;

    private boolean isAllowedToInsert=true;
    private boolean  isEmpty = true;

    public void createPlayground() {
        Platform.runLater( () -> setNamesButton.requestFocus());

        Shape rectangleWithHoles = createGameStructureGrid();
        rootGridPane.add(rectangleWithHoles, 0, 1);

        List<Rectangle> rectangleList = createClickableColumns();

        for (Rectangle rectangle : rectangleList) {
            rootGridPane.add(rectangle, 0, 1);
        }
            setNamesButton.setOnAction(actionEvent -> {
                PLAYER_ONE = playerOneTextField.getText();
                PLAYER_TWO = playerTwoTextField.getText();
                playerNameLabel.setText(isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO);
            });

    }


    private Shape createGameStructureGrid() {
        Shape rectangleWithHoles = new Rectangle((COLUMNS + 1) * DIAMETER, (ROWS + 1) * DIAMETER);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {

                Circle circle = new Circle();
                circle.setRadius(DIAMETER / 2);
                circle.setCenterX(DIAMETER / 2);
                circle.setCenterY(DIAMETER / 2);
                circle.setSmooth(true);

                circle.setTranslateX(col * (DIAMETER + 5) + DIAMETER / 4);
                circle.setTranslateY(row * (DIAMETER + 5) + DIAMETER / 4);
                rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
            }
        }
        rectangleWithHoles.setFill(Color.WHITE);
        return rectangleWithHoles;
    }

    private List<Rectangle> createClickableColumns() {

        List<Rectangle> rectangleList = new ArrayList<>();

        for (int col = 0; col < COLUMNS; col++) {
            Rectangle rectangle = new Rectangle(DIAMETER, (ROWS + 1) * DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col * (DIAMETER + 5) + DIAMETER / 4);

            rectangle.setOnMouseEntered(mouseEvent -> {
                rectangle.setFill(Color.valueOf("eeeeee26"));
            });
            rectangle.setOnMouseExited(mouseEvent -> rectangle.setFill(Color.TRANSPARENT));

            final int column = col;
            rectangle.setOnMouseClicked(mouseEvent -> {
                if (isAllowedToInsert){
                    isAllowedToInsert=false;
                    insertDisc(new Disc(isPlayerOneTurn), column);
                }
            });
            rectangleList.add(rectangle);

        }
        return rectangleList;
    }

    private void insertDisc(Disc disc, int column) {

        int row = ROWS - 1;
        while (row >= 0) {
            if (getDiscIfPresent(row, column) == null)
                break;
            row--;
        }
        if (row < 0)
            return;

        insertedDiscArray[row][column] = disc;
        insertedDiscPane.getChildren().add(disc);
        disc.setTranslateX(column * (DIAMETER + 5) + DIAMETER / 4);

        int currentRow = row;
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.25), disc);
        translateTransition.setToY(row * (DIAMETER + 5) + DIAMETER / 4);
        translateTransition.setOnFinished(actionEvent -> {
            isAllowedToInsert = true;
            if (gameEnded(currentRow, column)) {
                gameOver();
                return;
            }
            isPlayerOneTurn = !isPlayerOneTurn;
            playerNameLabel.setText(isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO);
        });
        translateTransition.play();
    }

    private boolean gameEnded(int row, int column) {
        List<Point2D> verticalPoints = IntStream.rangeClosed(row - 3, row + 3)  // If, row = 3, column = 3, then row = 0,1,2,3,4,5,6
                .mapToObj(r -> new Point2D(r, column))  // 0,3  1,3  2,3  3,3  4,3  5,3  6,3 [ Just an example for better understanding ]
                .collect(Collectors.toList());

        List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(col -> new Point2D(row, col))
                .collect(Collectors.toList());

        Point2D startPoint1 = new Point2D(row - 3, column + 3);
        List<Point2D> diagonal1Points = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startPoint1.add(i, -i))
                .collect(Collectors.toList());

        Point2D startPoint2 = new Point2D(row - 3, column - 3);
        List<Point2D> diagonal2Points = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startPoint2.add(i, i))
                .collect(Collectors.toList());

        boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
                || checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);

        return isEnded;
    }

    private boolean checkCombinations(List<Point2D> Points) {

        int chain = 0;

        for (Point2D point : Points) {

            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();

            Disc disc = getDiscIfPresent(rowIndexForArray, columnIndexForArray);

            if (disc != null && disc.isPlayerOneMove == isPlayerOneTurn) {
                chain++;
                if (chain == 4) {
                    return true;
                }
            } else {
                chain = 0;
            }
        }
        return false;
}

    private Disc getDiscIfPresent(int row,int column){
        if (row >= ROWS || row <0 || column>=COLUMNS || column<0){
            return  null;
        }
        return insertedDiscArray  [row][column];
}
    private void gameOver() {
        String winner = isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO;
        Alert alert  = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four ");
        alert.setHeaderText("Congratulation !! "+winner +" You Won this match ");
        alert.setContentText("DO You Want To Play It again ? ");

        ButtonType yesBtn = new ButtonType("Yes");
        ButtonType noBtn = new ButtonType("No,Exit");
        alert.getButtonTypes().setAll(yesBtn,noBtn);

        Platform.runLater(() ->{
            Optional<ButtonType> btnClicked = alert.showAndWait();
            if (btnClicked.isPresent() && btnClicked.get()==yesBtn ){
                resetGame();
            }
            else {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void resetGame() {
        insertedDiscPane.getChildren().clear();
        for (int row=0 ; row<insertedDiscArray.length;row++){
            Arrays.fill(insertedDiscArray[row], null);
        }
//        for (int row=0 ; row<insertedDiscArray.length;row++){
//            for (int column =0; column< insertedDiscArray[row].length;column++){
//                insertedDiscArray [row] [column] = null;
//            }
//        }
        isPlayerOneTurn=true;
        playerNameLabel.setText(PLAYER_ONE);

        createPlayground();
    }

    private static class Disc extends Circle{
        private final boolean isPlayerOneMove;

        public Disc(boolean isPlayerOneMove){
            this.isPlayerOneMove = isPlayerOneMove;
            setRadius(DIAMETER/2);

            setFill(isPlayerOneMove ? Color.valueOf(discColor1) : Color.valueOf(discColor2));
            setCenterX(DIAMETER/2);
            setCenterY(DIAMETER/2);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}