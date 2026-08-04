package org.Hieke;

public class InsertRowChange implements UndoableChange {

    private final KnittingChart chart;
    private final int index;


    public InsertRowChange(
            KnittingChart chart,
            int index
    ) {

        this.chart = chart;
        this.index = index;

    }


    @Override
    public void undo() {

        chart.deleteRow(index);

    }


    @Override
    public void redo() {

        chart.insertRow(index);

    }

}