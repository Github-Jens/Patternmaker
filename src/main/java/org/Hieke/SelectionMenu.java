package org.Hieke;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


public class SelectionMenu extends ContextMenu {

    private final SymbolPalette symbolPalette;
    private final Palette palette;
    private final ChartCanvas chartCanvas;
    private final SelectionMenuController controller;

    private FloatingSelectionMenu floatingSelectionMenu;
    private FloatingSelectionMenuController floatingSelectionMenuController;

    private DeleteSelectionPopup deleteSelectionPopup;

    private ReplacePopup replacePopup;



    private SymbolPickerPopup symbolPicker;
    private ColourPickerPopup colourPicker;
    private ColourPickerPopup frameColourPicker;

    private double menuX;
    private double menuY;

    private boolean childPopupActive = false;


    public SelectionMenu(
            SelectionMenuController controller,
            SymbolPalette symbolPalette,
            Palette palette,
            ChartCanvas chartCanvas
    ){

        this.controller = controller;
        this.symbolPalette = symbolPalette;
        this.palette = palette;
        this.chartCanvas = chartCanvas;


        setOnHidden(event -> {

            if (!childPopupActive) {

                controller.closeSelectionMenu();

            }

        });

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

                                    controller.fillWithSymbol(definition);

                                },
                                controller::closePopup
                        );

            }
            childPopupActive = true;

            controller.openPopup();
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

                                    controller.fillWithColour(index);

                                },
                                controller::closePopup
                        );

            }
            childPopupActive = true;

            controller.openPopup();
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

                                    controller.frameWithColour(index);

                                },
                                controller::closePopup
                        );

            }
            childPopupActive = true;

            controller.openPopup();
            frameColourPicker.show(
                    chartCanvas,
                    menuX + 50,
                    menuY
            );

        });

        MenuItem replace =
                new MenuItem(
                        "Replace..."
                );

        replace.setOnAction(event -> {

            if (replacePopup == null) {

                replacePopup =
                        new ReplacePopup(
                                controller.getReplaceController(),
                                symbolPalette,
                                controller::closePopup
                        );

            }


            childPopupActive = true;

            controller.openPopup();

            controller.setPopupBlocksCanvas(true);

            replacePopup.show(
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

            controller.copy();

        });


        MenuItem paste =
                new MenuItem(
                        "Paste"
                );

        paste.setDisable(
                !controller.hasClipboardData()
        );


        paste.setOnAction(event -> {

            controller.paste();

        });


        MenuItem cut =
                new MenuItem(
                        "Cut"
                );

        cut.setOnAction(event -> {

            controller.cut();

        });

        MenuItem mirrorHorizontal =
                new MenuItem(
                        "Mirror Horizontal"
                );

        mirrorHorizontal.setOnAction(event -> {

            controller.mirrorHorizontal();

        });


        MenuItem mirrorVertical =
                new MenuItem(
                        "Mirror Vertical"
                );



        mirrorVertical.setOnAction(event -> {

            controller.mirrorVertical();

        });

        MenuItem reflect =
                new MenuItem("Reflect...");

        MenuItem move =
                new MenuItem(
                        "Move..."
                );

        reflect.setOnAction(event ->
                controller.reflectSelection()
        );

        move.setOnAction(event -> {

            controller.startMoveMode();

            hide();

        });

        MenuItem rotate =
                new MenuItem(
                        "Rotate..."
                );


        rotate.setOnAction(event -> {

            controller.startRotationMode();


            if (floatingSelectionMenuController == null) {

                floatingSelectionMenuController =
                        controller.openRotationMode();

            }


            if (floatingSelectionMenu == null) {

                floatingSelectionMenu =
                        new FloatingSelectionMenu(
                                floatingSelectionMenuController
                        );

            }


            floatingSelectionMenu.show(
                    chartCanvas,
                    menuX,
                    menuY
            );


            hide();

        });

        MenuItem insert =
                new MenuItem(
                        "Insert..."
                );


        insert.setOnAction(event -> {

            controller.insert();

        });

        MenuItem deleteRows =
                new MenuItem(
                        "Delete Selected Rows"
                );

        deleteRows.setOnAction(event -> {

            controller.deleteRows();

        });

        MenuItem deleteColumns =
                new MenuItem(
                        "Delete Selected Columns"
                );

        deleteColumns.setOnAction(event -> {

            controller.deleteColumns();

        });


        MenuItem cancel =
                new MenuItem(
                        "Cancel"
                );

        cancel.setOnAction(event -> {

            controller.cancelSelection();

            hide();

        });

        MenuItem deleteOption =
                new MenuItem(
                        "Delete..."
                );

        deleteOption.setOnAction(event -> {

                if (deleteSelectionPopup == null) {

                    deleteSelectionPopup =
                            new DeleteSelectionPopup(
                                    controller.getDeleteSelectionController(),
                                    controller::closePopup
                            );

                }



            childPopupActive = true;

            controller.openPopup();

            controller.setPopupBlocksCanvas(true);

            deleteSelectionPopup.show(
                    chartCanvas,
                    menuX + 50,
                    menuY
            );

        });

        MenuItem clear =
                new MenuItem(
                        "Delete All"
                );


        clear.setOnAction(event -> {

            controller.clear();

        });


        getItems()
                .addAll(
                        fillSymbol,
                        fillColour,
                        frameColour,
                        replace,
                        new SeparatorMenuItem(),
                        copy,
                        paste,
                        cut,
                        deleteOption,
                        clear,
                        new SeparatorMenuItem(),
                        mirrorHorizontal,
                        mirrorVertical,
                        reflect,
                        move,
                        rotate,
                        new SeparatorMenuItem(),
                        insert,
                        deleteRows,
                        deleteColumns,
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

    public FloatingSelectionMenu getFloatingSelectionMenu() {

        return floatingSelectionMenu;

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

        controller.closePopup();

    }

}