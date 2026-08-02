package org.Hieke;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;

import java.util.Stack;

public class ChartCanvas extends Canvas {

    private static final int CELL_SIZE = 30;
    private static final int RULER_SIZE = 30;

    private final ChartEditor editor;

    private final ObjectProperty<StitchType> selectedType;
    private final ObjectProperty<Color> selectedColor;
    private final IntegerProperty selectedColorIndex;

    private final ChartRenderer renderer;
    private CanvasRenderContext renderContext;
    private ScrollPane scrollPane;
    private double zoom = 1.0;

    private double offsetX = 0;
    private double offsetY = 0;

    private double lastMouseX;
    private double lastMouseY;

    private boolean panning = false;

    public ChartCanvas(KnittingChart chart,
                       ObjectProperty<StitchType> selectedType,
                       ObjectProperty<Color> selectedColor,
                       IntegerProperty selectedColorIndex,
                       ScrollPane scrollPane) {
        this.editor = new ChartEditor(chart);
        this.selectedType = selectedType;
        this.selectedColor = selectedColor;
        this.selectedColorIndex = selectedColorIndex;
        this.scrollPane = scrollPane;
        this.renderer = new ChartRenderer(chart);
        setWidth(
                (chart.getColumns() + 2) * CELL_SIZE
        );

        setHeight(
                (chart.getRows() + 2) * CELL_SIZE
        );
        this.renderContext =
                new CanvasRenderContext(getGraphicsContext2D());
        drawChart();
        setupMouseControls();
        setupZoomControls();

    }

    private void drawChart() {

        double scaledCellSize = CELL_SIZE * zoom;
        int renderColumns = editor.getChart().getColumns() + 2;
        int renderRows    = editor.getChart().getRows() + 2;

        int firstColumn = Math.max(
                0,
                (int)Math.floor(
                        -offsetX / scaledCellSize
                )
        );

        int lastColumn = Math.min(
                renderColumns - 1,
                (int)Math.ceil(
                        (getWidth() - offsetX)
                                / scaledCellSize
                )
        );

        int firstRow = Math.max(
                0,
                (int)Math.floor(
                        -offsetY / scaledCellSize
                )
        );

        int lastRow = Math.min(
                renderRows - 1,
                (int)Math.ceil(
                        (getHeight() - offsetY)
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

            editor.beginStroke();

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
            editor.endStroke();
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
                row >= editor.getChart().getRows() ||
                column >= editor.getChart().getColumns()) {
            return;
        }
        if (selectedType.get() == null &&
                selectedColorIndex.get() == -1) {
            return;
        }

        editor.paintCell(
                row,
                column,
                selectedType.get(),
                selectedColor.get(),
                selectedColorIndex.get()
        );

        drawChart();
    }

    public void undo() {

        editor.undo();

        drawChart();

    }
    public void redo() {

        editor.redo();

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


            if (zoom < 0.25) {
                zoom = 0.25;
            }

            if (zoom > 1.0) {
                zoom = 1.0;
            }


            double zoomFactor = zoom / oldZoom;


            offsetX =
                    mouseX - (mouseX - offsetX) * zoomFactor;


            offsetY =
                    mouseY - (mouseY - offsetY) * zoomFactor;



            drawChart();

        });

    }

    public void resetView() {

        zoom = 1.0;

        offsetX = 0;
        offsetY = 0;


        if (scrollPane != null) {

            scrollPane.setHvalue(0);
            scrollPane.setVvalue(0);

        }


        drawChart();

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
                firstRow,
                lastRow,
                firstColumn,
                lastColumn,
                editor.getChart().getRows() + 2,
                editor.getChart().getColumns() + 2
        );
    }
    public boolean isModified() {

        return editor.isModified();

    }

    public void markSaved() {

        editor.markSaved();

    }

    public void setScrollPane(ScrollPane scrollPane) {

        this.scrollPane = scrollPane;

    }
    public KnittingChart getChart() {

        return editor.getChart();

    }

}
