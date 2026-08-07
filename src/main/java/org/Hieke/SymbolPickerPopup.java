package org.Hieke;

import javafx.scene.layout.StackPane;
import javafx.stage.Popup;

import java.util.function.Consumer;


public class SymbolPickerPopup extends Popup {


    public SymbolPickerPopup(
            SymbolPalette palette,
            Consumer<StitchDefinition> onSelected,
            Runnable onClosed
    ) {


        SymbolGridView grid =
                new SymbolGridView(
                        palette,
                        null,
                        definition -> {

                            onSelected.accept(definition);

                            hide();

                        }
                );


        StackPane container =
                new StackPane(grid);


        container.setStyle("""
        -fx-background-color: white;
        -fx-border-color: black;
        -fx-border-width: 1;
        -fx-padding: 5;
        """);


        getContent()
                .add(container);


        setAutoHide(true);

        setOnHidden(event -> {

            onClosed.run();

        });

    }

}