package org.Hieke;

import javafx.scene.paint.Color;

public class StitchChange {

    private final Stitch stitch;

    private final StitchDefinition oldStitch;
    private final StitchDefinition newStitch;

    private final Color oldBackgroundColor;
    private final Color newBackgroundColor;


    public StitchChange(
            Stitch stitch,
            StitchDefinition oldStitch,
            StitchDefinition newStitch,
            Color oldBackgroundColor,
            Color newBackgroundColor
    ) {

        this.stitch = stitch;

        this.oldStitch = oldStitch;
        this.newStitch = newStitch;

        this.oldBackgroundColor = oldBackgroundColor;
        this.newBackgroundColor = newBackgroundColor;
    }


    public void undo() {

        stitch.setDefinition(
                oldStitch
        );

        stitch.setBackgroundColor(
                oldBackgroundColor
        );

    }


    public void redo() {

        stitch.setDefinition(
                newStitch
        );

        stitch.setBackgroundColor(
                newBackgroundColor
        );

    }

}