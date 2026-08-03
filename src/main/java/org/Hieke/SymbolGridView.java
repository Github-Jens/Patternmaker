package org.Hieke;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;


public class SymbolGridView extends GridPane {


    private final SymbolPalette palette;
    private final ObjectProperty<StitchType> selectedType;
    private final Consumer<StitchType> action;


    public SymbolGridView(
            SymbolPalette palette,
            ObjectProperty<StitchType> selectedType,
            Consumer<StitchType> action
    ) {
        System.out.println("Creating SymbolGridView with palette: " + palette);

        this.palette = palette;
        this.selectedType = selectedType;
        this.action = action;


        setHgap(5);
        setVgap(5);
        setAlignment(Pos.CENTER_LEFT);


        palette.getSymbols()
                .addListener(
                        (ListChangeListener<StitchType>) change -> {

                            System.out.println(
                                    "Symbol list changed. Size: "
                                            + palette.size()
                            );

                            refresh();

                        }
                );


        if (selectedType != null) {

            selectedType.addListener(
                    (observable, oldValue, newValue) -> refresh()
            );

        }


        refresh();

    }


    /**
     * Rebuilds the grid from the current symbol palette.
     */
    private void refresh() {

        getChildren()
                .clear();


        for (int i = 0; i < palette.size(); i++) {


            SymbolButton button =
                    new SymbolButton(
                            palette.getSymbol(i),
                            selectedType,
                            action
                    );


            int column =
                    i % 2;


            int row =
                    i / 2;


            add(
                    button,
                    column,
                    row
            );

        }

    }

}