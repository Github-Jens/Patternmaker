package org.Hieke;

import javafx.scene.paint.Color;

public interface RenderContext {

    void setStroke(String color);

    void setFill(String color);

    void drawLine(
            double x1,
            double y1,
            double x2,
            double y2
    );

    void drawText(
            String text,
            double x,
            double y
    );
    void fillRect(
            Color color,
            double x,
            double y,
            double width,
            double height
    );

}