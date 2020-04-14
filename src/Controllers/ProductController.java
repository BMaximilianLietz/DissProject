package Controllers;

import Data.*;
import Misc.HelperMethods;
import com.sun.glass.ui.GlassRobot;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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

    public TextField productVersionTF;
    public TextField competitorPriceTF;
    public TextField competitorNameTF;
    public GridPane competitorGridPane;
    public TextField competitorSales;
    public Slider competitorQualitySlider;
    public Label sliderTitle;

    public ArrayList<Object> activeProject;
    private Integer numberOfProducts = 0;
    private Boolean productRelationExists = false;


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
            for (int i = 0; i < competitorInit.size(); i++) {
                addCompetitor((String) competitorInit.get(i).get(1),
                        (Double) competitorInit.get(i).get(2),
                        (Double) competitorInit.get(i).get(5),
                        (Double) competitorInit.get(i).get(4),
                        competitorGridPane);
            }
        }
        sliderTitle.setText("Competitor's product quality\nfrom lowest to highest");
    }

    public void addProductBtnClick(ActionEvent actionEvent) {

        ArrayList<Object> queryResults = ProductConnector.insertIntoProduct((Integer) activeProject.get(0),
                productNameTF.getText(), productDescriptionTF.getText(), Date.valueOf(LocalDate.now()), 0.0,
                0.0, productVersionTF.getText(), Boolean.valueOf(null), Boolean.valueOf(null), 0.0);

        ProductPricingConnector.insertAllProductPricing((Integer) queryResults.get(0), "", 0.0,
                "", "", "", "", "","",
                "", "",0, "", "", "",
                0.0, 0.0, 0.0, 0.0, 0,
                0.0, 0.0, 0.0, "");

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
            SceneController.openView(scene, getClass(), activeProject, product,"priceSettingView.fxml");
        });
        Button productCostingButton = new Button("Costing");
        productCostingButton.onActionProperty().setValue(actionEvent1 -> {
            Scene scene = ((Node)actionEvent1.getTarget()).getScene();
            SceneController.openView(scene, getClass(), activeProject, product, "costingView.fxml");
        });

        Button productCopyButton = new Button("Clone");
        productCopyButton.onActionProperty().setValue(actionEvent1 -> {

            ArrayList<Object> productClone = new ArrayList<>(product);
            productClone.set(2, (String) productClone.get(2) + " Copy");

            ArrayList<Object> queryResults = ProductConnector.insertIntoProduct((Integer) activeProject.get(0),
                    (String) productClone.get(2),
                    (String) productClone.get(3),
                    Date.valueOf(LocalDate.now()),
                    (Double) productClone.get(5),
                    (Double) productClone.get(6),
                    (String) productClone.get(7),
                    Boolean.valueOf(null), Boolean.valueOf(null),
                    (Double) productClone.get(10));

            ArrayList<Object> productPricingCopy = ProductPricingConnector.getAllProductPricingByProductId(
                    (Integer) product.get(0)
            );

            ProductPricingConnector.insertAllProductPricing((Integer) queryResults.get(0),
                    (String)productPricingCopy.get(1),
                    (Double)productPricingCopy.get(2),
                    (String)productPricingCopy.get(3),
                    (String)productPricingCopy.get(4),
                    (String)productPricingCopy.get(5),
                    (String)productPricingCopy.get(6),
                    (String)productPricingCopy.get(7),
                    (String)productPricingCopy.get(8),
                    (String)productPricingCopy.get(9),
                    (String)productPricingCopy.get(10),
                    (Integer)productPricingCopy.get(11),
                    (String)productPricingCopy.get(12),
                    (String)productPricingCopy.get(13),
                    (String)productPricingCopy.get(14),
                    (Double)productPricingCopy.get(15),
                    (Double)productPricingCopy.get(16),
                    (Double)productPricingCopy.get(17),
                    (Double)productPricingCopy.get(18),
                    (Integer)productPricingCopy.get(19),
                    (Double)productPricingCopy.get(20),
                    (Double)productPricingCopy.get(21),
                    (Double)productPricingCopy.get(22),
                    (String)productPricingCopy.get(23));

            addProduct(queryResults, gridPaneLeft);
        });

        Button productDeleteButton = new Button("Delete");
        productDeleteButton.onActionProperty().setValue(actionEvent1 -> {
            ProductConnector.deleteProductByProductId((Integer) product.get(0));
//            gridPaneChosen.getChildren().remove()
        });

        int rowIndex = gridPaneChosen.getRowCount()+1;

        HBox hbox = new HBox(productPricingButton, productCostingButton);
        HBox hBox2 = new HBox(productCopyButton, productDeleteButton);
        VBox vBox = new VBox(hbox, hBox2);

        gridPaneChosen.add(productNameLb, 0, rowIndex);
        gridPaneChosen.add(productDescriptionLb, 1, rowIndex);
        gridPaneChosen.add(productPriceLb, 2, rowIndex);
        gridPaneChosen.add(productCostsLb,3, rowIndex);
        gridPaneChosen.add(productVersionLb, 4, rowIndex);
        gridPaneChosen.add(vBox, 5, rowIndex);

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

            numberOfProducts++;
            if (numberOfProducts > 1) {
                if (!productRelationExists) {
                    ComboBox productRelationCB = new ComboBox();
                    productRelationCB.setId("productRelationCBId");
                    productRelationCB.getItems().add("Select product relationship");
                    productRelationCB.getItems().add("Complements");
                    productRelationCB.getItems().add("Substitutes");
                    productRelationCB.getItems().add("Unrelated");
                    productRelationCB.setOnAction(e -> productRelationCBChange());
                    gridPaneChosen.add(productRelationCB, 2, 0);
                }
            }
        }
    }

    public void productRelationCBChange() {
        System.out.println("Test");
    }

    // Navigation

    public void openProjectViewMenuItemClick(ActionEvent actionEvent) {
        SceneController.openView(menuBar.getScene(), getClass(), activeProject, "projectView.fxml");
    }

    public void addCompetitorBtnClick(ActionEvent actionEvent) {
        Double competitorQuality = Double.valueOf(Math.round(competitorQualitySlider.getValue()));
        addCompetitor(competitorNameTF.getText(),
                Double.parseDouble(competitorPriceTF.getText()),
                Double.parseDouble(competitorSales.getText()),
                competitorQuality,
                competitorGridPane);
        CompetitorConnector.insertIntoCompetitors(competitorNameTF.getText(),
                Double.parseDouble(competitorPriceTF.getText()),
                (Integer) activeProject.get(0),
                competitorQuality, Double.parseDouble(competitorSales.getText()));
    }

    public void addCompetitor(String competitorName, Double competitorPrice,
                              Double competitorSales, Double competitorQuality,
                              GridPane gridPaneChosen) {
        Label competitorNameLb = new Label(competitorName);
        Label competitorPriceLb = new Label(competitorPrice.toString());
        Label competitorSalesLb = new Label(competitorSales.toString());
        Label competitorQualityLb = new Label(competitorQuality.toString());

        int rowIndex = gridPaneChosen.getRowCount()+1;

        gridPaneChosen.add(competitorNameLb, 0, rowIndex);
        gridPaneChosen.add(competitorPriceLb, 1, rowIndex);
        gridPaneChosen.add(competitorSalesLb, 2, rowIndex);
        gridPaneChosen.add(competitorQualityLb, 3, rowIndex);
    }

    public void competitorComparisonBtnClick(ActionEvent actionEvent) {
        ArrayList<ArrayList<Object>> competitorQueryResults =
                CompetitorConnector.getCompetitorsByProjectId((Integer) activeProject.get(0));
        Double largestPrice = 0.0;

        for (int i = 0; i < competitorQueryResults.size(); i++) {
            if ((Double) competitorQueryResults.get(i).get(2) > largestPrice) {
                largestPrice = (Double) competitorQueryResults.get(i).get(2);
            }
        }
        int divisor = 1;
        String unit = "";
        if (largestPrice >= 1000) {
            divisor = 100;
            unit = " k GBP";
        } else if (largestPrice >= 1000000) {
            divisor = 100000;
            unit = " m GBP";
        } else if (largestPrice >= 1000000000) {
            divisor = 100000000;
            unit = " bn GBP";
        }

        GraphController competitorGraph = new GraphController();
        competitorGraph.setBubbleChartAxes("Product Quality Points",
                "Product Prices",
                unit);

        ArrayList<ArrayList<Object>> init = ProductConnector.getAllByProjectId((Integer) activeProject.get(0));

        for (int i = 0; i < init.size(); i++) {
            ArrayList<Object> ownProductPricing = ProductPricingConnector.getAllProductPricingByProductId(
                    (Integer) init.get(i).get(0)
            );
            Double ownProductPrice = (Double) init.get(i).get(6);
            Double ownProductItemQuality = (Double) ownProductPricing.get(20);
            if ((ownProductPrice != 0) && (ownProductItemQuality != 0)) {
                ArrayList<Double> temp = new ArrayList<>();
                temp.add(ownProductItemQuality);
                temp.add(ownProductPrice);
//                temp.add();
                competitorGraph.setBubbleChartData("Your product",
                        temp,
                        "Competitor Comparison",
                        divisor);
            }
        }

        for (ArrayList<Object> competitorQueryResult : competitorQueryResults) {
            ArrayList<Double> temp = new ArrayList<>();
            temp.add((Double) competitorQueryResult.get(4));
            temp.add((Double) competitorQueryResult.get(2));
            temp.add((Double) competitorQueryResult.get(5));
            competitorGraph.setBubbleChartData((String) competitorQueryResult.get(1),
                    temp,
                    "Competitor Comparison",
                    divisor);
        }
        competitorGraph.showBubbleChart();
    }

    // TODO move to helper methods, as it is required at several various points
    public void priceIndexComparison(ActionEvent actionEvent) {
        ArrayList<ArrayList<Object>> init =
                ProductConnector.getAllByProjectId((Integer) activeProject.get(0));
        ArrayList<ArrayList<Object>> competitorQueryResults =
                CompetitorConnector.getCompetitorsByProjectId((Integer) activeProject.get(0));
        ArrayList<ArrayList<String>> barChartData = new ArrayList<>();

        // price index formula: (competitor price / your price) * 100
        // for the market: priceIndex1 + priceIndex2 + ... + priceIndexN / number of competitors
        for (int i = 0; i < init.size(); i++) {
            ArrayList<Double> priceIndexCurrentProduct = new ArrayList<>();
            Double ownProductPrice = (Double) init.get(i).get(6);
            if (ownProductPrice != 0) {
                for (int j = 0; j < competitorQueryResults.size(); j++) {
                    ArrayList<String> barChartTemp = new ArrayList<>();
                    barChartData.add(barChartTemp);
                    barChartTemp.add((String)competitorQueryResults.get(j).get(1));
                    priceIndexCurrentProduct.add(
                            (((Double)competitorQueryResults.get(j).get(2))/ownProductPrice)*100);
                    barChartTemp.add(priceIndexCurrentProduct.get(j).toString());
                    System.out.println("Index: " + competitorQueryResults.get(j).get(1) + " " + priceIndexCurrentProduct.get(j));
                }
                Double sumPriceIndices = 0.0;
                for (int j = 0; j < priceIndexCurrentProduct.size(); j++) {
                    sumPriceIndices += priceIndexCurrentProduct.get(j);
                }
                Double indexResult = sumPriceIndices/competitorQueryResults.size();
                ArrayList<String> barChartTemp = new ArrayList<>();
                barChartData.add(barChartTemp);
                barChartTemp.add("Average");
                barChartTemp.add(indexResult.toString());

                GraphController barChart = new GraphController();
                barChart.setBarChartAxes("Price Index", "Product/Competitor Name");
                barChart.setBarChartData("lorem", barChartData,
                        "Price Index Comparison for " + init.get(i).get(2));
                barChart.showBarChart();

                System.out.println("Final price Index: " + indexResult);
            }
        }

        // TODO add update product pricing
    }
}
