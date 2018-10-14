package com.gitlab.saylenty.visual.controllers;

import com.gitlab.saylenty.entity.Point;
import com.gitlab.saylenty.generator.PointPositionGenerator;
import com.gitlab.saylenty.strategy.PointsFinderStrategy;
import com.gitlab.saylenty.strategy.RotationPointsFinder;
import com.gitlab.saylenty.strategy.SimplePointsFinder;
import com.gitlab.saylenty.strategy.concurrent.task.PositionFinderCountedCompleter;
import com.gitlab.saylenty.strategy.concurrent.task.PositionFinderTask;
import com.gitlab.saylenty.visual.elements.LabeledChartNode;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;

import java.util.List;

public class UIMainController {

    private List<Point[]> result;
    private int currentSolution;
    private PointsFinderStrategy currentStrategy;

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

    @FXML
    public ToggleGroup Algorithm;
    @FXML
    public RadioButton CountedCompleter;
    @FXML
    public RadioButton ForkJoinTask;
    @FXML
    public RadioButton SimpleSolution;
    @FXML
    public RadioButton RotationSolution;
    @FXML
    public TextArea InfoLog;
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
            // run task and wait until complete
            long startTime = System.currentTimeMillis();
            result = currentStrategy.findSolution(matrix);
            long endTime = System.currentTimeMillis();
            InfoLog.appendText(String.format("Completed in %d ms\n", endTime - startTime));
        }
        if (currentSolution < result.size()) {
            ScatterChart.getData().clear(); // clear the current chart
            // we need to find the chart min-max bounds
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;

            Point[] points = result.get(currentSolution);
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(String.format("Points visual representation %d", currentSolution + 1));
            int bound = points.length;
            for (int i = 0; i < bound; i++) {
                Point point = points[i];
                int x = point.getX();
                int absX = Math.abs(x);
                if (absX > maxX){
                    maxX = absX;
                }
                int y = point.getY();
                int absY = Math.abs(y);
                if (absY > maxY){
                    maxY = absY;
                }
                XYChart.Data<Number, Number> data = new XYChart.Data<>(x, y);
                data.setNode(new LabeledChartNode(i));
                series.getData().add(data);
            }
            ScatterChart.getData().add(series);
            currentSolution++;
            // update new bounds for a chart
            updateChartScales(-maxX, maxX, -maxY, maxY, 1, 1);
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
        currentStrategy = new PositionFinderCountedCompleter(new PointPositionGenerator());
        resetResults();
    }

    @FXML
    public void OnForkJoinTaskSelected() {
        InfoLog.appendText("Use ForkJoinTask algorithm\n");
        currentStrategy = new PositionFinderTask(new PointPositionGenerator());
        resetResults();
    }

    @FXML
    public void OnSimpleSolutionSelected() {
        InfoLog.appendText("Use SimpleSolution algorithm\n");
        currentStrategy = new SimplePointsFinder(new PointPositionGenerator());
        resetResults();
    }

    @FXML
    public void OnRotationSolutionSelected() {
        InfoLog.appendText("Use RotationSolution algorithm\n");
        currentStrategy = new RotationPointsFinder(new PointPositionGenerator());
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
