package Data;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.JFuzzyLogic;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;


public class FuzzyLogic {

    private FunctionBlock functionBlock;

    public void init(String fileNamePath) {
        String fileName = "C:\\Users\\maxim\\IdeaProjects\\DissertationProject\\src\\Misc\\ruleBase.fcl";
        FIS fis = FIS.load(fileName, true);
        if (fis == null) {
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }
        functionBlock = fis.getFunctionBlock(null);
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

