package org.Hieke;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class ToolPanel extends VBox {

    private final EditorState editorState;

    public ToolPanel(
            Palette palette,
            EditorState editorState
    ) {
        this.editorState = editorState;
        SymbolPaletteView symbolPaletteView =
                new SymbolPaletteView(
                        editorState.getSymbolPalette(),
                        editorState.selectedTypeProperty()
                );


        // Left Panel

        // Tools Panel

        VBox toolPanel = new VBox();
        toolPanel.setSpacing(5);

        Label toolTitle = new Label("Tools");

        Button selectButton = new Button("Select");

        selectButton.setFocusTraversable(false);

        selectButton.setOnAction(event -> {

            if (editorState.activeToolProperty().get() == Tool.SELECT) {

                // Deselect select tool
                editorState.activeToolProperty()
                        .set(Tool.DRAW);

                selectButton.setStyle("");

            } else {

                // Activate select tool
                editorState.activeToolProperty()
                        .set(Tool.SELECT);

                selectButton.setStyle(
                        "-fx-background-color: lightblue;"
                );

            }

        });

        toolPanel.getChildren().addAll(
                toolTitle,
                selectButton
        );

        //Symbols
        VBox symbolPanel = new VBox();

        symbolPanel.setSpacing(5);

        Label symbolTitle =
                new Label("Symbols");

        symbolPanel.getChildren()
                .addAll(
                        symbolTitle,
                        symbolPaletteView
                );

// Palette

        PaletteView paletteView =
                new PaletteView(
                        palette,
                        editorState.selectedColorProperty(),
                        editorState.selectedColorIndexProperty()
                );


// Combine left side

        setSpacing(15);

        getChildren().addAll(
                toolPanel,
                symbolPanel,
                paletteView
        );

    }
}