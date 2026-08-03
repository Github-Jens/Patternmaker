package org.Hieke;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;

import java.util.function.Consumer;


public class SymbolButton extends Button {


    private final StitchType type;


    public SymbolButton(
            StitchType type,
            ObjectProperty<StitchType> selectedType,
            Consumer<StitchType> action
    ) {

        this.type = type;


        if (type == null) {

            setText("");

        }
        else {

            setText(type.getSymbol());

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


        if (selectedType != null) {

            selectedType.addListener(
                    (observable, oldValue, newValue) -> {

                        updateStyle(
                                selectedType.get()
                        );

                    }
            );

        }


        setOnAction(event -> {

            action.accept(type);

        });


        if (selectedType != null) {

            updateStyle(
                    selectedType.get()
            );

        }
        else {

            updateStyle(null);

        }

    }


    private void updateStyle(
            StitchType selected
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


        if (selected == type) {

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


    public StitchType getType() {

        return type;

    }

}