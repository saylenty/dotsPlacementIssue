package com.gitlab.saylenty.visual;

import com.gitlab.saylenty.generator.PointPositionGenerator;
import com.gitlab.saylenty.infrastructure.Point;
import com.gitlab.saylenty.strategy.PointsFinderStrategy;
import com.gitlab.saylenty.task.PositionFinderCountedCompleter;
import com.gitlab.saylenty.task.PositionFinderTask;
import com.gitlab.saylenty.visual.elements.LabeledChartNode;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

import java.util.List;

public class UIMainController {

    // TODO read matrix from outside
    private final int[][] matrix = new int[][]{
            {0, 4, 3, 4, 3, 3, 6, 3, 4, 2},
            {4, 0, 3, 4, 7, 7, 6, 5, 4, 2},
            {3, 3, 0, 3, 6, 6, 9, 6, 7, 5},
            {4, 4, 3, 0, 3, 5, 10, 7, 8, 6},
            {3, 7, 6, 3, 0, 4, 9, 6, 7, 5},
            {3, 7, 6, 5, 4, 0, 5, 2, 7, 5},
            {6, 6, 9, 10, 9, 5, 0, 3, 4, 4},
            {3, 5, 6, 7, 6, 2, 3, 0, 5, 3},
            {4, 4, 7, 8, 7, 7, 4, 5, 0, 2},
            {2, 2, 5, 6, 5, 5, 4, 3, 2, 0}
    };

    public RadioButton CountedCompleter;
    public RadioButton ForkJoinTask;
    public TextArea InfoLog;

    private List<List<Point>> result;
    private int currentSolution;
    private PointsFinderStrategy currentStrategy;

    @FXML
    public ScatterChart<Number, Number> ScatterChart;
    @FXML
    public Button Next;
    @FXML
    public NumberAxis xAxis;
    @FXML
    public NumberAxis yAxis;

    @FXML
    private void initialize() {
        OnCountedCompleterSelected();
        OnGetNextSolution();
    }

    @FXML
    public void OnGetNextSolution() {
        if (result == null) {
            // TODO add ability to choose strategy from UI
            // run task and wait until complete
            long startTime = System.currentTimeMillis();
            result = currentStrategy.findSolution(matrix);
            long endTime = System.currentTimeMillis();
            InfoLog.appendText(String.format("Completed in %d ms\n", endTime - startTime));
        }
        if (currentSolution < result.size()) {
            ScatterChart.getData().clear(); // clear the current chart
            // we need to find the chart min-max bounds
            int minX = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxY = Integer.MIN_VALUE;

            List<Point> points = result.get(currentSolution);
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(String.format("Points visual representation %d", currentSolution + 1));
            int bound = points.size();
            for (int i = 0; i < bound; i++) {
                Point point = points.get(i);
                if (point.getX() < minX) {
                    minX = point.getX();
                }
                if (point.getX() > maxX) {
                    maxX = point.getX();
                }
                if (point.getY() < minY) {
                    minY = point.getY();
                }
                if (point.getY() > maxY) {
                    maxY = point.getY();
                }
                XYChart.Data<Number, Number> data = new XYChart.Data<>(point.getX(), point.getY());
                data.setNode(new LabeledChartNode(i));
                series.getData().add(data);
            }
            ScatterChart.getData().add(series);
            currentSolution++;
            // update new bounds for a chart
            updateChartScales(minX, maxX, minY, maxY, 1, 1);
            // make the solutions be cyclic
            if (currentSolution == result.size()) {
                currentSolution = 0;
            }
        } else {
            // TODO No solution found
        }
    }

    @FXML
    public void OnCountedCompleterSelected() {
        InfoLog.appendText("Use CountedCompleter algorithm\n");
        currentStrategy = new PositionFinderCountedCompleter(matrix, new PointPositionGenerator());
        resetResults();
    }

    @FXML
    public void OnForkJoinTaskSelected() {
        InfoLog.appendText("Use ForkJoinTask algorithm\n");
        currentStrategy = new PositionFinderTask(matrix, new PointPositionGenerator());
        resetResults();
    }

    private void resetResults() {
        result = null;
        currentSolution = 0;
        ScatterChart.getData().clear();
    }

    /**
     * Applies specified bounds for a chart
     *
     * @param minX     minimal xAxis value
     * @param maxX     maximal xAxis value
     * @param minY     minimal yAxis value
     * @param maxY     maximal xAxis value
     * @param margin   upper-bottom margin
     * @param tickUnit tick for xAxis and yAxis
     */
    private void updateChartScales(int minX, int maxX, int minY, int maxY, int margin, int tickUnit) {
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(minX - margin);
        xAxis.setUpperBound(maxX + margin);
        xAxis.setTickUnit(tickUnit);
        yAxis.setLowerBound(minY - margin);
        yAxis.setUpperBound(maxY + margin);
        yAxis.setTickUnit(tickUnit);
    }
}
