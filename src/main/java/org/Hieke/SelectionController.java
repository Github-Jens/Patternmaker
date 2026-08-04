package org.Hieke;


public class SelectionController {

    private final EditorState editorState;
    private final KnittingChart chart;

    private boolean selecting = false;


    public SelectionController(
            EditorState editorState,
            KnittingChart chart
    ) {

        this.editorState = editorState;
        this.chart = chart;

    }


    public void startSelection(
            double mouseX,
            double mouseY,
            ViewTransform transform
    ) {

        int[] cell =
                getCell(
                        mouseX,
                        mouseY,
                        transform
                );


        if (cell == null) {

            return;

        }


        selecting = true;


        editorState.getSelection()
                .setStart(
                        cell[0],
                        cell[1]
                );


        editorState.getSelection()
                .setEnd(
                        cell[0],
                        cell[1]
                );

    }


    public void updateSelection(
            double mouseX,
            double mouseY,
            ViewTransform transform
    ){


        if (!selecting) {

            return;

        }


        int[] cell =
                getCell(
                        mouseX,
                        mouseY,
                        transform
                );


        if (cell == null) {

            return;

        }


        editorState.getSelection()
                .setEnd(
                        cell[0],
                        cell[1]
                );

    }


    public void finishSelection() {

        selecting = false;

    }


    public boolean isSelecting() {

        return selecting;

    }


    private int[] getCell(
            double mouseX,
            double mouseY,
            ViewTransform transform
    ) {

        int column =
                transform.screenToColumn(
                        mouseX
                );


        int row =
                transform.screenToRow(
                        mouseY
                );


        if (row < 0 || column < 0 ||
                row >= chart.getRows() ||
                column >= chart.getColumns()) {

            return null;

        }


        return new int[]{
                row,
                column
        };

    }

}