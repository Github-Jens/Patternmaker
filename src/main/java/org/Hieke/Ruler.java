package org.Hieke;

import javafx.scene.paint.Color;

public class Ruler {

    private final int size;

    public Ruler(int size) {
        this.size = size;
    }


    public void render(
            RenderContext context,
            RenderSettings settings
    ) {

        double scaledCellSize =
                settings.cellSize * settings.zoom;
        context.setFill("#000000");


        // Columns

        for (int column = settings.firstColumn;
             column <= settings.lastColumn;
             column++) {


            double x =
                    column * scaledCellSize
                            + settings.offsetX
                            + size
                            + scaledCellSize / 2;


            context.drawText(
                    String.valueOf(column + 1),
                    x,
                    size / 2
            );
        }



        // Rows

        for (int row = settings.firstRow;
             row <= settings.lastRow;
             row++) {


            double y =
                    row * scaledCellSize
                            + settings.offsetY
                            + size
                            + scaledCellSize / 2;


            context.drawText(
                    String.valueOf(row + 1),
                    size / 2,
                    y
            );
        }

    }
    public void renderBackground(
            RenderContext context,
            double width,
            double height
    ) {

        context.setFill("#FFFFFF");

        // Top ruler area
        context.fillRect(
                Color.WHITE,
                0,
                0,
                width,
                size
        );

        // Left ruler area
        context.fillRect(
                Color.WHITE,
                0,
                0,
                size,
                height
        );
    }
}