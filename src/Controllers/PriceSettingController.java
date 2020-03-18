package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class PriceSettingController {

    @FXML
    public ComboBox comboBox1;
    public Button btn1;
    public GridPane gridPane;
    public TextField numberCustomersTF;
    public TextField valueAddedTF;
    public TextField desiredMarginTF;
    public TextField depreciationTF;
    public ComboBox prePocessingCB;
    public ComboBox itemImitabilityCB;
    public ComboBox pricingStrategyCB;
    public ComboBox targetCB;
    public ComboBox priceClusteringCB;
    public ComboBox itemQualityCB;
    public ComboBox marketSaturationCB;
    public ComboBox isMarketSegmentedCB;

    public void initialize() {
        numberCustomersTF.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
        });
        valueAddedTF.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
        });
        desiredMarginTF.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
        });
        depreciationTF.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
        });

    }

    public void handleButtonClick(javafx.event.ActionEvent actionEvent) {
        System.out.println("OMG this actually works");
    }

    public void comboBoxItemChange(ActionEvent actionEvent) {
//        System.out.println(comboBox1.getSelectionModel().getSelectedItem().toString() + " " + actionEvent.getSource());
        String element = ((Control)actionEvent.getSource()).getId();
        element = element.substring(0, element.length()-2);
        String value = ((ComboBox)actionEvent.getSource()).getSelectionModel().getSelectedItem().toString().replaceAll("\\s+", "");;
        System.out.println(element + " " + value);

        /*
        if ((element.equals("comboBox5"))&&(value.equals("High"))) {
            System.out.println("if statement is true");
            Label lbl = new Label("Row 6");
            lbl.setPadding(new Insets(0,0,0,40));
            ComboBox myComboBox = new ComboBox();
            myComboBox.getItems().addAll(
                    "Very Low",
                    "Low",
                    "Medium",
                    "High",
                    "Very High"
            );

//            Button button6 = new Button("Button 6");
            gridPane.add(lbl, 0,6);
            gridPane.add(myComboBox,1,6);
        }

         */
    }
}
