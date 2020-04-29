package Controllers;

import javafx.fxml.FXMLLoader;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


public class FuzzyLogic {

    public FunctionBlock functionBlock;

    public void init(String selectedFunctionBlock) throws MalformedURLException {
//        URL location = getClass().getResource("/src/Models/"+selectedFunctionBlock+".fcl");
        File fileRelative2 = new File("src/Models/" + selectedFunctionBlock + ".fcl");
        String fileName = "src/Models/" + selectedFunctionBlock + ".fcl";
        URL location = fileRelative2.toURI().toURL();
        System.out.println(fileRelative2.getAbsolutePath());
        System.out.println(fileRelative2.toURI());
        System.out.println(fileRelative2.toURI().toURL().getPath());
        System.out.println(location);
        FIS fis = FIS.load(location.getPath(), true);
        if (fis == null) {
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }
        functionBlock = fis.getFunctionBlock(selectedFunctionBlock);
    }

    public FunctionBlock getFunctionBlock() {
        return functionBlock;
    }

    public void setFunctionBlock(FunctionBlock functionBlock) {
        this.functionBlock = functionBlock;
    }

    public void functionBlockSetVariable(String variableName, Double variableValue) {
        functionBlock.setVariable(variableName, variableValue);
    }

    public void evaluate() {
        functionBlock.evaluate();
    }

    public void getChartFunctionBlock() {
        JFuzzyChart.get().chart(functionBlock);
    }

    public void getChartVariable(String variableToGet) {
        Variable variable = functionBlock.getVariable(variableToGet);
        JFuzzyChart.get().chart(variable, variable.getDefuzzifier(), true);
    }

    public static void replacePriceTerm(int index, String ranges) {
        File fileRelative2 = new File("src/Models/pricingFB.fcl");
        File log= new File(fileRelative2.getAbsolutePath());
        String term;
        if (index == 1) {
            term = "TERM priceLow";
        } else if (index == 2) {
            term = "TERM priceMedium";
        } else if (index == 3) {
            term = "TERM priceMax";
        } else if (index == 4) {
            term = "TERM priceHigh";
        } else if (index == 5) {
            term = "TERM priceCompetition";
        } else if (index == 6) {
            term = "TERM decreaseStrongly";
        } else if (index == 7) {
            term = "TERM increaseStrongly";
        } else if (index == 8) {
            term = "TERM priceValueAdded";
        } else {
            term = "// eh...";
        }
        try {
            FileReader fr = new FileReader(log);
            String s;
            String totalStr = "";
            try (BufferedReader br = new BufferedReader(fr)) {
                while ((s = br.readLine()) != null) {
                    if (s.contains(term)) {
                        totalStr += term + " := " + ranges + ";" +"\n" ;
                    } else {
                        totalStr += s +"\n";
                    }
                }
                //totalStr = totalStr.replaceAll(string1, replacementString);
                FileWriter fw = new FileWriter(log);
                fw.write(totalStr);
                fw.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void replaceClusteringTerm(int index, String ranges) {
        File fileRelative2 = new File("src/Models/clusteringRangeFB.fcl");
        File log= new File(fileRelative2.getAbsolutePath());
        String term;
        if (index == 1) {
            term = "TERM tooLow";
        } else if (index == 2) {
            term = "TERM lowerBorder";
        } else if (index == 3) {
            term = "TERM acceptablePriceRange";
        } else if (index == 4) {
            term = "TERM upperBorder";
        } else if (index == 5) {
            term = "TERM tooHigh";
        } else if (index == 6) {
            term = "TERM decreaseStrongly";
        } else if (index == 7) {
            term = "TERM increaseStrongly";
        } else {
            term = "// eh...";
        }
        try {
            FileReader fr = new FileReader(log);
            String s;
            String totalStr = "";
            try (BufferedReader br = new BufferedReader(fr)) {
                while ((s = br.readLine()) != null) {
                    if (s.contains(term)) {
                        totalStr += term + " := " + ranges + ";" +"\n" ;
                    } else {
                        totalStr += s +"\n";
                    }
                }
                FileWriter fw = new FileWriter(log);
                fw.write(totalStr);
                fw.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void chart() {}

    public void test() {
        String fileName = "Models/pricingFB.fcl";
        FIS fis = FIS.load(fileName, true);
        if (fis == null) {
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }
        // Show ruleset
        FunctionBlock functionBlock = fis.getFunctionBlock("tipper");

        functionBlock.setVariable("commodity", 1);
        functionBlock.setVariable("quality", 1);
        functionBlock.setVariable("imitability", 1);
        functionBlock.setVariable("customerExpectations", 1);
        functionBlock.setVariable("marketCharacteristics", 1);
        functionBlock.setVariable("pricingStrategy", 1);
        functionBlock.setVariable("marketSize", 1);
        functionBlock.setVariable("priceDiscrimination", 1);
        functionBlock.setVariable("brand", 1);
        functionBlock.setVariable("channel", 1);
        functionBlock.setVariable("priceDetermination", 1);
        functionBlock.setVariable("priceElasticity", 1);

        functionBlock.evaluate();

        System.out.println(functionBlock);
        System.out.println("Price: " + functionBlock.getVariable("price").getValue());
    }
}

