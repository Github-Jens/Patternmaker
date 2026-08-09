package org.Hieke;

import javafx.scene.canvas.Canvas;

import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class ChartMouseController {

    private final Runnable refresh;
    private final java.util.function.BiConsumer<Integer, Integer> paint;
    private final java.util.function.BiConsumer<Double, Double> updateCursor;
    private final java.util.function.BiConsumer<Double, Double> showMenu;
    private final java.util.function.Consumer<Boolean> setMenuDragging;
    private final Runnable closePopup;

    private final Canvas canvas;
    private final ChartEditor editor;
    private final EditorState editorState;
    private final SelectionController selectionController;
    private final ViewTransform transform;


    private double lastMouseX;
    private double lastMouseY;

    private boolean panning = false;
    private boolean consumedClick = false;
    private boolean movingSelection = false;

    private int selectionGrabOffsetRow;
    private int selectionGrabOffsetColumn;


    public ChartMouseController(
            Canvas canvas,
            ChartEditor editor,
            EditorState editorState,
            SelectionController selectionController,
            ViewTransform transform,
            Runnable refresh,
            BiConsumer<Integer, Integer> paint,
            BiConsumer<Double, Double> updateCursor,
            BiConsumer<Double, Double> showMenu,
            Runnable closePopup,
            Consumer<Boolean> setMenuDragging
    ) {

        this.canvas = canvas;
        this.editor = editor;
        this.editorState = editorState;
        this.selectionController = selectionController;
        this.transform = transform;

        this.refresh = refresh;
        this.setMenuDragging = setMenuDragging;
        this.paint = paint;
        this.updateCursor = updateCursor;
        this.showMenu = showMenu;
        this.closePopup = closePopup;

    }


    public void install() {

        setupMouseControls();

    }


    private void setupMouseControls() {

        canvas.setOnMousePressed(event -> {

            if (editorState.isPopupActive()) {


                closePopup.run();

                consumedClick = true;

                return;

            }

            if (editorState.getMode() == EditorMode.FLOATING_SELECTION) {

                FloatingSelection floating =
                        editorState.getFloatingSelection();

                if (floating != null) {

                    int column =
                            transform.screenToColumn(
                                    event.getX()
                            );

                    int row =
                            transform.screenToRow(
                                    event.getY()
                            );


                    if (isMouseInsideFloatingSelection(
                            row,
                            column
                    )) {

                        movingSelection = true;
                        setMenuDragging.accept(true);

                        selectionGrabOffsetRow =
                                row - floating.getRow();

                        selectionGrabOffsetColumn =
                                column - floating.getColumn();

                        return;

                    }


                    editor.commitFloatingSelectionMove(
                            editorState
                    );

                    editorState.setFloatingSelection(null);

                    editorState.getSelection()
                            .clear();

                    editorState.setMode(
                            EditorMode.NORMAL
                    );

                    refresh.run();

                }

                return;
            }



            //this allows moving while in rotating mode
            if (editorState.getMode() == EditorMode.ROTATION) {

                FloatingSelection floating =
                        editorState.getFloatingSelection();

                if (floating != null) {

                    int column =
                            transform.screenToColumn(
                                    event.getX()
                            );

                    int row =
                            transform.screenToRow(
                                    event.getY()
                            );

                    if (isMouseInsideFloatingSelection(
                            row,
                            column
                    )) {

                        movingSelection = true;
                        setMenuDragging.accept(true);

                        selectionGrabOffsetRow =
                                row - floating.getRow();

                        selectionGrabOffsetColumn =
                                column - floating.getColumn();

                        return;

                    }

                }

                return;

            }


            editorState.getSelection()
                    .clear();

            refresh.run();

            if (event.isMiddleButtonDown()) {

                panning = true;

                lastMouseX = event.getX();
                lastMouseY = event.getY();

                return;
            }


            if (editorState.activeToolProperty().get() == Tool.SELECT) {

                selectionController.startSelection(
                        event.getX(),
                        event.getY(),
                        transform
                );

                return;
            }


            if (editorState.activeToolProperty().get() == Tool.DRAW
                    || editorState.activeToolProperty().get() == Tool.ERASE) {

                editor.startPainting();

                int column =
                        transform.screenToColumn(
                                event.getX()
                        );

                int row =
                        transform.screenToRow(
                                event.getY()
                        );

                paint.accept(
                        row,
                        column
                );

            }

        });

        canvas.setOnMouseDragged(event -> {

            if ((editorState.getMode() == EditorMode.FLOATING_SELECTION
                    || editorState.getMode() == EditorMode.ROTATION)
                    && movingSelection) {


                FloatingSelection floating =
                        editorState.getFloatingSelection();


                if (floating != null) {

                    int column =
                            transform.screenToColumn(
                                    event.getX()
                            );

                    int row =
                            transform.screenToRow(
                                    event.getY()
                            );


                    editor.moveFloatingSelection(
                            editorState,
                            row - selectionGrabOffsetRow,
                            column - selectionGrabOffsetColumn
                    );


                    refresh.run();

                }

                return;

            }

            if (editorState.getMode() != EditorMode.NORMAL) {

                return;

            }

            updateCursor.accept(
                    event.getX(),
                    event.getY()
            );


            if (panning) {

                double deltaX =
                        event.getX() - lastMouseX;

                double deltaY =
                        event.getY() - lastMouseY;


                transform.pan(
                        deltaX,
                        deltaY
                );


                lastMouseX = event.getX();
                lastMouseY = event.getY();


                refresh.run();

                return;
            }


            if (editorState.activeToolProperty().get() == Tool.SELECT
                    && selectionController.isSelecting()) {


                selectionController.updateSelection(
                        event.getX(),
                        event.getY(),
                        transform
                );


                refresh.run();

                return;
            }


            if (editorState.activeToolProperty().get() == Tool.DRAW
                    || editorState.activeToolProperty().get() == Tool.ERASE) {


                int column =
                        transform.screenToColumn(
                                event.getX()
                        );

                int row =
                        transform.screenToRow(
                                event.getY()
                        );

                paint.accept(
                        row,
                        column
                );

            }

        });

        canvas.setOnMouseMoved(event -> {

            updateCursor.accept(
                    event.getX(),
                    event.getY()
            );

        });

        canvas.setOnMouseReleased(event -> {

            if (consumedClick) {

                consumedClick = false;

                return;

            }


            if (panning) {

                panning = false;

                return;

            }


            if (editorState.getMode() == EditorMode.FLOATING_SELECTION
                    || editorState.getMode() == EditorMode.ROTATION) {

                movingSelection = false;

                setMenuDragging.accept(false);

                return;

            }


            if (selectionController.isSelecting()) {

                selectionController.finishSelection();

                refresh.run();

                showMenu.accept(
                        event.getScreenX(),
                        event.getScreenY()
                );

                return;

            }


            editor.finishPainting();

        });

    }

    private boolean isMouseInsideFloatingSelection(
            int row,
            int column
    ) {

        FloatingSelection floating =
                editorState.getFloatingSelection();

        if (floating == null) {

            return false;

        }


        return row >= floating.getRow()
                && row < floating.getRow() + floating.getRows()
                && column >= floating.getColumn()
                && column < floating.getColumn() + floating.getColumns();

    }

}