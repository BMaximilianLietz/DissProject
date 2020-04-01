package Controllers;

import Data.CostingConnector;
import Data.ProductConnector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CostingController {

    public ScrollPane scrollPaneIterations;
    public Label projectedEmployeeCostLB;
    public TextField labourCostsWorkingHour;
    public TextField FTEEquivalent;
    public TextField equipmentNameTextField;
    public TextField equipmentPriceTextField;
    public TextField equipmentQuantityTextField;
    public Label equipmentTotalCostInfoLabel;
    public Label equipmentTotalCostLabel;
    public GridPane gridPaneHardware;
    public TextField projectName;
    public MenuBar menuBar;
    List<List<String>> iterationList;

    @FXML
    public BorderPane borderPane;
    public GridPane gridPaneLeft;
    public TextField iterationTextField;
    public TextField userStoryNameTextField;
    public TextField storyPointsTextField;
    public GridPane gridPaneUserStories;
    public Label storyPointsTotalLabel;
    public Label storyPointsTotalPoints;
    public GridPane gridPaneIterations;
    public Text iterationsListLabel;
    public Label iterationsTotalPoints;

    private int combinedCosts;
    private double fTEEquivalent;
    private double labourCostsWH;
    private double totalEmployeeCosts;
    private int storyPointsTotal;
    private double totalEquipmentCosts = 0;
    private ArrayList<ArrayList<Object>> equipmentList = new ArrayList<>();
    private ArrayList<ArrayList<Object>> storyList = new ArrayList<>();

    private ArrayList<Object> activeProduct;
    private ArrayList<Object> activeProject;

    public void initialize() {
        FTEEquivalent.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    fTEEquivalent = Double.parseDouble(FTEEquivalent.getText());
                }
            }
        });

        ArrayList<Object> userStoryQueryResults =
                CostingConnector.getUserStoriesByProduct((Integer)activeProduct.get(0));
        ArrayList<Object> equipmentQueryResults =
                CostingConnector.getEquipmentByProduct((Integer)activeProduct.get(0));
        ArrayList<Object> getCostingQueryRsults =
                CostingConnector.getCostingByProduct((Integer)activeProduct.get(0));

        for (int i = 0; i < userStoryQueryResults.size(); i++) {
//            addUserStory(gridPaneLeft.getScene(), );
            /*
            addUserStory(gridPaneLeft.getScene(),
            Integer.parseInt(storyPointsTextField.getText()),
            iterationTextField.getText(),
            userStoryNameTextField.getText());

             */
        }
        for (int i = 0; i < userStoryQueryResults.size(); i++) {
//            addEquipmentBtnClick();
        }
        // Do costing manually, only two fields anyway


    }

    public void addUserStoryBtnClick(ActionEvent actionEvent) {
        addUserStory(((Node)actionEvent.getTarget()).getScene(), Integer.parseInt(storyPointsTextField.getText()),
                iterationTextField.getText(), userStoryNameTextField.getText());
    }

    public void calculateEmployeeCosts(ActionEvent actionEvent) {
        try {
            totalEquipmentCosts = storyPointsTotal * Double.parseDouble(FTEEquivalent.getText()) * Double.parseDouble(labourCostsWorkingHour.getText());
            projectedEmployeeCostLB.setText(String.valueOf(totalEquipmentCosts) + "\u00a3");


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.toString());
            alert.initOwner(((Node)actionEvent.getTarget()).getScene().getWindow());
            alert.showAndWait();
            return;
        }
    }

    public void addEquipmentBtnClick(ActionEvent actionEvent) {
        System.out.println(equipmentNameTextField.getText());
        System.out.println(equipmentPriceTextField.getText());
        System.out.println(equipmentQuantityTextField.getText());

//        int addedRow = gridPaneLeft.getRowCount()+1;
        //int addedRowUserStories = gridPaneUserStories.getRowCount();
    }

    public void costingViewSaveButtonClick(ActionEvent actionEvent) {
        Double check1 = -1.0;
        Double check2 = -1.0;
        try {
            check1 = Double.parseDouble(FTEEquivalent.getText());
            check2 = Double.parseDouble(labourCostsWorkingHour.getText());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.initOwner(((Node)actionEvent.getTarget()).getScene().getWindow());
            alert.showAndWait();
            return;
        }
        double temp = totalEquipmentCosts + totalEmployeeCosts;
//        CostingConnector.insertIntoProductCosting(n, check1, check2);
//        CostingConnector.insertIntoUserStories(storyList);
//        CostingConnector.insertIntoEquipment(n, equipmentList);
        //ProductConnector.insertIntoProduct(1, projectName.getText(), temp);
    }

    public void openProjectViewMenuItemClick(ActionEvent actionEvent) {
        SceneController.openView(menuBar.getScene(), getClass(), "projectView.fxml");
    }

    public void openProductViewMenuItemClick(ActionEvent actionEvent) {
        SceneController.openView(menuBar.getScene(), getClass(), activeProject, "projectView.fxml");
    }

    public void addUserStory(Scene scene, Integer storyPoints, String iteration, String storyName){
//        ((Node)actionEvent.getTarget()).getScene()
        int actualPoints = storyPoints;

        Label iterationLabel;
        Text preExistingLbl;

        try {
            iterationLabel = (Label) scene.lookup("#" + iteration);
            preExistingLbl = (Text) scene.lookup("#list" + iteration);
            preExistingLbl.setText(preExistingLbl.getText() + ", " + storyName);
        } catch (Exception e) {
            System.out.println(e.getCause() + " " + e.getStackTrace());
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.prefWidth(250.0);
            scrollPane.pannableProperty().setValue(true);
            scrollPane.fitToWidthProperty().setValue(true);
            scrollPane.fitToHeightProperty().setValue(true);
            scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

            try {
                iterationLabel = new Label(iteration);
                iterationLabel.setId(iteration);

                Text text = new Text();
                text.idProperty().setValue("list"+iteration);
                text.setText(storyName);
                System.out.println("id: " + text.getId());
                text.wrappingWidthProperty().bind(scrollPane.widthProperty());

                scrollPane.contentProperty().setValue(text);
                gridPaneIterations.add(iterationLabel, 0, gridPaneIterations.getRowCount() + 1);
                gridPaneIterations.add(scrollPane, 1, gridPaneIterations.getRowCount());

            } catch (Exception eh) {
                System.out.println("Something in the convoluted try catch statement went wrong");
            }
        }

        Label iterationLbl = new Label(iteration);
        Label storyNameLbl = new Label(storyName);
        Label storyPointsLbl = new Label(storyPoints.toString());

        System.out.println(GridPane.getRowIndex(storyPointsTotalLabel));
        GridPane.setRowIndex(storyPointsTotalLabel, GridPane.getRowIndex(storyPointsTotalLabel)+1);
        GridPane.setRowIndex(storyPointsTotalPoints, GridPane.getRowIndex(storyPointsTotalPoints)+1);
        System.out.println(gridPaneUserStories.getRowCount());

        int rowIndex = gridPaneUserStories.getRowCount()-2;
        gridPaneUserStories.add(storyNameLbl, 0, rowIndex);
        gridPaneUserStories.add(storyPointsLbl, 1,rowIndex);

        int totalPoints = Integer.parseInt(storyPointsTotalPoints.getText())+actualPoints;
        storyPointsTotal = totalPoints;
        ArrayList<Object> bufferList = new ArrayList<Object>();
        bufferList.add(userStoryNameTextField.getText());
        bufferList.add(actualPoints);
        bufferList.add(iterationTextField.getText());
        storyList.add(bufferList);
        storyPointsTotalPoints.setText(String.valueOf(totalPoints));
    }

    public void addEquipment(Scene scene, String equipmentName, Double equipmentPrice, Integer equipmentQuantity) {

        totalEquipmentCosts += equipmentPrice * equipmentQuantity;

        Label equipmentNameLabel = new Label(equipmentName);
        Label equipmentPriceLabel = new Label(equipmentPrice.toString());
        Label equipmentQuantityLabel = new Label(equipmentQuantity.toString());
        Label equipmentWholePrice = new Label (String.valueOf(equipmentPrice*equipmentQuantity));

        int rowIndex = GridPane.getRowIndex(equipmentTotalCostInfoLabel)+1;
        GridPane.setRowIndex(equipmentTotalCostInfoLabel, rowIndex);
        GridPane.setRowIndex(equipmentTotalCostLabel, rowIndex);
//        System.out.println(gridPaneHardware.getRowCount());

        int rowIndex2 = gridPaneHardware.getRowCount()-2;
        gridPaneHardware.add(equipmentNameLabel, 0, rowIndex2);
        gridPaneHardware.add(equipmentWholePrice, 1,rowIndex2);
        gridPaneHardware.add(equipmentPriceLabel, 2,rowIndex2);
        gridPaneHardware.add(equipmentQuantityLabel, 3,rowIndex2);

        equipmentTotalCostLabel.setText(String.valueOf(totalEquipmentCosts) + "\u00a3");
        ArrayList<Object> bufferList = new ArrayList<Object>();
        bufferList.add(equipmentName);
        bufferList.add(equipmentQuantity.toString());
        bufferList.add(equipmentPrice.toString());
        equipmentList.add(bufferList);
    }

    /*
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("The Input for Story Points must be an integer");
            alert.initOwner(scene.getWindow());
            alert.showAndWait();
            return;
             */
}
