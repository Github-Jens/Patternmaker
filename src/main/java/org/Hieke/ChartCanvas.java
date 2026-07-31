package org.Hieke;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.Stack;

public class ChartCanvas extends Canvas {

    private static final int CELL_SIZE = 30;
    private static final int RULER_SIZE = 30;
    private final KnittingChart chart;
    private final ObjectProperty<StitchType> selectedType;
    private final ObjectProperty<Color> selectedColor;
    private final Ruler ruler;
    private final ChartRenderer renderer;
    private CanvasRenderContext renderContext;
    private double zoom = 1.0;

    private double offsetX = 0;
    private double offsetY = 0;

    private double lastMouseX;
    private double lastMouseY;

    private boolean panning = false;
    private boolean modified = false;

    private final Stack<Stroke> undoStack = new Stack<>();
    private final Stack<Stroke> redoStack = new Stack<>();
    private Stroke currentStroke;

    public ChartCanvas(KnittingChart chart,
                       ObjectProperty<StitchType> selectedType,
                       ObjectProperty<Color> selectedColor) {
        this.chart = chart;
        this.selectedType = selectedType;
        this.selectedColor = selectedColor;
        this.ruler = new Ruler(RULER_SIZE);
        this.renderer = new ChartRenderer(chart);
        setWidth(600);
        setHeight(600);
        this.renderContext =
                new CanvasRenderContext(getGraphicsContext2D());
        drawChart();
        setupMouseControls();
        setupZoomControls();

    }

    private void drawChart() {

        double scaledCellSize = CELL_SIZE * zoom;

        int firstColumn = Math.max(
                0,
                (int)Math.floor(
                        (-offsetX - RULER_SIZE) / scaledCellSize
                )
        );

        int lastColumn = Math.min(
                chart.getColumns() - 1,
                (int)Math.ceil(
                        (getWidth() - offsetX - RULER_SIZE)
                                / scaledCellSize
                )
        );

        int firstRow = Math.max(
                0,
                (int)Math.floor(
                        (-offsetY - RULER_SIZE) / scaledCellSize
                )
        );

        int lastRow = Math.min(
                chart.getRows() - 1,
                (int)Math.ceil(
                        (getHeight() - offsetY - RULER_SIZE)
                                / scaledCellSize
                )
        );

        GraphicsContext gc = getGraphicsContext2D();
        gc.save();
        gc.clearRect(
                0,
                0,
                getWidth(),
                getHeight()
        );

        renderer.render(
                renderContext,
                getRenderSettings(
                        firstRow,
                        lastRow,
                        firstColumn,
                        lastColumn
                ),
                getWidth(),
                getHeight()
        );




        gc.restore();

    }

    private void setupMouseControls() {

        setOnMousePressed(event -> {

            if (event.isMiddleButtonDown()) {

                panning = true;

                lastMouseX = event.getX();
                lastMouseY = event.getY();

                return;
            }


            currentStroke = new Stroke();

            paintAt(
                    event.getX(),
                    event.getY()
            );

        });


        setOnMouseDragged(event -> {


            if (panning) {

                double deltaX =
                        event.getX() - lastMouseX;

                double deltaY =
                        event.getY() - lastMouseY;


                offsetX += deltaX;
                offsetY += deltaY;


                lastMouseX = event.getX();
                lastMouseY = event.getY();


                drawChart();

                return;
            }


            paintAt(
                    event.getX(),
                    event.getY()
            );

        });


        setOnMouseReleased(event -> {


            if (panning) {

                panning = false;

                return;
            }


            if (currentStroke != null) {

                undoStack.push(currentStroke);

                redoStack.clear();

                currentStroke = null;

            }

        });

    }

    private void paintAt(double mouseX, double mouseY) {

        double scaledCellSize = CELL_SIZE * zoom;


        int column =
                (int)((mouseX - offsetX - RULER_SIZE)
                        / scaledCellSize);

        int row =
                (int)((mouseY - offsetY - RULER_SIZE)
                        / scaledCellSize);


        if (row < 0 || column < 0 ||
                row >= chart.getRows() ||
                column >= chart.getColumns()) {
            return;
        }
        if (selectedType.get() == null &&
                selectedColor.get() == null) {

            return;

        }

        Stitch stitch = chart.getStitch(row, column);


        StitchType oldType = stitch.getType();
        Color oldBackground =
                stitch.getBackgroundColor();


        StitchType newType = oldType;
        Color newBackground = oldBackground;


// Symbol handling
        if (selectedType.get() != null) {

            if (selectedType.get() == StitchType.EMPTY) {

                // Eraser
                newType = StitchType.EMPTY;
                newBackground = null;

            } else {

                newType = selectedType.get();

            }

        }


// Colour handling
        if (selectedType.get() != StitchType.EMPTY) {

            newBackground = selectedColor.get();

        }


        if (oldType == newType &&
                java.util.Objects.equals(
                        oldBackground,
                        newBackground
                )) {

            return;

        }


        StitchChange change =
                new StitchChange(
                        stitch,
                        oldType,
                        newType,
                        oldBackground,
                        newBackground
                );


        change.redo();
        modified = true;



        if (currentStroke != null) {

            currentStroke.addChange(change);

        }


        drawChart();
    }

    public void undo() {

        if (!undoStack.isEmpty()) {

            Stroke stroke = undoStack.pop();

            stroke.undo();
            modified = true;

            redoStack.push(stroke);

            drawChart();
        }

    }
    public void redo() {

        if (!redoStack.isEmpty()) {

            Stroke stroke = redoStack.pop();

            stroke.redo();
            modified = true;

            undoStack.push(stroke);

            drawChart();
        }

    }

    public KnittingChart getChart() {
        return chart;
    }

    public void resetView() {

        zoom = 1.0;

        offsetX = 0;
        offsetY = 0;

        drawChart();

    }

    private void setupZoomControls() {

        setOnScroll(event -> {

            double mouseX = event.getX();
            double mouseY = event.getY();


            double oldZoom = zoom;


            if (event.getDeltaY() > 0) {

                zoom *= 1.1;

            } else {

                zoom /= 1.1;

            }


            if (zoom < 0.2) {
                zoom = 0.2;
            }

            if (zoom > 5) {
                zoom = 5;
            }


            double zoomFactor = zoom / oldZoom;


            offsetX =
                    mouseX - (mouseX - offsetX) * zoomFactor;


            offsetY =
                    mouseY - (mouseY - offsetY) * zoomFactor;


            drawChart();

        });

    }
    private RenderSettings getRenderSettings(
            int firstRow,
            int lastRow,
            int firstColumn,
            int lastColumn
    ) {

        return new RenderSettings(
                CELL_SIZE,
                zoom,
                offsetX,
                offsetY,
                RULER_SIZE,
                firstRow,
                lastRow,
                firstColumn,
                lastColumn
        );
    }
    public boolean isModified() {
        return modified;
    }

    public void markSaved() {
        modified = false;
    }

}
