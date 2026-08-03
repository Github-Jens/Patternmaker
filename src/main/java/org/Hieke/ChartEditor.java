package org.Hieke;

import javafx.scene.paint.Color;
import org.Hieke.KnittingChart;

import java.util.Stack;

public class ChartEditor {

    private final KnittingChart chart;

    private final Stack<Stroke> undoStack = new Stack<>();
    private final Stack<Stroke> redoStack = new Stack<>();
    private Stroke currentStroke;

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
        else if (selectedColorIndex >= 0) {

            newBackground = selectedColor;

        }
        else {

            // No colour selected:
            // Keep the existing background colour unchanged

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

    public boolean isModified() {

        return modified;

    }


    public void markSaved() {

        modified = false;

    }

}