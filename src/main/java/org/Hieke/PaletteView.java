package org.Hieke;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PaletteView extends VBox {

    private final Palette palette;
    private final ObjectProperty<Color> selectedColor;

    private final GridPane colorContainer;
    private final IntegerProperty selectedIndex =
            new SimpleIntegerProperty(-1);


    public PaletteView(
            Palette palette,
            ObjectProperty<Color> selectedColor,
             IntegerProperty selectedIndex
    ) {

        this.palette = palette;
        this.selectedColor = selectedColor;
        this.selectedIndex.bindBidirectional(selectedIndex);

        colorContainer = new GridPane();

        colorContainer.setHgap(5);
        colorContainer.setVgap(5);
        colorContainer.setAlignment(Pos.CENTER_LEFT);

        getChildren().add(
                colorContainer
        );


        selectedColor.addListener((observable, oldValue, newValue) -> {
            refresh();
        });


        refresh();

    }


    private void refresh() {

        colorContainer.getChildren()
                .clear();


        for (int i = 0; i < palette.size(); i++) {

            Button button =
                    createColorButton(i);


            int column = i % 2;
            int row = i / 2;


            colorContainer.add(
                    button,
                    column,
                    row
            );

        }


        Button addButton =
                new Button("+ Add Colour");


        addButton.setOnAction(event -> {

            palette.addColor();

            refresh();

        });


        int addRow =
                (palette.size() + 1) / 2;


        colorContainer.add(
                addButton,
                0,
                addRow,
                2,
                1
        );

    }


    private Button createColorButton(
            int index
    ) {

        Color color =
                palette.getColor(index);


        Button button =
                new Button();


        button.setPrefSize(
                35,
                35
        );



        updateButtonColor(
                button,
                color,
                index
        );


        // Left click = select colour
        button.setOnAction(event -> {

            selectedIndex.set(index);

            selectedColor.set(
                    palette.getColor(index)
            );

        });


        // Right click = colour menu
        button.setOnContextMenuRequested(event -> {

            ContextMenu menu =
                    new ContextMenu();


            MenuItem change =
                    new MenuItem(
                            "Change Colour"
                    );


            ColorPicker picker =
                    new ColorPicker(
                            palette.getColor(index)
                    );


            change.setOnAction(e -> {

                palette.setColor(
                        index,
                        picker.getValue()
                );

                refresh();

            });


            menu.getItems()
                    .add(change);


            // Do not allow removing "No Colour"
            if (index != 0) {

                MenuItem remove =
                        new MenuItem(
                                "Remove Colour"
                        );


                remove.setOnAction(e -> {

                    if (selectedIndex.get() == index) {

                        selectedIndex.set(-1);
                        selectedColor.set(null);

                    }
                    else if (selectedIndex.get() > index) {

                        selectedIndex.set(
                                selectedIndex.get() - 1
                        );

                    }


                    palette.getColors()
                            .remove(index);


                    refresh();

                });


                menu.getItems()
                        .add(remove);

            }


            menu.show(
                    button,
                    event.getScreenX(),
                    event.getScreenY()
            );

        });


        return button;

    }
    private void updateButtonColor(
            Button button,
            Color color,
            int index
    ) {

        StringBuilder style =
                new StringBuilder();


        if (color == null) {

            style.append(
                    "-fx-background-color: transparent;"
            );

            button.setText("X");

        }
        else {

            style.append(
                    "-fx-background-color: "
            );

            style.append(
                    color.toString()
                            .replace("0x", "#")
            );

            style.append(";");

            button.setText("");

        }


        if (index == selectedIndex.get()) {

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


        button.setStyle(
                style.toString()
        );

    }

    public boolean isColorSelected() {

        return selectedIndex.get() >= 0;

    }
    public IntegerProperty selectedIndexProperty() {
        return selectedIndex;
    }


}


