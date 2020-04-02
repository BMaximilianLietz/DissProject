package Controllers;

import Data.ProductConnector;
import Data.ProjectConnector;
import Misc.HelperMethods;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.ArrayList;

public class ProductController {
    public Label projectName;
    public MenuBar menuBar;
    public GridPane gridPaneLeft;
    public TextField productNameTF;
    public TextField productDescriptionTF;
    public TextField productCostsTF;

    public String activeProjectName;
    public ArrayList<Object> activeProject;
    public TextField productVersionTF;

    public void initialize() {
        activeProject = SceneController.activeProject;
        projectName.setText(activeProject.get(1).toString());
        int projectId = (int) activeProject.get(0);
        if (activeProject.size() != 0) {
            ArrayList<ArrayList<Object>> init = ProductConnector.getAllByProjectId(projectId);
            if (init.size() != 0) {
                for (int i = 0; i < init.size(); i++) {
                    addProduct(init.get(i), gridPaneLeft);
                }
            } else {
                System.out.println("Product init list empty");
            }
        }
    }

    public void addProductBtnClick(ActionEvent actionEvent) {

        ArrayList<Object> queryResults = ProductConnector.insertIntoProduct((Integer) activeProject.get(0),
                productNameTF.getText(), productDescriptionTF.getText(), null, null,
                null, productVersionTF.getText());

        addProduct(queryResults, gridPaneLeft);
    }

    public void addProduct(ArrayList<Object> product, GridPane gridPaneChosen) {
        System.out.println("Product: " + product);
        System.out.println(product);
        Label productNameLb = new Label((String)product.get(2));
        Label productDescriptionLb = new Label((String)product.get(3));
        Label productVersionLb = new Label((String)product.get(7));
        Label productCostsLb;
        Label productPriceLb;

        try {
            productPriceLb = new Label(String.valueOf(((Double)product.get(6))));
        } catch (Exception e) {
            productPriceLb = new Label("Product Pricing Required");
            System.out.println(e.getMessage() + " " + e.getStackTrace());
        }

        try {
            productCostsLb = new Label(String.valueOf(((Double)product.get(5))));
        } catch (Exception e) {
            productCostsLb = new Label("Product Costing Required");
            System.out.println(e.getMessage() + " " + e.getStackTrace());
        }

        Button productPricingButton = new Button(((String)product.get(2)) + " pricing");
        productPricingButton.onActionProperty().setValue(actionEvent1 -> {
            Scene scene = ((Node)actionEvent1.getTarget()).getScene();
            // TODO Change hard-coded product ID
            //ProductConnector.getProductByProjectAndProduct(projectId, productName);
            SceneController.openView(scene, getClass(), activeProject, product,"priceSetting.fxml");
        });
        Button productCostingButton = new Button(((String)product.get(2)) + " costing");
        productCostingButton.onActionProperty().setValue(actionEvent1 -> {
            Scene scene = ((Node)actionEvent1.getTarget()).getScene();
            SceneController.openView(scene, getClass(), activeProject, product, "costingView.fxml");
        });

        int rowIndex = gridPaneChosen.getRowCount()+1;

        gridPaneChosen.add(productNameLb, 0, rowIndex);
        gridPaneChosen.add(productDescriptionLb, 1, rowIndex);
        gridPaneChosen.add(productPriceLb, 2, rowIndex);
        gridPaneChosen.add(productCostsLb,3, rowIndex);
        gridPaneChosen.add(productVersionLb, 4, rowIndex);
        gridPaneChosen.add(productPricingButton, 5, rowIndex);
        gridPaneChosen.add(productCostingButton, 6, rowIndex);
    }

    // Navigation

    public void openProjectViewMenuItemClick(ActionEvent actionEvent) {
        SceneController.openView(menuBar.getScene(), getClass(), activeProject, "projectView.fxml");
    }
}
