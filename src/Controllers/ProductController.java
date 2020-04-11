package Controllers;

import Data.*;
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
import java.time.LocalDate;
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
        if (competitorInit.size() > 0) {
            for (int i = 0; i < init.size(); i++) {
                addCompetitor((String) competitorInit.get(i).get(1),
                        (Double) competitorInit.get(i).get(2),
                        competitorGridPane);
            }
        }
    }

    public void addProductBtnClick(ActionEvent actionEvent) {

        ArrayList<Object> queryResults = ProductConnector.insertIntoProduct((Integer) activeProject.get(0),
                productNameTF.getText(), productDescriptionTF.getText(), Date.valueOf(LocalDate.now()), 0.0,
                0.0, productVersionTF.getText(), Boolean.valueOf(null), Boolean.valueOf(null), 0.0);

        ProductPricingConnector.insertAllProductPricing((Integer) queryResults.get(0), "", 0.0,
                "", "", "", "", "","",
                "", "",0, "", "", "",
                0.0, 0.0, 0.0, 0.0, 0);

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

        ArrayList<Object> productCopy = new ArrayList<>(product);

        Button productPricingButton = new Button("Pricing");
        productPricingButton.onActionProperty().setValue(actionEvent1 -> {
            Scene scene = ((Node)actionEvent1.getTarget()).getScene();
            SceneController.openView(scene, getClass(), activeProject, productCopy,"priceSetting.fxml");
        });
        Button productCostingButton = new Button("Costing");
        productCostingButton.onActionProperty().setValue(actionEvent1 -> {
            Scene scene = ((Node)actionEvent1.getTarget()).getScene();
            SceneController.openView(scene, getClass(), activeProject, productCopy, "costingView.fxml");
        });

        int rowIndex = gridPaneChosen.getRowCount()+1;

        HBox hbox = new HBox(productPricingButton, productCostingButton);
        gridPaneChosen.add(productNameLb, 0, rowIndex);
        gridPaneChosen.add(productDescriptionLb, 1, rowIndex);
        gridPaneChosen.add(productPriceLb, 2, rowIndex);
        gridPaneChosen.add(productCostsLb,3, rowIndex);
        gridPaneChosen.add(productVersionLb, 4, rowIndex);
        gridPaneChosen.add(hbox, 5, rowIndex);

        if ((((String)activeProject.get(4)).equals("Bait & Hook"))||
                (((String)activeProject.get(4)).equals("Multi-platform"))) {

            CheckBox subsidizing = new CheckBox();
            CheckBox subsidized = new CheckBox();

            Label subsidizingTitleLbl = new Label("Subsidizing/Subsidized");
            if (((Boolean)product.get(8))) {
                subsidized.selectedProperty().setValue(true);
            } else {

            }
            if (((Boolean)product.get(9))) {
                subsidizing.selectedProperty().setValue(true);
            }

            subsidizing.onActionProperty().setValue(actionEvent -> {
                subsidized.selectedProperty().setValue(false);

                ProductConnector.updateProductById((Integer) productCopy.get(0),
                        productCopy.get(2).toString(),
                        productCopy.get(3).toString(),
                        Date.valueOf(LocalDate.now()),
                        (Double) productCopy.get(5),
                        (Double) productCopy.get(6),
                        (String) productCopy.get(7),
                        (Integer)productCopy.get(1),
                        false, true,
                        (Double) productCopy.get(10));
                SubsidyConnector.insertIntoSubsidies((Integer) productCopy.get(0), null,
                        (Integer) activeProject.get(0));
            });

            subsidized.onActionProperty().setValue(actionEvent -> {
                subsidizing.selectedProperty().setValue(false);

                ProductConnector.updateProductById((Integer) productCopy.get(0),
                        productCopy.get(2).toString(),
                        productCopy.get(3).toString(),
                        Date.valueOf(LocalDate.now()),
                        (Double) productCopy.get(5),
                        (Double) productCopy.get(6),
                        (String) productCopy.get(7),
                        (Integer)productCopy.get(1),
                        true, false,
                        (Double) productCopy.get(10));

                ArrayList<ArrayList<Object>> check =
                        SubsidyConnector.getSubsidizerByProjectId((Integer) activeProject.get(0));
                if (check.size() > 0 ) {
                    for (int i = 0; i < check.size(); i++) {
                        if ((check.get(i).get(1) == null)||(Integer.parseInt(check.get(i).get(1).toString()))==0) {
                            SubsidyConnector.updateSubsidizerByProjectId((Integer)productCopy.get(0),
                                    (Integer) activeProject.get(0));
                        } else {
                            Integer subsidizingId = (Integer)check.get(i).get(1);
                            SubsidyConnector.insertIntoSubsidies(subsidizingId,
                                    (Integer) productCopy.get(0), (Integer) activeProject.get(0));
                        }
                    }
                }
            });
            SceneController.activeProduct = ProductConnector.getProductByProjectAndProduct((Integer)activeProject.get(0),
                    (String)product.get(2));

            HBox subsidyHBox = new HBox(subsidizing, subsidized);
            subsidyHBox.spacingProperty().setValue(15);

            gridPaneChosen.add(subsidizingTitleLbl, 6, 2);
            gridPaneChosen.add(subsidyHBox, 6, rowIndex);
        }
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
