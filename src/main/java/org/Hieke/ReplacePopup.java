package org.Hieke;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.StringConverter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ReplacePopup extends Popup {

    private final ReplaceController controller;
    private final Runnable closePopup;

    private final ComboBox<ReplaceType> replaceType;

    private final VBox sourceContainer;
    private final VBox targetContainer;

    private final SymbolPalette symbolPalette;

    private ComboBox<ReplaceSymbolOption> sourceSymbol;
    private ComboBox<ReplaceSymbolOption> targetSymbol;
    private ComboBox<Integer> sourceColour;
    private ComboBox<Integer> targetColour;

    public ReplacePopup(
            ReplaceController controller,
            SymbolPalette symbolPalette,
            Runnable closePopup
    ) {

        this.controller = controller;
        this.symbolPalette = symbolPalette;
        this.closePopup = closePopup;


        VBox layout =
                new VBox(10);

        layout.setPadding(
                new Insets(10)
        );


        Label replaceLabel =
                new Label("Replace:");


        replaceType =
                new ComboBox<>();

        replaceType.getItems().addAll(
                ReplaceType.values()
        );

        replaceType.setValue(
                ReplaceType.SYMBOL_TO_SYMBOL
        );


        sourceContainer =
                new VBox(5);

        targetContainer =
                new VBox(5);


        replaceType.setOnAction(event -> {

            updateSelectors();

        });


        Button apply =
                new Button("Apply");

        apply.setOnAction(event -> {

            ReplaceType type =
                    replaceType.getValue();


            if (type == ReplaceType.SYMBOL_TO_SYMBOL) {

                ReplaceSymbolOption sourceOption =
                        sourceSymbol.getValue();

                ReplaceSymbolOption targetOption =
                        targetSymbol.getValue();

                StitchDefinition source =
                        sourceOption == null
                                ? null
                                : sourceOption.getDefinition();

                StitchDefinition target =
                        targetOption == null
                                ? null
                                : targetOption.getDefinition();

                controller.replaceSymbol(
                        source,
                        target
                );

            }
            else if (type == ReplaceType.SYMBOL_TO_COLOUR) {

                ReplaceSymbolOption sourceOption =
                        sourceSymbol.getValue();

                Integer colourIndex =
                        targetColour.getValue();

                if (sourceOption != null
                        && colourIndex != null) {

                    StitchDefinition source =
                            sourceOption.getDefinition();

                    controller.replaceSymbolWithColour(
                            source,
                            controller.getPaletteColor(
                                    colourIndex
                            )
                    );

                }

            }

            else if (type == ReplaceType.COLOUR_TO_SYMBOL) {

                Integer colourIndex =
                        sourceColour.getValue();

                ReplaceSymbolOption targetOption =
                        targetSymbol.getValue();

                if (colourIndex != null
                        && targetOption != null) {

                    StitchDefinition target =
                            targetOption.getDefinition();

                    controller.replaceColourWithSymbol(
                            controller.getPaletteColor(
                                    colourIndex
                            ),
                            target
                    );

                }

            }
            else if (type == ReplaceType.COLOUR_TO_COLOUR) {

                Integer sourceIndex =
                        sourceColour.getValue();

                Integer targetIndex =
                        targetColour.getValue();

                if (sourceIndex != null
                        && targetIndex != null) {

                    Color source =
                            controller.getPaletteColor(
                                    sourceIndex
                            );

                    Color target =
                            controller.getPaletteColor(
                                    targetIndex
                            );

                    controller.replaceColour(
                            source,
                            target
                    );

                }

            }


            hide();
            closePopup.run();

        });


        Button cancel =
                new Button("Cancel");

        cancel.setOnAction(event -> {

            hide();

            closePopup.run();

        });


        VBox buttons =
                new VBox(
                        5,
                        apply,
                        cancel
                );


        layout.getChildren().addAll(
                replaceLabel,
                replaceType,
                new Separator(),
                sourceContainer,
                targetContainer,
                new Separator(),
                buttons
        );


        layout.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #999999;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 10;"
        );

        getContent().add(layout);


        updateSelectors();

    }


    private void updateSelectors() {

        sourceContainer.getChildren().clear();
        targetContainer.getChildren().clear();


        ReplaceType type =
                replaceType.getValue();


        if (type == null) {
            return;
        }


        switch (type) {

            case SYMBOL_TO_SYMBOL:

                addSymbolSource();
                addSymbolTarget();

                break;


            case SYMBOL_TO_COLOUR:

                addSymbolSource();
                addColourTarget();

                break;


            case COLOUR_TO_SYMBOL:

                addColourSource();
                addSymbolTarget();

                break;


            case COLOUR_TO_COLOUR:

                addColourSource();
                addColourTarget();

                break;

        }

    }


    private void addSymbolSource() {

        sourceContainer.getChildren().add(
                new Label(
                        "Symbol to replace:"
                )
        );

        sourceSymbol =
                new ComboBox<>();

        sourceSymbol.getItems().add(
                new ReplaceSymbolOption(
                        "No symbol",
                        null
                )
        );

        for (StitchDefinition definition :
                controller.getSymbolsUsedInChart()) {

            sourceSymbol.getItems().add(
                    new ReplaceSymbolOption(
                            definition.getName()
                                    + " ("
                                    + definition.getSymbol()
                                    + ")",
                            definition
                    )
            );

        }


        sourceSymbol.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(
                            ReplaceSymbolOption option
                    ) {

                        if (option == null) {
                            return "";
                        }

                        return option.getDisplayName();

                    }

                    @Override
                    public ReplaceSymbolOption fromString(
                            String string
                    ) {

                        return null;
                    }

                }
        );


        sourceContainer.getChildren().add(
                sourceSymbol
        );

    }


    private void addSymbolTarget() {

        targetContainer.getChildren().add(
                new Label(
                        "Replace with symbol:"
                )
        );

        targetSymbol =
                new ComboBox<>();

        for (StitchDefinition definition :
                symbolPalette.getSymbols()) {

            targetSymbol.getItems().add(
                    new ReplaceSymbolOption(
                            definition.getName()
                                    + " ("
                                    + definition.getSymbol()
                                    + ")",
                            definition
                    )
            );

        }


        targetSymbol.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(
                            ReplaceSymbolOption option
                    ) {

                        if (option == null) {
                            return "";
                        }

                        return option.getDisplayName();

                    }

                    @Override
                    public ReplaceSymbolOption fromString(
                            String string
                    ) {

                        return null;
                    }

                }
        );


        targetContainer.getChildren().add(
                targetSymbol
        );

    }


    private void addColourSource() {

        sourceContainer.getChildren().add(
                new Label(
                        "Colour to replace:"
                )
        );

        sourceColour =
                new ComboBox<>();

        sourceColour.setItems(
                FXCollections.observableArrayList(
                        controller.getColoursUsedInChart()
                )
        );

        setupColourComboBox(sourceColour);

        sourceContainer.getChildren().add(
                sourceColour
        );

    }


    private void addColourTarget() {

        targetContainer.getChildren().add(
                new Label(
                        "Replace with colour:"
                )
        );

        targetColour =
                new ComboBox<>();

        targetColour.setItems(
                controller.getPaletteColourIndices()
        );


        setupColourComboBox(targetColour);

        targetContainer.getChildren().add(
                targetColour
        );

    }

    private void setupColourComboBox(
            ComboBox<Integer> comboBox
    ) {

        comboBox.setCellFactory(listView ->
                new javafx.scene.control.ListCell<>() {

                    @Override
                    protected void updateItem(
                            Integer index,
                            boolean empty
                    ) {

                        super.updateItem(
                                index,
                                empty
                        );

                        if (empty || index == null) {

                            setGraphic(null);
                            setText(null);

                            return;

                        }

                        if (index == 0) {

                            setGraphic(null);
                            setText("No colour");

                            return;
                        }

                        Color color =
                                controller.getPaletteColor(index);

                        Rectangle rectangle =
                                new Rectangle(
                                        20,
                                        20
                                );

                        rectangle.setFill(color);
                        rectangle.setStroke(Color.GRAY);
                        rectangle.setStrokeWidth(1);

                        setGraphic(rectangle);

                        setText(null);
                    }

                }
        );

        comboBox.setButtonCell(
                new javafx.scene.control.ListCell<>() {

                    @Override
                    protected void updateItem(
                            Integer index,
                            boolean empty
                    ) {

                        super.updateItem(
                                index,
                                empty
                        );

                        if (empty || index == null) {

                            setGraphic(null);
                            setText(null);

                            return;

                        }

                        if (index == 0) {

                            setGraphic(null);
                            setText("No colour");

                            return;
                        }

                        Color color =
                                controller.getPaletteColor(index);

                        Rectangle rectangle =
                                new Rectangle(
                                        20,
                                        20
                                );

                        rectangle.setFill(color);

                        setGraphic(rectangle);
                        setText(null);

                    }

                }
        );

    }
}