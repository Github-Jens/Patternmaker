package org.Hieke;

public class SelectionTransformer {


    public SelectionSnapshot createSnapshot(
            KnittingChart chart,
            ChartSelection selection
    ) {

        int startRow =
                Math.min(
                        selection.getStartRow(),
                        selection.getEndRow()
                );

        int endRow =
                Math.max(
                        selection.getStartRow(),
                        selection.getEndRow()
                );


        int startColumn =
                Math.min(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );

        int endColumn =
                Math.max(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );


        int rows =
                endRow - startRow + 1;

        int columns =
                endColumn - startColumn + 1;


        Stitch[][] snapshot =
                new Stitch[rows][columns];


        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < columns; column++) {


                snapshot[row][column] =
                        chart.getStitch(
                                        startRow + row,
                                        startColumn + column
                                )
                                .copy();

            }

        }


        return new SelectionSnapshot(
                snapshot
        );

    }

    public void applySnapshot(
            KnittingChart chart,
            int startRow,
            int startColumn,
            SelectionSnapshot snapshot
    ) {
        for (int row = 0;
             row < snapshot.getRows();
             row++) {


            for (int column = 0;
                 column < snapshot.getColumns();
                 column++) {


                chart.getStitch(
                                startRow + row,
                                startColumn + column
                        )
                        .copyFrom(
                                snapshot.get(
                                        row,
                                        column
                                )
                        );

            }

        }

    }


    public SelectionSnapshot mirrorHorizontal(
            SelectionSnapshot source
    ) {

        Stitch[][] result =
                new Stitch[
                        source.getRows()
                        ][
                        source.getColumns()
                        ];


        for (int row = 0;
             row < source.getRows();
             row++) {


            for (int column = 0;
                 column < source.getColumns();
                 column++) {


                result[row][column] =
                        source.get(
                                        row,
                                        source.getColumns()
                                                - column
                                                - 1
                                )
                                .copy();

            }

        }


        return new SelectionSnapshot(
                result
        );

    }

    public SelectionSnapshot mirrorVertical(
            SelectionSnapshot source
    ) {

        Stitch[][] result =
                new Stitch[
                        source.getRows()
                        ][
                        source.getColumns()
                        ];


        for (int row = 0;
             row < source.getRows();
             row++) {


            for (int column = 0;
                 column < source.getColumns();
                 column++) {


                result[row][column] =
                        source.get(
                                        source.getRows()
                                                - row
                                                - 1,
                                        column
                                )
                                .copy();

            }

        }


        return new SelectionSnapshot(
                result
        );

    }

}