package org.Hieke;

public class InsertColumnChange implements UndoableChange {

    private final KnittingChart chart;
    private final int index;


    public InsertColumnChange(
            KnittingChart chart,
            int index
    ) {

        this.chart = chart;
        this.index = index;

    }


    @Override
    public void undo() {

        chart.deleteColumn(index);

    }


    @Override
    public void redo() {

        chart.insertColumn(index);

    }

}