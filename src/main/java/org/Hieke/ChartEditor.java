package org.Hieke;

import javafx.scene.paint.Color;
import org.Hieke.KnittingChart;

import java.util.Stack;

public class ChartEditor {

    private final KnittingChart chart;

    private final Stack<Stroke> undoStack = new Stack<>();
    private final Stack<Stroke> redoStack = new Stack<>();
    private Stroke currentStroke;
    private final ChartClipboard clipboard =
            new ChartClipboard();

    private boolean modified = false;

    public ChartEditor(
            KnittingChart chart
    ) {
        this.chart = chart;
    }

    public void paintCell(
            int row,
            int column,
            Tool activeTool,
            StitchDefinition selectedStitch,
            Color selectedColor,
            int selectedColorIndex
    ) {

        Stitch stitch =
                chart.getStitch(row, column);


        StitchDefinition oldStitch =
                stitch.getDefinition();

        Color oldBackground =
                stitch.getBackgroundColor();


        StitchDefinition newStitch = oldStitch;

        Color newBackground = oldBackground;


        // Tool handling
        if (activeTool == Tool.ERASE) {

            newStitch = null;
            newBackground = null;

        }
        else {

            newStitch = selectedStitch;

        }

        // Colour handling
        if (activeTool == Tool.ERASE) {

            newBackground = null;

        }
        else if (selectedColor != null) {

            newBackground = selectedColor;

        }


        StitchChange change =
                new StitchChange(
                        stitch,
                        oldStitch,
                        newStitch,
                        oldBackground,
                        newBackground
                );


        change.redo();
        modified = true;


        if (currentStroke != null) {

            currentStroke.addChange(change);

        }

    }

    public void fillSelection(
            ChartSelection selection,
            Tool activeTool,
            StitchDefinition definition,
            Color selectedColor,
            int selectedColorIndex
    ){


        if (!selection.hasSelection()) {

            return;

        }


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


        beginStroke();


        for (int row = startRow; row <= endRow; row++) {


            for (int column = startColumn;
                 column <= endColumn;
                 column++) {


                paintCell(
                        row,
                        column,
                        activeTool,
                        definition,
                        selectedColor,
                        selectedColorIndex
                );

            }

        }


        endStroke();

    }

    public void clearSelection(
            ChartSelection selection
    ) {

        if (!selection.hasSelection()) {

            return;

        }


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


        beginStroke();


        for (int row = startRow; row <= endRow; row++) {

            for (int column = startColumn;
                 column <= endColumn;
                 column++) {


                paintCell(
                        row,
                        column,
                        Tool.ERASE,
                        null,
                        null,
                        -1
                );

            }
        }


        endStroke();

    }

    public void copySelection(
            ChartSelection selection
    ) {

        if (!selection.hasSelection()) {
            return;
        }


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


        ChartClipboardData[][] data =
                new ChartClipboardData
                        [endRow - startRow + 1]
                        [endColumn - startColumn + 1];


        for (int row = startRow; row <= endRow; row++) {

            for (int column = startColumn; column <= endColumn; column++) {

                Stitch stitch =
                        chart.getStitch(row,column);


                data[row-startRow][column-startColumn] =
                        new ChartClipboardData(
                                stitch.getDefinition(),
                                stitch.getBackgroundColor()
                        );

            }

        }


        clipboard.copy(data);

    }



    public void paste(
            int targetRow,
            int targetColumn
    ) {

        if (!clipboard.hasData()) {
            return;
        }


        ChartClipboardData[][] data =
                clipboard.getData();


        beginStroke();


        for (int row = 0; row < data.length; row++) {

            for (int column = 0; column < data[row].length; column++) {


                int chartRow =
                        targetRow + row;


                int chartColumn =
                        targetColumn + column;


                if (chartRow < 0 ||
                        chartColumn < 0 ||
                        chartRow >= chart.getRows() ||
                        chartColumn >= chart.getColumns()) {

                    continue;

                }


                ChartClipboardData cell =
                        data[row][column];


                paintCell(
                        chartRow,
                        chartColumn,
                        Tool.DRAW,
                        cell.getDefinition(),
                        cell.getColor(),
                        -1
                );

            }

        }


        endStroke();

    }

    public void beginStroke() {

        currentStroke = new Stroke();

    }


    public void endStroke() {

        if (currentStroke != null) {

            undoStack.push(currentStroke);

            redoStack.clear();

            currentStroke = null;

        }

    }

    public void undo() {

        if (!undoStack.isEmpty()) {

            Stroke stroke = undoStack.pop();

            stroke.undo();

            redoStack.push(stroke);

        }

    }


    public void redo() {

        if (!redoStack.isEmpty()) {

            Stroke stroke = redoStack.pop();

            stroke.redo();

            undoStack.push(stroke);

        }

    }

    public KnittingChart getChart() {
        return chart;
    }

    public boolean hasClipboardData() {

        return clipboard.hasData();

    }

    public boolean isModified() {

        return modified;

    }


    public void markSaved() {

        modified = false;

    }

}