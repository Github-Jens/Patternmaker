package org.Hieke;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class EditorState {


    // Currently active tool
    private final ObjectProperty<Tool> activeTool =
            new SimpleObjectProperty<>(Tool.DRAW);


    // Currently selected stitch type
    private final ObjectProperty<StitchType> selectedType =
            new SimpleObjectProperty<>(null);


    // Currently selected colour
    private final ObjectProperty<Color> selectedColor =
            new SimpleObjectProperty<>(null);

    //Currently selected Grid tiles

    private final ChartSelection selection =
            new ChartSelection();

    //Currentl Symbol Palette
    private final SymbolPalette symbolPalette =
            new SymbolPalette();


    // Index in palette
    // -1 = no colour operation
    // 0  = remove colour
    // 1+ = apply palette colour
    private final IntegerProperty selectedColorIndex =
            new SimpleIntegerProperty(-1);



    public ObjectProperty<Tool> activeToolProperty() {

        return activeTool;

    }


    public ObjectProperty<StitchType> selectedTypeProperty() {

        return selectedType;

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