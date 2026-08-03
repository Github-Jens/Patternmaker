package org.Hieke;

public class ChartRenderer {

    private final KnittingChart chart;
    private static final String GRID_COLOR = "#CCCCCC";


    public ChartRenderer(KnittingChart chart) {
        this.chart = chart;

    }
    public void render(
            RenderContext context,
            RenderSettings settings,
            double width,
            double height
    ) {

        renderBackgrounds(
                context,
                settings
        );


        renderGrid(
                context,
              settings
        );


        renderStitches(
                context,
                settings
        );

        renderRulerCells(
                context,
                settings
        );

    }

    public void renderGrid(
            RenderContext context,
            RenderSettings settings
    ) {

        context.setStroke(GRID_COLOR);

        double scaledCellSize =
                settings.cellSize * settings.zoom;


        // Horizontal lines
        for (int row = 1;
             row < settings.renderRows - 1;
             row++) {

            double y =
                    row * scaledCellSize
                            + settings.offsetY;


            context.drawLine(
                    settings.offsetX + scaledCellSize,
                    y,
                    settings.offsetX
                            + (settings.renderColumns - 1)
                            * scaledCellSize,
                    y
            );
        }


        // Vertical lines
        for (int column = 1;
             column < settings.renderColumns - 1;
             column++) {

            double x =
                    column * scaledCellSize
                            + settings.offsetX;


            context.drawLine(
                    x,
                    settings.offsetY + scaledCellSize,
                    x,
                    settings.offsetY
                            + (settings.renderRows - 1)
                            * scaledCellSize
            );
        }


        // Ruler/chart separator lines

        // top separator
        context.drawLine(
                settings.offsetX + scaledCellSize,
                settings.offsetY + scaledCellSize,
                settings.offsetX
                        + (settings.renderColumns - 1) * scaledCellSize,
                settings.offsetY + scaledCellSize
        );

        // bottom separator
        context.drawLine(
                settings.offsetX + scaledCellSize,
                settings.offsetY
                        + (settings.renderRows - 1) * scaledCellSize,
                settings.offsetX
                        + (settings.renderColumns - 1) * scaledCellSize,
                settings.offsetY
                        + (settings.renderRows - 1) * scaledCellSize
        );

        // left separator
        context.drawLine(
                settings.offsetX + scaledCellSize,
                settings.offsetY + scaledCellSize,
                settings.offsetX + scaledCellSize,
                settings.offsetY
                        + (settings.renderRows - 1) * scaledCellSize
        );

        // right separator
        context.drawLine(
                settings.offsetX
                        + (settings.renderColumns - 1) * scaledCellSize,
                settings.offsetY + scaledCellSize,
                settings.offsetX
                        + (settings.renderColumns - 1) * scaledCellSize,
                settings.offsetY
                        + (settings.renderRows - 1) * scaledCellSize
        );
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


                if (!isStitchCell(row, column, settings)) {
                    continue;
                }

                Stitch stitch =
                        chart.getStitch(
                                chartRow(row),
                                chartColumn(column)
                        );


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


        double cellX =
                (stitch.getColumn() + 1) * scaledCellSize
                        + settings.offsetX;


        double cellY =
                (stitch.getRow() + 1) * scaledCellSize
                        + settings.offsetY;



        // Draw stitch symbol in the center of the cell
        double x =
                cellX + scaledCellSize / 2;


        double y =
                cellY + scaledCellSize / 2;


        if (stitch.getDefinition() != null) {

            context.drawText(
                    stitch.getSymbol(),
                    x,
                    y
            );

        }
    }
    private void renderBackgrounds(
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


                if (!isStitchCell(row, column, settings)) {
                    continue;
                }

                Stitch stitch =
                        chart.getStitch(
                                chartRow(row),
                                chartColumn(column)
                        );


                if (stitch.getBackgroundColor() == null) {
                    continue;
                }


                double x =
                        column * scaledCellSize
                                + settings.offsetX;


                double y =
                        row * scaledCellSize
                                + settings.offsetY;


                context.fillRect(
                        stitch.getBackgroundColor(),
                        x,
                        y,
                        scaledCellSize,
                        scaledCellSize
                );

            }
        }
    }
    private void renderRulerCells(
            RenderContext context,
            RenderSettings settings
    ) {

        renderTopRuler(
                context,
                settings
        );

        renderBottomRuler(
                context,
                settings
        );

        renderLeftRuler(
                context,
                settings
        );

        renderRightRuler(
                context,
                settings
        );
    }
    private void renderTopRuler(
            RenderContext context,
            RenderSettings settings
    ) {

        double scaledCellSize =
                settings.cellSize * settings.zoom;

        context.setFill("#000000");


        for (int column = 0;
             column < chart.getColumns();
             column++) {


            double x =
                    (column + 1.5) * scaledCellSize
                            + settings.offsetX;


            context.drawText(
                    String.valueOf(chart.getColumns() - column),
                    x,
                    settings.offsetY
                            + scaledCellSize / 2
            );
        }
    }


    private void renderBottomRuler(
            RenderContext context,
            RenderSettings settings
    ) {

        double scaledCellSize =
                settings.cellSize * settings.zoom;


        for (int column = 0;
             column < chart.getColumns();
             column++) {


            double x =
                    (column + 1.5) * scaledCellSize
                            + settings.offsetX;


            context.drawText(
                    String.valueOf(chart.getColumns() - column),
                    x,
                    settings.offsetY
                            + (settings.renderRows - 0.5)
                            * scaledCellSize
            );
        }
    }


    private void renderLeftRuler(
            RenderContext context,
            RenderSettings settings
    ) {

        double scaledCellSize =
                settings.cellSize * settings.zoom;


        for (int row = 0;
             row < chart.getRows();
             row++) {


            double y =
                    (row + 1.5) * scaledCellSize
                            + settings.offsetY;


            context.drawText(
                    String.valueOf(chart.getRows() - row),
                    settings.offsetX
                            + scaledCellSize / 2,
                    y
            );
        }
    }


    private void renderRightRuler(
            RenderContext context,
            RenderSettings settings
    ) {

        double scaledCellSize =
                settings.cellSize * settings.zoom;


        for (int row = 0;
             row < chart.getRows();
             row++) {


            double y =
                    (row + 1.5) * scaledCellSize
                            + settings.offsetY;


            context.drawText(
                    String.valueOf(chart.getRows() - row),
                    settings.offsetX
                            + (settings.renderColumns - 0.5)
                            * scaledCellSize,
                    y
            );
        }
    }


    private boolean isStitchCell(
            int renderRow,
            int renderColumn,
            RenderSettings settings
    ) {
        return renderRow > 0
                && renderRow < chart.getRows() + 1
                && renderColumn > 0
                && renderColumn < chart.getColumns() + 1;
    }


    private int chartRow(int renderRow) {
        return renderRow - 1;
    }


    private int chartColumn(int renderColumn) {
        return renderColumn - 1;
    }
}