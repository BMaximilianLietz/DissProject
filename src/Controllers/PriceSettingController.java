package Controllers;

import Data.CompetitorConnector;
import Data.ProductConnector;
import Data.ProductPricingConnector;
import Data.SubsidyConnector;
import Misc.HelperMethods;
import Models.FuzzyLogic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import net.sourceforge.jFuzzyLogic.rule.Rule;

import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

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
    public Label commoditizationValueForm;
    private Double commValue;

    public Label projectName;
    public Label productName;
    public Label productCost;
    public Label proposedPricePerCustomerLB;
    public Double proposedPricePerCustomer = 0.0;
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
    public Slider itemQualitySl;
    public ComboBox competitorOrientationCB;
    public TextField customerHighestPriceTF;
    public TextField numberCustomersMaxPriceTF;
    public TextField minimumPricePDTF;
    public Text proposedStrategyTX;
    private Double subsidizationPercentage = null;

    private ArrayList<Object> activeProject;
    private ArrayList<Object> activeProduct;
    private Double activeProductCost;
    private Double modifiedProductCost;
    private Double priceClusterFLOutput = -99.0;
    private Boolean isSubscription = false;
    private Boolean isSubsidized = false;

    // Fuzzy logic variables
//    public FuzzyLogic priceSettingFLM;
//    public FuzzyLogic priceDevelopmentFLM;
//    public FuzzyLogic commoditizationFLM;
//    public FuzzyLogic clusteringRangeFLM;

//    TODO remove or put somewhere else
    ArrayList<ArrayList<Object>> graphData = new ArrayList<>();

    Boolean isInsert = true;

    public void initialize() {
        // TODO Add repeating payments textfield/combobox
        // TODO implement preferred pricing strategy somehow
        // TODO add subscription
        activeProject = SceneController.activeProject;
        Integer productIdPassed = (Integer) SceneController.activeProduct.get(0);
        activeProduct = ProductConnector.getProductByProductId(productIdPassed);
        proposedPricePerCustomer = (Double) activeProduct.get(6);

        System.out.println(activeProduct);

        modifiedProductCost  = (Double) activeProduct.get(5);

//        priceSettingFLM = new FuzzyLogic();
//        priceSettingFLM.init("pricingFB");

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

        // TODO do the below for cluster
        Object[] nodeArray = {pricingStrategyCB, desiredMarginTF, targetCB, priceClusteringCB, itemQualityCB,
                marketSaturationCB, isMarketSegmentedCB, brandValueCB, distributionChannelCB, priceElasticityCB,
                numberCustomersTF, preProcessingCB, itemImitabilityCB, degreePriceCompetitionCB, desiredMarkupTF,
                allowedVarianceTF, competitorOrientationCB, /* <-- 17 - new */ numberCustomersMaxPriceTF, customerHighestPriceTF,
                valueAddedTF, itemQualitySl, pricingGoalCB, timePeriodCB, depreciationTF, customerExpectationsCB,
                customerExpectationImportanceCB, competitionRelatedPriceReductionCB, minimumPricePDTF};

        ArrayList<Object> queryResults =
                ProductPricingConnector.getAllProductPricingByProductId(Integer.parseInt(activeProduct.get(0).toString()));

        if (activeProduct.size() != 0) {

            if (queryResults.size() > 0) {

                isInsert = false;

                for (int i = 0; i < nodeArray.length; i++) {
                    //System.out.println(i);
                    String queryItem = String.valueOf(queryResults.get(i+1));
                    if (nodeArray[i].getClass().getName().equals("javafx.scene.control.ComboBox")) {
                        if (i < 16) {
                            ((ComboBox)nodeArray[i]).getSelectionModel().select(queryItem);
                        } else if (i == 16) {
                            competitorOrientationCB.getSelectionModel().select((String) queryResults.get(23));
                        } else if (i == 21) {
                            int select = (int) queryResults.get(26);
                            pricingGoalCB.getSelectionModel().select(select);
                        } else if (i == 22) {
                            int select = (int) queryResults.get(27);
                            timePeriodCB.getSelectionModel().select(select);
                        } else if (i == 24) {
                            int select = (int) queryResults.get(29);
                            customerExpectationsCB.getSelectionModel().select(select);
                        } else if (i == 25) {
                            int select = (int) queryResults.get(30);
                            customerExpectationImportanceCB.getSelectionModel().select(select);
                        } else if (i == 26) {
                            System.out.println("i == 26 so: " + (Integer) queryResults.get(31));
                            int select = (int) queryResults.get(31);
                            competitionRelatedPriceReductionCB.getSelectionModel().select(select);
                        }
                    } else {
                        if (i < 16) {
                            ((TextField)nodeArray[i]).setText(queryItem);
                        } else if (i == 17) {
                            numberCustomersMaxPriceTF.setText(String.valueOf(((Double) queryResults.get(24))));
                        } else if (i == 18) {
                            customerHighestPriceTF.setText(String.valueOf(queryResults.get(21)));
                        } else if (i == 19) {
                            valueAddedTF.setText(String.valueOf(queryResults.get(33)));
                        } else if (i == 20) {
                            System.out.println(itemQualitySl);
                            itemQualitySl.setValue((Double)queryResults.get(20));
                        } else if (i == 23) {
                            depreciationTF.setText(String.valueOf((Double) queryResults.get(28)));
                        } else if (i == 27) {
                            minimumPricePDTF.setText(String.valueOf((Double) queryResults.get(32)));
                        }
                    }
                }
                priceClusteringCheck(priceClusteringCB);
            }
        }
        // TODO add combobox for when is subsidized
        // TODO add something for when it is subsidizing
        System.out.println(activeProduct);
        if ((Boolean)activeProduct.get(8)) {
            isSubsidized = true;
            // TODO - make other change value depending on this?
            Label degree_of_subsidizationLbl = new Label("Degree of Subsidization in %");
            degree_of_subsidizationLbl.setPadding(new Insets(0,0,0,20));

            TextField subsidizationDegreesTF = new TextField();
            subsidizationDegreesTF.setId("subsidizationDegreesTF");

            moveGridpPaneChildrenDown(gridPane, 1, 2);

            gridPane.add(degree_of_subsidizationLbl, 2, 2);
            gridPane.add(subsidizationDegreesTF, 3,2);

            subsidizationDegreesTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.equals("")) {
                    return;
                }
                try {
                    Double subsidizationValue = Double.parseDouble(newValue);
                    if (subsidizationValue > 100) {
                        subsidizationValue = 100.0;
                        HelperMethods.throwAlert(gridPane.getScene(), "Subsidization cannot be bigger than 100%");
                        subsidizationDegreesTF.setText("100");
                    }
                    modifiedProductCost = (Double)activeProduct.get(5);
                    modifiedProductCost -= modifiedProductCost*subsidizationValue/100;
                    productCost.setText(modifiedProductCost.toString());
                    ProductPricingConnector.updateProductSubsidizationDegree((Integer)activeProduct.get(0),
                            subsidizationValue);
                } catch (Exception e) {
                    HelperMethods.throwAlert(gridPane.getScene(), "Please insert a number");
                }
            });

        } else if ((Boolean)activeProduct.get(9)) {
            System.out.println("Hellllooooooooo");
            // TODO add running costs
            ArrayList<ArrayList<Integer>> subsidiesQueryResult =
                    SubsidyConnector.getSubsidizerByProductId((Integer) activeProduct.get(0));
            System.out.println(subsidiesQueryResult);
            for (int i = 0; i < subsidiesQueryResult.size(); i++) {
                ArrayList<Object> subsidizedProduct = ProductConnector.getProductByProductId(
                        (Integer)subsidiesQueryResult.get(i).get(1)
                );
                System.out.println(subsidizedProduct);
                Double subsidizedProductCost = (Double) subsidizedProduct.get(5);
                Integer subsidizationDegreeInt = (Integer) ProductPricingConnector.getAllProductPricingByProductId(
                        (Integer) subsidizedProduct.get(0))
                        .get(19);
                Double subsidizationDegree = Double.valueOf(subsidizationDegreeInt);
                System.out.println(subsidizationDegree);
                if (subsidizationDegree != 0) {
                    subsidizationDegree /= 100;
                    Double addedCost = (subsidizationDegree) * subsidizedProductCost;
                    modifiedProductCost += addedCost;
                }
            }
        }

        if (activeProject.get(4).equals("Subscription")) {
            isSubscription = true;

            Label frequencyOfPayments = new Label("Frequency of payments");
            frequencyOfPayments.setPadding(new Insets(0,0,0,20));

            ComboBox paymentFrequencyCB = new ComboBox();
            paymentFrequencyCB.setId("paymentFrequencyCBId");
            paymentFrequencyCB.getItems().add("Daily");
            paymentFrequencyCB.getItems().add("Weekly");
            paymentFrequencyCB.getItems().add("Biweekly");
            paymentFrequencyCB.getItems().add("Monthly");
            paymentFrequencyCB.getItems().add("Yearly");
            paymentFrequencyCB.getItems().add("Other");
            paymentFrequencyCB.setOnAction(e -> test());
//            subsidizationDegreesCB.setItems();
            moveGridpPaneChildrenDown(gridPane, 1, 2);
            moveGridpPaneChildrenDown(gridPane, 2, 2);

            Label subscriptionLengthLbl = new Label("Average estimated subscription\nlength in months");
            subscriptionLengthLbl.setPadding(new Insets(0,0,0,20));
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
        //addVariableInit((Control) actionEvent.getSource());

        priceClusteringCheck((ComboBox) actionEvent.getSource());
    }

    public void priceSettingSubmitBtnClick(ActionEvent actionEvent)  {
        // TODO implement check that every combobox/input field has a value
//        System.out.println(commoditizationValueForm.getText());
        commValue = commoditizationCalculation();
        if (commValue == 0) {
            HelperMethods.throwAlert(gridPane.getScene(), "Error");
        }
        if (false) {
            HelperMethods.throwAlert(gridPane.getScene(), "Please get the commoditization value first");
        } else {
            if (isSubscription) {
                utiliseSubscription();
            }

            if ((allowedVarianceTF.getText().equals("") || (numberCustomersTF.getText().equals("")) ||
                    (desiredMarginTF.getText().equals(""))||(desiredMarkupTF.getText().equals("")))) {
                HelperMethods.throwAlert(gridPane.getScene(), "Please fill out the allowed variance, " +
                        "the number of customers, the desired margin, and the desired markup.");
                return;
            }

            Double allowedVariance = Double.valueOf(allowedVarianceTF.getText())/100;
            Double cost = modifiedProductCost/Double.valueOf(numberCustomersTF.getText());
            Double margin = Double.valueOf(desiredMarginTF.getText())/100;
            Double markup = Double.valueOf(desiredMarkupTF.getText())/100;
            Double valueAdded = Double.parseDouble(valueAddedTF.getText());

            ArrayList<Double> priceCompetitionList = calculatePriceIndex();

            String priceLow = "(" + (cost*(1-allowedVariance)) + ", 0) (" + cost + ", 1) (" +
                    cost*(1+allowedVariance) + ", 0)";
            FuzzyLogic.replacePriceTerm(1, priceLow);

            String priceMed = "(" + (cost * ((1 + margin)*(1-allowedVariance))) + ", 0) (" + cost * (1+margin) + ", 1) (" +
                    cost * ((1+margin)*(allowedVariance+1)) + ", 0)";
            FuzzyLogic.replacePriceTerm(2, priceMed);

            String priceHigh = "(" + (cost * (1+markup*(1-allowedVariance))) + ", 0) (" + (cost * (1+markup)) + ", 1) (" +
                    (cost * ((1+markup)*(1+allowedVariance))) + ", 0)";
            FuzzyLogic.replacePriceTerm(4, priceHigh);

            String priceMaxString;
            if (customerHighestPriceTF.getText().equals("")) {
                priceMaxString = priceHigh;
            } else {
                Double priceMax = Double.parseDouble(customerHighestPriceTF.getText());
                priceMaxString = "(" + (cost * (1+margin)) + ", 0) (" + (priceMax*(1-allowedVariance)) + ", 1) (" +
                        (priceMax) + ", 0)";
            }
            FuzzyLogic.replacePriceTerm(3, priceMaxString);

            if (priceCompetitionList != null) {
                String priceCompetition = "(" + priceCompetitionList.get(0) + ",0) (" + priceCompetitionList.get(1) + ",1)" +
                        " (" + priceCompetitionList.get(2) + ",0)";
                FuzzyLogic.replacePriceTerm(5, priceCompetition);
            } else {
                String priceCompetition = "(" + (cost*((1-margin)*(1-allowedVariance))) + ", 0) (" + (cost) + ", 1) (" +
                        (cost*(1+margin*(1-allowedVariance))) + ", 0)";
                FuzzyLogic.replacePriceTerm(5, priceCompetition);
            }

            if (valueAdded == 0) {
                String priceValueAdded = "(" + (valueAdded*(1-allowedVariance)) + ", 0) (" + (valueAdded) + ", 1) (" +
                        (valueAdded*(1+allowedVariance)) + ", 0)";
                FuzzyLogic.replacePriceTerm(8, priceValueAdded);
            } else {
                // Todo - differentiate between price Med in some circumstances or priceLow in others?
                FuzzyLogic.replacePriceTerm(8, priceMed);
            }

            FuzzyLogic priceSettingFLM = new FuzzyLogic();
            priceSettingFLM.init("pricingFB");

            priceSettingFLM.functionBlockSetVariable("commoditization", (double) commValue);
//            System.out.println(commValue+": " + priceSettingFLM.getFunctionBlock().getVariable("commoditization").getValue());

            // preferred pricing strategy
            String preferredPricingStrategyCB = pricingStrategyCB.getId().substring(0, pricingStrategyCB.getId().length()-2);
            int pricingStrategyCBIndex = pricingStrategyCB.getSelectionModel().getSelectedIndex();
            priceSettingFLM.functionBlockSetVariable(preferredPricingStrategyCB, Double.valueOf(pricingStrategyCBIndex));
            System.out.println(preferredPricingStrategyCB +": " + priceSettingFLM.getFunctionBlock().getVariable(preferredPricingStrategyCB).getValue());

            // item quality CB
            String itemQualityTempCB = itemQualityCB.getId().substring(0, itemQualityCB.getId().length()-2);
            int itemQualityTempCBIndex = itemQualityCB.getSelectionModel().getSelectedIndex();
            priceSettingFLM.functionBlockSetVariable("itemQualityImportance", Double.valueOf(itemQualityTempCBIndex));

            // item quality slider
            String itemQualityTempSl = itemQualitySl.getId().substring(0, itemQualitySl.getId().length()-2);
            Double itemQualitySliderValue = itemQualitySl.getValue();
            priceSettingFLM.functionBlockSetVariable(itemQualityTempSl, itemQualitySliderValue);

            // market saturation
            String marketSaturationTempCB = marketSaturationCB.getId().substring(0, marketSaturationCB.getId().length()-2);
            Integer marketSaturationIndex = marketSaturationCB.getSelectionModel().getSelectedIndex();
            priceSettingFLM.functionBlockSetVariable(marketSaturationTempCB, Double.valueOf(marketSaturationIndex));
//            System.out.println(marketSaturationTempCB +" " + priceSettingFLM.getFunctionBlock().getVariable(marketSaturationTempCB).getValue());

            // brand value
            String brandValueTempCB = brandValueCB.getId().substring(0, brandValueCB.getId().length()-2);
            Integer brandValueCBIndex = brandValueCB.getSelectionModel().getSelectedIndex();
            priceSettingFLM.functionBlockSetVariable(brandValueTempCB, Double.valueOf(brandValueCBIndex));

            // distribution channel
            String distributionChannelTempCB = distributionChannelCB.getId().substring(0, distributionChannelCB.getId().length()-2);
            Integer distributionChannelCBIndex = distributionChannelCB.getSelectionModel().getSelectedIndex();
            priceSettingFLM.functionBlockSetVariable(distributionChannelTempCB, Double.valueOf(distributionChannelCBIndex));

            // price elasticity
            String priceElasticityTempCB = priceElasticityCB.getId().substring(0, priceElasticityCB.getId().length()-2);
            Integer priceElasticityCBIndex = priceElasticityCB.getSelectionModel().getSelectedIndex();
            priceSettingFLM.functionBlockSetVariable(priceElasticityTempCB, Double.valueOf(priceElasticityCBIndex));

            // price index - competitor comparison
            // TODO still implement?
            if (priceCompetitionList != null) {
                Double priceIndex = priceCompetitionList.get(3);
                priceSettingFLM.functionBlockSetVariable("priceIndex", priceIndex);
            } else {
                priceSettingFLM.functionBlockSetVariable("priceIndex", 100.0);
            }

            // competitor Orientation
            String competitorOrientationTempCB = competitorOrientationCB.getId().substring(0, competitorOrientationCB.getId().length()-2);
            Integer competitorOrientationCBIndex = competitorOrientationCB.getSelectionModel().getSelectedIndex();
            priceSettingFLM.functionBlockSetVariable(competitorOrientationTempCB, Double.valueOf(competitorOrientationCBIndex));

            priceSettingFLM.evaluate();
            for( Rule r : priceSettingFLM.getFunctionBlock().getFuzzyRuleBlock("No1").getRules() )
                System.out.println(r);
//            priceSettingFLM.getChartFunctionBlock();

            proposedPricePerCustomer = priceSettingFLM.getFunctionBlock().getVariable("price").getValue();
            System.out.println("Price: " + priceSettingFLM.getFunctionBlock().getVariable("price").getValue());
            System.out.println("priceRounded v1: " + proposedPricePerCustomer);
            if (proposedPricePerCustomer < 0.2) {
                proposedPricePerCustomer = 0.49;
                proposedStrategyTX.setText("Freemium with advertisement");
            }

            Double priceRangeLow = returnRanges().get(0);
            Double priceRangeHigh = returnRanges().get(1);

            Double clusterImportance = returnClusterImportance();

            Integer subsidizationDegrees = 0;
            try {
                TextField subsidizationDegreesTF = (TextField) gridPane.getScene().lookup("#subsidizationDegreesTF");
                subsidizationDegrees = Integer.parseInt(subsidizationDegreesTF.getText());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(Arrays.toString(e.getStackTrace()));
                System.out.println("Failed to set subsidization degrees");
            }

            Double clusteringReaction = HelperMethods.returnClusteringReaction(allowedVariance, returnClusterImportance(),
                    proposedPricePerCustomer,returnRanges().get(0), returnRanges().get(1));

            System.out.println("clusteringReaction " + clusteringReaction);
            System.out.println("priceRounded v1: " + proposedPricePerCustomer);
            proposedPricePerCustomer *= clusteringReaction;
//            HelperMethods.throwAlert(gridPane.getScene(), "v1 " + String.valueOf(proposedPricePerCustomer));


            DecimalFormat df = new DecimalFormat("#.##");

            proposedPricePerCustomerLB.setText(df.format(proposedPricePerCustomer));
//            HelperMethods.throwAlert(gridPane.getScene(), String.valueOf(proposedPricePerCustomer));

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
                    java.lang.String.valueOf(itemImitabilityCB.getSelectionModel().getSelectedItem()),
                    java.lang.String.valueOf(degreePriceCompetitionCB.getSelectionModel().getSelectedItem()),
                    Double.parseDouble(desiredMarkupTF.getText()),
                    Double.parseDouble(allowedVarianceTF.getText()),
                    priceRangeLow,
                    priceRangeHigh,
                    subsidizationDegrees,
                    itemQualitySl.getValue(),
                    Double.parseDouble(customerHighestPriceTF.getText()),
                    0.0,
                    String.valueOf(competitorOrientationCB.getSelectionModel().getSelectedItem()),
                    Double.parseDouble(numberCustomersMaxPriceTF.getText()),
                    clusterImportance,
                    pricingGoalCB.getSelectionModel().getSelectedIndex(),
                    timePeriodCB.getSelectionModel().getSelectedIndex(),
                    Double.parseDouble(depreciationTF.getText()),
                    customerExpectationsCB.getSelectionModel().getSelectedIndex(),
                    customerExpectationImportanceCB.getSelectionModel().getSelectedIndex(),
                    competitionRelatedPriceReductionCB.getSelectionModel().getSelectedIndex(),
                    Double.parseDouble(minimumPricePDTF.getText()),
                    Double.parseDouble(valueAddedTF.getText()));

//                System.out.println(activeProduct);
            System.out.println(activeProduct);
            activeProduct = ProductConnector.updateProductById((Integer) activeProduct.get(0),
                    (Integer) activeProduct.get(1),
                    (String) activeProduct.get(2),
                    (String) activeProduct.get(3),
                    (Date) activeProduct.get(4),
                    (Double) activeProduct.get(5),
                    proposedPricePerCustomer,
                    (String) activeProduct.get(7),
                    (Boolean) activeProduct.get(8),
                    (Boolean) activeProduct.get(9),
                    (Double) activeProduct.get(10));
//                System.out.println(activeProduct);


        }
        //priceSettingFLM.evaluate();
    }

    public void addVariableInit(Control source) {
        /*
        String element = source.getId();
        element = element.substring(0, element.length()-2);
        //String value = ((ComboBox)actionEvent.getSource()).getSelectionModel().getSelectedItem().toString().replaceAll("\\s+", "");;
        int selectedIndex = ((ComboBox)source).getSelectionModel().getSelectedIndex();

        priceSettingFLM.functionBlockSetVariable(element, (double) selectedIndex);

         */
    }

    public Double commoditizationCalculation() {
        FuzzyLogic commoditizationFLM = new FuzzyLogic();
        commoditizationFLM.init("commoditizationFB");

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

        System.out.println("commoditization for comparison " + commoditizationFLM.getFunctionBlock().getVariable("itemImitability").getValue());
        commoditizationFLM.evaluate();

        //commoditizationFLM.getChartFunctionBlock();
//        commoditizationFLM.getChartVariable("commoditizationOutputValue");

        //System.out.println(commoditizationFLM.getFunctionBlock());
//        commoditizationValueForm.setText(String.valueOf());
        Double commResult = commoditizationFLM.getFunctionBlock().getVariable("commoditizationOutputValue").getValue();
        return commResult;
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

    public void utiliseSubscription() {
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
        } else if (paymentFrequencyText.equals("Biweekly")) {
            time = 26;
        } else if (paymentFrequencyText.equals("Monthly")) {
            time = 12;
        } else if (paymentFrequencyText.equals("Yearly")) {
            time = 1;
        } else if (paymentFrequencyText.equals("Other")){
            System.out.println("To do");
        }
        Double cost = (Double)modifiedProductCost;
        // TODO - probably change so that user can choose to earn money faster and not by the time subscription expires
        modifiedProductCost = (cost/numberCustomers) / (time / subscriptionParse);
//        System.out.println(newCost);
        //timePeriodCB
    }

    public void priceClusteringCheck(ComboBox comboBox) {
        // TODO There's a bug that adds cluster ranges even though they already exist
        if (comboBox.getId().equals("priceClusteringCB")) {
            if (priceClusteringCB.getSelectionModel().getSelectedItem().equals("Yes")) {
                Label clusteringRanges = new Label("Clustering Ranges:");
                Label clusteringImportance = new Label("Clustering Importance\n(from low to high):");
                clusteringRanges.setPadding(new Insets(0,0,0,20));

                TextField rangeLow = new TextField();
                rangeLow.setPromptText("Lower bound");
                rangeLow.setId("rangeLowTF");
                rangeLow.prefWidth(75);
                rangeLow.setMaxWidth(75);
//                rangeLow.setMinWidth(60);

                TextField rangeHigh = new TextField();
                rangeHigh.setPromptText("Upper bound");
                rangeHigh.setId("rangeHighTF");
                rangeHigh.setPrefWidth(75);
                rangeHigh.setMaxWidth(75);
//                rangeHigh.setMinWidth(60);

                HBox hBox = new HBox(rangeLow, rangeHigh);
                int clusterRow = gridPane.getRowIndex(priceClusteringCB);
                moveGridpPaneChildrenDown(gridPane, clusterRow, 2);
                moveGridpPaneChildrenDown(gridPane, clusterRow, 2);

                gridPane.add(clusteringRanges, 2, clusterRow+1);
                gridPane.add(hBox, 3,clusterRow+1);

                Slider clusterImportanceSlider = new Slider();
                clusterImportanceSlider.setId("clusterImportanceSL");
                clusterImportanceSlider.setMajorTickUnit(10);
                clusterImportanceSlider.setShowTickMarks(false);
                clusterImportanceSlider.setMinorTickCount(0);

                gridPane.add(clusteringImportance, 2, clusterRow+2);
                gridPane.add(clusterImportanceSlider, 3,clusterRow+2);
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
//            priceDevelopmentFLM.functionBlockSetVariable(element, (double) selectedIndex);
        }
    }


    public void priceDevelopmentBtnClick(ActionEvent actionEvent) {
        FuzzyLogic priceDevelopmentFLM = new FuzzyLogic();
        priceDevelopmentFLM.init("priceDevelopmentFB");

        String depreciationBuffer = depreciationTF.getId().substring(0, depreciationTF.getId().length()-2);
        Double depreciationTFIndex = Double.parseDouble(depreciationTF.getText());
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

        // TODO add price clusters
//        String competitionRelatedPriceReductionCBBBuffer = competitionRelatedPriceReductionCB.getId().substring(0, competitionRelatedPriceReductionCB.getId().length()-2);
//        int competitionRelatedPriceReductionCBIndex = competitionRelatedPriceReductionCB.getSelectionModel().getSelectedIndex();
//        priceDevelopmentFLM.functionBlockSetVariable(competitionRelatedPriceReductionCBBBuffer, (double) competitionRelatedPriceReductionCBIndex);

        priceDevelopmentFLM.evaluate();

        graphData.clear();

        System.out.println(String.valueOf(priceDevelopmentFLM.getFunctionBlock().getVariable("priceDevelopment").getValue()));
        if ((proposedPricePerCustomer == 0.0)||(proposedPricePerCustomer == 0)) {
            HelperMethods.throwAlert(gridPane.getScene(), "Price needs to be calculated first");
            return;
        }
        System.out.println("Price after PD: " + proposedPricePerCustomer * priceDevelopmentFLM.getFunctionBlock().getVariable("priceDevelopment").getValue());

        Double priceDevelopment = proposedPricePerCustomer * 1;
        Double pDResult = priceDevelopmentFLM.getFunctionBlock().getVariable("priceDevelopment").getValue();

        // TODO make it so that the loop re-evaluate the proposed price over and over instead of reusing the same result
        int timePeriodIndex = timePeriodCB.getSelectionModel().getSelectedIndex();
        ArrayList<Object> tempArray = new ArrayList<>();
        tempArray.add(0);
        tempArray.add(priceDevelopment);
        graphData.add(tempArray);
        System.out.println("time period: " + timePeriodIndex);

        if (timePeriodIndex == 0) {

        } else if (timePeriodIndex == 1) {
            int n = 6;

            Double currentReductionPercentage = (1-pDResult)/12;
//            Double percentage = tempRemove4/priceDevelopment;
            for (int i = 1; i < n+1; i++) {
                Double allowedVariance = Double.valueOf(allowedVarianceTF.getText())/100;

                ArrayList<Object> temp = new ArrayList<>();
                temp.add(i);
                priceDevelopment -= (priceDevelopment*currentReductionPercentage);

                FuzzyLogic clusteringRangeFLM = new FuzzyLogic();
                clusteringRangeFLM.init("clusteringRangeFB");

                priceDevelopment *= HelperMethods.returnClusteringReaction(allowedVariance, returnClusterImportance(),
                        priceDevelopment,returnRanges().get(0), returnRanges().get(1));

                if (priceDevelopment < Double.parseDouble(minimumPricePDTF.getText())) {
                    priceDevelopment = Double.parseDouble(minimumPricePDTF.getText());
                }
                temp.add(priceDevelopment);
                graphData.add(temp);
            }
        } else if (timePeriodIndex == 2) {
            Double allowedVariance = Double.valueOf(allowedVarianceTF.getText())/100;
            // Result is wrong
            int n = 12;
            Double currentReductionPercentage = (1-pDResult)/12;
//            Double percentage = tempRemove4/priceDevelopment;
            for (int i = 1; i < n+1; i++) {
                ArrayList<Object> temp = new ArrayList<>();
                temp.add(i);
                priceDevelopment = priceDevelopment - (priceDevelopment*currentReductionPercentage);

                FuzzyLogic clusteringRangeFLM = new FuzzyLogic();
                clusteringRangeFLM.init("clusteringRangeFB");

                priceDevelopment *= HelperMethods.returnClusteringReaction(allowedVariance, returnClusterImportance(),
                        priceDevelopment,returnRanges().get(0), returnRanges().get(1));

                if (priceDevelopment < Double.parseDouble(minimumPricePDTF.getText())) {
                    priceDevelopment = Double.parseDouble(minimumPricePDTF.getText());
                }

                temp.add(priceDevelopment);
                graphData.add(temp);
            }
        }
        else if (timePeriodIndex == 3) {
            Double allowedVariance = Double.valueOf(allowedVarianceTF.getText())/100;
            int n = 36;
            Double currentReductionPercentage = (1-pDResult)/12;
//            Double percentage = tempRemove4/priceDevelopment;
            for (int i = 1; i < n+1; i++) {
                ArrayList<Object> temp = new ArrayList<>();
                temp.add(i);
                priceDevelopment = priceDevelopment - (priceDevelopment*currentReductionPercentage);

                priceDevelopment *= HelperMethods.returnClusteringReaction(allowedVariance, returnClusterImportance(),
                        priceDevelopment,returnRanges().get(0), returnRanges().get(1));

                if (priceDevelopment < Double.parseDouble(minimumPricePDTF.getText())) {
                    priceDevelopment = Double.parseDouble(minimumPricePDTF.getText());
                }
                temp.add(priceDevelopment);
                graphData.add(temp);
            }
        }

        GraphController pricing = new GraphController();
        pricing.setAxes("Proposed Price Development",
                "Number of Months");
        pricing.setLineChartData((String) activeProduct.get(2) + " price development",
                graphData, "Suggested price development");
        pricing.showLineChart();
//        System.out.println(graphData);
        System.out.println(pricing.getLineChart().getData());
    }

    // TODO move to helper methods, as it is required at several various points
    public void priceIndexComparison(ActionEvent actionEvent) {

    }

    public void competitorOrientationComboBoxChange(ActionEvent actionEvent) {
        if (competitorOrientationCB.getSelectionModel().getSelectedIndex() > 0) {

            // TODO add update product pricing
        }
    }

    public void priceClusteringChange() {
        Scene scene = gridPane.getScene();
        TextField rangeLowTF = (TextField) scene.lookup("#rangeLowTF");
        TextField rangeHighTF = (TextField) scene.lookup("#rangeHighTF");

        if ((rangeLowTF.getText().equals(""))||(rangeHighTF.getText().equals(""))) {
            HelperMethods.throwAlert(scene,"No empty ranges allowed");
            return;
        }

        Double rangeLow = Double.parseDouble(rangeLowTF.getText());
        Double rangeHigh = Double.parseDouble(rangeHighTF.getText());
        Slider clusterImportanceSL = (Slider) scene.lookup("#clusterImportanceSL");

        FuzzyLogic clusteringRangeFLM = new FuzzyLogic();
        clusteringRangeFLM.init("clusteringRangeFB");

    }

    public ArrayList<Double> calculatePriceIndex() {
        //region individual price index calculation for current product
        // region
        ArrayList<Double> competitivePricingReturn = new ArrayList<>();
        ArrayList<ArrayList<Object>> competitorQueryResults =
                CompetitorConnector.getCompetitorsByProjectId((Integer) activeProject.get(0));

        if (competitorQueryResults.size() == 0) {
            return null;
        }

        ArrayList<Double> priceIndexCurrentProduct = new ArrayList<>();

        FuzzyLogic competitionFLM = new FuzzyLogic();
        competitionFLM.init("competitionFB");

//        System.out.println(commoditizationValueForm.getText());

        competitionFLM.functionBlockSetVariable("commoditization",
                commValue);

        String competitorOrientationTemp = competitorOrientationCB.getId().substring(0, competitorOrientationCB.getId().length()-2);
        int competitorOrientationIndex = competitorOrientationCB.getSelectionModel().getSelectedIndex();
        competitionFLM.functionBlockSetVariable(competitorOrientationTemp, (double) competitorOrientationIndex);

        String pricingStrategyTemp = pricingStrategyCB.getId().substring(0, pricingStrategyCB.getId().length()-2);
        int pricingStrategyIndex = pricingStrategyCB.getSelectionModel().getSelectedIndex();
        competitionFLM.functionBlockSetVariable(pricingStrategyTemp, (double) pricingStrategyIndex);

        String pricingGoalTemp = pricingGoalCB.getId().substring(0, pricingGoalCB.getId().length()-2);
        int pricingGoalIndex = pricingGoalCB.getSelectionModel().getSelectedIndex();
        competitionFLM.functionBlockSetVariable(pricingGoalTemp, (double) pricingGoalIndex);

        competitionFLM.evaluate();

        Double priceIndex = competitionFLM.getFunctionBlock().getVariable("priceIndex").getValue() *100;
        System.out.println("priceIndex " + priceIndex);

        Double lowestPrice = Double.MAX_VALUE;
        Double highestPrice = 0.0;

        for (int j = 0; j < competitorQueryResults.size(); j++) {
            Double competitorPrice = (Double)competitorQueryResults.get(j).get(2);
            priceIndexCurrentProduct.add(
                    (competitorPrice*100)/priceIndex);
            if (competitorPrice > highestPrice) {
                highestPrice = competitorPrice;
            }
            if (lowestPrice > competitorPrice) {
                lowestPrice = competitorPrice;
            }
        }
        Double sumPriceIndices = 0.0;
        for (int j = 0; j < priceIndexCurrentProduct.size(); j++) {
            sumPriceIndices += priceIndexCurrentProduct.get(j);
        }
        // TODO pass this value to price setting competition value
        Double indexResult = sumPriceIndices/competitorQueryResults.size();
        System.out.println("Final price Index (price setting): " + indexResult);
        if (highestPrice == lowestPrice) {
            highestPrice = indexResult*1.1;
            lowestPrice = indexResult/1.1;
        }
        System.out.println(lowestPrice);
        competitivePricingReturn.add(lowestPrice);
        competitivePricingReturn.add(indexResult);
        competitivePricingReturn.add(highestPrice);
        competitivePricingReturn.add(priceIndex);

        return competitivePricingReturn;
        //endregion
    }

    public void calculatePriceElasticity() {

        // TODO calculate price elasticity for price development / revenue maximization goal
        // Point 1
        int customersNormal = Integer.parseInt(numberCustomersTF.getText());
        Double priceNormal = (Double) activeProduct.get(6);

        // Point 2
        int customersMax = Integer.parseInt(numberCustomersMaxPriceTF.getText());
        Double priceMax = Double.parseDouble(customerHighestPriceTF.getText());
    }

    public ArrayList<Double> returnRanges() {
        Double priceRangeLow = 0.0;
        Double priceRangeHigh = 0.0;

        try {
            TextField rangeLowTF = (TextField) gridPane.getScene().lookup("#rangeLowTF");
            TextField rangeHighTF = (TextField) gridPane.getScene().lookup("#rangeHighTF");
            priceRangeLow = Double.parseDouble(rangeLowTF.getText());
            priceRangeHigh = Double.parseDouble(rangeHighTF.getText());
        } catch (Exception e) {
            System.out.println("Failed to set ranges");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        ArrayList<Double> toReturn = new ArrayList<>();
        toReturn.add(priceRangeLow);
        toReturn.add(priceRangeHigh);
        return toReturn;
    }

    public Double returnClusterImportance() {
        Double clusterImportance = 50.0;

        try {
            clusterImportance = ((Slider) gridPane.getScene().lookup("#clusterImportanceSL")).getValue();
        } catch (Exception e) {
            System.out.println("Failed to set cluster importance");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return clusterImportance;
    }


}
