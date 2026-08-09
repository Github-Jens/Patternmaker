package org.Hieke;

public class FloatingSelection {

    private SelectionSnapshot snapshot;
    private final SelectionSnapshot originalSnapshot;

    private final int originalRow;
    private final int originalColumn;

    private int row;
    private int column;


    public FloatingSelection(
            SelectionSnapshot snapshot,
            SelectionSnapshot originalSnapshot,
            int row,
            int column
    ) {

        this.snapshot = snapshot;
        this.originalSnapshot = originalSnapshot;

        this.originalRow = row;
        this.originalColumn = column;

        this.row = row;
        this.column = column;

    }


    public SelectionSnapshot getSnapshot() {

        return snapshot;

    }


    public SelectionSnapshot getOriginalSnapshot() {

        return originalSnapshot;

    }


    public int getRow() {

        return row;

    }


    public int getColumn() {

        return column;

    }


    public int getRows() {

        return snapshot.getRows();

    }


    public int getColumns() {

        return snapshot.getColumns();

    }


    public void move(
            int row,
            int column
    ) {

        this.row = row;
        this.column = column;

    }

    public void rotate90(
            SelectionTransformer transformer
    ) {

        snapshot =
                transformer.rotate90(snapshot);

    }

    public void rotateCounterClockwise90(
            SelectionTransformer transformer
    ) {

        snapshot =
                transformer.rotateCounterClockwise90(
                        snapshot
                );

    }

    public int getOriginalRow() {

        return originalRow;

    }


    public int getOriginalColumn() {

        return originalColumn;

    }

}