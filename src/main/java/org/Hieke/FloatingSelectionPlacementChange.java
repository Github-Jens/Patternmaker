package org.Hieke;

public class FloatingSelectionPlacementChange implements UndoableChange {

    private final SelectionTransformer transformer;

    private final KnittingChart chart;

    private final int originalRow;
    private final int originalColumn;

    private final int finalRow;
    private final int finalColumn;

    private final SelectionSnapshot before;
    private final SelectionSnapshot after;


    public FloatingSelectionPlacementChange(
            SelectionTransformer transformer,
            KnittingChart chart,
            int originalRow,
            int originalColumn,
            int finalRow,
            int finalColumn,
            SelectionSnapshot before,
            SelectionSnapshot after
    ) {

        this.transformer = transformer;
        this.chart = chart;

        this.originalRow = originalRow;
        this.originalColumn = originalColumn;

        this.finalRow = finalRow;
        this.finalColumn = finalColumn;

        this.before = before;
        this.after = after;

    }


    @Override
    public void undo() {

        transformer.clearArea(
                chart,
                finalRow,
                finalColumn,
                after.getRows(),
                after.getColumns()
        );

        transformer.applySnapshot(
                chart,
                originalRow,
                originalColumn,
                before
        );

    }


    @Override
    public void redo() {

        transformer.clearArea(
                chart,
                originalRow,
                originalColumn,
                before.getRows(),
                before.getColumns()
        );

        transformer.applySnapshot(
                chart,
                finalRow,
                finalColumn,
                after
        );

    }

}