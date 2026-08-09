package org.Hieke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SelectionRenderer {

    private final ViewTransform transform;


    public SelectionRenderer(
            ViewTransform transform
    ) {

        this.transform = transform;

    }


    public void render(
            GraphicsContext gc,
            ChartSelection selection,
            FloatingSelection floatingSelection,
            int chartRows,
            int chartColumns
    )  {

        if (!selection.hasSelection() &&
                floatingSelection == null) {

            return;

        }


        if (selection.hasSelection() &&
                floatingSelection == null)  {

            double scaledCellSize =
                    transform.getScaledCellSize();


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


            double x =
                    transform.chartToScreenX(
                            startColumn
                    );


            double y =
                    transform.chartToScreenY(
                            startRow
                    );


            double width =
                    (endColumn - startColumn + 1)
                            * scaledCellSize;


            double height =
                    (endRow - startRow + 1)
                            * scaledCellSize;


            gc.setStroke(Color.BLUE);
            gc.setLineWidth(1);

            gc.strokeRect(
                    x,
                    y,
                    width,
                    height
            );

        }


        if (floatingSelection != null) {

            double chartX =
                    transform.chartToScreenX(0);

            double chartY =
                    transform.chartToScreenY(0);

            double chartWidth =
                    chartColumns * transform.getScaledCellSize();

            double chartHeight =
                    chartRows * transform.getScaledCellSize();


            gc.save();

            gc.beginPath();
            gc.rect(
                    chartX,
                    chartY,
                    chartWidth,
                    chartHeight
            );
            gc.closePath();
            gc.clip();


            renderFloatingSelection(
                    gc,
                    floatingSelection
            );


            gc.restore();

        }

    }

    private void renderFloatingSelection(
            GraphicsContext gc,
            FloatingSelection floatingSelection
    ) {

        SelectionSnapshot snapshot =
                floatingSelection.getSnapshot();


        double scaledCellSize =
                transform.getScaledCellSize();


        int startRow =
                floatingSelection.getRow();

        int startColumn =
                floatingSelection.getColumn();


        for (int row = 0;
             row < snapshot.getRows();
             row++) {


            for (int column = 0;
                 column < snapshot.getColumns();
                 column++) {


                Stitch stitch =
                        snapshot.get(row, column);




                if (stitch == null) {

                    continue;

                }
                System.out.println(
                        "Floating stitch " + row + "," + column +
                                " top=" + stitch.getTopBorderColor() +
                                " right=" + stitch.getRightBorderColor() +
                                " bottom=" + stitch.getBottomBorderColor() +
                                " left=" + stitch.getLeftBorderColor()
                );


                double x =
                        transform.chartToScreenX(
                                startColumn + column
                        );


                double y =
                        transform.chartToScreenY(
                                startRow + row
                        );


                if (stitch.getBackgroundColor() != null) {

                    gc.setFill(
                            stitch.getBackgroundColor()
                    );

                    gc.fillRect(
                            x,
                            y,
                            scaledCellSize,
                            scaledCellSize
                    );

                }

// Draw rotated borders
                if (stitch.getTopBorderColor() != null) {

                    gc.setStroke(stitch.getTopBorderColor());

                    gc.strokeLine(
                            x,
                            y,
                            x + scaledCellSize,
                            y
                    );

                }


                if (stitch.getRightBorderColor() != null) {

                    gc.setStroke(stitch.getRightBorderColor());

                    gc.strokeLine(
                            x + scaledCellSize,
                            y,
                            x + scaledCellSize,
                            y + scaledCellSize
                    );

                }


                if (stitch.getBottomBorderColor() != null) {

                    gc.setStroke(stitch.getBottomBorderColor());

                    gc.strokeLine(
                            x,
                            y + scaledCellSize,
                            x + scaledCellSize,
                            y + scaledCellSize
                    );

                }


                if (stitch.getLeftBorderColor() != null) {

                    gc.setStroke(stitch.getLeftBorderColor());

                    gc.strokeLine(
                            x,
                            y,
                            x,
                            y + scaledCellSize
                    );

                }


                if (stitch.getDefinition() != null) {

                    gc.setFill(Color.BLACK);

                    gc.fillText(
                            stitch.getSymbol(),
                            x + scaledCellSize / 2,
                            y + scaledCellSize / 2
                    );

                }

                gc.setStroke(Color.web("#CCCCCC"));
                gc.setLineWidth(1);

                gc.strokeLine(
                        x,
                        y,
                        x + scaledCellSize,
                        y
                );

                gc.strokeLine(
                        x,
                        y,
                        x,
                        y + scaledCellSize
                );

                gc.strokeLine(
                        x + scaledCellSize,
                        y,
                        x + scaledCellSize,
                        y + scaledCellSize
                );

                gc.strokeLine(
                        x,
                        y + scaledCellSize,
                        x + scaledCellSize,
                        y + scaledCellSize
                );



            }



        }


        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);

        gc.strokeRect(
                transform.chartToScreenX(startColumn),
                transform.chartToScreenY(startRow),
                snapshot.getColumns() * scaledCellSize,
                snapshot.getRows() * scaledCellSize
        );




    }

}