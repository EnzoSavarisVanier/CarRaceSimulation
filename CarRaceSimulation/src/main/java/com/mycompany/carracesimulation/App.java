package com.mycompany.carracesimulation;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        CarRaceModel model = new CarRaceModel();
        CarRaceView view = new CarRaceView(primaryStage);
        CarRaceController controller = new CarRaceController(model, view);
        controller.startSlideshow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
