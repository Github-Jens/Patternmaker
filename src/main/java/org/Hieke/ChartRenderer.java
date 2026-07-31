package org.Hieke;

public class ChartRenderer {

    private final KnittingChart chart;
    private final Ruler ruler;

    public ChartRenderer(KnittingChart chart) {
        this.chart = chart;
        this.ruler = new Ruler(30);
    }
    public void render(
            RenderContext context,
            RenderSettings settings,
            double width,
            double height
    ) {

        ruler.renderBackground(
                context,
                width,
                height
        );


        renderGrid(
                context,
                settings
        );


        renderStitches(
                context,
                settings
        );


        ruler.render(
                context,
                settings
        );
    }

    public void renderGrid(
            RenderContext context,
            RenderSettings settings

    ) {

        double scaledCellSize =
                settings.cellSize * settings.zoom;


        // Horizontal lines
        for (int row = settings.firstRow;
             row <= settings.lastRow + 1;
             row++) {


            double y =
                    row * scaledCellSize
                            + settings.offsetY
                            + settings.rulerSize;


            context.drawLine(
                    settings.offsetX + settings.rulerSize,
                    y,
                    chart.getColumns() * scaledCellSize
                            + settings.offsetX
                            + settings.rulerSize,
                    y
            );
        }



        // Vertical lines
        for (int column = settings.firstColumn;
             column <= settings.lastColumn + 1;
             column++) {


            double x =
                    column * scaledCellSize
                            + settings.offsetX
                            + settings.rulerSize;


            context.drawLine(
                    x,
                    settings.offsetY + settings.rulerSize,
                    x,
                    chart.getRows() * scaledCellSize
                            + settings.offsetY
                            + settings.rulerSize
            );
        }
    }
    public void renderStitches(
            RenderContext context,
            RenderSettings settings
    ) {

        double scaledCellSize =
                settings.cellSize * settings.zoom;


        for (int row = settings.firstRow;
             row <= settings.lastRow;
             row++) {


            for (int column = settings.firstColumn;
                 column <= settings.lastColumn;
                 column++) {


                Stitch stitch =
                        chart.getStitch(row, column);


                if (stitch.getType() == StitchType.EMPTY) {
                    continue;
                }



                drawStitch(
                        context,
                        settings,
                        stitch
                );
            }
        }
    }
    private void drawStitch(
            RenderContext context,
            RenderSettings settings,
            Stitch stitch
    ) {

        double scaledCellSize =
                settings.cellSize * settings.zoom;


        double x =
                stitch.getColumn() * scaledCellSize
                        + scaledCellSize / 2
                        + settings.offsetX
                        + settings.rulerSize;


        double y =
                stitch.getRow() * scaledCellSize
                        + scaledCellSize / 2
                        + settings.offsetY
                        + settings.rulerSize;


        context.drawText(
                stitch.getSymbol(),
                x,
                y
        );
    }
}