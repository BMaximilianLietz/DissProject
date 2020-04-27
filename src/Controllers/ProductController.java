package Controllers;

import Data.*;
import Misc.HelperMethods;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
            System.out.println("init size " + init.size());
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
        numberOfProducts = ProductConnector.getAllByProjectId((Integer)activeProject.get(0)).size()+1;
        System.out.println(numberOfProducts);
        int currentProjectSize = ProductConnector.getAllByProjectId((Integer) activeProject.get(0)).size();

        if ((activeProject.get(4).equals("Multi-platform"))||((activeProject.get(4).equals("Bait & Hook")))||
                (activeProject.get(4).equals("Freemium"))) {
            if (numberOfProducts > 2) {
                HelperMethods.throwAlert(gridPaneLeft.getScene(), activeProject.get(4) + " cannot have more than two " +
                        "products at the moment.");
                return;
            }
        }
        if ((productNameTF.getText().equals(""))||(productDescriptionTF.getText().equals(""))) {
            HelperMethods.throwAlert(gridPaneLeft.getScene(), "Please fill out form");
            return;
        }
        String productDescription;
        if (productDescriptionTF.getText().equals("")) {
            productDescription = "-";
        } else {
            productDescription = productDescriptionTF.getText();
        }

        ArrayList<Object> queryResults = ProductConnector.insertIntoProduct((Integer) activeProject.get(0),
                productNameTF.getText(), productDescription, Date.valueOf(LocalDate.now()), 0.0,
                0.0, productVersionTF.getText(), Boolean.valueOf(null), Boolean.valueOf(null), 0.0);

        ProductPricingConnector.insertAllProductPricing((Integer) queryResults.get(0), "", 0.0,
                "", "", "", "", "","",
                "", "",0, "", "", "",
                0.0, 0.0, 0.0, 0.0, 0,
                0.0, 0.0, 0.0, "", 0.0,
                0.0, 0,0,0.0,0,
                0,0,0.0, 0.0,
                "", "");

        addProduct(queryResults, gridPaneLeft);
    }

    public void addProduct(ArrayList<Object> product, GridPane gridPaneChosen) {
        Label productNameLb = new Label((String)product.get(2));
        Label productDescriptionLb = new Label((String)product.get(3));
        Label productVersionLb = new Label((String)product.get(7));
        TextField productCostsTF;
        Label productPriceLb;

        try {
            DecimalFormat df = new DecimalFormat("#.##");
            productPriceLb = new Label(df.format((Double)product.get(6)));
        } catch (Exception e) {
            productPriceLb = new Label("Product Pricing Required");
            System.out.println(e.getMessage() + " " + e.getStackTrace());
        }
        productPriceLb.setId("productPriceLb" + product.get(0));

        try {
            productCostsTF = new TextField(String.valueOf(((Double)product.get(5))));
        } catch (Exception e) {
            productCostsTF = new TextField("Product Costing Required");
            System.out.println(e.getMessage() + " " + e.getStackTrace());
        }

        productCostsTF.setId("costId" + (Integer) product.get(0));
        productCostsTF.textProperty().addListener((observable, oldValue, newValue) -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Double newCost = Double.parseDouble(newValue);

                    ProductConnector.updateProductById((Integer) product.get(0),
                            (Integer) product.get(1),
                            (String) product.get(2),
                            (String) product.get(3),
                            (Date) product.get(4),
                            newCost,
                            (Double) product.get(6),
                            (String) product.get(7),
                            (Boolean) product.get(8),
                            (Boolean) product.get(9),
                            (Double) product.get(10));
                    /*
                    System.out.println("Hellooo");
                    System.out.println(newValue);

                     */
                }
            }, 1);
        });

        ArrayList<Object> productCopy = new ArrayList<>(product);

        Button productPricingButton = new Button("Pricing");
        productPricingButton.setId("productPricingButton" + product.get(0));
        if (((Boolean)product.get(8))&&(activeProject.get(4).equals("Freemium"))) {
            productPricingButton.setDisable(true);
        } else {
            productPricingButton.onActionProperty().setValue(actionEvent1 -> {
                Scene scene = ((Node)actionEvent1.getTarget()).getScene();
                SceneController.openView(scene, getClass(), activeProject, product,"priceSettingView.fxml");
            });
        }
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
                    (String)productPricingCopy.get(23),
                    (Double)productPricingCopy.get(24),
                    (Double)productPricingCopy.get(25),
                    (Integer) productPricingCopy.get(26),
                    (Integer)productPricingCopy.get(27),
                    (Double)productPricingCopy.get(28),
                    (Integer) productPricingCopy.get(29),
                    (Integer) productPricingCopy.get(30),
                    (Integer) productPricingCopy.get(31),
                    (Double)productPricingCopy.get(32),
                    (Double)productPricingCopy.get(33),
                    (String) productPricingCopy.get(34),
                    (String) productPricingCopy.get(35));

            addProduct(queryResults, gridPaneLeft);
        });

        Button productDeleteButton = new Button("Delete");
        productDeleteButton.onActionProperty().setValue(actionEvent1 -> {
            ProductConnector.deleteProductByProductId((Integer) product.get(0));
//            gridPaneChosen.getChildren().remove()
        });

        int rowIndex = gridPaneChosen.getRowCount()+1;

        HBox hbox = new HBox(productPricingButton, productCostingButton);
        hbox.setSpacing(5);
        HBox hBox2 = new HBox(productCopyButton, productDeleteButton);
        hBox2.setSpacing(5);
        VBox vBox = new VBox(hbox, hBox2);
        vBox.setSpacing(5);

        gridPaneChosen.add(productNameLb, 0, rowIndex);
        gridPaneChosen.add(productDescriptionLb, 1, rowIndex);
        gridPaneChosen.add(productPriceLb, 2, rowIndex);
        gridPaneChosen.add(productCostsTF,3, rowIndex);
        gridPaneChosen.add(productVersionLb, 4, rowIndex);
        gridPaneChosen.add(vBox, 6, rowIndex);

        if ((((String)activeProject.get(4)).equals("Bait & Hook"))||
                (((String)activeProject.get(4)).equals("Multi-platform"))||
                (((String)activeProject.get(4)).equals("Sub. + Multi-platform"))) {

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

            subsidizing.setId("subsidizing" + product.get(0));
            subsidizing.onActionProperty().setValue(actionEvent -> {
                subsidized.selectedProperty().setValue(false);

                ProductConnector.updateProductById((Integer) productCopy.get(0),
                        (Integer)productCopy.get(1),
                        productCopy.get(2).toString(),
                        productCopy.get(3).toString(),
                        Date.valueOf(LocalDate.now()),
                        (Double) productCopy.get(5),
                        (Double) productCopy.get(6),
                        (String) productCopy.get(7),
                        false, true,
                        (Double) productCopy.get(10));

                ArrayList<ArrayList<Integer>> check =
                        SubsidyConnector.getSubsidizerByProjectId((Integer) activeProject.get(0));
                if (check.size() > 0 ) {
                    for (int i = 0; i < check.size(); i++) {
                        if ((check.get(i).get(0) == null)||(Integer.parseInt(check.get(i).get(0).toString()))==0) {
                            if (Integer.parseInt(check.get(i).get(0).toString()) == (Integer)productCopy.get(1)) {
                                System.out.println("Can't subsidize itself");
                            } else {
                                SubsidyConnector.updateSubsidizedByProjectId((Integer)productCopy.get(0),
                                        (Integer) activeProject.get(0));
                            }
                        } else {
                            SubsidyConnector.deleteByProject((Integer) activeProject.get(0));
                            Integer subsidizedId = (Integer)check.get(i).get(0);
                            SubsidyConnector.insertIntoSubsidies((Integer) productCopy.get(0),
                                    subsidizedId, (Integer) activeProject.get(0));
                            HelperMethods.throwAlert(gridPaneChosen.getScene(), "Unfortunately subsidies are a one to one " +
                                    "relationship at the moment.");
                            CheckBox subsidizingTick = (CheckBox) gridPaneChosen.getScene().lookup("#subsidizing" + subsidizedId);
                            subsidizingTick.setSelected(false);
                            CheckBox subsidizedTick = (CheckBox) gridPaneChosen.getScene().lookup("#subsidized" + subsidizedId);
                            subsidizedTick.setSelected(true);
                            ProductConnector.updateProductById((Integer)product.get(0),
                                    (Integer)product.get(1),
                                    (String)product.get(2),
                                    (String)product.get(3),
                                    (Date)product.get(4),
                                    (Double) product.get(5),
                                    (Double) product.get(6),
                                    (String) product.get(7),
                                    false,
                                    true,
                                    (Double) product.get(10));
                            ProductConnector.updateProductById(subsidizedId,
                                    (Integer)product.get(1),
                                    (String)product.get(2),
                                    (String)product.get(3),
                                    (Date)product.get(4),
                                    (Double) product.get(5),
                                    (Double) product.get(6),
                                    (String) product.get(7),
                                    true,
                                    false,
                                    (Double) product.get(10));
                        }
                    }
                } else {
                    SubsidyConnector.insertIntoSubsidies((Integer) productCopy.get(0), null,
                            (Integer) activeProject.get(0));
                }
            });
            subsidized.setId("subsidized" + product.get(0));
            subsidized.onActionProperty().setValue(actionEvent -> {
                subsidizing.selectedProperty().setValue(false);

                ProductConnector.updateProductById((Integer) productCopy.get(0),
                        (Integer)productCopy.get(1),
                        productCopy.get(2).toString(),
                        productCopy.get(3).toString(),
                        Date.valueOf(LocalDate.now()),
                        (Double) productCopy.get(5),
                        (Double) productCopy.get(6),
                        (String) productCopy.get(7),
                        true, false,
                        (Double) productCopy.get(10));

                ArrayList<ArrayList<Integer>> check =
                        SubsidyConnector.getSubsidizerByProjectId((Integer) activeProject.get(0));
                System.out.println("-------------------- subsidized clicked --------------------");
                System.out.println(check);
                if (check.size() > 0 ) {
                    System.out.println("debug subsidizED 1");
                    for (int i = 0; i < check.size(); i++) {
                        if ((check.get(i).get(1) == null)||(check.get(i).get(1)==0)) {
                            System.out.println("debug subsidizED 1.2");
                            if (Integer.parseInt(check.get(i).get(1).toString()) == (Integer)productCopy.get(0)) {
                                System.out.println("Can't subsidize itself");
                            } else {
                                SubsidyConnector.updateSubsidizerByProjectId((Integer)productCopy.get(0),
                                        (Integer) activeProject.get(0));
                            }
                        } else {
                            SubsidyConnector.deleteByProject((Integer) activeProject.get(0));
                            System.out.println("debug subsidizED 1.2");
                            Integer subsidizingId = (Integer)check.get(i).get(1);
                            SubsidyConnector.insertIntoSubsidies(subsidizingId,
                                    (Integer) productCopy.get(0), (Integer) activeProject.get(0));
                            HelperMethods.throwAlert(gridPaneChosen.getScene(), "Unfortunately subsidies are a one to one " +
                                    "relationship at the moment.");
                            CheckBox subsidizingTick = (CheckBox) gridPaneChosen.getScene().lookup("#subsidized" + subsidizingId);
                            subsidizingTick.setSelected(false);
                            CheckBox subsidizedTick = (CheckBox) gridPaneChosen.getScene().lookup("#subsidizing" + subsidizingId);
                            subsidizedTick.setSelected(true);
                            ProductConnector.updateProductById((Integer)product.get(0),
                                    (Integer)product.get(1),
                                    (String)product.get(2),
                                    (String)product.get(3),
                                    (Date)product.get(4),
                                    (Double) product.get(5),
                                    (Double) product.get(6),
                                    (String) product.get(7),
                                    true,
                                    false,
                                    (Double) product.get(10));

                        }
                    }
                } else {
                    System.out.println("debug subsidizED 2");
                    SubsidyConnector.insertIntoSubsidies(null,
                            (Integer) productCopy.get(0), (Integer) activeProject.get(0));
                }
            });
            SceneController.activeProduct = ProductConnector.getProductByProjectAndProduct((Integer)activeProject.get(0),
                    (String)product.get(2));

            HBox subsidyHBox = new HBox(subsidizing, subsidized);
            System.out.println("########## IDs " + product.get(0) + " ##########");
            System.out.println(subsidizing.getId());
            System.out.println(subsidized.getId());
            subsidyHBox.spacingProperty().setValue(15);

            gridPaneChosen.add(subsidizingTitleLbl, 5, 2);
            gridPaneChosen.add(subsidyHBox, 5, rowIndex);

        } else if (((String)activeProject.get(4)).equals("Freemium")) {

            /*############################################################*/
            /*                         Holla                              */
            /*############################################################*/

            CheckBox subsidized = new CheckBox();

            Label subsidizingTitleLbl = new Label("Is Freemium?");
            if (((Boolean)product.get(8))) {
                subsidized.selectedProperty().setValue(true);
            }

            subsidized.setId("Freemium"+product.get(0));
            subsidized.onActionProperty().setValue(actionEvent -> {
                productPricingButton.setDisable(true);
                // TODO replace this with setting other boxes to false
                //subsidizing.selectedProperty().setValue(false);

                ProductConnector.updateProductById((Integer) productCopy.get(0),
                        (Integer)productCopy.get(1),
                        productCopy.get(2).toString(),
                        productCopy.get(3).toString(),
                        Date.valueOf(LocalDate.now()),
                        (Double) productCopy.get(5),
                        0.0,
                        (String) productCopy.get(7),
                        true, false,
                        (Double) productCopy.get(10));

                ArrayList<ArrayList<Integer>> check =
                        SubsidyConnector.getSubsidizerByProjectId((Integer) activeProject.get(0));

                if (check.size() > 0){
                    for (int i = 0; i < check.size(); i++) {
                        if (check.get(i).get(1) != product.get(0)) {
                            Integer subsidizingId = (Integer)check.get(i).get(1);
                            CheckBox subsidizingTick = (CheckBox) gridPaneChosen.getScene().lookup("#Freemium" + subsidizingId);
                            subsidizingTick.setSelected(false);

                            SubsidyConnector.deleteByProject((Integer) activeProject.get(0));
                            SubsidyConnector.insertIntoSubsidies(subsidizingId,
                                    (Integer) productCopy.get(0), (Integer) activeProject.get(0));
                            HelperMethods.throwAlert(gridPaneChosen.getScene(), "Unfortunately Freemium is a one to one " +
                                    "relationship at the moment.");

                            ProductPricingConnector.updateProductSubsidizationDegree((Integer) product.get(0),
                                    100.0);
                            ProductPricingConnector.updateProductSubsidizationDegree(subsidizingId,
                                    100.0);

                            ProductConnector.updateProductById(subsidizingId,
                                    (Integer)product.get(1),
                                    (String)product.get(2),
                                    (String)product.get(3),
                                    (Date)product.get(4),
                                    (Double) product.get(5),
                                    (Double) product.get(6),
                                    (String) product.get(7),
                                    false,
                                    true,
                                    (Double) product.get(10));
                            ((Button)gridPaneChosen.getScene().lookup("#productPricingButton" + subsidizingId)).setDisable(false);
                        }
                    }
                } else {
                    SubsidyConnector.insertIntoSubsidies(null,
                            (Integer) productCopy.get(0), (Integer) activeProject.get(0));
                }
            });
            SceneController.activeProduct = ProductConnector.getProductByProjectAndProduct((Integer)activeProject.get(0),
                    (String)product.get(2));

            System.out.println("########## IDs " + product.get(0) + " ##########");
            System.out.println(subsidized.getId());

            gridPaneChosen.add(subsidizingTitleLbl, 5, 2);
            gridPaneChosen.add(subsidized, 5, rowIndex);
        }

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
                productRelationCB.getSelectionModel().select(0);
                gridPaneChosen.add(productRelationCB, 1, 1);

                ComboBox strengthProductRelation = new ComboBox();
                if (numberOfProducts == 2) {
                    strengthProductRelation.setId("strengthProductRelation");
                    strengthProductRelation.getItems().add("Weak");
                    strengthProductRelation.getItems().add("Medium");
                    strengthProductRelation.getItems().add("Strong");
                    gridPaneChosen.add(strengthProductRelation, 2, 1);
                } else {
//                    ((ComboBox) gridPaneChosen.getScene().lookup("#strengthProductRelation")).getItems().removeAll();
                }

                Button updateProductRelation = new Button();
                gridPaneChosen.add(updateProductRelation, 3, 1);
                updateProductRelation.setText("Update");
                updateProductRelation.onActionProperty().setValue(actionEvent -> {
                    updateButtonClick();
                });
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
        if ((competitorNameTF.getText().equals(""))||(competitorPriceTF.getText().equals("")) ||
                (competitorSales.getText().equals(""))) {
            HelperMethods.throwAlert(gridPaneLeft.getScene(), "Please fill out form");
            return;
        }

        String competitorName = competitorNameTF.getText();
        Double competitorPrice = Double.parseDouble(competitorPriceTF.getText());
        Double competitorSalesDouble = Double.parseDouble(competitorSales.getText());

        Double competitorQuality = Double.valueOf(Math.round(competitorQualitySlider.getValue()));
        addCompetitor(competitorName,
                competitorPrice,
                competitorSalesDouble,
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
        if (competitorQueryResults.size() == 0) {
            HelperMethods.throwAlert(gridPaneLeft.getScene(),"Please add at least one competitor first.");
            return;
        }
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

        if (competitorQueryResults.size() == 0) {
            HelperMethods.throwAlert(gridPaneLeft.getScene(),"Please add at least one competitor first.");
            return;
        }

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

    public void updateButtonClick() {
        int sign = 1;
        int index;
        if (numberOfProducts > 2) {
            index = 1;
        } else {
            index = ((ComboBox) gridPaneLeft.getScene().lookup("#productRelationCBId")).getSelectionModel().getSelectedIndex();
        }
        if (index == 1) {
            sign = 1;
        } else if (index == 2){
            sign = -1;
        } else if (index == 3) {
            HelperMethods.throwAlert(gridPaneLeft.getScene(), "Nothing to update");
            return;
        }

        int indexStrength = ((ComboBox) gridPaneLeft.getScene().lookup("#strengthProductRelation")).getSelectionModel().getSelectedIndex();
        ArrayList<ArrayList<Object>> productList = ProductConnector.getAllByProjectId((Integer) activeProject.get(0));
        Double crossPriceElasticity = 1.0;
        if (indexStrength == 0) {
            crossPriceElasticity = 0.15;
        } else if (indexStrength == 1) {
            crossPriceElasticity = 0.45;
        } else if (indexStrength == 2) {
            crossPriceElasticity = 0.9;
        }

        ArrayList<Object> respectiveProductPricingB = ProductPricingConnector.getAllProductPricingByProductId((Integer) productList.get(1).get(0));
        Integer b = (Integer) respectiveProductPricingB.get(11);
        // Marginal costs - ignored for now
        Double mc = 1.0;
        // get substitutes or complement combobox

        System.out.println(productList);
        for (int i = 0; i < productList.size(); i++) {
//            System.out.println("i " + i);
            ArrayList<Object> respectiveProductPricing = ProductPricingConnector.getAllProductPricingByProductId((Integer) productList.get(i).get(0));
            Double currentPrice = (Double) productList.get(i).get(6);
            if (currentPrice == 0) {
                HelperMethods.throwAlert(gridPaneLeft.getScene(), "Product prices have to be set first");
                return;
            }
            Double maxPrice = (Double) respectiveProductPricing.get(21);
            Integer currentCustomers = (Integer) respectiveProductPricing.get(11);
            Double maxPriceCustomers = (Double) respectiveProductPricing.get(24);
//            Double percentageChangePrice = Math.abs(100 - ((currentPrice/maxPrice)*100));
//            Double percentageChangeCustomers = Math.abs(100 - (currentCustomers/maxPriceCustomers*100));
            Double percentageChangePrice = 100 - ((currentPrice/maxPrice)*100);
            Double percentageChangeCustomers = 100 - (currentCustomers/maxPriceCustomers*100);
            Double selfPriceElasticity = percentageChangeCustomers/percentageChangePrice;
            Double test = 0.0;
            for (int j = 1; j < productList.size(); j++) {
//                System.out.println("j: " + j);
                ArrayList<Object> tempProductPricing = ProductPricingConnector.getAllProductPricingByProductId((Integer) productList.get(j).get(0));
                Integer salesQuantity = (Integer) tempProductPricing.get(11);
                // TODO consider what to do with tbd
                Integer tbd = 1 * sign;
//                Double crossPriceElasticity = (currentPrice/salesQuantity)*tbd;
                crossPriceElasticity *= tbd;
                Double priceB = (Double) productList.get(j).get(6);
                Double returnValue = (crossPriceElasticity/(1+selfPriceElasticity))*((salesQuantity*(priceB-mc))/currentCustomers);

                test += returnValue;
            }
//            System.out.println("1st part: " + (selfPriceElasticity/(selfPriceElasticity+1)) * mc);
            Double newPrice = Math.abs((selfPriceElasticity/(selfPriceElasticity+1)) * mc - test);
//            System.out.println("selfPriceElasticity: " + selfPriceElasticity);
            FuzzyLogic clusteringRangeFLM = new FuzzyLogic();
            FuzzyLogic clusteringRangeFLMCopy = new FuzzyLogic();
            clusteringRangeFLM.init("clusteringRangeFB");

//            System.out.println("priceClusterFLOutput " + priceClusterFLOutput);
            System.out.println("Suggested new price: " + (currentPrice + newPrice));
            Double productBundlingPrice = currentPrice + newPrice;

            Double priceClusterFLOutput = HelperMethods.returnClusteringReaction(
                    (Double) respectiveProductPricing.get(16),
                    (Double) respectiveProductPricing.get(25),
                    productBundlingPrice,
                    (Double) respectiveProductPricing.get(17),
                    (Double) respectiveProductPricing.get(18));
            productBundlingPrice *= priceClusterFLOutput;

            DecimalFormat df = new DecimalFormat("#.##");
            productBundlingPrice = Double.parseDouble(df.format(productBundlingPrice));

            try {
                ProductConnector.updateProductById((Integer) productList.get(i).get(0),
                        (Integer) productList.get(i).get(1),
                        (String) productList.get(i).get(2),
                        (String) productList.get(i).get(3),
                        (Date) productList.get(i).get(4),
                        (Double) productList.get(i).get(5),
                        productBundlingPrice,
                        (String) productList.get(i).get(7),
                        (Boolean) productList.get(i).get(8),
                        (Boolean) productList.get(i).get(9),
                        (Double) productList.get(i).get(10));

                Label productPriceLb = ((Label) gridPaneLeft.getScene().lookup("#productPriceLb" + productList.get(i).get(0)));
                productPriceLb.setText(df.format(productBundlingPrice));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
                System.out.println(e.getCause());
                HelperMethods.throwAlert(gridPaneLeft.getScene(), "There seems to be an error updating the prices.");
            }

//            System.out.println("priceClusterFLOutputCopy " + priceClusterFLOutputCopy);
            //priceClusterFLOutput = clusteringRangeFLM.getFunctionBlock().getVariable("clusteringReaction").getValue();

            System.out.println("-------------------- ROUND " + i + " OVER --------------------");
        }
    }
}
