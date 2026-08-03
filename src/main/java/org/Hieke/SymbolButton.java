package org.Hieke;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;

import java.util.function.Consumer;


public class SymbolButton extends Button {


    private final StitchDefinition definition;


    public SymbolButton(
            StitchDefinition definition,
            ObjectProperty<StitchDefinition> selectedStitch,
            Consumer<StitchDefinition> action
    ) {

        this.definition = definition;


        if (definition.getSymbol() == null) {

            setText("");

        }
        else {

            setText(
                    definition.getSymbol()
            );

        }


        setPrefSize(
                35,
                35
        );

        setMinSize(
                35,
                35
        );

        setMaxSize(
                35,
                35
        );


        setFocusTraversable(false);


        if (selectedStitch != null) {

            selectedStitch.addListener(
                    (observable, oldValue, newValue) -> {

                        updateStyle(
                                selectedStitch.get()
                        );

                    }
            );

        }


        setOnAction(event -> {

            action.accept(
                    definition
            );

        });


        if (selectedStitch != null) {

            updateStyle(
                    selectedStitch.get()
            );

        }
        else {

            updateStyle(null);

        }

    }


    private void updateStyle(
            StitchDefinition selected
    ) {

        StringBuilder style =
                new StringBuilder();


        style.append(
                "-fx-font-size: 18px;"
        );

        style.append(
                "-fx-alignment: center;"
        );

        style.append(
                "-fx-padding: 0;"
        );


        if (selected == definition) {

            style.append(
                    "-fx-border-color: blue;"
            );

            style.append(
                    "-fx-border-width: 3;"
            );

        }
        else {

            style.append(
                    "-fx-border-color: black;"
            );

            style.append(
                    "-fx-border-width: 1;"
            );

        }


        setStyle(
                style.toString()
        );

    }


    public StitchDefinition getDefinition() {

        return definition;

    }

}