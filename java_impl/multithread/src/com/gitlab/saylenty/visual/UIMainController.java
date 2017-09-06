package com.gitlab.saylenty.visual;

import com.gitlab.saylenty.generator.PointPositionGenerator;
import com.gitlab.saylenty.infrastructure.Point;
import com.gitlab.saylenty.task.PositionFinderCountedCompleter;
import com.gitlab.saylenty.visual.elements.LabeledChartNode;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

import java.util.List;
import java.util.stream.IntStream;

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

    private List<List<Point>> result;
    private int currentSolution;

    @FXML
    public ScatterChart<Number, Number> ScatterChart;
    @FXML
    public Button Next;
    @FXML
    public NumberAxis xAxis;
    @FXML
    public NumberAxis yAxis;
    @FXML
    private void initialize(){
        OnGetNextSolution();
    }

    public void OnGetNextSolution() {
        if (result == null){
            // TODO add ability to choose strategy from UI
            // create task
            PositionFinderCountedCompleter positionFinderTask = new PositionFinderCountedCompleter(matrix,
                    new PointPositionGenerator());
            // run task and wait until complete
            result = positionFinderTask.invoke();
        }
        ScatterChart.getData().clear();
        if (currentSolution < result.size()) {
            List<Point> points = result.get(currentSolution);
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(String.format("Points visual representation %d", currentSolution + 1));
            IntStream.range(0, points.size()).forEach(i -> {
                Point point = points.get(i);
                XYChart.Data<Number, Number> data = new XYChart.Data<>(point.getX(), point.getY());
                data.setNode(new LabeledChartNode(i));
                series.getData().add(data);
            });
            ScatterChart.getData().add(series);
            currentSolution++;
        }
        // make the solutions be cyclic
        if (currentSolution == result.size()) {
            currentSolution = 0;
        }
    }
}
