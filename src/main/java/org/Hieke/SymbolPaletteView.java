package org.Hieke;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SymbolPaletteView extends VBox {


    private final SymbolGridView symbolGrid;
    private final StitchLibrary library;


    public SymbolPaletteView(
            SymbolPalette palette,
            ObjectProperty<StitchDefinition> selectedStitch,
            StitchLibrary library
    ) {

        this.library = library;


        symbolGrid =
                new SymbolGridView(
                        palette,
                        selectedStitch,
                        definition -> {

                            if (selectedStitch.get() == definition) {

                                selectedStitch.set(null);

                            }
                            else {

                                selectedStitch.set(definition);

                            }

                        }
                );


        Button addButton =
                new Button(
                        "+ Add Stitch"
                );


        addButton.setOnAction(event -> {

            StitchLibraryDialog dialog =
                    new StitchLibraryDialog(
                            library
                    );


            dialog.showAndWait()
                    .ifPresent(definition -> {

                        palette.addSymbol(
                                definition
                        );

                    });

        });


        VBox.setMargin(
                addButton,
                new Insets(10, 0, 0, 0)
        );


        getChildren()
                .addAll(
                        symbolGrid,
                        addButton
                );

    }

}