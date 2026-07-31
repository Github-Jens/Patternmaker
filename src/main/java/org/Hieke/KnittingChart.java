package org.Hieke;

public class KnittingChart {

    private Stitch[][] stitches;
    private int rows;
    private int columns;

    public KnittingChart(int rows, int columns) {

        this.rows = rows;
        this.columns = columns;
        stitches = new Stitch[rows][columns];

        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < columns; column++) {

                stitches[row][column] =
                        new Stitch(StitchType.EMPTY, row, column);

            }
        }
    }

    public Stitch getStitch(int row, int column) {
        return stitches[row][column];
    }
    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}