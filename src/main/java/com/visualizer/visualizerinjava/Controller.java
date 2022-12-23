package com.visualizer.visualizerinjava;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Controller {

    private int SPEED = 200;
    private final Color INITIALCOLOR = Color.HOTPINK;
    private final Color CHECKCOLOR = Color.LIME;

    @FXML
    GridPane mainGrid;

    @FXML
    Button pauseButton;

    @FXML
    TextField dataSizeTextField;

    @FXML
    Pane visualizerPane;

    @FXML
    Slider speedSlider;

    @FXML
    Button runButton;

    int padding = 5;
    ArrayList<Rectangle> data;
    ArrayList<Integer> values;

    SequentialTransition animation;



    @FXML
    public void sort(){

        SPEED /= (int) speedSlider.getValue() ;

        speedSlider.setDisable(true);
        dataSizeTextField.setDisable(true);
        runButton.setDisable(true);

        animation = new SequentialTransition();
        ArrayList<ParallelTransition> transitions = new ArrayList<>();

        for (int i = 0; i < values.size() - 1; i++){

            for (int j = 0; j < values.size() - i - 1; j++) {

                int value1 = values.get(j);
                int value2 = values.get(j + 1);

                ParallelTransition p1 = new ParallelTransition();
                FillTransition fillRect1 = new FillTransition(Duration.millis(SPEED), data.get(j), CHECKCOLOR, INITIALCOLOR);
                FillTransition fillRect2 = new FillTransition(Duration.millis(SPEED), data.get(j + 1), CHECKCOLOR, INITIALCOLOR);
                p1.getChildren().addAll(fillRect1, fillRect2);
                transitions.add(p1);


                // Swap!
                if (value1 > value2) {
                    ParallelTransition p = swap(j, j + 1);
                    transitions.add(p);
                }

//                FillTransition fillRect11 = new FillTransition(Duration.millis(200), data.get(j), Color.LIMEGREEN, Color.PINK);
//                FillTransition fillRect22 = new FillTransition(Duration.millis(200), data.get(j + 1), Color.LIMEGREEN, Color.PINK);
//                p1.getChildren().addAll(fillRect11, fillRect22);
            }

        }

        animation.setOnFinished(e -> {
            speedSlider.setDisable(false);
            dataSizeTextField.setDisable(false);
            runButton.setDisable(false);
        });

        animation.getChildren().addAll(transitions);
        animation.play();
    }

    @FXML
    public ParallelTransition swap(int j, int k){

        Rectangle rect1 = data.get(j);
        Rectangle rect2 = data.get(k);

        double offset = ((k - j) * (padding + rect1.getWidth()));

        ParallelTransition parallelTransition = new ParallelTransition();


        TranslateTransition translateRectangle1 = new TranslateTransition();
        translateRectangle1.setDuration(Duration.millis(SPEED));
        translateRectangle1.setNode(rect1);
        translateRectangle1.setByX(offset);


        TranslateTransition translateRectangle2 = new TranslateTransition();
        translateRectangle2.setDuration(Duration.millis(SPEED));
        translateRectangle2.setNode(rect2);
        translateRectangle2.setByX(-offset);

        parallelTransition.getChildren().addAll(translateRectangle1, translateRectangle2);

        Collections.swap(values, j, k);
        Collections.swap(data, j, k);

        return parallelTransition;
    }


    @FXML
    public void generateData(){

        data = new ArrayList<>();
        values = new ArrayList<>();

        visualizerPane.getChildren().clear();
        int size = Integer.parseInt(dataSizeTextField.getText());
        int rectWidth = (int) ((visualizerPane.getWidth() - (padding * (size - 1))) / size);

        int location = 2;

        for (int i = 0; i < size; i++){

            int rectHeight = getRandomHeight();

            Rectangle rectangle = new Rectangle();
            rectangle.setHeight(rectHeight);
            rectangle.setWidth(rectWidth);
            rectangle.setX(location);
            rectangle.setY(visualizerPane.getHeight() - rectHeight);
            rectangle.setFill(INITIALCOLOR);

            visualizerPane.getChildren().add(rectangle);

            data.add(rectangle);
            values.add(rectHeight);

            location += rectWidth + padding;
       }

    }

    public void pause(){
        if (animation.getStatus() == Animation.Status.PAUSED){
            return;
        }
        animation.pause();
        pauseButton.setText("Play");
        pauseButton.setOnAction(e -> {
            animation.play();
            pauseButton.setText("Pause");
            pauseButton.setOnAction(ee -> pause());
        });

    }

    public void stop(){
        visualizerPane.getChildren().clear();
        speedSlider.setDisable(false);
        dataSizeTextField.setDisable(false);
        runButton.setDisable(false);
        pauseButton.setText("Pause");

        SPEED = 200;
        generateData();
    }

    public int getRandomHeight(){
        Random random = new Random();
        return random.nextInt((int) visualizerPane.getHeight());
    }

}