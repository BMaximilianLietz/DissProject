package Controllers;

import Data.CostingConnector;
import Data.ProductConnector;
import Data.ProjectConnector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.border.Border;
import java.awt.*;
import java.sql.Date;
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

    //Running Costs
    public GridPane gridPaneRunningCosts;
    public Label storyPointsTotalRepeatLabel;
    public Label storyPointsTotalRepeatPoints;
    public TextField userStoryNameRepeatTF;
    public TextField storyPointsRepeatTF;
    public CheckBox isVariableCH;
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
    private ArrayList<ArrayList<Object>> runningCosts = new ArrayList<>();
    private int runningCostsTotal = 0;

    private ArrayList<Object> activeProduct;
    private ArrayList<Object> activeProject;
    private Boolean isUpdate = false;

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

//        activeProduct = SceneController.activeProduct;
        activeProduct = ProductConnector.getProductByProductId((Integer)SceneController.activeProduct.get(0));
        activeProject = SceneController.activeProject;
//        activeProject = ProjectConnector.getProjectByProjectName()

        ArrayList<ArrayList<Object>> userStoryQueryResults =
                CostingConnector.getUserStoriesByProduct((Integer)activeProduct.get(0));
        ArrayList<ArrayList<Object>> equipmentQueryResults =
                CostingConnector.getEquipmentByProduct((Integer)activeProduct.get(0));
        ArrayList<ArrayList<Object>> getCostingQueryRsults =
                CostingConnector.getCostingByProduct((Integer)activeProduct.get(0));

        //TODO update for each and everyone one of the above required...

        if (userStoryQueryResults.size() > 0) {

            isUpdate = true;

            System.out.println(userStoryQueryResults);
            for (int i = 0; i < userStoryQueryResults.size(); i++) {
//            addUserStory(gridPaneLeft.getScene(), );
                if (!(Boolean) userStoryQueryResults.get(i).get(5)) {
                    addUserStory(gridPaneLeft.getScene(),
                            (Integer) userStoryQueryResults.get(i).get(2),
                            (String) userStoryQueryResults.get(i).get(3),
                            (String) userStoryQueryResults.get(i).get(1));
                } else {
                    addRunningCost((Integer) userStoryQueryResults.get(i).get(2),
                            (String) userStoryQueryResults.get(i).get(1));
                    //runningCostsTotal += (Integer)userStoryQueryResults.get(i).get(2);
                    System.out.println("product running costs " + (Integer)userStoryQueryResults.get(i).get(2));
                    System.out.println("total " + runningCostsTotal);
                }
            }
        }
        if (equipmentQueryResults.size() > 0) {
            for (int i = 0; i < equipmentQueryResults.size(); i++) {
//            addEquipmentBtnClick();
//            Scene scene, String equipmentName, Double equipmentPrice, Integer equipmentQuantity
                addEquipment(gridPaneLeft.getScene(),
                        (String) equipmentQueryResults.get(i).get(1),
                        (Double) equipmentQueryResults.get(i).get(3),
                        (Integer) equipmentQueryResults.get(i).get(2));
            }
            // Do costing manually, only two fields anyway
            FTEEquivalent.setText(getCostingQueryRsults.get(0).get(1).toString());
            labourCostsWorkingHour.setText(getCostingQueryRsults.get(0).get(2).toString());
        }

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
        addEquipment(gridPaneLeft.getScene(),
                equipmentNameTextField.getText(),
                Double.parseDouble(equipmentPriceTextField.getText()),
                Integer.parseInt(equipmentQuantityTextField.getText()));

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
        CostingConnector.insertIntoProductCosting((Integer)activeProduct.get(0), check1, check2);
        if (storyList.size() > 0) {
            CostingConnector.insertIntoUserStories(storyList);
        }
        if (equipmentList.size() > 0) {
            CostingConnector.insertIntoEquipment((Integer)activeProduct.get(0), equipmentList);
        }
        ProductConnector.updateProductById((Integer)activeProduct.get(0),
                (Integer)activeProduct.get(1),
                (String) activeProduct.get(2),
                (String)activeProduct.get(3),
                (Date) activeProduct.get(4),
                (Double) activeProduct.get(5),
                (Double) activeProduct.get(6),
                (String) activeProduct.get(7),
                (Boolean) activeProduct.get(8),
                (Boolean) activeProduct.get(9),
                Double.parseDouble(storyPointsTotalRepeatPoints.getText()));
        //ProductConnector.insertIntoProduct(1, projectName.getText(), temp);
    }

    public void openProjectViewMenuItemClick(ActionEvent actionEvent) {
        SceneController.openView(menuBar.getScene(), getClass(), "projectView.fxml");
    }

    public void openProductViewMenuItemClick(ActionEvent actionEvent) {
        SceneController.openView(menuBar.getScene(), getClass(), activeProject, "productView.fxml");
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
                text.wrappingWidthProperty().bind(scrollPane.widthProperty());

                int rowIndex = gridPaneIterations.getRowCount() + 1;
                scrollPane.contentProperty().setValue(text);
                gridPaneIterations.add(iterationLabel, 0, rowIndex);
                gridPaneIterations.add(scrollPane, 1, rowIndex);

            } catch (Exception eh) {
                System.out.println("Something in the convoluted try catch statement went wrong");
            }
        }

        Label iterationLbl = new Label(iteration);
        Label storyNameLbl = new Label(storyName);
        Label storyPointsLbl = new Label(storyPoints.toString());

        int rowIndex = GridPane.getRowIndex(storyPointsTotalLabel)+1;
        GridPane.setRowIndex(storyPointsTotalLabel, rowIndex);
        GridPane.setRowIndex(storyPointsTotalPoints, rowIndex);

        int rowIndex2 = gridPaneUserStories.getRowCount()-2;
        gridPaneUserStories.add(storyNameLbl, 0, rowIndex2);
        gridPaneUserStories.add(storyPointsLbl, 1,rowIndex2);

        int totalPoints = Integer.parseInt(storyPointsTotalPoints.getText())+actualPoints;
        storyPointsTotal = totalPoints;
        ArrayList<Object> bufferList = new ArrayList<Object>();
        bufferList.add(userStoryNameTextField.getText());
        bufferList.add(actualPoints);
        bufferList.add(iterationTextField.getText());
        bufferList.add((Integer)activeProduct.get(0));
        bufferList.add((false));
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

    public void addRunningCostsBtnClick(ActionEvent actionEvent) {
        addRunningCost(Integer.parseInt(storyPointsRepeatTF.getText()),
                userStoryNameRepeatTF.getText());

        ArrayList<ArrayList<Object>> bufferBufferList = new ArrayList<>();
        ArrayList<Object> bufferList = new ArrayList<Object>();
        bufferList.add(userStoryNameRepeatTF.getText());
        bufferList.add(Integer.parseInt(storyPointsRepeatTF.getText()));
        bufferList.add("");
        bufferList.add((Integer)activeProduct.get(0));
        bufferList.add((true));
        System.out.println(bufferList);
        bufferBufferList.add(bufferList);
        CostingConnector.insertIntoUserStories(bufferBufferList);
    }

    public void addRunningCost(Integer storyPoints, String storyName) {

        Label storyNameLbl = new Label(storyName);
        Label storyPointsLbl = new Label(storyPoints.toString());

        int rowIndex2 = GridPane.getRowIndex(storyPointsTotalRepeatLabel) + 1;
        GridPane.setRowIndex(storyPointsTotalRepeatLabel, rowIndex2);
        GridPane.setRowIndex(storyPointsTotalRepeatPoints, rowIndex2);

        int rowIndex = gridPaneRunningCosts.getRowCount() - 2;
        gridPaneRunningCosts.add(storyNameLbl, 0, rowIndex);
        gridPaneRunningCosts.add(storyPointsLbl, 1,rowIndex);

        runningCostsTotal += storyPoints;
        storyPointsTotalRepeatPoints.setText(String.valueOf(runningCostsTotal));

        ProductConnector.updateProductById((Integer)activeProduct.get(0),
                (Integer)activeProduct.get(1),
                (String) activeProduct.get(2),
                (String)activeProduct.get(3),
                (Date) activeProduct.get(4),
                (Double) activeProduct.get(5),
                (Double) activeProduct.get(6),
                (String) activeProduct.get(7),
                (Boolean) activeProduct.get(8),
                (Boolean) activeProduct.get(9),
                Double.parseDouble(storyPointsTotalRepeatPoints.getText()));
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
