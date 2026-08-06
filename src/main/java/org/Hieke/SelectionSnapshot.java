package org.Hieke;

public class SelectionSnapshot {

    private final Stitch[][] stitches;


    public SelectionSnapshot(
            Stitch[][] stitches
    ) {

        this.stitches = stitches;

    }


    public Stitch get(
            int row,
            int column
    ) {

        return stitches[row][column];

    }


    public int getRows() {

        return stitches.length;

    }


    public int getColumns() {

        return stitches[0].length;

    }


    public Stitch[][] getStitches() {

        return stitches;

    }

}