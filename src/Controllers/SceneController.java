package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

public class SceneController {

    static String activeProjectName;
    static String activeProductName;
    static Double activeProductCost;
    static int activeProductId;

    public static void openView(Scene scene, Class getClass, String fileName) {
        setRoot(scene, getClass, fileName);
    }

    public static void openView(Scene scene, Class getClass, String projectName, String fileName) {
        activeProjectName = projectName;
        setRoot(scene, getClass, fileName);
    }

    public static void openView(Scene scene, Class getClass, String projectName, String productName, String fileName) {
        activeProjectName = projectName;
        activeProductName = productName;
        setRoot(scene, getClass, fileName);
    }

    public static void openView(Scene scene, Class getClass, String projectName, String productName, int productId, Double productCost, String fileName) {
        activeProjectName = projectName;
        activeProductName = productName;
        activeProductCost = productCost;
        activeProductId = productId;
        setRoot(scene, getClass, fileName);
    }

    private static void setRoot(Scene scene, Class getClass, String fileName) {
        try {
            scene.setRoot(FXMLLoader.load(getClass.getResource("../Views/" + fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
