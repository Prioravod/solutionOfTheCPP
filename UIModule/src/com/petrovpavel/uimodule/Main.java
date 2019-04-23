package com.petrovpavel.uimodule;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        URL fxmlURL = getClass().getResource("/mainwindow.fxml");
        Parent root = FXMLLoader.load(fxmlURL);
        primaryStage.setTitle("Find way");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.getIcons().add(new Image("/main.png"));
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
