package org.Hieke;

import javafx.scene.paint.Color;

public class ChartClipboardData {

    private final StitchDefinition definition;
    private final Color color;


    public ChartClipboardData(
            StitchDefinition definition,
            Color color
    ) {

        this.definition = definition;
        this.color = color;

    }


    public StitchDefinition getDefinition() {

        return definition;

    }


    public Color getColor() {

        return color;

    }

}