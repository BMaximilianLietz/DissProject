package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.util.ArrayList;

public class SceneController {

    static ArrayList<Object> activeProject;
    static ArrayList<Object> activeProduct;

    public static void openView(Scene scene, Class getClass, String fileName) {
        setRoot(scene, getClass, fileName);
    }

    public static void openView(Scene scene, Class getClass, ArrayList<Object> project, String fileName) {
        activeProject = project;
        setRoot(scene, getClass, fileName);
    }

    public static void openView(Scene scene, Class getClass, ArrayList<Object> project, ArrayList<Object> product, String fileName) {
        activeProject = project;
        activeProduct = product;
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
