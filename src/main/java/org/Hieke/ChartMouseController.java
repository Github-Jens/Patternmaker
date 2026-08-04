package org.Hieke;

import javafx.scene.canvas.Canvas;


public class ChartMouseController {

    private final Runnable refresh;
    private final java.util.function.BiConsumer<Double, Double> paint;
    private final java.util.function.BiConsumer<Double, Double> updateCursor;
    private final java.util.function.BiConsumer<Double, Double> showMenu;


    private final Canvas canvas;
    private final ChartEditor editor;
    private final EditorState editorState;
    private final SelectionController selectionController;
    private final ViewTransform transform;


    private double lastMouseX;
    private double lastMouseY;

    private boolean panning = false;


    public ChartMouseController(
            Canvas canvas,
            ChartEditor editor,
            EditorState editorState,
            SelectionController selectionController,
            ViewTransform transform,
            Runnable refresh,
            java.util.function.BiConsumer<Double, Double> paint,
            java.util.function.BiConsumer<Double, Double> updateCursor,
            java.util.function.BiConsumer<Double, Double> showMenu
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

    }


    public void install() {

        setupMouseControls();

    }


    private void setupMouseControls() {

        canvas.setOnMousePressed(event -> {

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

                editor.beginStroke();

                paint.accept(
                        event.getX(),
                        event.getY()
                );

            }

        });

        canvas.setOnMouseDragged(event -> {

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


                paint.accept(
                        event.getX(),
                        event.getY()
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


            editor.endStroke();

        });

    }

}