package org.Hieke;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;

import java.util.function.Consumer;


/**
 * Popup menu used for selecting a colour from the editor palette.
 *
 * Used by:
 * - SelectionMenu
 *
 * Returns the selected palette index.
 */
public class ColourPickerPopup extends ContextMenu {


    public ColourPickerPopup(
            Palette palette,
            Consumer<Integer> onSelected,
            Runnable onClosed
    ) {
        setOnHidden(event -> {

            onClosed.run();

        });

        for (int i = 0; i < palette.size(); i++) {

            int index = i;

            Color colour =
                    palette.getColor(i);


            MenuItem item =
                    new MenuItem(
                            colour == null
                                    ? "No Colour"
                                    : "Colour " + (i + 1)
                    );


            if (colour != null) {

                item.setStyle(
                        "-fx-background-color: "
                                + colour.toString()
                                .replace("0x", "#")
                );

            }


            item.setOnAction(event -> {

                onSelected.accept(index);

                hide();

            });


            getItems().add(item);

        }

    }

}