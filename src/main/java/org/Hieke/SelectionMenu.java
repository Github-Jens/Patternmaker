package org.Hieke;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


public class SelectionMenu extends ContextMenu {


    private final ChartEditor editor;
    private final EditorState editorState;
    private final Runnable refresh;
    private final SymbolPalette symbolPalette;
    private final Node owner;
    private SymbolPickerPopup symbolPicker;


    public SelectionMenu(
            ChartEditor editor,
            EditorState editorState,
            SymbolPalette symbolPalette,
            Runnable refresh,
            Node owner
    ){

        this.editor = editor;
        this.editorState = editorState;
        this.refresh = refresh;
        this.symbolPalette = symbolPalette;
        this.owner = owner;


        MenuItem fillSymbol =
                new MenuItem(
                        "Fill with Symbol..."
                );


        fillSymbol.setOnAction(event -> {



            if (symbolPicker == null) {

                symbolPicker =
                        new SymbolPickerPopup(
                                symbolPalette,
                                type -> {



                                    editor.fillSelection(
                                            editorState.getSelection(),
                                            type,
                                            editorState.selectedColorProperty().get(),
                                            editorState.selectedColorIndexProperty().get()
                                    );


                                    editorState.getSelection()
                                            .clear();


                                    refresh.run();

                                }
                        );

            }


            symbolPicker.show(
                    owner,
                    javafx.geometry.Side.RIGHT,
                    0,
                    0
            );

        });


        MenuItem fillColour =
                new MenuItem(
                        "Fill with Colour..."
                );


        MenuItem copy =
                new MenuItem(
                        "Copy"
                );


        MenuItem cut =
                new MenuItem(
                        "Cut"
                );


        MenuItem cancel =
                new MenuItem(
                        "Cancel"
                );


        getItems()
                .addAll(
                        fillSymbol,
                        fillColour,
                        new SeparatorMenuItem(),
                        copy,
                        cut,
                        new SeparatorMenuItem(),
                        cancel
                );

    }

}