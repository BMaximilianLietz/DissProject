package Controllers;

import Data.ProductConnector;
import Data.ProductPricingConnector;
import Models.FuzzyLogic;
import javafx.beans.binding.ObjectExpression;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class PriceSettingController {

    @FXML
    public ComboBox comboBox1;
    public Button btn1;
    public GridPane gridPane;
    public TextField numberCustomersTF;
    public TextField valueAddedTF;
    public TextField desiredMarginTF;
    public TextField desiredMarkupTF;
    public TextField allowedVarianceTF;
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
    public Label proposedPricePerCustomer;
    private ArrayList<Object> activeProject;
    private ArrayList<Object> activeProduct;

    public void initialize() {
        activeProject = SceneController.activeProject;
        activeProduct = SceneController.activeProduct;

        priceSettingFLM = new FuzzyLogic();
        priceSettingFLM.init("pricing");

        commoditizationFLM = new FuzzyLogic();
        commoditizationFLM.init("commoditizationOutput");

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

        projectName.setText((String)SceneController.activeProject.get(1));
        productName.setText((String)activeProduct.get(2));
        productCost.setText(((Double)activeProduct.get(5)).toString());

        Object[] nodeArray = {pricingStrategyCB, desiredMarginTF, targetCB, priceClusteringCB, itemQualityCB,
                marketSaturationCB, isMarketSegmentedCB, brandValueCB, distributionChannelCB, priceElasticityCB,
                numberCustomersTF, preProcessingCB, itemImitabilityCB, degreePriceCompetitionCB, desiredMarkupTF,
                allowedVarianceTF};

        System.out.println("Id: " + activeProduct.get(0));

        ArrayList<Object> queryResults =
                ProductPricingConnector.getAllProductPricingByProductId((Integer)activeProduct.get(0));
//        System.out.println(queryResults);

        for (int i = 0; i < nodeArray.length; i++) {
            //System.out.println(i);
            String queryItem = String.valueOf(queryResults.get(i+1));
//            System.out.println(queryItem);
            if (nodeArray[i].getClass().getName().equals("javafx.scene.control.ComboBox")) {
                ((ComboBox)nodeArray[i]).getSelectionModel().select(queryItem);
                if ((i<10)||(i>13)) {
                    System.out.println(queryItem);
                    addVariableInit((ComboBox)nodeArray[i]);
                }
            } else {
                ((TextField)nodeArray[i]).setText(queryItem);
            }
        }
    }

    public void handleButtonClick(javafx.event.ActionEvent actionEvent) {
        System.out.println("OMG this actually works");
    }

    public void comboBoxItemChange(ActionEvent actionEvent) {
//        System.out.println(comboBox1.getSelectionModel().getSelectedItem().toString() + " " + actionEvent.getSource());
        addVariableInit((Control) actionEvent.getSource());

        //priceSettingFLM.getChartVariable(element);
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
            /*
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
                    java.lang.String.valueOf(degreePriceCompetitionCB.getSelectionModel().getSelectedItem()),
                    Double.parseDouble(desiredMarkupTF.getText()),
                    Double.parseDouble(allowedVarianceTF.getText()));

             */

            double commValue = Double.parseDouble(commoditizationValueForm.getText());
            priceSettingFLM.functionBlockSetVariable("commoditization", (double) commValue);

            priceSettingFLM.evaluate();

            System.out.println("Price: " + priceSettingFLM.getFunctionBlock().getVariable("price").getValue());
            proposedPricePerCustomer.setText(String.valueOf(priceSettingFLM.getFunctionBlock().getVariable("price").getValue()));

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
//        commoditizationFLM.getChartVariable("commoditizationOutputValue");

        //System.out.println(commoditizationFLM.getFunctionBlock());
        commoditizationValueForm.setText(String.valueOf(commoditizationFLM.getFunctionBlock().getVariable("commoditizationOutputValue").getValue()));
    }

    public void openProjectViewMenuItemClick(ActionEvent actionEvent) {
        Scene scene = ((Node)actionEvent.getTarget()).getScene();
        SceneController.openView(scene, getClass(), "productView.fxml");
    }

    public void openProductViewMenuItemClick(ActionEvent actionEvent) {
        Scene scene = ((Node)actionEvent.getTarget()).getScene();
        SceneController.openView(scene, getClass(), "productView.fxml");
    }

    public void calculatePriceButtonClick(ActionEvent actionEvent) {

        Double allowedVariance = Double.valueOf(allowedVarianceTF.getText())/100;
        Double cost = Double.valueOf(productCost.getText())/Double.valueOf(numberCustomersTF.getText());
        Double margin = Double.valueOf(desiredMarginTF.getText())/100;
        Double appliedMargin = cost * (1+margin);
        Double markup = Double.valueOf(desiredMarkupTF.getText())/100;
        Double appliedMarkup = cost * (1+markup);

        String priceLow = "(" + (cost*((1-margin)*(1-allowedVariance))) + ", 0) (" + (cost*(1-margin)) + ", 1) (" +
                (cost*(1+margin*(1-allowedVariance))) + ", 0)";
        FuzzyLogic.replacePriceTerm(1, priceLow);
        String priceMed = "(" + (cost * ((1 + margin)*(1-allowedVariance))) + ", 0) (" + cost * (1+margin) + ", 1) (" +
                cost * ((1+margin)*(allowedVariance+1)) + ", 0)";
        FuzzyLogic.replacePriceTerm(2, priceMed);
        String priceHigh = "(" + (cost * (1+markup*(1-allowedVariance))) + ", 0) (" + (cost * (1+markup)) + ", 1) (" +
                (cost * ((1+markup)*(1+allowedVariance))) + ", 0)";
        FuzzyLogic.replacePriceTerm(3, priceHigh);
    }

    public void addVariableInit(Control source) {
        String element = source.getId();
        element = element.substring(0, element.length()-2);
        //String value = ((ComboBox)actionEvent.getSource()).getSelectionModel().getSelectedItem().toString().replaceAll("\\s+", "");;
        int selectedIndex = ((ComboBox)source).getSelectionModel().getSelectedIndex();

        System.out.println("element " + element + " and " + selectedIndex);
        priceSettingFLM.functionBlockSetVariable(element, (double) selectedIndex);
    }
}
