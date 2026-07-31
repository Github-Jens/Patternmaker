package org.Hieke;

import javafx.scene.paint.Color;

public class StitchChange {

    private final Stitch stitch;

    private final StitchType oldType;
    private final StitchType newType;

    private final Color oldBackgroundColor;
    private final Color newBackgroundColor;


    public StitchChange(
            Stitch stitch,
            StitchType oldType,
            StitchType newType,
            Color oldBackgroundColor,
            Color newBackgroundColor
    ) {

        this.stitch = stitch;

        this.oldType = oldType;
        this.newType = newType;

        this.oldBackgroundColor = oldBackgroundColor;
        this.newBackgroundColor = newBackgroundColor;
    }


    public void undo() {

        stitch.setType(oldType);

        stitch.setBackgroundColor(
                oldBackgroundColor
        );
    }


    public void redo() {

        stitch.setType(newType);

        stitch.setBackgroundColor(
                newBackgroundColor
        );
    }
}