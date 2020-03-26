package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../Views/costingView.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.show();
        primaryStage.setFullScreen(true);

        /*
        fuzzyLogicView eh = new fuzzyLogicView();
        eh.initUI(primaryStage);
         */

        /*
        FuzzyLogic eh = new FuzzyLogic();
        eh.test();

         */
    }


    public static void main(String[] args) {
        launch(args);
    }
}
