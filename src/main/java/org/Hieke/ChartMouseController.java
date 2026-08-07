package org.Hieke;

import javafx.scene.canvas.Canvas;

import java.util.function.BiConsumer;


public class ChartMouseController {

    private final Runnable refresh;
    private final java.util.function.BiConsumer<Integer, Integer> paint;
    private final java.util.function.BiConsumer<Double, Double> updateCursor;
    private final java.util.function.BiConsumer<Double, Double> showMenu;
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
            Runnable closePopup
    ) {

        this.canvas = canvas;
        this.editor = editor;
        this.editorState = editorState;
        this.selectionController = selectionController;
        this.transform = transform;

        this.refresh = refresh;
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


            if (editorState.getMode() != EditorMode.NORMAL) {

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

}