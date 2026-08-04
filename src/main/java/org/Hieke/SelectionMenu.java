package org.Hieke;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


public class SelectionMenu extends ContextMenu {


    private final ChartEditor editor;
    private final EditorState editorState;
    private final Runnable refresh;
    private final SymbolPalette symbolPalette;
    private final Palette palette;
    private final ChartCanvas chartCanvas;


    private SymbolPickerPopup symbolPicker;
    private ColourPickerPopup colourPicker;
    private ColourPickerPopup frameColourPicker;

    private double menuX;
    private double menuY;


    public SelectionMenu(
            ChartEditor editor,
            EditorState editorState,
            SymbolPalette symbolPalette,
            Palette palette,
            Runnable refresh,
            ChartCanvas chartCanvas
    ){

        this.editor = editor;
        this.editorState = editorState;
        this.refresh = refresh;
        this.symbolPalette = symbolPalette;
        this.palette = palette;
        this.chartCanvas = chartCanvas;

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
                                            Tool.DRAW,
                                            definition,
                                            null,
                                            -1
                                    );

                                    finishAction();

                                }
                        );

            }


            symbolPicker.show(
                    chartCanvas,
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

                                    finishAction();

                                }
                        );

            }


            colourPicker.show(
                    chartCanvas,
                    menuX + 50,
                    menuY
            );

        });

        //Frame Colour menu

        MenuItem frameColour =
                new MenuItem(
                        "Frame with Colour..."
                );


        frameColour.setOnAction(event -> {


            if (frameColourPicker == null) {

                frameColourPicker =
                        new ColourPickerPopup(
                                palette,
                                index -> {


                                    editor.frameSelection(
                                            editorState.getSelection(),
                                            palette.getColor(index)
                                    );

                                    finishAction();

                                }
                        );

            }


            frameColourPicker.show(
                    chartCanvas,
                    menuX + 50,
                    menuY
            );

        });

        MenuItem copy =
                new MenuItem(
                        "Copy"
                );


        copy.setOnAction(event -> {

            editor.copySelection(
                    editorState.getSelection()
            );

            finishAction();

        });


        MenuItem paste =
                new MenuItem(
                        "Paste"
                );

        paste.setDisable(
                !editor.hasClipboardData()
        );


        paste.setOnAction(event -> {

            chartCanvas.paste();
            finishAction();

        });


        MenuItem cut =
                new MenuItem(
                        "Cut"
                );

        cut.setOnAction(event -> {

            editor.copySelection(
                    editorState.getSelection()
            );


            editor.clearSelection(
                    editorState.getSelection()
            );

            finishAction();

        });

        MenuItem deleteRows =
                new MenuItem(
                        "Delete Selected Rows"
                );

        deleteRows.setOnAction(event -> {

            ChartSelection selection =
                    editorState.getSelection();


            int startRow =
                    Math.min(
                            selection.getStartRow(),
                            selection.getEndRow()
                    );

            int endRow =
                    Math.max(
                            selection.getStartRow(),
                            selection.getEndRow()
                    );


            for (int row = startRow;
                 row <= endRow;
                 row++) {

                editor.deleteRow(startRow);

            }

            finishAction();

        });

        MenuItem deleteColumns =
                new MenuItem(
                        "Delete Selected Columns"
                );

        deleteColumns.setOnAction(event -> {

            ChartSelection selection =
                    editorState.getSelection();


            int startColumn =
                    Math.min(
                            selection.getStartColumn(),
                            selection.getEndColumn()
                    );

            int endColumn =
                    Math.max(
                            selection.getStartColumn(),
                            selection.getEndColumn()
                    );


            for (int column = startColumn;
                 column <= endColumn;
                 column++) {

                editor.deleteColumn(startColumn);

            }

            finishAction();

        });


        MenuItem cancel =
                new MenuItem(
                        "Cancel"
                );

        cancel.setOnAction(event -> {

            hide();
            finishAction();

        });

        MenuItem clear =
                new MenuItem(
                        "Delete"
                );


        clear.setOnAction(event -> {

            editor.clearSelection(
                    editorState.getSelection()
            );

            finishAction();

        });


        getItems()
                .addAll(
                        fillSymbol,
                        fillColour,
                        frameColour,
                        new SeparatorMenuItem(),
                        copy,
                        paste,
                        cut,
                        clear,
                        new SeparatorMenuItem(),
                        deleteRows,
                        deleteColumns,
                        new SeparatorMenuItem(),
                        cancel
                );

    }

    private void finishAction() {

        editorState.getSelection()
                .clear();

        refresh.run();

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

        if (frameColourPicker != null) {

            frameColourPicker.hide();

        }

    }

}