package Misc;

import Models.FuzzyLogic;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.util.ArrayList;

public class HelperMethods {
    public static void throwAlert(Scene scene, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.initOwner(scene.getWindow());
        alert.showAndWait();
        return;
    }

    public static void priceClusteringChange(Double currentPrice, Double allowedVariance,
                                               Double rangeLow, Double rangeHigh) {
        allowedVariance = allowedVariance/100;
        Double average = (rangeLow + rangeHigh)/2;
        Double averageDistance = rangeHigh - average;
//        Double currentPrice = (Double) activeProduct.get(6);

        String tooLow = "(0, 1) (" + (rangeLow-(rangeLow*allowedVariance)) + ", 1) (" + rangeLow + ", 0)";
        String lowerBorder = "(" + (rangeLow*(1-allowedVariance)) + ",0) (" + rangeLow + ",1) " +
                "(" + (rangeLow*(1+allowedVariance)) +",0)";
//        String acceptablePriceRange = "gbell 1 " + averageDistance +  " " + average;
        String acceptablePriceRange = "(" + rangeLow + ", 0) ("+ average + ", 1) (" + rangeHigh + ", 0)";
        String upperBorder = "(" + (rangeHigh*(1-allowedVariance)) + ",0) (" + rangeHigh + ",1) " +
                "(" + (rangeHigh*(1+allowedVariance)) +",0)";
        String tooHigh = "(" + rangeHigh + ", 0) (" + (rangeHigh*(1+allowedVariance)) + ", 1)";

        FuzzyLogic.replaceClusteringTerm(1, tooLow);
        FuzzyLogic.replaceClusteringTerm(2, lowerBorder);
        FuzzyLogic.replaceClusteringTerm(3, acceptablePriceRange);
        FuzzyLogic.replaceClusteringTerm(4, upperBorder);
        FuzzyLogic.replaceClusteringTerm(5, tooHigh);

        String decreaseStrongly;
        String increaseStrongly;
        String povChange = "(" + rangeLow/currentPrice + " ,0) (" + average/currentPrice + ",1) " +
                " (" + rangeHigh/currentPrice + ",0)";

        if (currentPrice < rangeLow) {
            decreaseStrongly = "(0,1) (0.9, 1)";
            increaseStrongly = povChange;
        } else if (currentPrice > rangeHigh) {
            increaseStrongly = "(0,1) (0.9, 1)";
            decreaseStrongly = "(" + average/currentPrice + " ,0) (" + rangeHigh/currentPrice + ",1) " +
                    " (" + currentPrice/currentPrice + ",0)";
        } else {
            increaseStrongly = "(0.9,0) (0.95,1) (1,0)";
            decreaseStrongly = "(1,0) (1.05, 1) (1.1,0)";
        }

        FuzzyLogic.replaceClusteringTerm(6, decreaseStrongly);
        FuzzyLogic.replaceClusteringTerm(7, increaseStrongly);

        FuzzyLogic.replacePriceTerm(6, decreaseStrongly);
        FuzzyLogic.replacePriceTerm(7, increaseStrongly);
    }

    public static Double returnClusteringReaction(Double allowedVariance, Double clusterImportance, Double currentPrice,
                                           Double rangeLow, Double rangeHigh) {
        priceClusteringChange(currentPrice, allowedVariance, rangeLow, rangeHigh);

        FuzzyLogic clusteringRangeFLM = new FuzzyLogic();
        clusteringRangeFLM.init("clusteringRangeFB");

        clusteringRangeFLM.functionBlockSetVariable("currentPrice", currentPrice);
        clusteringRangeFLM.functionBlockSetVariable("rangeImportance", clusterImportance);

        clusteringRangeFLM.evaluate();

        Double clusteringReaction = clusteringRangeFLM.getFunctionBlock().getVariable("clusteringReaction").getValue();
        return  clusteringReaction;
    }
}
