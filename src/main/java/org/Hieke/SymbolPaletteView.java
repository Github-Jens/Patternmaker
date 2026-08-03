/**
 * Displays the user's customizable stitch palette.
 *
 * The view delegates the creation and behaviour of individual
 * stitch buttons to SymbolGridView.
 *
 * Used by:
 * - ToolPanel
 */

package org.Hieke;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.VBox;

public class SymbolPaletteView extends VBox {


    private final SymbolGridView symbolGrid;


    public SymbolPaletteView(
            SymbolPalette palette,
            ObjectProperty<StitchType> selectedType
    ) {

        symbolGrid =
                new SymbolGridView(
                        palette,
                        selectedType,
                        type -> {

                            if (selectedType.get() == type) {

                                selectedType.set(null);

                            }
                            else {

                                selectedType.set(type);

                            }

                        }
                );


        getChildren()
                .add(symbolGrid);

    }

}