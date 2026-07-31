package org.Hieke;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;

import java.util.Stack;

public class ChartCanvas extends Canvas {

    private static final int CELL_SIZE = 30;
    private final KnittingChart chart;
    private final ObjectProperty<StitchType> selectedType;
    private final Stack<Stroke> undoStack = new Stack<>();
    private final Stack<Stroke> redoStack = new Stack<>();
    private Stroke currentStroke;

    public ChartCanvas(KnittingChart chart,
                       ObjectProperty<StitchType> selectedType) {
        this.chart = chart;
        this.selectedType = selectedType;
        setWidth(chart.getColumns() * CELL_SIZE);
        setHeight(chart.getRows() * CELL_SIZE);
        drawChart();
        setupMouseControls();

    }

    private void drawChart() {

        GraphicsContext gc = getGraphicsContext2D();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.clearRect(
                0,
                0,
                getWidth(),
                getHeight()
        );

        for (int row = 0; row <= chart.getRows(); row++) {

            double y = row * CELL_SIZE;

            gc.strokeLine(
                    0,
                    y,
                    getWidth(),
                    y
            );
        }


        for (int column = 0; column <= chart.getColumns(); column++) {

            double x = column * CELL_SIZE;

            gc.strokeLine(
                    x,
                    0,
                    x,
                    getHeight()
            );
        }

        for (int row = 0; row < chart.getRows(); row++) {

            for (int column = 0; column < chart.getColumns(); column++) {

                Stitch stitch = chart.getStitch(row, column);

                drawStitch(gc, stitch);

            }
        }

    }

    private void setupMouseControls() {

        setOnMousePressed(event -> {

            currentStroke = new Stroke();

            paintAt(
                    event.getX(),
                    event.getY()
            );

        });


        setOnMouseDragged(event -> {

            paintAt(
                    event.getX(),
                    event.getY()
            );

        });


        setOnMouseReleased(event -> {

            if (currentStroke != null) {

                undoStack.push(currentStroke);

                redoStack.clear();

                currentStroke = null;

            }

        });

    }

    private void drawStitch(GraphicsContext gc, Stitch stitch) {
        String symbol = stitch.getSymbol();

        double x = stitch.getColumn() * CELL_SIZE + CELL_SIZE / 2;
        double y = stitch.getRow() * CELL_SIZE + CELL_SIZE / 2;


        gc.fillText(
                symbol,
                x ,
                y
        );
    }

    private void paintAt(double mouseX, double mouseY) {

        int column = (int)(mouseX / CELL_SIZE);
        int row = (int)(mouseY / CELL_SIZE);


        if (row < 0 || column < 0 ||
                row >= chart.getRows() ||
                column >= chart.getColumns()) {
            return;
        }
        if (selectedType.get() == null) {
            return;
        }

        Stitch stitch = chart.getStitch(row, column);

        StitchType oldType = stitch.getType();
        StitchType newType = selectedType.get();


        if (oldType == newType) {
            return;
        }


        StitchChange change =
                new StitchChange(
                        stitch,
                        oldType,
                        newType
                );


        change.redo();


        if (currentStroke != null) {

            currentStroke.addChange(change);

        }


        drawChart();
    }

    public void undo() {

        if (!undoStack.isEmpty()) {

            Stroke stroke = undoStack.pop();

            stroke.undo();

            redoStack.push(stroke);

            drawChart();
        }

    }
    public void redo() {

        if (!redoStack.isEmpty()) {

            Stroke stroke = redoStack.pop();

            stroke.redo();

            undoStack.push(stroke);

            drawChart();
        }

    }
    public KnittingChart getChart() {
        return chart;
    }
}
