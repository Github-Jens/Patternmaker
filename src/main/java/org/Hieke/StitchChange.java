package org.Hieke;

public class StitchChange {

    private final Stitch stitch;
    private final StitchType oldType;
    private final StitchType newType;


    public StitchChange(Stitch stitch,
                        StitchType oldType,
                        StitchType newType) {

        this.stitch = stitch;
        this.oldType = oldType;
        this.newType = newType;
    }


    public void undo() {
        stitch.setType(oldType);
    }


    public void redo() {
        stitch.setType(newType);
    }
}