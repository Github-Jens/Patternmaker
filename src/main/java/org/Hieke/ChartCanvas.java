package org.Hieke;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;

public class ChartCanvas extends Canvas {

    private final ChartEditor editor;
    private final SelectionMenuController selectionMenuController;
    private final ChartModificationController modificationController;

    private final EditorState editorState;
    private final Palette palette;

    private final ChartRenderer renderer;
    private CanvasRenderContext renderContext;
    private ScrollPane scrollPane;
    private final ViewTransform transform;
    private final ChartMouseController mouseController;
    private final SelectionRenderer selectionRenderer;

    private SelectionMenu activeSelectionMenu;

    public ChartCanvas(
            KnittingChart chart,
            EditorState editorState,
            Palette palette,
            ScrollPane scrollPane
    ) {

        this.editor = new ChartEditor(chart);

        this.modificationController =
                new ChartModificationController(
                        editor,
                        this::drawChart
                );

        this.editorState = editorState;
        editorState.activeToolProperty()
                .addListener((observable, oldTool, newTool) -> {

                    if (newTool != Tool.SELECT) {

                        editorState.getSelection()
                                .clear();

                        drawChart();

                    }

                });
        this.palette = palette;
        SelectionController selectionController =
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

        this.mouseController =
                new ChartMouseController(
                        this,
                        editor,
                        editorState,
                        selectionController,
                        transform,
                        this::drawChart,
                        (row, column) -> {
                            editor.paint(
                                    row,
                                    column,
                                    editorState
                            );

                            drawChart();

                        },
                        this::updateCursorPosition,
                        this::showSelectionMenu,
                        this::closePopups,
                        dragging -> {

                            if (activeSelectionMenu != null) {

                                FloatingSelectionMenu menu =
                                        activeSelectionMenu.getFloatingSelectionMenu();

                                if (menu != null) {

                                    menu.setDragging(dragging);

                                }

                            }

                        }
                );

        this.selectionMenuController =
                new SelectionMenuController(
                        editor,
                        editorState,
                        palette,
                        editorState.getSymbolPalette(),
                        this::drawChart,
                        this::updateCanvasSize
                );

        mouseController.install();

        setupZoomControls();
        drawChart();

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

        RenderSettings settings =
                getRenderSettings(
                        firstRow,
                        lastRow,
                        firstColumn,
                        lastColumn
                );


        renderer.render(
                renderContext,
                settings,
                getWidth(),
                getHeight()
        );

        selectionRenderer.render(
                getGraphicsContext2D(),
                editorState.getSelection(),
                editorState.getFloatingSelection(),
                editor.getChart().getRows(),
                editor.getChart().getColumns()
        );

        gc.restore();

    }

    private void updateCursorPosition(
            double mouseX,
            double mouseY
    ) {

        int column =
                transform.screenToColumn(mouseX);

        int row =
                transform.screenToRow(mouseY);


        if (row < 0 ||
                column < 0 ||
                row >= editor.getChart().getRows() ||
                column >= editor.getChart().getColumns()) {

            selectionMenuController.updateCursorPosition(
                    -1,
                    -1
            );

            return;
        }


        selectionMenuController.updateCursorPosition(
                row,
                column
        );

    }

    public void undo() {

        editor.undo();

        updateCanvasSize();
        drawChart();

    }


    public void redo() {

        editor.redo();

        updateCanvasSize();
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
                        selectionMenuController,
                        editorState.getSymbolPalette(),
                        palette,
                        this
                );


        activeSelectionMenu.setMenuPosition(
                screenX,
                screenY
        );

        editorState.setPopupActive(true);


        activeSelectionMenu.show(
                this,
                screenX,
                screenY
        );

    }

    private void closePopups() {

        if (activeSelectionMenu != null) {

            activeSelectionMenu.closeAllPickers();

            activeSelectionMenu.hide();

            activeSelectionMenu = null;

        }

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

    public void refresh() {

        updateCanvasSize();
        drawChart();

    }

    public ChartEditor getEditor() {

        return editor;

    }

}
