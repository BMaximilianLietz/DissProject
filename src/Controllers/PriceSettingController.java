package Controllers;

import Data.CompetitorConnector;
import Data.ProductConnector;
import Data.ProductPricingConnector;
import Data.SubsidyConnector;
import Models.FuturePricing;
import Models.FuzzyLogic;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectExpression;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.Inet4Address;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
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
    public FuzzyLogic priceDevelopmentFLM;
    public FuzzyLogic commoditizationFLM;
    public Label commoditizationValueForm;

    public Label projectName;
    public Label productName;
    public Label productCost;
    public Label proposedPricePerCustomer;
    public MenuBar menuBar;
    public ComboBox timePeriodCB;

    // Price development fields
    public ComboBox pricingGoalCB;
    public TextField depreciationTF;
    public ComboBox customerExpectationsCB;
    public ComboBox customerExpectationImportanceCB;
    public ComboBox competitionRelatedPriceReductionCB;


    public GridPane gridPaneGraph;
    public GridPane gridPaneMeta;


    private ArrayList<Object> activeProject;
    private ArrayList<Object> activeProduct;
    private Double modifiedProductCost;

//    TODO remove or put somewhere else
    ArrayList<ArrayList<Object>> graphData = new ArrayList<>();

    Boolean isInsert = true;

    public void initialize() {
        // TODO Add repeating payments textfield/combobox
        // TODO implement preferred pricing strategy somehow
        // TODO add subscription
        activeProject = SceneController.activeProject;
        activeProduct = SceneController.activeProduct;

        modifiedProductCost  = (Double) activeProduct.get(5);

        priceSettingFLM = new FuzzyLogic();
        priceSettingFLM.init("pricing");

        commoditizationFLM = new FuzzyLogic();
        commoditizationFLM.init("commoditizationOutput");

        priceDevelopmentFLM = new FuzzyLogic();
        priceDevelopmentFLM.initPD("priceDevelopmentBlock");

        numberCustomersTF.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println(newValue);
        });
        valueAddedTF.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println(newValue);
        });
        desiredMarginTF.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println(newValue);
        });
        depreciationTF.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println(newValue);
        });

        Object[] nodeArray = {pricingStrategyCB, desiredMarginTF, targetCB, priceClusteringCB, itemQualityCB,
                marketSaturationCB, isMarketSegmentedCB, brandValueCB, distributionChannelCB, priceElasticityCB,
                numberCustomersTF, preProcessingCB, itemImitabilityCB, degreePriceCompetitionCB, desiredMarkupTF,
                allowedVarianceTF};

        ArrayList<Object> queryResults =
                ProductPricingConnector.getAllProductPricingByProductId(Integer.parseInt(activeProduct.get(0).toString()));

        if (activeProduct.size() != 0) {

            if (queryResults.size() > 0) {

                isInsert = false;

                for (int i = 0; i < nodeArray.length; i++) {
                    //System.out.println(i);
                    String queryItem = String.valueOf(queryResults.get(i+1));
                    if (nodeArray[i].getClass().getName().equals("javafx.scene.control.ComboBox")) {
                        ((ComboBox)nodeArray[i]).getSelectionModel().select(queryItem);
                        if ((i<10)||(i>13)) {
                            addVariableInit((ComboBox)nodeArray[i]);
                        }
                    } else {
                        ((TextField)nodeArray[i]).setText(queryItem);
                    }
                }
                priceClusteringCheck(priceClusteringCB);
            }
        }
        // TODO add combobox for when is subsidized
        // TODO add something for when it is subsidizing
        System.out.println(activeProduct);
        if ((Boolean)activeProduct.get(8)) {
            // TODO - make other change value depending on this?
            Label degree_of_subsidizationLbl = new Label("Degree of Subsidization in %");
            degree_of_subsidizationLbl.setPadding(new Insets(0,0,0,20));

            TextField subsidizationDegreesTF = new TextField();
            subsidizationDegreesTF.setId("subsidizationDegreesTF");

            moveGridpPaneChildrenDown(gridPane, 1, 2);

            gridPane.add(degree_of_subsidizationLbl, 2, 2);
            gridPane.add(subsidizationDegreesTF, 3,2);

        } else if ((Boolean)activeProduct.get(9)) {
            // TODO add running costs
            ArrayList<ArrayList<Integer>> subsidiesQueryResult =
                    SubsidyConnector.getSubsidizerByProductId((Integer) activeProduct.get(0));
            for (int i = 0; i < subsidiesQueryResult.size(); i++) {
                ArrayList<Object> subsidizedProduct = ProductConnector.getProductByProductId(
                        (Integer)subsidiesQueryResult.get(i).get(1)
                );
                Double subsidizedProductCost = (Double) subsidizedProduct.get(5);
                Integer subsidizationDegreeInt = (Integer) ProductPricingConnector.getAllProductPricingByProductId(
                        (Integer) subsidizedProduct.get(0))
                        .get(19);
                Double subsidizationDegree = Double.valueOf(subsidizationDegreeInt);
                if (subsidizationDegree != 0) {
                    subsidizationDegree /= 100;
                    Double addedCost = (subsidizationDegree) * subsidizedProductCost;
                    modifiedProductCost += addedCost;
                }
            }
        }

        if (activeProject.get(4).equals("Subscription")) {
            Label frequencyOfPayments = new Label("Frequency of payments");
            frequencyOfPayments.setPadding(new Insets(0,0,0,40));

            ComboBox paymentFrequencyCB = new ComboBox();
            paymentFrequencyCB.setId("paymentFrequencyCBId");
            paymentFrequencyCB.getItems().add("Daily");
            paymentFrequencyCB.getItems().add("Weekly");
            paymentFrequencyCB.getItems().add("Monthly");
            paymentFrequencyCB.getItems().add("Yearly");
            paymentFrequencyCB.getItems().add("Other");
            paymentFrequencyCB.setOnAction(e -> test());
//            subsidizationDegreesCB.setItems();
            moveGridpPaneChildrenDown(gridPane, 1, 2);
            moveGridpPaneChildrenDown(gridPane, 2, 2);

            Label subscriptionLengthLbl = new Label("Subscription length in months");
            subscriptionLengthLbl.setPadding(new Insets(0,0,0,40));
            TextField subscriptionLengthTF = new TextField();
            subscriptionLengthTF.setId("subscriptionLengthTF");

            gridPane.add(frequencyOfPayments, 2, 2);
            gridPane.add(paymentFrequencyCB, 3,2);
            gridPane.add(subscriptionLengthLbl, 2, 3);
            gridPane.add(subscriptionLengthTF, 3,3);
        }

        projectName.setText((String)SceneController.activeProject.get(1));
        productName.setText((String)activeProduct.get(2));
        productCost.setText((modifiedProductCost).toString());
    }

    public void handleButtonClick(javafx.event.ActionEvent actionEvent) {
        System.out.println("OMG this actually works");
    }

    public void comboBoxItemChange(ActionEvent actionEvent) {
//        System.out.println(comboBox1.getSelectionModel().getSelectedItem().toString() + " " + actionEvent.getSource());
        addVariableInit((Control) actionEvent.getSource());

        priceClusteringCheck((ComboBox) actionEvent.getSource());
    }

    public void priceSettingSubmitBtnClick(ActionEvent actionEvent)  {
        // TODO implement check that every combobox/input field has a value
//        System.out.println(commoditizationValueForm.getText());
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

            double priceRounded = priceSettingFLM.getFunctionBlock().getVariable("price").getValue();
            DecimalFormat df = new DecimalFormat("#.##");

            System.out.println("Price: " + priceSettingFLM.getFunctionBlock().getVariable("price").getValue());
            proposedPricePerCustomer.setText(df.format(priceRounded));

            Integer subsidizationDegrees = 0;
            try {
                TextField subsidizationDegreesTF = (TextField) gridPane.getScene().lookup("#subsidizationDegreesTF");
                subsidizationDegrees = Integer.parseInt(subsidizationDegreesTF.getText());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }

            if (isInsert) {
                // TODO remove hardcoded price ranges
                ProductPricingConnector.insertAllProductPricing((Integer)activeProduct.get(0),
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
                        Double.parseDouble(allowedVarianceTF.getText()),
                        0.0, 0.0,
                        subsidizationDegrees);

                activeProduct = ProductConnector.updateProductById((Integer) activeProduct.get(0),
                        activeProduct.get(2).toString(),
                        activeProduct.get(3).toString(),
                        Date.valueOf(LocalDate.now()),
                        (Double) activeProduct.get(5),
                        Double.parseDouble(proposedPricePerCustomer.getText()),
                        activeProduct.get(7).toString(),
                        (Integer)activeProduct.get(1),
                        (Boolean) activeProduct.get(8),
                        (Boolean) activeProduct.get(9),
                        (Double) activeProduct.get(10));
            } else {
                // TODO remove hardcoded price ranges
                ProductPricingConnector.updateProductPricing((Integer)activeProduct.get(0),
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
                        Double.parseDouble(allowedVarianceTF.getText()),
                        0.0, 0.0,
                        subsidizationDegrees);

//                System.out.println(activeProduct);
                activeProduct = ProductConnector.updateProductById((Integer) activeProduct.get(0),
                        activeProduct.get(2).toString(),
                        activeProduct.get(3).toString(),
                        Date.valueOf(LocalDate.now()),
                        Double.parseDouble(activeProduct.get(5).toString()),
                        Double.parseDouble(proposedPricePerCustomer.getText()),
                        activeProduct.get(7).toString(),
                        Integer.parseInt(activeProduct.get(1).toString()),
                        (Boolean) activeProduct.get(8),
                        (Boolean) activeProduct.get(9),
                        (Double) activeProduct.get(10));
//                System.out.println(activeProduct);
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

    public void calculatePriceButtonClick(ActionEvent actionEvent) {

        Double allowedVariance = Double.valueOf(allowedVarianceTF.getText())/100;
        Double cost = modifiedProductCost/Double.valueOf(numberCustomersTF.getText());
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

        priceSettingFLM.functionBlockSetVariable(element, (double) selectedIndex);
    }

    public void openProjectViewMenuItemClick(ActionEvent actionEvent) {
        SceneController.openView(menuBar.getScene(), getClass(), "projectView.fxml");
    }

    public void openProductViewMenuItemClick(ActionEvent actionEvent) {
        SceneController.openView(menuBar.getScene(), getClass(), activeProject, "productView.fxml");
    }

    public void moveGridpPaneChildrenDown(GridPane chosenGridPane, int row, int group) {
        for (Node node : chosenGridPane.getChildren()) {
            if ((chosenGridPane.getRowIndex(node) > row)&&
                    ((chosenGridPane.getColumnIndex(node)== group)||(chosenGridPane.getColumnIndex(node)==group+1))) {
                chosenGridPane.setRowIndex(node, chosenGridPane.getRowIndex(node)+1);
            }
        }
    }

    public void test() {
        System.out.println("hello");
    }

    public void testSubscription(ActionEvent actionEvent) {
        Scene scene = gridPane.getScene();
        ComboBox paymentFrequencyCB = (ComboBox) scene.lookup("#paymentFrequencyCBId");
        String paymentFrequencyText = paymentFrequencyCB.getSelectionModel().getSelectedItem().toString();
        TextField subscriptionLengthTF = (TextField) scene.lookup("#subscriptionLengthTF");
        Double subscriptionParse = Double.parseDouble(subscriptionLengthTF.getText());
        int numberCustomers = Integer.parseInt(numberCustomersTF.getText());
        int time = 1;

        // TODO add other options such as biweekly perhaps
        // TODO replace with switch/case?
        if (paymentFrequencyText.equals("Daily")) {
            time = 365;
        } else if (paymentFrequencyText.equals("Weekly")) {
            time = 53;
        } else if (paymentFrequencyText.equals("Monthly")) {
            time = 12;
        } else if (paymentFrequencyText.equals("Yearly")) {
            time = 1;
        } else if (paymentFrequencyText.equals("Other")){
            System.out.println("To do");
        }
        Double cost = (Double)modifiedProductCost;
        // TODO - probably change so that user can choose to earn money faster and not by the time subscription expires
        Double newCost = (cost/numberCustomers) / (time / subscriptionParse);
        System.out.println(newCost);
        //timePeriodCB
    }

    public void priceClusteringCheck(ComboBox comboBox) {
        if (comboBox.getId().equals("priceClusteringCB")) {
            if (priceClusteringCB.getSelectionModel().getSelectedItem().equals("Yes")) {
                Label clusteringRanges = new Label("Clustering Ranges:");
                clusteringRanges.setPadding(new Insets(0,0,0,40));

                TextField rangeLow = new TextField();
                rangeLow.setPromptText("Lower price range");

                TextField rangeHigh = new TextField();
                rangeHigh.setPromptText("Upper price Range");

                HBox hBox = new HBox(rangeLow, rangeHigh);
                int clusterRow = gridPane.getRowIndex(priceClusteringCB);
                moveGridpPaneChildrenDown(gridPane, clusterRow, 2);

                gridPane.add(clusteringRanges, 2, clusterRow+1);
                gridPane.add(hBox, 3,clusterRow+1);

            }
        }
    }

    public void priceComboBoxChange(ActionEvent actionEvent) {
        String element = ((ComboBox) actionEvent.getSource()).getId();
        element = element.substring(0, element.length()-2);
        //String value = ((ComboBox)actionEvent.getSource()).getSelectionModel().getSelectedItem().toString().replaceAll("\\s+", "");;
        int selectedIndex = ((ComboBox) actionEvent.getSource()).getSelectionModel().getSelectedIndex();

        if (element.equals("timePeriod")) {
            // TODO for loop for the time period
        } else {
            priceDevelopmentFLM.functionBlockSetVariable(element, (double) selectedIndex);
        }
    }


    public void priceDevelopmentBtnClick(ActionEvent actionEvent) {
        // TODO add price clusters
        String depreciationBuffer = depreciationTF.getId().substring(0, depreciationTF.getId().length()-2);
        int depreciationTFIndex = Integer.parseInt(depreciationTF.getText());
        priceDevelopmentFLM.functionBlockSetVariable(depreciationBuffer, (double) depreciationTFIndex);

        String pricingGoalCBBuffer = pricingGoalCB.getId().substring(0, pricingGoalCB.getId().length()-2);
        int pricingGoalCBIndex = pricingGoalCB.getSelectionModel().getSelectedIndex();
        priceDevelopmentFLM.functionBlockSetVariable(pricingGoalCBBuffer, (double) pricingGoalCBIndex);

        String customerExpectationsCBuffer = customerExpectationsCB.getId().substring(0, customerExpectationsCB.getId().length()-2);
        int customerExpectationsCBIndex = customerExpectationsCB.getSelectionModel().getSelectedIndex();
        priceDevelopmentFLM.functionBlockSetVariable(customerExpectationsCBuffer, (double) customerExpectationsCBIndex);

        String customerExpectationImportanceCBBuffer = customerExpectationImportanceCB.getId().substring(0, customerExpectationImportanceCB.getId().length()-2);
        int customerExpectationImportanceCBIndex = customerExpectationImportanceCB.getSelectionModel().getSelectedIndex();
        priceDevelopmentFLM.functionBlockSetVariable(customerExpectationImportanceCBBuffer, (double) customerExpectationImportanceCBIndex);

        String competitionRelatedPriceReductionCBBBuffer = competitionRelatedPriceReductionCB.getId().substring(0, competitionRelatedPriceReductionCB.getId().length()-2);
        int competitionRelatedPriceReductionCBIndex = competitionRelatedPriceReductionCB.getSelectionModel().getSelectedIndex();
        priceDevelopmentFLM.functionBlockSetVariable(competitionRelatedPriceReductionCBBBuffer, (double) competitionRelatedPriceReductionCBIndex);

        priceDevelopmentFLM.evaluate();

        graphData.clear();

        System.out.println(String.valueOf(priceDevelopmentFLM.getFunctionBlock().getVariable("priceDevelopment").getValue()));
        Double test = (Double) activeProduct.get(6);
        System.out.println("Final: " + test * priceDevelopmentFLM.getFunctionBlock().getVariable("priceDevelopment").getValue());

        Double tempRemove = test * 1;
        Double tempRemove2 = priceDevelopmentFLM.getFunctionBlock().getVariable("priceDevelopment").getValue();
        // TODO make it so that the loop re-evaluate the proposed price over and over instead of reusing the same result
        int timePeriodIndex = timePeriodCB.getSelectionModel().getSelectedIndex();
        ArrayList<Object> tempRemove3 = new ArrayList<>();
        tempRemove3.add(0);
        tempRemove3.add(tempRemove);
        graphData.add(tempRemove3);
        System.out.println("time period: " + timePeriodIndex);

        if (timePeriodIndex == 0) {

        } else if (timePeriodIndex == 1) {
//            Result is wrong
            Double tempRemove4 = tempRemove * Math.pow(tempRemove2, 1);
            Double percentage = tempRemove4/tempRemove;
            for (int i = 1; i < 7; i++) {
                ArrayList<Object> temp = new ArrayList<>();
                temp.add(i);
                tempRemove = tempRemove * (1-percentage/12);
                temp.add(tempRemove);
                graphData.add(temp);
            }
        } else if (timePeriodIndex == 2) {
            // Result is wrong
            Double tempRemove4 = tempRemove * Math.pow(tempRemove2, 1);
            Double percentage = tempRemove4/tempRemove;
            for (int i = 1; i < 13; i++) {
                ArrayList<Object> temp = new ArrayList<>();
                temp.add(i);
                tempRemove *= (1-percentage/12);
                temp.add(tempRemove);
                graphData.add(temp);
            }
        }
        else if (timePeriodIndex == 3) {
            Double tempRemove4 = tempRemove * Math.pow(tempRemove2, 3.0);
            Double percentage = tempRemove4/tempRemove;
            for (int i = 1; i < 37; i++) {
                ArrayList<Object> temp = new ArrayList<>();
                temp.add(i);
                tempRemove = tempRemove * (1-(percentage/36));
                temp.add(tempRemove);
                graphData.add(temp);
            }
        }

        FuturePricing pricing = new FuturePricing();
        pricing.lineChartInit("Price in Pounds",
                "Number of Months",
                "Proposed Price Development");
        pricing.setLineChartData("Lorem ipsum", graphData);
        System.out.println(graphData);
        System.out.println(pricing.getLineChart().getData());

        gridPaneGraph.add(pricing.getLineChart(), 0,0);
    }
}
