package org.Hieke;

public class KnittingChart {

    private Stitch[][] stitches;
    private int rows;
    private int columns;

    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 300;

    public KnittingChart(int rows, int columns) {

        this.rows = rows;
        this.columns = columns;
        stitches = new Stitch[rows][columns];

        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < columns; column++) {

                stitches[row][column] =
                        new Stitch(null, row, column);

            }
        }
    }

    public Stitch getStitch(int row, int column) {
        return stitches[row][column];
    }
    public void setStitch(
            int row,
            int column,
            Stitch stitch
    ) {

        stitches[row][column] = stitch;

    }
    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void insertRow(int index) {

        if (rows >= MAX_SIZE) {
            return;
        }

        if (index < 0 || index > rows) {
            return;
        }


        Stitch[][] newStitches =
                new Stitch[rows + 1][columns];


        for (int row = 0; row < index; row++) {

            for (int column = 0; column < columns; column++) {

                newStitches[row][column] =
                        stitches[row][column];

            }

        }


        for (int row = index; row < rows; row++) {

            for (int column = 0; column < columns; column++) {

                newStitches[row + 1][column] =
                        stitches[row][column];

            }

        }


        for (int column = 0; column < columns; column++) {

            newStitches[index][column] =
                    new Stitch(
                            null,
                            index,
                            column
                    );

        }


        stitches = newStitches;
        rows++;


        updateCoordinates();

    }

    public void insertColumn(int index) {

        if (columns >= MAX_SIZE) {
            return;
        }

        if (index < 0 || index > columns) {
            return;
        }


        Stitch[][] newStitches =
                new Stitch[rows][columns + 1];


        // Copy columns before insertion point
        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < index; column++) {

                newStitches[row][column] =
                        stitches[row][column];

            }

        }


        // Copy columns after insertion point
        for (int row = 0; row < rows; row++) {

            for (int column = index; column < columns; column++) {

                newStitches[row][column + 1] =
                        stitches[row][column];

            }

        }


        // Create empty column
        for (int row = 0; row < rows; row++) {

            newStitches[row][index] =
                    new Stitch(
                            null,
                            row,
                            index
                    );

        }


        stitches = newStitches;
        columns++;

        updateCoordinates();

    }

    public void deleteRow(int index) {

        if (index < 0 || index >= rows || rows <= MIN_SIZE) {
            return;
        }


        Stitch[][] newStitches =
                new Stitch[rows - 1][columns];


        int newRow = 0;


        for (int row = 0; row < rows; row++) {

            if (row == index) {
                continue;
            }


            for (int column = 0; column < columns; column++) {

                newStitches[newRow][column] =
                        stitches[row][column];

            }

            newRow++;

        }


        stitches = newStitches;
        rows--;

        updateCoordinates();

    }


    public void deleteColumn(int index) {

        if (index < 0 || index >= columns || columns <= MIN_SIZE) {
            return;
        }


        Stitch[][] newStitches =
                new Stitch[rows][columns - 1];


        for (int row = 0; row < rows; row++) {

            int newColumn = 0;


            for (int column = 0; column < columns; column++) {

                if (column == index) {
                    continue;
                }


                newStitches[row][newColumn] =
                        stitches[row][column];

                newColumn++;

            }

        }


        stitches = newStitches;
        columns--;

        updateCoordinates();

    }
    public void restoreRow(
            Stitch[] row,
            int index
    ) {

        insertRow(index);

        for (int column = 0; column < columns; column++) {

            stitches[index][column] = row[column];

        }

        updateCoordinates();

    }

    public void restoreColumn(
            Stitch[] column,
            int index
    ) {

        if (index < 0 || index > columns) {
            return;
        }


        Stitch[][] newStitches =
                new Stitch[rows][columns + 1];


        for (int row = 0; row < rows; row++) {

            int newColumn = 0;


            for (int currentColumn = 0;
                 currentColumn < columns + 1;
                 currentColumn++) {


                if (currentColumn == index) {

                    newStitches[row][currentColumn] =
                            column[row];

                }
                else {

                    newStitches[row][currentColumn] =
                            stitches[row][newColumn];

                    newColumn++;

                }

            }

        }


        stitches = newStitches;
        columns++;

        updateCoordinates();

    }

    private void updateCoordinates() {

        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < columns; column++) {

                stitches[row][column]
                        .setPosition(
                                row,
                                column
                        );

            }

        }

    }
    public Stitch[] getRow(int index) {

        Stitch[] row = new Stitch[columns];

        for (int column = 0; column < columns; column++) {

            row[column] = stitches[index][column];

        }

        return row;

    }


}