package org.Hieke;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SymbolPaletteView extends VBox {


    private final SymbolGridView symbolGrid;


    public SymbolPaletteView(
            SymbolPalette palette,
            ObjectProperty<StitchType> selectedType
    ) {

        System.out.println(
                "SymbolPaletteView received: " + palette
        );


        symbolGrid =
                new SymbolGridView(
                        palette,
                        selectedType,
                        type -> {

                            selectedType.set(type);

                        }
                );


        Button addButton =
                new Button("+");


        addButton.setOnAction(event -> {

            System.out.println(
                    "Adding to: " + palette
            );

            palette.addSymbol(
                    StitchType.PURL
            );

        });


        HBox controls =
                new HBox(
                        addButton
                );


        setSpacing(5);


        getChildren()
                .addAll(
                        symbolGrid,
                        controls
                );

    }

}