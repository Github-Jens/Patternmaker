package org.Hieke;

public class DeleteColumnChange implements UndoableChange {

    private final KnittingChart chart;
    private final Stitch[] deletedColumn;
    private final int index;


    public DeleteColumnChange(
            KnittingChart chart,
            Stitch[] deletedColumn,
            int index
    ) {

        this.chart = chart;
        this.deletedColumn = deletedColumn;
        this.index = index;

    }


    @Override
    public void undo() {

        chart.restoreColumn(
                deletedColumn,
                index
        );

    }


    @Override
    public void redo() {

        chart.deleteColumn(index);

    }

}