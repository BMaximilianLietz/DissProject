package Controllers;

import Data.ProductConnector;
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

import java.util.ArrayList;

public class ProductController {
    public Label projectName;
    public MenuBar menuBar;
    public GridPane gridPaneLeft;
    public TextField productNameTF;
    public TextField productDescriptionTF;
    public TextField productCostsTF;

    public String activeProject;
    public TextField productVersionTF;

    public void initialize() {
        activeProject = SceneController.activeProjectName;
        projectName.setText(activeProject);
        ArrayList<Object> init = ProductConnector.getAllByProduct();
        addProduct(
                String.valueOf(init.get(2)),
                String.valueOf(init.get(3)),
                Double.parseDouble(String.valueOf(init.get(5))),
                Double.parseDouble(String.valueOf(init.get(6))),
                String.valueOf(init.get(7)));
    }

    public void openProjectViewMenuItemClick(ActionEvent actionEvent) {
        SceneController.openView(menuBar.getScene(), getClass(), "", "projectView.fxml");
    }

    public void addProductBtnClick(ActionEvent actionEvent) {
        addProduct(productNameTF.getText(), productDescriptionTF.getText(), null, null, productVersionTF.getText());
    }

    public void addProduct(String productName, String productDescription, Double productPrice,
                           Double productCosts, String productVersion) {
        Label productNameLb = new Label(productName);
        Label productDescriptionLb = new Label(productDescription);
        Label productVersionLb = new Label(productVersion);
        Label productCostsLb;
        Label productPriceLb;

        if (productPrice == 0.0) { productPriceLb = new Label("Product Pricing Required"); }
        else { productPriceLb = new Label(String.valueOf(productPrice)); }

        if (productCosts == 0.0) { productCostsLb = new Label("Product Costing Required"); }
        else { productCostsLb = new Label(String.valueOf(productCosts)); }

        Button productPricingButton = new Button(productName + " pricing");
        productPricingButton.onActionProperty().setValue(actionEvent1 -> {
            Scene scene = ((Node)actionEvent1.getTarget()).getScene();
            // TODO Change hard-coded product ID
            SceneController.openView(scene, getClass(), "<Project TODO>", productName, 1, productPrice, "priceSetting.fxml");
        });
        Button productCostingButton = new Button(productName + " costing");
        productCostingButton.onActionProperty().setValue(actionEvent1 -> {
            Scene scene = ((Node)actionEvent1.getTarget()).getScene();
            SceneController.openView(scene, getClass(), "<Project TODO>", productName, "costingView.fxml");
        });

        int rowIndex = gridPaneLeft.getRowCount()+1;

        gridPaneLeft.add(productNameLb, 0, rowIndex);
        gridPaneLeft.add(productDescriptionLb, 1, rowIndex);
        gridPaneLeft.add(productPriceLb, 2, rowIndex);
        gridPaneLeft.add(productCostsLb,3, rowIndex);
        gridPaneLeft.add(productVersionLb, 4, rowIndex);
        gridPaneLeft.add(productPricingButton, 5, rowIndex);
        gridPaneLeft.add(productCostingButton, 6, rowIndex);
    }
}
