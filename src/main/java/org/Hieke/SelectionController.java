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


    public void startRowSelection(
            double mouseY,
            ViewTransform transform,
            boolean fromRight
    ) {

        int row =
                getRowFromRuler(
                        mouseY,
                        transform,
                        fromRight
                );


        if (row < 0 ||
                row >= chart.getRows()) {

            return;

        }


        selecting = true;


        editorState.getSelection()
                .setStart(
                        row,
                        0
                );

        editorState.getSelection()
                .setEnd(
                        row,
                        chart.getColumns() - 1
                );

    }


    public void startColumnSelection(
            double mouseX,
            ViewTransform transform,
            boolean fromBottom
    ) {

        int column =
                getColumnFromRuler(
                        mouseX,
                        transform,
                        fromBottom
                );


        if (column < 0 ||
                column >= chart.getColumns()) {

            return;

        }


        selecting = true;


        editorState.getSelection()
                .setStart(
                        0,
                        column
                );

        editorState.getSelection()
                .setEnd(
                        chart.getRows() - 1,
                        column
                );

    }


    public void updateSelection(
            double mouseX,
            double mouseY,
            ViewTransform transform
    ) {

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


    public void updateRowSelection(
            double mouseY,
            ViewTransform transform,
            boolean fromRight
    ) {

        if (!selecting) {
            return;
        }

        int row =
                getRowFromRuler(
                        mouseY,
                        transform,
                        fromRight
                );

        if (row < 0 || row >= chart.getRows()) {
            return;
        }

        int startRow =
                editorState.getSelection()
                        .getStartRow();

        editorState.getSelection()
                .setEnd(
                        row,
                        chart.getColumns() - 1
                );

    }


    public void updateColumnSelection(
            double mouseX,
            ViewTransform transform,
            boolean fromBottom
    ) {

        if (!selecting) {
            return;
        }

        int column =
                getColumnFromRuler(
                        mouseX,
                        transform,
                        fromBottom
                );

        if (column < 0 || column >= chart.getColumns()) {
            return;
        }

        int startColumn =
                editorState.getSelection()
                        .getStartColumn();

        editorState.getSelection()
                .setEnd(
                        chart.getRows() - 1,
                        column
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


        if (row < 0 ||
                column < 0 ||
                row >= chart.getRows() ||
                column >= chart.getColumns()) {

            return null;

        }


        return new int[]{
                row,
                column
        };

    }


    private int getRowFromRuler(
            double mouseY,
            ViewTransform transform,
            boolean fromRight
    ) {

        return (int) Math.floor(
                (mouseY
                        - transform.getOffsetY()
                        - transform.getScaledRulerSize())
                        / transform.getScaledCellSize()
        );

    }


    private int getColumnFromRuler(
            double mouseX,
            ViewTransform transform,
            boolean fromBottom
    ) {

        return (int) Math.floor(
                (mouseX
                        - transform.getOffsetX()
                        - transform.getScaledRulerSize())
                        / transform.getScaledCellSize()
        );

    }

}