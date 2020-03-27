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

    private String productName;
    private String productId;

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
    }

    public void addUserStoryBtnClick(ActionEvent actionEvent) {

        // TODO add points for iterations
        //<Label fx:id="iterationsTotalPoints" GridPane.columnIndex="2" GridPane.rowIndex="1">0</Label>

//        int addedRow = gridPaneLeft.getRowCount()+1;
        //int addedRowUserStories = gridPaneUserStories.getRowCount();
        int actualPoints;
        Scene scene = ((Node)actionEvent.getTarget()).getScene();
        try {
            actualPoints = Integer.parseInt(storyPointsTextField.getText());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("The Input for Story Points must be an integer");
            alert.initOwner(((Node)actionEvent.getTarget()).getScene().getWindow());
            alert.showAndWait();
            return;
        }

        Label iterationLabel;
        Text preExistingLbl;

        try {
            iterationLabel = (Label) scene.lookup("#" + iterationTextField.getText());
            preExistingLbl = (Text) scene.lookup("#list" + iterationTextField.getText());
            preExistingLbl.setText(preExistingLbl.getText() + ", " + userStoryNameTextField.getText());
        } catch (Exception e) {
            System.out.println(e.getCause() + " " + e.getStackTrace());
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.prefWidth(250.0);
            scrollPane.pannableProperty().setValue(true);
            scrollPane.fitToWidthProperty().setValue(true);
            scrollPane.fitToHeightProperty().setValue(true);
            scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

            try {
                iterationLabel = new Label(iterationTextField.getText());
                iterationLabel.setId(iterationTextField.getText());

                Text text = new Text();
                text.idProperty().setValue("list"+iterationTextField.getText());
                text.setText(userStoryNameTextField.getText());
                System.out.println("id: " + text.getId());
                text.wrappingWidthProperty().bind(scrollPane.widthProperty());

                scrollPane.contentProperty().setValue(text);
                gridPaneIterations.add(iterationLabel, 0, gridPaneIterations.getRowCount() + 1);
                gridPaneIterations.add(scrollPane, 1, gridPaneIterations.getRowCount());

            } catch (Exception eh) {
                System.out.println("Something in the convoluted try catch statement went wrong");
            }
        }

        /*
        iterationsListLabel.wrappingWidthProperty().bind(scrollPaneIterations.widthProperty());

        <ScrollPane fx:id="scrollPaneIterations" GridPane.columnIndex="1" GridPane.rowIndex="1"
                                prefWidth="250.0"
                                pannable="true" fitToWidth="true" fitToHeight="true" hbarPolicy="NEVER">
                        <content>
                            <Text fx:id="iterationsListLabel"></Text>
                        </content>
                    </ScrollPane>
         */
        Label iteration = new Label(iterationTextField.getText());
        Label storyName = new Label(userStoryNameTextField.getText());
        Label storyPoints = new Label(storyPointsTextField.getText());

        System.out.println(GridPane.getRowIndex(storyPointsTotalLabel));
        GridPane.setRowIndex(storyPointsTotalLabel, GridPane.getRowIndex(storyPointsTotalLabel)+1);
        GridPane.setRowIndex(storyPointsTotalPoints, GridPane.getRowIndex(storyPointsTotalPoints)+1);
        System.out.println(gridPaneUserStories.getRowCount());

        gridPaneUserStories.add(storyName, 0, gridPaneUserStories.getRowCount()-2);
        gridPaneUserStories.add(storyPoints, 1,gridPaneUserStories.getRowCount()-2);

        int totalPoints = Integer.parseInt(storyPointsTotalPoints.getText())+actualPoints;
        storyPointsTotal = totalPoints;
        ArrayList<Object> bufferList = new ArrayList<Object>();
        bufferList.add(userStoryNameTextField.getText());
        bufferList.add(actualPoints);
        bufferList.add(iterationTextField.getText());
        storyList.add(bufferList);
        storyPointsTotalPoints.setText(String.valueOf(totalPoints));
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
        double equipmentPrice;
        double equipmentQuantity;
        double equipmentCosts;
        try {
            equipmentPrice = Double.parseDouble(equipmentPriceTextField.getText());
            equipmentQuantity = Double.parseDouble(equipmentQuantityTextField.getText());
            totalEquipmentCosts += equipmentPrice * equipmentQuantity;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("The Input for Equipment Price/Quantity must be a decimal");
            alert.initOwner(((Node)actionEvent.getTarget()).getScene().getWindow());
            alert.showAndWait();
            return;
        }

        Label equipmentNameLabel = new Label(equipmentNameTextField.getText());
        Label equipmentPriceLabel = new Label(equipmentPriceTextField.getText());
        Label equipmentQuantityLabel = new Label(equipmentQuantityTextField.getText());
        Label equipmentWholePrice = new Label (String.valueOf(equipmentPrice*equipmentQuantity));

        GridPane.setRowIndex(equipmentTotalCostInfoLabel, GridPane.getRowIndex(equipmentTotalCostInfoLabel)+1);
        GridPane.setRowIndex(equipmentTotalCostLabel, GridPane.getRowIndex(equipmentTotalCostLabel)+1);
        System.out.println(gridPaneHardware.getRowCount());

        gridPaneHardware.add(equipmentNameLabel, 0, gridPaneHardware.getRowCount()-2);
        gridPaneHardware.add(equipmentWholePrice, 1,gridPaneHardware.getRowCount()-2);
        gridPaneHardware.add(equipmentPriceLabel, 2,gridPaneHardware.getRowCount()-2);
        gridPaneHardware.add(equipmentQuantityLabel, 3,gridPaneHardware.getRowCount()-2);

        equipmentTotalCostLabel.setText(String.valueOf(totalEquipmentCosts) + "\u00a3");
        ArrayList<Object> bufferList = new ArrayList<Object>();
        bufferList.add(equipmentNameTextField.getText());
        bufferList.add(equipmentQuantityTextField.getText());
        bufferList.add(equipmentPriceTextField.getText());
        equipmentList.add(bufferList);
    }

    public void costingViewSaveButtonClick(ActionEvent actionEvent) {
        Random rand = new Random();
        int m = rand.nextInt(100);
        Random random = new Random();
        int n = random.nextInt(1000);
        n = 1;
        productId = String.valueOf(n);
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
        CostingConnector.insertIntoProductCosting(n, check1, check2);
        CostingConnector.insertIntoUserStories(storyList);
        CostingConnector.insertIntoEquipment(n, equipmentList);
        ProductConnector.insertIntoProduct(1, projectName.getText(), temp);
    }
}
