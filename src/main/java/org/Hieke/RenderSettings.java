package org.Hieke;

public class RenderSettings {

    public final double cellSize;
    public final double zoom;
    public final double offsetX;
    public final double offsetY;
    public final double rulerSize;

    public final int firstRow;
    public final int lastRow;
    public final int firstColumn;
    public final int lastColumn;


    public RenderSettings(
            double cellSize,
            double zoom,
            double offsetX,
            double offsetY,
            double rulerSize,
            int firstRow,
            int lastRow,
            int firstColumn,
            int lastColumn
    ) {

        this.cellSize = cellSize;
        this.zoom = zoom;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.rulerSize = rulerSize;

        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstColumn = firstColumn;
        this.lastColumn = lastColumn;
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

        this.rulerSize = cellSize;


        this.firstRow = 0;
        this.lastRow = rows - 1;

        this.firstColumn = 0;
        this.lastColumn = columns - 1;
    }
}