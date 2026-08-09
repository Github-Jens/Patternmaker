package org.Hieke;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * Stores the current state of the editor.
 *
 * <p>
 * This class acts as the central storage for user selections,
 * active tools, selected drawing properties, clipboard positions,
 * and temporary editor operations such as floating selections.
 * </p>
 *
 * <p>
 * The state is separated from the UI and rendering logic so that
 * controllers and tools can access and modify the current editor
 * situation without directly depending on visual components.
 * </p>
 */
public class EditorState {


    /**
     * The currently active editing tool.
     *
     * <p>
     * Controls how mouse input affects the chart.
     * Examples are drawing, selecting, and erasing.
     * </p>
     */
    private final ObjectProperty<Tool> activeTool =
            new SimpleObjectProperty<>(Tool.DRAW);

    private final ObjectProperty<EditorMode> mode =
            new SimpleObjectProperty<>(EditorMode.NORMAL);


    /**
     * The currently selected stitch definition used when drawing.
     */
    private final ObjectProperty<StitchDefinition> selectedStitch =
            new SimpleObjectProperty<>(null);


    /**
     * The currently selected background colour used when drawing.
     */
    private final ObjectProperty<Color> selectedColor =
            new SimpleObjectProperty<>(null);


    /**
     * The current chart selection.
     *
     * <p>
     * Represents the cells selected by the user for operations
     * such as filling, copying, rotating, or deleting.
     * </p>
     */
    private final ChartSelection selection =
            new ChartSelection();


    /**
     * Provides available stitch symbols for the editor.
     */
    private final SymbolPalette symbolPalette;


    /**
     * Index of the currently selected palette colour.
     *
     * <p>
     * A value of -1 means that no palette colour is selected.
     * </p>
     */
    private final IntegerProperty selectedColorIndex =
            new SimpleIntegerProperty(-1);


    /**
     * Current paste target row.
     */
    private int pasteRow = -1;


    /**
     * Current paste target column.
     */
    private int pasteColumn = -1;

    //checks if i have a popup window open to delay some operations
    private boolean popupActive = false;
    private boolean popupBlocksCanvas = false;


    /**
     * Temporary floating selection used for transformations.
     *
     * <p>
     * A floating selection is removed from the chart temporarily
     * and can be moved, rotated, or transformed before being placed
     * back into the chart.
     * </p>
     */
    private FloatingSelection floatingSelection;


    /**
     * Creates a new editor state.
     *
     * @param library stitch library used to populate the symbol palette
     */
    public EditorState(
            StitchLibrary library
    ) {

        symbolPalette =
                new SymbolPalette(
                        library
                );


        activeTool.addListener((observable, oldTool, newTool) -> {

            if (oldTool != newTool) {

                selection.clear();

            }

        });

    }


    /**
     * Returns the property containing the active tool.
     *
     * @return active tool property
     */
    public ObjectProperty<Tool> activeToolProperty() {

        return activeTool;

    }


    /**
     * Returns the property containing the selected stitch.
     *
     * @return selected stitch property
     */
    public ObjectProperty<StitchDefinition> selectedStitchProperty() {

        return selectedStitch;

    }


    /**
     * Returns the property containing the selected colour.
     *
     * @return selected colour property
     */
    public ObjectProperty<Color> selectedColorProperty() {

        return selectedColor;

    }


    /**
     * Returns the selected palette colour index property.
     *
     * @return selected colour index property
     */
    public IntegerProperty selectedColorIndexProperty() {

        return selectedColorIndex;

    }


    /**
     * Returns the current chart selection.
     *
     * @return current selection
     */
    public ChartSelection getSelection() {

        return selection;

    }


    /**
     * Returns the symbol palette.
     *
     * @return available stitch symbols
     */
    public SymbolPalette getSymbolPalette() {

        return symbolPalette;

    }


    /**
     * Sets the position where pasted content will be placed.
     *
     * @param row target row
     * @param column target column
     */
    public void setPastePosition(
            int row,
            int column
    ) {

        pasteRow = row;
        pasteColumn = column;

    }


    /**
     * Returns the paste target row.
     *
     * @return paste row
     */
    public int getPasteRow() {

        return pasteRow;

    }


    /**
     * Returns the paste target column.
     *
     * @return paste column
     */
    public int getPasteColumn() {

        return pasteColumn;

    }


    /**
     * Returns the current floating selection.
     *
     * @return floating selection, or {@code null} if none exists
     */
    public FloatingSelection getFloatingSelection() {

        return floatingSelection;

    }


    /**
     * Sets the current floating selection.
     *
     * @param selection new floating selection
     */
    public void setFloatingSelection(
            FloatingSelection selection
    ) {

        this.floatingSelection = selection;

    }


    /**
     * Removes the current floating selection.
     */
    public void clearFloatingSelection() {

        floatingSelection = null;

    }

    /**
     * Returns the current editor mode property.
     *
     * @return editor mode property
     */
    public ObjectProperty<EditorMode> modeProperty() {

        return mode;

    }

    /**
     * Returns the current editor mode.
     *
     * @return current editor mode
     */
    public EditorMode getMode() {

        return mode.get();

    }

    /**
     * Changes the current editor mode.
     *
     * @param mode new editor mode
     */
    public void setMode(
            EditorMode mode
    ) {

        this.mode.set(mode);

    }

    public boolean isPopupActive() {

        return popupActive;

    }

    public void setPopupActive(
            boolean active
    ) {

        popupActive = active;

    }
    public boolean isPopupBlocksCanvas() {

        return popupBlocksCanvas;

    }

    public void setPopupBlocksCanvas(
            boolean blocks
    ) {

        popupBlocksCanvas = blocks;

    }

}