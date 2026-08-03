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

}