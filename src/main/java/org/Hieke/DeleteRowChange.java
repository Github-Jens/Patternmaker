package org.Hieke;

public class DeleteRowChange implements UndoableChange {

    private final KnittingChart chart;
    private final Stitch[] deletedRow;
    private final int index;


    public DeleteRowChange(
            KnittingChart chart,
            Stitch[] deletedRow,
            int index
    ) {

        this.chart = chart;
        this.deletedRow = deletedRow;
        this.index = index;

    }


    @Override
    public void undo() {

        chart.restoreRow(
                deletedRow,
                index
        );

    }


    @Override
    public void redo() {

        chart.deleteRow(index);

    }

}