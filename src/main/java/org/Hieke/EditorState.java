package org.Hieke;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class EditorState {

    private final ObjectProperty<Tool> activeTool =
            new SimpleObjectProperty<>(Tool.DRAW);


    private final ObjectProperty<StitchDefinition> selectedStitch =
            new SimpleObjectProperty<>(null);


    private final ObjectProperty<Color> selectedColor =
            new SimpleObjectProperty<>(null);


    private final ChartSelection selection =
            new ChartSelection();


    private final SymbolPalette symbolPalette;


    private final IntegerProperty selectedColorIndex =
            new SimpleIntegerProperty(-1);

    private int pasteRow = -1;
    private int pasteColumn = -1;

    private FloatingSelection floatingSelection;



    public EditorState(
            StitchLibrary library
    ) {

        symbolPalette =
                new SymbolPalette(
                        library
                );

    }


    public ObjectProperty<Tool> activeToolProperty() {

        return activeTool;

    }


    public ObjectProperty<StitchDefinition> selectedStitchProperty() {

        return selectedStitch;

    }


    public ObjectProperty<Color> selectedColorProperty() {

        return selectedColor;

    }


    public IntegerProperty selectedColorIndexProperty() {

        return selectedColorIndex;

    }


    public ChartSelection getSelection() {

        return selection;

    }


    public SymbolPalette getSymbolPalette() {

        return symbolPalette;

    }
    public void setPastePosition(
            int row,
            int column
    ) {

        pasteRow = row;
        pasteColumn = column;

    }


    public int getPasteRow() {

        return pasteRow;

    }


    public int getPasteColumn() {

        return pasteColumn;

    }

    public FloatingSelection getFloatingSelection() {

        return floatingSelection;

    }


    public void setFloatingSelection(
            FloatingSelection selection
    ) {

        this.floatingSelection = selection;

    }


    public void clearFloatingSelection() {

        floatingSelection = null;

    }
}