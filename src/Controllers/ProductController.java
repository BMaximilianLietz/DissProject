package Controllers;

import Data.CompetitorConnector;
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
import javafx.scene.layout.HBox;
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
    public TextField competitorPriceTF;
    public TextField competitorNameTF;
    public GridPane competitorGridPane;

    public void initialize() {
        activeProject = SceneController.activeProject;
        projectName.setText(activeProject.get(1).toString());
        int projectId = (int) activeProject.get(0);
        ArrayList<ArrayList<Object>> init = ProductConnector.getAllByProjectId(projectId);

        if (init.size() != 0) {
            for (int i = 0; i < init.size(); i++) {
                addProduct(init.get(i), gridPaneLeft);
            }
        } else {
            System.out.println("Product init list empty");
        }
        ArrayList<ArrayList<Object>> competitorInit =
                CompetitorConnector.getCompetitorsByProjectId(projectId);
        System.out.println(competitorInit);
        if (competitorInit.size() > 0) {
            for (int i = 0; i < init.size(); i++) {
                System.out.println(competitorInit.get(i));
                addCompetitor((String) competitorInit.get(i).get(1),
                        (Double) competitorInit.get(i).get(2),
                        competitorGridPane);
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

        Button productPricingButton = new Button("Pricing");
        productPricingButton.onActionProperty().setValue(actionEvent1 -> {
            Scene scene = ((Node)actionEvent1.getTarget()).getScene();
            SceneController.openView(scene, getClass(), activeProject, product,"priceSetting.fxml");
        });
        Button productCostingButton = new Button("Costing");
        productCostingButton.onActionProperty().setValue(actionEvent1 -> {
            Scene scene = ((Node)actionEvent1.getTarget()).getScene();
            SceneController.openView(scene, getClass(), activeProject, product, "costingView.fxml");
        });

        CheckBox subsidizing = new CheckBox();
        CheckBox subsidized = new CheckBox();

        subsidizing.onActionProperty().setValue(actionEvent -> {
            System.out.println("Hellllooooooooo");
            subsidized.selectedProperty().setValue(false);
        });

        HBox subsidyHBox = new HBox(subsidizing, subsidized);
        subsidyHBox.spacingProperty().setValue(15);
        HBox hbox = new HBox(productPricingButton, productCostingButton);

        int rowIndex = gridPaneChosen.getRowCount()+1;

        gridPaneChosen.add(productNameLb, 0, rowIndex);
        gridPaneChosen.add(productDescriptionLb, 1, rowIndex);
        gridPaneChosen.add(productPriceLb, 2, rowIndex);
        gridPaneChosen.add(productCostsLb,3, rowIndex);
        gridPaneChosen.add(productVersionLb, 4, rowIndex);
        gridPaneChosen.add(subsidyHBox, 5, rowIndex);
        gridPaneChosen.add(hbox, 6, rowIndex);
    }

    // Navigation

    public void openProjectViewMenuItemClick(ActionEvent actionEvent) {
        SceneController.openView(menuBar.getScene(), getClass(), activeProject, "projectView.fxml");
    }

    public void addCompetitorBtnClick(ActionEvent actionEvent) {
        addCompetitor(competitorNameTF.getText(),
                Double.parseDouble(competitorPriceTF.getText()),
                competitorGridPane);
        CompetitorConnector.insertIntoCompetitors(competitorNameTF.getText(),
                Double.parseDouble(competitorPriceTF.getText()),
                (Integer) activeProject.get(0));
    }

    public void addCompetitor(String competitorName, Double competitorPrice, GridPane gridPaneChosen) {
        Label competitorNameLb = new Label(competitorName);
        Label competitorPriceLb = new Label(competitorPrice.toString());

        int rowIndex = gridPaneChosen.getRowCount()+1;

        gridPaneChosen.add(competitorNameLb, 0, rowIndex);
        gridPaneChosen.add(competitorPriceLb, 1, rowIndex);
    }
}
