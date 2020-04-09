package Models;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.chart.NumberAxis;

import java.util.ArrayList;


public class FuturePricing {
    // TODO rename

    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    final LineChart<Number,Number> lineChart =
            new LineChart<Number,Number>(xAxis,yAxis);

    public void lineChartInit(String yAxisLabel, String xAxisLabel, String lineChartTitle) {

        yAxis.setLabel(yAxisLabel);
        xAxis.setLabel(xAxisLabel);
        lineChart.setTitle(lineChartTitle);

    }

    public void setLineChartData(String seriesName, ArrayList<ArrayList<Object>> graphPoints) {
        XYChart.Series series = new XYChart.Series();
        series.setName(seriesName);
        for (int i = 0; i < graphPoints.size(); i++) {

            int x = i;
            Double y = (Double) graphPoints.get(i).get(1);
            series.getData().add(new XYChart.Data(x, y));

//            series.getData().add(new XYChart.Data(x, y));
        }
        lineChart.getData().add(series);
    }

    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }

}
