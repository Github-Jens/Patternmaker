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
                Stitch original =
                        chart.getStitch(
                                startRow + row,
                                startColumn + column
                        );


                System.out.println(
                        "Chart stitch "
                                + row + "," + column
                                + " top=" + original.getTopBorderColor()
                                + " right=" + original.getRightBorderColor()
                                + " bottom=" + original.getBottomBorderColor()
                                + " left=" + original.getLeftBorderColor()
                );


                snapshot[row][column] =
                        original.copy();

            }

        }


        return new SelectionSnapshot(
                snapshot
        );

    }

    public SelectionSnapshot createSnapshot(
            KnittingChart chart,
            int startRow,
            int startColumn,
            int rows,
            int columns
    ) {

        Stitch[][] snapshot =
                new Stitch[rows][columns];


        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < columns; column++) {


                int chartRow = startRow + row;
                int chartColumn = startColumn + column;


                if (chartRow < 0 ||
                        chartColumn < 0 ||
                        chartRow >= chart.getRows() ||
                        chartColumn >= chart.getColumns()) {

                    snapshot[row][column] =
                            new Stitch(
                                    null,
                                    chartRow,
                                    chartColumn
                            );

                    continue;
                }


                snapshot[row][column] =
                        chart.getStitch(
                                        chartRow,
                                        chartColumn
                                )
                                .copy();

            }

        }


        return new SelectionSnapshot(snapshot);

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


                int chartRow =
                        startRow + row;

                int chartColumn =
                        startColumn + column;


                if (chartRow < 0 ||
                        chartColumn < 0 ||
                        chartRow >= chart.getRows() ||
                        chartColumn >= chart.getColumns()) {

                    continue;

                }


                Stitch source =
                        snapshot.get(row, column);


                System.out.println(
                        "Applying "
                                + chartRow
                                + ","
                                + chartColumn
                                + " top="
                                + source.getTopBorderColor()
                                + " right="
                                + source.getRightBorderColor()
                                + " bottom="
                                + source.getBottomBorderColor()
                                + " left="
                                + source.getLeftBorderColor()
                );


                chart.getStitch(
                                chartRow,
                                chartColumn
                        )
                        .copyFrom(source);

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

    public SelectionSnapshot rotate90(
            SelectionSnapshot source
    ) {

        Stitch[][] result =
                new Stitch[
                        source.getColumns()
                        ][
                        source.getRows()
                        ];


        for (int row = 0;
             row < source.getRows();
             row++) {


            for (int column = 0;
                 column < source.getColumns();
                 column++) {


                Stitch original =
                        source.get(row, column);

                System.out.println(
                        "Before rotation "
                                + row + "," + column
                                + " top=" + original.getTopBorderColor()
                                + " right=" + original.getRightBorderColor()
                                + " bottom=" + original.getBottomBorderColor()
                                + " left=" + original.getLeftBorderColor()
                );

                Stitch rotated =
                        original.copy();


                rotated.setTopBorderColor(
                        original.getLeftBorderColor()
                );

                rotated.setRightBorderColor(
                        original.getTopBorderColor()
                );

                rotated.setBottomBorderColor(
                        original.getRightBorderColor()
                );

                rotated.setLeftBorderColor(
                        original.getBottomBorderColor()
                );


                result[column]
                        [source.getRows() - row - 1]
                        =
                        rotated;

            }

        }


        return new SelectionSnapshot(
                result
        );

    }

    public SelectionSnapshot rotateCounterClockwise90(
            SelectionSnapshot source
    ) {

        Stitch[][] result =
                new Stitch[
                        source.getColumns()
                        ][
                        source.getRows()
                        ];


        for (int row = 0;
             row < source.getRows();
             row++) {


            for (int column = 0;
                 column < source.getColumns();
                 column++) {


                Stitch original =
                        source.get(row, column);


                Stitch rotated =
                        original.copy();


                // Rotate borders counter-clockwise
                rotated.setTopBorderColor(
                        original.getRightBorderColor()
                );

                rotated.setRightBorderColor(
                        original.getBottomBorderColor()
                );

                rotated.setBottomBorderColor(
                        original.getLeftBorderColor()
                );

                rotated.setLeftBorderColor(
                        original.getTopBorderColor()
                );


                result[
                        source.getColumns()
                                - column
                                - 1
                        ][row] = rotated;

            }

        }


        return new SelectionSnapshot(
                result
        );

    }

    public void replaceSelection(
            KnittingChart chart,
            int startRow,
            int startColumn,
            int oldRows,
            int oldColumns,
            SelectionSnapshot snapshot
    ) {

        clearArea(
                chart,
                startRow,
                startColumn,
                oldRows,
                oldColumns
        );


        applySnapshot(
                chart,
                startRow,
                startColumn,
                snapshot
        );

    }



    public void clearArea(
            KnittingChart chart,
            int startRow,
            int startColumn,
            int rows,
            int columns
    ) {

        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < columns; column++) {


                int chartRow =
                        startRow + row;

                int chartColumn =
                        startColumn + column;


                if (chartRow < 0 ||
                        chartColumn < 0 ||
                        chartRow >= chart.getRows() ||
                        chartColumn >= chart.getColumns()) {

                    continue;

                }


                chart.getStitch(
                                chartRow,
                                chartColumn
                        )
                        .copyFrom(
                                new Stitch(
                                        null,
                                        chartRow,
                                        chartColumn
                                )
                        );

            }

        }

    }

}