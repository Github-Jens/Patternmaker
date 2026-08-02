package org.Hieke;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class ToolPanel extends VBox {

    private final ObjectProperty<StitchType> selectedType;


    public ToolPanel(
            Palette palette,
            ObjectProperty<StitchType> selectedType,
            ObjectProperty<Color> selectedColor,
            IntegerProperty selectedColorIndex
    ) {
        this.selectedType = selectedType;

        //Left VBOX

        // Left Panel

        VBox symbolPanel = new VBox();
        symbolPanel.setSpacing(5);

        Label symbolTitle = new Label("Symbols");

        Button knitButton = new Button("K - Knit");
        Button purlButton = new Button("P - Purl");
        Button yarnButton = new Button("O - Yarn Over");
        Button k2togButton = new Button("/ - K2tog");
        Button eraserButton = new Button("Eraser");
        knitButton.setFocusTraversable(false);
        purlButton.setFocusTraversable(false);
        yarnButton.setFocusTraversable(false);
        k2togButton.setFocusTraversable(false);
        eraserButton.setFocusTraversable(false);

        knitButton.setOnAction(event ->
                selectStitchTool(
                        StitchType.KNIT,
                        knitButton,
                        knitButton,
                        purlButton,
                        yarnButton,
                        k2togButton,
                        eraserButton
                )
        );

        purlButton.setOnAction(event ->
                selectStitchTool(
                        StitchType.PURL,
                        purlButton,
                        knitButton,
                        purlButton,
                        yarnButton,
                        k2togButton,
                        eraserButton
                )
        );

        yarnButton.setOnAction(event ->
                selectStitchTool(
                        StitchType.YARN_OVER,
                        yarnButton,
                        knitButton,
                        purlButton,
                        yarnButton,
                        k2togButton,
                        eraserButton
                )
        );

        k2togButton.setOnAction(event ->
                selectStitchTool(
                        StitchType.K2TOG,
                        k2togButton,
                        knitButton,
                        purlButton,
                        yarnButton,
                        k2togButton,
                        eraserButton
                )
        );


        symbolPanel.getChildren().addAll(
                symbolTitle,
                knitButton,
                purlButton,
                yarnButton,
                k2togButton,
                eraserButton
        );

        eraserButton.setOnAction(event ->
                selectStitchTool(
                        StitchType.EMPTY,
                        eraserButton,
                        knitButton,
                        purlButton,
                        yarnButton,
                        k2togButton,
                        eraserButton
                )
        );


// Palette

        PaletteView paletteView =
                new PaletteView(
                        palette,
                        selectedColor,
                        selectedColorIndex
                );


// Combine left side

        setSpacing(15);

        getChildren().addAll(
                symbolPanel,
                paletteView
        );

    }

    private void selectTool(Button selectedButton, Button... buttons) {

        for (Button button : buttons) {

            button.setStyle("");

        }

        if (selectedButton != null) {

            selectedButton.setStyle(
                    "-fx-background-color: lightblue;"
            );

        }
    }
    private void selectStitchTool(
            StitchType type,
            Button selectedButton,
            Button... buttons
    ) {

        if (selectedType.get() == type) {

            // Deselect current stitch tool
            selectedType.set(null);

            selectTool(
                    null,
                    buttons
            );

        } else {

            // Select new stitch tool
            selectedType.set(type);

            selectTool(
                    selectedButton,
                    buttons
            );

        }
    }

}