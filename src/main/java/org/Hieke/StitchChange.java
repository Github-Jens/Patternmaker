package org.Hieke;

import javafx.scene.paint.Color;

public class StitchChange implements UndoableChange {

    private final Stitch stitch;

    private final StitchDefinition oldStitch;
    private final StitchDefinition newStitch;

    private final Color oldBackgroundColor;
    private final Color newBackgroundColor;

    private final Color oldTopBorderColor;
    private final Color newTopBorderColor;

    private final Color oldRightBorderColor;
    private final Color newRightBorderColor;

    private final Color oldBottomBorderColor;
    private final Color newBottomBorderColor;

    private final Color oldLeftBorderColor;
    private final Color newLeftBorderColor;


    public StitchChange(
            Stitch stitch,
            StitchDefinition oldStitch,
            StitchDefinition newStitch,
            Color oldBackgroundColor,
            Color newBackgroundColor,

            Color oldTopBorderColor,
            Color newTopBorderColor,

            Color oldRightBorderColor,
            Color newRightBorderColor,

            Color oldBottomBorderColor,
            Color newBottomBorderColor,

            Color oldLeftBorderColor,
            Color newLeftBorderColor

    ) {

        this.stitch = stitch;

        this.oldStitch = oldStitch;
        this.newStitch = newStitch;

        this.oldBackgroundColor = oldBackgroundColor;
        this.newBackgroundColor = newBackgroundColor;

        this.oldTopBorderColor = oldTopBorderColor;
        this.newTopBorderColor = newTopBorderColor;

        this.oldRightBorderColor = oldRightBorderColor;
        this.newRightBorderColor = newRightBorderColor;

        this.oldBottomBorderColor = oldBottomBorderColor;
        this.newBottomBorderColor = newBottomBorderColor;

        this.oldLeftBorderColor = oldLeftBorderColor;
        this.newLeftBorderColor = newLeftBorderColor;
    }

    @Override
    public void undo() {

        stitch.setDefinition(
                oldStitch
        );

        stitch.setBackgroundColor(
                oldBackgroundColor
        );

        stitch.setTopBorderColor(
                oldTopBorderColor
        );

        stitch.setRightBorderColor(
                oldRightBorderColor
        );

        stitch.setBottomBorderColor(
                oldBottomBorderColor
        );

        stitch.setLeftBorderColor(
                oldLeftBorderColor
        );

    }

    @Override
    public void redo() {

        stitch.setDefinition(
                newStitch
        );

        stitch.setBackgroundColor(
                newBackgroundColor
        );

        stitch.setTopBorderColor(
                newTopBorderColor
        );

        stitch.setRightBorderColor(
                newRightBorderColor
        );

        stitch.setBottomBorderColor(
                newBottomBorderColor
        );

        stitch.setLeftBorderColor(
                newLeftBorderColor
        );

    }

}