package com.gitlab.saylenty.visual.elements;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class LabeledChartNode extends StackPane {
    public LabeledChartNode(int text) {
        setPrefSize(15, 15);
        getChildren().setAll(new Label(String.valueOf(text)));
        toFront();
    }
}
