package com.mycompany.carracesimulation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {
    int currentCar = 0;

    @Override
    public void start(Stage stage) {
        ImageView view = new ImageView(load("Car1.png")); //loads first car
        VBox root = new VBox(10, view);
        // next car every 3 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            currentCar = (currentCar + 1) % 4; //if car count goes off bounds, back to 0
            view.setImage(load("Car" + (currentCar + 1) + ".png"));
        }));
        timeline.setCycleCount(3); //goes to other car three times
        timeline.play();
        //play sound
        MediaPlayer player = new MediaPlayer(new Media(getClass().getResource("/sound.mp3").toString()));
        player.play();

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    Image load(String file) { //for loading images
        return new Image(getClass().getResource("/" + file).toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}