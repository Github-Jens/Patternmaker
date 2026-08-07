package org.Hieke;

public class SelectionBounds {

    public final int startRow;
    public final int endRow;

    public final int startColumn;
    public final int endColumn;


    public SelectionBounds(
            ChartSelection selection
    ) {

        startRow =
                Math.min(
                        selection.getStartRow(),
                        selection.getEndRow()
                );

        endRow =
                Math.max(
                        selection.getStartRow(),
                        selection.getEndRow()
                );


        startColumn =
                Math.min(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );

        endColumn =
                Math.max(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );

    }


    public int getRows() {

        return endRow - startRow + 1;

    }


    public int getColumns() {

        return endColumn - startColumn + 1;

    }

}