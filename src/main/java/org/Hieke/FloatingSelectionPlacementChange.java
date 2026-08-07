package org.Hieke;

public class FloatingSelectionPlacementChange implements UndoableChange {

    private final SelectionTransformer transformer;

    private final KnittingChart chart;

    private final int startRow;
    private final int startColumn;

    private final SelectionSnapshot before;
    private final SelectionSnapshot after;


    public FloatingSelectionPlacementChange(
            SelectionTransformer transformer,
            KnittingChart chart,
            int startRow,
            int startColumn,
            SelectionSnapshot before,
            SelectionSnapshot after
    ) {

        this.transformer = transformer;
        this.chart = chart;

        this.startRow = startRow;
        this.startColumn = startColumn;

        this.before = before;
        this.after = after;

    }


    @Override
    public void undo() {

        transformer.replaceSelection(
                chart,
                startRow,
                startColumn,
                after.getRows(),
                after.getColumns(),
                before
        );

    }


    @Override
    public void redo() {

        transformer.replaceSelection(
                chart,
                startRow,
                startColumn,
                before.getRows(),
                before.getColumns(),
                after
        );

    }

}