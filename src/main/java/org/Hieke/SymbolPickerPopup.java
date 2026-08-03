package org.Hieke;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;

import java.util.function.Consumer;


public class SymbolPickerPopup extends ContextMenu {


    /**
     * Creates a popup symbol selector.
     *
     * Uses the same SymbolGridView as the main symbol palette
     * so both interfaces share the same appearance and behaviour.
     *
     * @param palette available stitch symbols
     * @param onSelected action executed when a symbol is selected
     */
    public SymbolPickerPopup(
            SymbolPalette palette,
            Consumer<StitchType> onSelected
    ) {


        SymbolGridView grid =
                new SymbolGridView(
                        palette,
                        null,
                        type -> {

                            onSelected.accept(type);

                            hide();

                        }
                );


        CustomMenuItem item =
                new CustomMenuItem(
                        grid
                );


        item.setHideOnClick(false);


        getItems()
                .add(item);

    }

}