package org.Hieke;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;

public class ChartCanvas extends Canvas {

    private final ChartEditor editor;
    private final SelectionController selectionController;

    private final EditorState editorState;
    private final Palette palette;

    private final ChartRenderer renderer;
    private CanvasRenderContext renderContext;
    private ScrollPane scrollPane;
    private final ViewTransform transform;
    private final ChartMouseController mouseController;
    private final SelectionRenderer selectionRenderer;

    private int cursorRow = -1;
    private int cursorColumn = -1;

    private SelectionMenu activeSelectionMenu;

    public ChartCanvas(
            KnittingChart chart,
            EditorState editorState,
            Palette palette,
            ScrollPane scrollPane
    ) {

        this.editor = new ChartEditor(chart);
        this.editorState = editorState;
        this.palette = palette;
        this.selectionController =
                new SelectionController(
                        editorState,
                        chart
                );
        this.scrollPane = scrollPane;
        this.renderer = new ChartRenderer(chart);

        this.transform =
                new ViewTransform(
                        ViewTransform.DEFAULT_CELL_SIZE,
                        ViewTransform.DEFAULT_RULER_SIZE
                );

        this.selectionRenderer =
                new SelectionRenderer(
                        transform
                );

        setWidth(
                (chart.getColumns() + 2)
                        * ViewTransform.DEFAULT_CELL_SIZE
        );

        setHeight(
                (chart.getRows() + 2)
                        * ViewTransform.DEFAULT_CELL_SIZE
        );



        this.renderContext =
                new CanvasRenderContext(getGraphicsContext2D());
        drawChart();

        this.mouseController =
                new ChartMouseController(
                        this,
                        editor,
                        editorState,
                        selectionController,
                        transform,
                        this::drawChart,
                        this::paintAt,
                        this::updateCursorPosition,
                        this::showSelectionMenu
                );

        mouseController.install();

        setupZoomControls();

    }

    private void drawChart() {

        double scaledCellSize = transform.getScaledCellSize();
        int renderColumns = editor.getChart().getColumns() + 2;
        int renderRows    = editor.getChart().getRows() + 2;

        int firstColumn = Math.max(
                0,
                (int)Math.floor(
                        -transform.getOffsetX()
                                / scaledCellSize
                )
        );

        int lastColumn = Math.min(
                renderColumns - 1,
                (int)Math.ceil(
                        (getWidth() - transform.getOffsetX())
                                / scaledCellSize
                )
        );

        int firstRow = Math.max(
                0,
                (int)Math.floor(
                        -transform.getOffsetY()
                                / scaledCellSize
                )
        );

        int lastRow = Math.min(
                renderRows - 1,
                (int)Math.ceil(
                        (getHeight() - transform.getOffsetY())
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

        selectionRenderer.render(
                getGraphicsContext2D(),
                editorState.getSelection()
        );

        gc.restore();

    }

    private void paintAt(double mouseX, double mouseY) {

        int column =
                transform.screenToColumn(
                        mouseX
                );


        int row =
                transform.screenToRow(
                        mouseY
                );


        if (row < 0 || column < 0 ||
                row >= editor.getChart().getRows() ||
                column >= editor.getChart().getColumns()) {
            return;
        }


        if (editorState.activeToolProperty().get() != Tool.ERASE
                && editorState.selectedStitchProperty().get() == null
                && editorState.selectedColorIndexProperty().get() == -1) {

            return;

        }


        editor.paintCell(
                row,
                column,
                editorState.activeToolProperty().get(),
                editorState.selectedStitchProperty().get(),
                editorState.selectedColorProperty().get(),
                editorState.selectedColorIndexProperty().get()
        );

        drawChart();

    }

    private void updateCursorPosition(
            double mouseX,
            double mouseY
    ) {

        int column =
                transform.screenToColumn(
                        mouseX
                );


        int row =
                transform.screenToRow(
                        mouseY
                );


        if (row < 0 ||
                column < 0 ||
                row >= editor.getChart().getRows() ||
                column >= editor.getChart().getColumns()) {

            cursorRow = -1;
            cursorColumn = -1;

            return;
        }


        cursorRow = row;
        cursorColumn = column;

    }

    public void paste() {

        if (cursorRow < 0 ||
                cursorColumn < 0) {

            return;

        }


        editor.paste(
                cursorRow,
                cursorColumn
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

            double newZoom =
                    transform.getZoom();


            if (event.getDeltaY() > 0) {

                newZoom *= 1.1;

            }
            else {

                newZoom /= 1.1;

            }


            if (newZoom < 0.25) {

                newZoom = 0.25;

            }


            if (newZoom > 1.0) {

                newZoom = 1.0;

            }


            transform.zoom(
                    newZoom,
                    event.getX(),
                    event.getY()
            );


            drawChart();

        });

    }

    public void resetView() {

        transform.reset();

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
                ViewTransform.DEFAULT_CELL_SIZE,
                transform.getZoom(),
                transform.getOffsetX(),
                transform.getOffsetY(),
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
    private void showSelectionMenu(
            double screenX,
            double screenY
    ) {

        if (activeSelectionMenu != null) {

            activeSelectionMenu.hide();

        }


        activeSelectionMenu =
                new SelectionMenu(
                        editor,
                        editorState,
                        editorState.getSymbolPalette(),
                        palette,
                        this::drawChart,
                        this
                );


        activeSelectionMenu.setMenuPosition(
                screenX,
                screenY
        );


        activeSelectionMenu.show(
                this,
                screenX,
                screenY
        );

    }

    public void insertRow(int index) {

        editor.insertRow(index);

        updateCanvasSize();

    }


    public void deleteRow(int index) {

        editor.deleteRow(index);
        updateCanvasSize();

    }


    public void insertColumn(int index) {

        editor.insertColumn(index);
        updateCanvasSize();

    }


    public void deleteColumn(int index) {

        editor.deleteColumn(index);
        updateCanvasSize();

    }

    private void updateCanvasSize() {

        setWidth(
                (editor.getChart().getColumns() + 2)
                        * transform.getCellSize()
        );

        setHeight(
                (editor.getChart().getRows() + 2)
                        * transform.getCellSize()
        );

        drawChart();

    }

}
