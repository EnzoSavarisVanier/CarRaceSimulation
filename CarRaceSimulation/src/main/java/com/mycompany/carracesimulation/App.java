package com.mycompany.carracesimulation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {
    Color[] colors = {Color.RED, Color.DODGERBLUE, Color.LIMEGREEN, Color.GOLD};
    int currentCar = 0;
    Stage stage;
    MediaPlayer player;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        ImageView view = new ImageView(new Image(App.class.getResource("/Car1.png").toString()));
        VBox root = new VBox(10, view);

        // what happens every time the timer goes off
        EventHandler<ActionEvent> nextCar = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentCar = currentCar + 1;
                if (currentCar < 4) {   // after the last car there is nothing more to show
                    String path = "/Car" + (currentCar + 1) + ".png";
                    view.setImage(new Image(App.class.getResource(path).toString()));
                }
            }
        };

        // runs 4 times, the 4th time the slideshow is over
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), nextCar));
        timeline.setCycleCount(4);
        timeline.setOnFinished(e -> showRace());
        timeline.play();

        player = new MediaPlayer(new Media(App.class.getResource("/sound.mp3").toString()));
        player.play();

        stage.setTitle("Car Race");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    // draws the track and the cars
    void showRace() {
        player.stop();

        Pane track = new Pane();
        track.setStyle("-fx-background-color: lightgreen;");

        Rectangle road = new Rectangle(0, 40, 800, 280);
        road.setFill(Color.DARKGRAY);
        track.getChildren().add(road);

        // lines between the lanes
        for (int i = 1; i < 4; i++) {
            Line divider = new Line(0, 40 + i * 70, 800, 40 + i * 70);
            divider.setStroke(Color.WHITE);
            divider.getStrokeDashArray().addAll(15.0, 10.0);
            track.getChildren().add(divider);
        }

        Line startLine = new Line(110, 40, 110, 320);
        startLine.setStroke(Color.WHITE);
        startLine.setStrokeWidth(4);

        Line finishLine = new Line(720, 40, 720, 320);
        finishLine.setStroke(Color.BLACK);
        finishLine.setStrokeWidth(4);

        track.getChildren().addAll(startLine, finishLine);

        // one car per lane, front of the car on the start line
        for (int i = 0; i < 4; i++) {
            Group car = makeCar(i + 1, colors[i]);
            car.setLayoutX(10);
            car.setLayoutY(49 + i * 70);
            track.getChildren().add(car);
        }

        stage.setScene(new Scene(track, 800, 360));
        stage.sizeToScene();
    }

    // builds one car out of shapes
    Group makeCar(int number, Color color) {
        Polygon body = new Polygon(0, 42, 0, 28, 25, 4, 65, 4, 100, 28, 100, 42);
        body.setFill(color);

        Rectangle window1 = new Rectangle(28, 8, 18, 12);
        Rectangle window2 = new Rectangle(50, 8, 18, 12);
        window1.setFill(Color.LIGHTBLUE);
        window2.setFill(Color.LIGHTBLUE);

        Circle wheel1 = new Circle(22, 44, 8, Color.BLACK);
        Circle wheel2 = new Circle(78, 44, 8, Color.BLACK);

        // line in the wheel
        Line spoke1 = new Line(22, 44, 30, 44);
        Line spoke2 = new Line(78, 44, 86, 44);
        spoke1.setStroke(Color.WHITE);
        spoke2.setStroke(Color.WHITE);

        Text num = new Text(44, 38, "" + number);
        num.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        return new Group(body, window1, window2, wheel1, wheel2, spoke1, spoke2, num);
    }

    public static void main(String[] args) {
        launch(args);
    }
}