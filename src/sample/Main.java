package sample;

import Controllers.ProjectController;
import Controllers.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import net.sourceforge.jFuzzyLogic.FIS;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL location = getClass().getResource("/Views/projectView.fxml");
        Parent root = FXMLLoader.load(location);
        primaryStage.setTitle("Pricing System");
        primaryStage.setScene(new Scene(root, 0, 0));
        primaryStage.setMaximized(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.show();
        SceneController.stage = primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
