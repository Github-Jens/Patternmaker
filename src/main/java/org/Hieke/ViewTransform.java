package org.Hieke;

public class ViewTransform {

    public static final double DEFAULT_CELL_SIZE = 30;
    public static final double DEFAULT_RULER_SIZE = 30;

    private final double cellSize;
    private final double rulerSize;

    private double zoom = 1.0;

    private double offsetX = 0;
    private double offsetY = 0;


    public ViewTransform(
            double cellSize,
            double rulerSize
    ) {

        this.cellSize = cellSize;
        this.rulerSize = rulerSize;

    }


    public double getZoom() {

        return zoom;

    }


    public double getOffsetX() {

        return offsetX;

    }


    public double getOffsetY() {

        return offsetY;

    }


    public double getScaledCellSize() {

        return cellSize * zoom;

    }


    public double getScaledRulerSize() {

        return rulerSize * zoom;

    }


    public int screenToColumn(
            double mouseX
    ) {

        return (int)(
                (mouseX - offsetX - getScaledRulerSize())
                        / getScaledCellSize()
        );

    }


    public int screenToRow(
            double mouseY
    ) {

        return (int)(
                (mouseY - offsetY - getScaledRulerSize())
                        / getScaledCellSize()
        );

    }


    public double chartToScreenX(
            int column
    ) {

        return offsetX
                + getScaledRulerSize()
                + column * getScaledCellSize();

    }


    public double chartToScreenY(
            int row
    ) {

        return offsetY
                + getScaledRulerSize()
                + row * getScaledCellSize();

    }


    public void pan(
            double dx,
            double dy
    ) {

        offsetX += dx;
        offsetY += dy;

    }


    public void zoom(
            double newZoom,
            double mouseX,
            double mouseY
    ) {

        double oldZoom = zoom;


        zoom = newZoom;


        double zoomFactor =
                zoom / oldZoom;


        offsetX =
                mouseX
                        - (mouseX - offsetX)
                        * zoomFactor;


        offsetY =
                mouseY
                        - (mouseY - offsetY)
                        * zoomFactor;

    }
    public double getCellSize() {

        return cellSize;

    }


    public double getRulerSize() {

        return rulerSize;

    }


    public void reset() {

        zoom = 1.0;

        offsetX = 0;
        offsetY = 0;

    }

}