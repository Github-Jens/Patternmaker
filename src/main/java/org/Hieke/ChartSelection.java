package org.Hieke;

public class ChartSelection {

    private int startRow;
    private int startColumn;

    private int endRow;
    private int endColumn;


    public ChartSelection() {

        clear();

    }


    public void setStart(
            int row,
            int column
    ) {

        startRow = row;
        startColumn = column;

        endRow = row;
        endColumn = column;

    }


    public void setEnd(
            int row,
            int column
    ) {

        endRow = row;
        endColumn = column;

    }


    public int getStartRow() {

        return startRow;

    }


    public int getStartColumn() {

        return startColumn;

    }


    public int getEndRow() {

        return endRow;

    }


    public int getEndColumn() {

        return endColumn;

    }


    public void clear() {

        startRow = -1;
        startColumn = -1;
        endRow = -1;
        endColumn = -1;

    }


    public boolean isEmpty() {

        return !hasSelection();

    }

    public boolean hasSelection() {

        return startRow >= 0
                && startColumn >= 0
                && endRow >= 0
                && endColumn >= 0;

    }

}