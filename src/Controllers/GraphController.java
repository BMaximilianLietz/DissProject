package Controllers;

import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.util.ArrayList;


public class GraphController {
    // TODO rename

    private final NumberAxis lineXAxis = new NumberAxis();

    private final NumberAxis lineYAxis = new NumberAxis();
    private final LineChart<Number,Number> lineChart =
            new LineChart<Number,Number>(lineXAxis,lineYAxis);

    private final NumberAxis bubbleXAxis = new NumberAxis();
    private final NumberAxis bubbleYAxis = new NumberAxis();
    private final BubbleChart<Number,Number> bubbleChart = new
            BubbleChart<Number,Number>(bubbleXAxis,bubbleYAxis);

    private final CategoryAxis barXAxis = new CategoryAxis();
    private final NumberAxis barYAxis = new NumberAxis();
    private final BarChart<String,Number> barChart =
            new BarChart<String,Number>(barXAxis,barYAxis);

    //region Line chart functionality
    //region
    public void setAxes(String xAxisLabel, String yAxisLabel) {
        lineXAxis.setLabel(yAxisLabel);
        lineYAxis.setLabel(xAxisLabel);
    }

    public void setLineChartData(String seriesName, ArrayList<ArrayList<Object>> graphPoints, String lineChartTitle) {
        XYChart.Series series = new XYChart.Series();
        series.setName(seriesName);
        lineChart.setTitle(lineChartTitle);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = i;
            Double y = (Double) graphPoints.get(i).get(1);
            series.getData().add(new XYChart.Data(x, y));
        }
        lineChart.getData().add(series);
    }

    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }

    public void showLineChart() {
        Stage stage = new Stage();
        Scene scene = new Scene(lineChart);
        stage.setScene(scene);
        stage.show();
    }
    //endregion

    //region Bubble chart functionality
    //region
    public void setBubbleChartAxes(String yAxisLabel, String xAxisLabel, String unit) {
        bubbleYAxis.setLabel(yAxisLabel + unit);
        bubbleXAxis.setLabel(xAxisLabel);
    }

    public void setBubbleChartData(String seriesName, ArrayList<Double> graphPoints,
                                   String bubbleChartTitle, int divisor) {
        XYChart.Series series = new XYChart.Series();
        series.setName(seriesName);
        bubbleChart.setTitle(bubbleChartTitle);

//        TODO add z and make it price index variable (but small)
        Double x = graphPoints.get(0);
        Double y = graphPoints.get(1) / divisor;
//        Double z = graphPoints.get(i).get(2);

        series.getData().add(new XYChart.Data(x, y));

        bubbleChart.getData().add(series);
    }

    public BubbleChart<Number, Number> getBubbleChart() {
        return bubbleChart;
    }

    public void showBubbleChart() {
        Stage stage = new Stage();
        Scene scene = new Scene(bubbleChart);
        stage.setScene(scene);
        stage.show();
    }
    //endregion

    //region Bar chart functionality
    //region
    public void setBarChartAxes(String yAxisLabel, String xAxisLabel) {
        barYAxis.setLabel(yAxisLabel);
        barXAxis.setLabel(xAxisLabel);
    }

    public void setBarChartData(String seriesName, ArrayList<ArrayList<String>> graphPoints, String barChartTitle) {
        XYChart.Series series = new XYChart.Series();
        series.setName(seriesName);
        barChart.setTitle(barChartTitle);
        for (int i = 0; i < graphPoints.size(); i++) {
            String x = graphPoints.get(i).get(0);
            Double y = Double.parseDouble(graphPoints.get(i).get(1));
            series.getData().add(new XYChart.Data(x, y));
        }
        barChart.getData().add(series);
    }

    public void showBarChart() {
        Stage stage = new Stage();
        Scene scene = new Scene(barChart);
        stage.setScene(scene);
        stage.show();
    }
    //endregion


}
