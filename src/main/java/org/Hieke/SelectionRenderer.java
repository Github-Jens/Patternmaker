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
            ChartSelection selection
    ) {

        if (!selection.hasSelection()) {
            return;
        }


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

}