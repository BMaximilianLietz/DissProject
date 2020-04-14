package Misc;

import Controllers.SceneController;
import Data.CompetitorConnector;
import Data.ProductConnector;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class HelperMethods {
    public static void throwAlert(Scene scene, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.initOwner(scene.getWindow());
        alert.showAndWait();
        return;
    }
}
