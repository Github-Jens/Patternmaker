package org.Hieke;

/**
 * Represents the current operating state of the editor.
 *
 * <p>
 * The active {@link Tool} describes what the user normally does
 * when interacting with the chart. This enum describes larger
 * editor operations that temporarily change how input is handled.
 * </p>
 */
public enum EditorMode {

    /**
     * Normal editing mode.
     */
    NORMAL,


    /**
     * A selection has been lifted from the chart and is currently
     * floating for transformations or movement.
     */
    FLOATING_SELECTION,


    /**
     * A floating selection is currently being rotated.
     */
    ROTATION

}