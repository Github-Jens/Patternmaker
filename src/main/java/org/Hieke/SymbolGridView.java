package org.Hieke;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;


public class SymbolGridView extends GridPane {


    private final SymbolPalette palette;
    private final ObjectProperty<StitchDefinition> selectedStitch;
    private final Consumer<StitchDefinition> action;

    private ContextMenu activeMenu;


    public SymbolGridView(
            SymbolPalette palette,
            ObjectProperty<StitchDefinition> selectedStitch,
            Consumer<StitchDefinition> action
    ) {

        this.palette = palette;
        this.selectedStitch = selectedStitch;
        this.action = action;


        setHgap(5);
        setVgap(5);
        setAlignment(Pos.CENTER_LEFT);


        palette.getSymbols()
                .addListener(
                        (ListChangeListener<StitchDefinition>) change -> {



                            refresh();

                        }
                );


        if (selectedStitch != null)  {

            selectedStitch.addListener(
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
                            selectedStitch,
                            action
                    );


            createContextMenu(
                    button,
                    i
            );


            createContextMenu(
                    button,
                    i
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

    private void createContextMenu(
            SymbolButton button,
            int index
    ) {

        button.setOnContextMenuRequested(event -> {


            // Do not allow removing the normal stitch
            if (index == 0) {
                return;
            }


            if (activeMenu != null) {

                activeMenu.hide();

            }


            ContextMenu menu =
                    new ContextMenu();


            MenuItem remove =
                    new MenuItem(
                            "Remove Stitch"
                    );


            remove.setOnAction(e -> {


                if (selectedStitch.get()
                        == button.getDefinition()) {

                    selectedStitch.set(null);

                }


                palette.removeSymbol(
                        index
                );


            });


            menu.getItems()
                    .add(remove);


            menu.show(
                    button,
                    event.getScreenX(),
                    event.getScreenY()
            );


            activeMenu = menu;

        });

    }

}