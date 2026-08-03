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
    private final Palette palette;
    private final Node owner;

    private SymbolPickerPopup symbolPicker;
    private ColourPickerPopup colourPicker;

    private double menuX;
    private double menuY;


    public SelectionMenu(
            ChartEditor editor,
            EditorState editorState,
            SymbolPalette symbolPalette,
            Palette palette,
            Runnable refresh,
            Node owner
    ){

        this.editor = editor;
        this.editorState = editorState;
        this.refresh = refresh;
        this.symbolPalette = symbolPalette;
        this.owner = owner;
        this.palette = palette;

        //Symbol Picker menu
        MenuItem fillSymbol =
                new MenuItem(
                        "Fill with Symbol..."
                );


        fillSymbol.setOnAction(event -> {



            if (symbolPicker == null) {

                symbolPicker =
                        new SymbolPickerPopup(
                                symbolPalette,
                                definition -> {



                                    editor.fillSelection(
                                            editorState.getSelection(),
                                            editorState.activeToolProperty().get(),
                                            definition,
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
                    menuX + 50,
                    menuY
            );

        });

        //Colourpicker Menu

        //Colour Picker menu

        MenuItem fillColour =
                new MenuItem(
                        "Fill with Colour..."
                );


        fillColour.setOnAction(event -> {


            if (colourPicker == null) {

                colourPicker =
                        new ColourPickerPopup(
                                palette,
                                index -> {


                                    editor.fillSelection(
                                            editorState.getSelection(),
                                            Tool.DRAW,
                                            null,
                                            palette.getColor(index),
                                            index
                                    );


                                    editorState.getSelection()
                                            .clear();


                                    refresh.run();

                                }
                        );

            }


            colourPicker.show(
                    owner,
                    menuX + 50,
                    menuY
            );

        });


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
    public void setMenuPosition(
            double x,
            double y
    ) {

        this.menuX = x;
        this.menuY = y;

    }

    public void closeAllPickers() {

        if (symbolPicker != null) {

            symbolPicker.hide();

        }


        if (colourPicker != null) {

            colourPicker.hide();

        }

    }

}