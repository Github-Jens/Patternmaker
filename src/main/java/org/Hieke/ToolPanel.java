package org.Hieke;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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


        VBox symbolPanel =
                new VBox();

        symbolPanel.setSpacing(5);


        Label symbolTitle =
                new Label("Symbols");


        symbolPanel.getChildren()
                .addAll(
                        symbolTitle,
                        symbolPaletteView
                );


        PaletteView paletteView =
                new PaletteView(
                        palette,
                        editorState.selectedColorProperty(),
                        editorState.selectedColorIndexProperty()
                );


        setSpacing(15);


        getChildren().addAll(
                symbolPanel,
                paletteView
        );

    }

}