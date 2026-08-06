package org.Hieke;

public class TransformationChange implements UndoableChange {

    private final KnittingChart chart;
    private final int startRow;
    private final int startColumn;

    private final SelectionSnapshot before;
    private final SelectionSnapshot after;


    public TransformationChange(
            KnittingChart chart,
            int startRow,
            int startColumn,
            SelectionSnapshot before,
            SelectionSnapshot after
    ) {

        this.chart = chart;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.before = before;
        this.after = after;

    }


    @Override
    public void undo() {

        new SelectionTransformer()
                .applySnapshot(
                        chart,
                        startRow,
                        startColumn,
                        before
                );

    }


    @Override
    public void redo() {

        new SelectionTransformer()
                .applySnapshot(
                        chart,
                        startRow,
                        startColumn,
                        after
                );

    }

}