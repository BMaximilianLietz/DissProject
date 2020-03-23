package Controllers;

import Models.FuzzyLogic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    public ComboBox preProcessingCB;
    public ComboBox itemImitabilityCB;
    public ComboBox pricingStrategyCB;
    public ComboBox targetCB;
    public ComboBox priceClusteringCB;
    public ComboBox itemQualityCB;
    public ComboBox marketSaturationCB;
    public ComboBox isMarketSegmentedCB;
    public ComboBox degreePriceCompetitionCB;
    public FuzzyLogic priceSettingFLM;
    public FuzzyLogic commoditizationFLM;
    public Label commoditizationValueForm;

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

        priceSettingFLM = new FuzzyLogic();
        priceSettingFLM.init("pricing");

        commoditizationFLM = new FuzzyLogic();
        commoditizationFLM.init("commoditizationOutput");

        /*
        longTermPricingFLM = new FuzzyLogic();
        commoditizationFLM.init("priceDevelopment");
         */
    }

    public void handleButtonClick(javafx.event.ActionEvent actionEvent) {
        System.out.println("OMG this actually works");
    }

    public void comboBoxItemChange(ActionEvent actionEvent) {
//        System.out.println(comboBox1.getSelectionModel().getSelectedItem().toString() + " " + actionEvent.getSource());
        String element = ((Control)actionEvent.getSource()).getId();
        element = element.substring(0, element.length()-2);
        String value = ((ComboBox)actionEvent.getSource()).getSelectionModel().getSelectedItem().toString().replaceAll("\\s+", "");;
        int selectedIndex = ((ComboBox)actionEvent.getSource()).getSelectionModel().getSelectedIndex();

        System.out.println(element + " " + value + " with index: " + selectedIndex);
        priceSettingFLM.functionBlockSetVariable(element, (double) selectedIndex);
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

    public void priceSettingSubmitBtnClick(ActionEvent actionEvent) {
        // TODO implement check that every combobox/input field has a value
        System.out.println(commoditizationValueForm.getText());
        if (commoditizationValueForm.getText().equals("Please fill out form")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please get the commoditization value first");
            alert.initOwner(((Node)actionEvent.getTarget()).getScene().getWindow());
            alert.showAndWait();
            return;
        } else {
            double commValue = Double.parseDouble(commoditizationValueForm.getText());
            priceSettingFLM.functionBlockSetVariable("commoditization", (double) commValue);

            priceSettingFLM.evaluate();
            priceSettingFLM.getChartFunctionBlock();

            System.out.println(priceSettingFLM.getFunctionBlock());
            System.out.println("Price: " + priceSettingFLM.getFunctionBlock().getVariable("price").getValue());
            try {

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FML");
                alert.setHeaderText("FML");
                alert.setContentText(e.toString());
                alert.initOwner(((Node)actionEvent.getTarget()).getScene().getWindow());
                alert.showAndWait();
                return;
            }
        }
        //priceSettingFLM.evaluate();
    }

    public void commoditizationButtonClick(ActionEvent actionEvent) {
        String numberCustomersTFBuffer = numberCustomersTF.getId().substring(0, numberCustomersTF.getId().length()-2);
        int numberCustomersTFIndex = Integer.parseInt(numberCustomersTF.getText());
        commoditizationFLM.functionBlockSetVariable(numberCustomersTFBuffer, (double) numberCustomersTFIndex);

        String preProcessingCBBuffer = preProcessingCB.getId().substring(0, preProcessingCB.getId().length()-2);
        int preProcessingCBIndex = preProcessingCB.getSelectionModel().getSelectedIndex();
        commoditizationFLM.functionBlockSetVariable(preProcessingCBBuffer, (double) preProcessingCBIndex);

        String itemImitabilityCBuffer = itemImitabilityCB.getId().substring(0, itemImitabilityCB.getId().length()-2);
        int itemImitabilityCBIndex = itemImitabilityCB.getSelectionModel().getSelectedIndex();
        commoditizationFLM.functionBlockSetVariable(itemImitabilityCBuffer, (double) itemImitabilityCBIndex);

        String degreePriceCompetitionCBBuffer = degreePriceCompetitionCB.getId().substring(0, degreePriceCompetitionCB.getId().length()-2);
        int degreePriceCompetitionCBIndex = degreePriceCompetitionCB.getSelectionModel().getSelectedIndex();
        commoditizationFLM.functionBlockSetVariable(degreePriceCompetitionCBBuffer, (double) degreePriceCompetitionCBIndex);

        commoditizationFLM.evaluate();

        //commoditizationFLM.getChartFunctionBlock();
        commoditizationFLM.getChartVariable("commoditizationOutputValue");

        //System.out.println(commoditizationFLM.getFunctionBlock());
        commoditizationValueForm.setText(String.valueOf(commoditizationFLM.getFunctionBlock().getVariable("commoditizationOutputValue").getValue()));
    }
}
