package com.mycompany.carracesimulation;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
    Group[] cars = new Group[4];
    TranslateTransition[] moves = new TranslateTransition[4];
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

    // draws the track, the cars and the buttons
    void showRace() {
        player.stop();
        
        Pane track = new Pane();
        track.setPrefSize(800, 340);
        track.setStyle("-fx-background-color: lightgreen;");

        Rectangle road = new Rectangle(0, 40, 800, 280);
        road.setFill(Color.GRAY);
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
            cars[i] = makeCar(i + 1, colors[i]);
            cars[i].setLayoutX(10);
            cars[i].setLayoutY(49 + i * 70);
            track.getChildren().add(cars[i]);

            // moves the car from the start line to the finish line with random speed
            moves[i] = new TranslateTransition(Duration.seconds(randomTime()), cars[i]);
            moves[i].setToX(610);
            moves[i].setInterpolator(Interpolator.LINEAR);
        }

        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button resetButton = new Button("Reset");
        startButton.setOnAction(e -> startRace());
        pauseButton.setOnAction(e -> pauseRace());
        resetButton.setOnAction(e -> resetRace());

        // slider changes how fast the cars go
        Slider speedSlider = new Slider(0.5, 3, 1);
        speedSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            for (TranslateTransition move : moves) {
                move.setRate(newValue.doubleValue());
            }
        });

        HBox controls = new HBox(10, startButton, pauseButton, resetButton, new Label("Speed:"), speedSlider);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(10));

        BorderPane racePane = new BorderPane();
        racePane.setCenter(track);
        racePane.setBottom(controls);

        stage.setScene(new Scene(racePane));
        stage.sizeToScene();
    }
    //actions for buttons
    void startRace() {
        for (TranslateTransition move : moves) {
            move.play();    
        }
    }

    void pauseRace() {
        for (TranslateTransition move : moves) {
            move.pause();
        }
    }

    void resetRace() {
        for (int i = 0; i < 4; i++) {
            moves[i].stop();
            cars[i].setTranslateX(0);
            moves[i].setDuration(Duration.seconds(randomTime()));   // new random time for the next race
        }
    }

    // random time in seconds
    double randomTime() {
        return 4 + Math.random() * 4;
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