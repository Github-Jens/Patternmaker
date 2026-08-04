package org.Hieke;

public class InsertColumnChange implements UndoableChange {

    private final KnittingChart chart;
    private final int index;
    private final int amount;


    public InsertColumnChange(
            KnittingChart chart,
            int index,
            int amount
    ) {

        this.chart = chart;
        this.index = index;
        this.amount = amount;

    }


    @Override
    public void undo() {

        for (int i = 0; i < amount; i++) {

            chart.deleteColumn(index);

        }

    }


    @Override
    public void redo() {

        for (int i = 0; i < amount; i++) {

            chart.insertColumn(index);

        }

    }
}