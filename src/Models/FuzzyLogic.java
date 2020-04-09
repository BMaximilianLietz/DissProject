package Models;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.JFuzzyLogic;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.io.*;


public class FuzzyLogic {

    public FunctionBlock functionBlock;

    public void init(String selectedFunctionBlock) {
        String fileName = "C:\\Users\\maxim\\IdeaProjects\\DissertationProject\\src\\Misc\\ruleBase.fcl";
        FIS fis = FIS.load(fileName, true);
        if (fis == null) {
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }
        functionBlock = fis.getFunctionBlock(selectedFunctionBlock);
    }

    public void initPD(String selectedFunctionBlock) {
        String fileName = "C:\\Users\\maxim\\IdeaProjects\\DissertationProject\\src\\Misc\\priceDevelopmentRules.fcl";
        FIS fis = FIS.load(fileName, true);
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
        //System.out.println(functionBlock.getVariable(variableName));
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
        File log= new File("C:/Users/maxim/IdeaProjects/DissertationProject/src/Misc/ruleBase.fcl");
        String term;
        if (index == 1) {
            term = "TERM priceLow";
        } else if (index == 2) {
            term = "TERM priceMedium";
        } else {
            term = "TERM priceHigh";
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

    public void chart() {}

    public void test() {
        String fileName = "C:\\Users\\maxim\\IdeaProjects\\DissertationProject\\src\\Misc\\ruleBase.fcl";
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

