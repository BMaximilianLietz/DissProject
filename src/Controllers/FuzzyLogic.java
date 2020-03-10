package Controllers;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.JFuzzyLogic;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;


public class FuzzyLogic {

    public void test() {
        String fileName = "C:\\Users\\maxim\\IdeaProjects\\DissertationProject\\src\\Misc\\ruleBase.fcl";
        FIS fis = FIS.load(fileName, true);
        if (fis == null) {
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }
        // Show ruleset
        FunctionBlock functionBlock = fis.getFunctionBlock(null);
        JFuzzyChart.get().chart(functionBlock);

        functionBlock.setVariable("commodity", 1);
        //functionBlock.setVariable("food", 7);

        functionBlock.evaluate();

        Variable tip = functionBlock.getVariable("price");
        JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);

        System.out.println(functionBlock);
        System.out.println("Price: " + functionBlock.getVariable("price").getValue());
    }
}

