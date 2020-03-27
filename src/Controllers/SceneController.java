package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

public class SceneController {

    static String activeProjectName;
    static String activeProductName;
    static Double activeProductCost;
    static int activeProductId;

    public static void openView(Scene scene, Class getClass, String projectName, String fileName) {
        activeProjectName = projectName;
        try {
            scene.setRoot(FXMLLoader.load(getClass.getResource("../Views/" + fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openView(Scene scene, Class getClass, String projectName, String productName, String fileName) {
        activeProjectName = projectName;
        activeProductName = productName;
        try {
            scene.setRoot(FXMLLoader.load(getClass.getResource("../Views/" + fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openView(Scene scene, Class getClass, String projectName, String productName, int productId, Double productCost, String fileName) {
        activeProjectName = projectName;
        activeProductName = productName;
        activeProductCost = productCost;
        activeProductId = productId;
        try {
            scene.setRoot(FXMLLoader.load(getClass.getResource("../Views/" + fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
