package org.Hieke;

public class RenderSettings {

    public final double cellSize;
    public final double zoom;
    public final double offsetX;
    public final double offsetY;

    public final int firstRow;
    public final int lastRow;
    public final int firstColumn;
    public final int lastColumn;

    public final int renderRows;
    public final int renderColumns;


    public RenderSettings(
            double cellSize,
            double zoom,
            double offsetX,
            double offsetY,
            int firstRow,
            int lastRow,
            int firstColumn,
            int lastColumn,
            int renderRows,
            int renderColumns
    ) {

        this.cellSize = cellSize;
        this.zoom = zoom;
        this.offsetX = offsetX;
        this.offsetY = offsetY;

        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstColumn = firstColumn;
        this.lastColumn = lastColumn;

        this.renderRows = renderRows;
        this.renderColumns = renderColumns;
    }


    public RenderSettings(
            double cellSize,
            int rows,
            int columns
    ) {

        this.cellSize = cellSize;
        this.zoom = 1.0;

        this.offsetX = 0;
        this.offsetY = 0;

        this.firstRow = 0;
        this.lastRow = rows + 1;

        this.firstColumn = 0;
        this.lastColumn = columns + 1;

        this.renderRows = rows + 2;
        this.renderColumns = columns + 2;
    }
}