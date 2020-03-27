package Controllers;

import Data.ProductPricingConnector;
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
    public ComboBox brandValueCB;
    public ComboBox distributionChannelCB;
    public ComboBox priceElasticityCB;
    public FuzzyLogic priceSettingFLM;
    public FuzzyLogic commoditizationFLM;
    public Label commoditizationValueForm;

    public Label projectName;
    public Label productName;
    public Label productCost;
    private Double activeProductCost;
    private int activeProductId;

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

        activeProductCost = SceneController.activeProductCost;
        activeProductId = SceneController.activeProductId;

        projectName.setText(SceneController.activeProjectName);
        productName.setText(SceneController.activeProductName);
        productCost.setText(String.valueOf(activeProductCost));
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
            ProductPricingConnector.insertAllProductPricing(activeProductId,
                    java.lang.String.valueOf(pricingStrategyCB.getSelectionModel().getSelectedItem()),
                    Double.parseDouble(desiredMarginTF.getText()),
                    java.lang.String.valueOf(targetCB.getSelectionModel().getSelectedItem()),
                    java.lang.String.valueOf(priceClusteringCB.getSelectionModel().getSelectedItem()),
                    java.lang.String.valueOf(itemQualityCB.getSelectionModel().getSelectedItem()),
                    java.lang.String.valueOf(marketSaturationCB.getSelectionModel().getSelectedItem()),
                    java.lang.String.valueOf(isMarketSegmentedCB.getSelectionModel().getSelectedItem()),
                    java.lang.String.valueOf(brandValueCB.getSelectionModel().getSelectedItem()),
                    java.lang.String.valueOf(distributionChannelCB.getSelectionModel().getSelectedItem()),
                    java.lang.String.valueOf(priceElasticityCB.getSelectionModel().getSelectedItem()),
                    Integer.parseInt(numberCustomersTF.getText()),
                    java.lang.String.valueOf(preProcessingCB.getSelectionModel().getSelectedItem()),
                    java.lang.String.valueOf(itemImitabilityCB.getSelectionModel().getSelectedIndex()),
                    java.lang.String.valueOf(degreePriceCompetitionCB.getSelectionModel().getSelectedItem()));

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

    public void openProjectViewMenuItemClick(ActionEvent actionEvent) {
    }

    public void openProductViewMenuItemClick(ActionEvent actionEvent) {
    }
}
