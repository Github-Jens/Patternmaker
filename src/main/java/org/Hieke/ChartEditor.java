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
            StitchType selectedType,
            Color selectedColor,
            int selectedColorIndex
    ) {

        Stitch stitch =
                chart.getStitch(row, column);


        StitchType oldType =
                stitch.getType();

        Color oldBackground =
                stitch.getBackgroundColor();


        StitchType newType = oldType;

        Color newBackground = oldBackground;


        // Symbol handling
        if (selectedType != null) {

            if (selectedType == StitchType.EMPTY) {

                newType = StitchType.EMPTY;
                newBackground = null;

            }
            else {

                newType = selectedType;

            }

        }


        // Colour handling
        if (selectedType == StitchType.EMPTY) {

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
                        oldType,
                        newType,
                        oldBackground,
                        newBackground
                );


        change.redo();
        modified = true;


        if (currentStroke != null) {

            currentStroke.addChange(change);

        }

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