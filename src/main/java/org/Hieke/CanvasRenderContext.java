package org.Hieke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.VPos;
import javafx.scene.text.TextAlignment;

public class CanvasRenderContext implements RenderContext {


    private final GraphicsContext gc;


    public CanvasRenderContext(GraphicsContext gc) {
        this.gc = gc;

    }


    @Override
    public void setStroke(String color) {
        gc.setStroke(Color.web(color));
    }


    @Override
    public void setFill(String color) {
        gc.setFill(Color.web(color));
    }


    @Override
    public void drawLine(
            double x1,
            double y1,
            double x2,
            double y2
    ) {

        gc.strokeLine(
                x1,
                y1,
                x2,
                y2
        );
    }


    @Override
    public void drawText(
            String text,
            double x,
            double y
    ) {

        gc.setFill(Color.BLACK);

        gc.setTextAlign(
                TextAlignment.CENTER
        );

        gc.setTextBaseline(
                VPos.CENTER
        );

        gc.fillText(
                text,
                x,
                y
        );
    }

    @Override
    public void fillRect(
            Color color,
            double x,
            double y,
            double width,
            double height
    ) {

        gc.setFill(color);

        gc.fillRect(
                x,
                y,
                width,
                height
        );
    }
}