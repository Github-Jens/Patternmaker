package org.Hieke;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    private BorderPane root;
    private ObjectProperty<StitchType> selectedType;
    private ObjectProperty<Color> selectedColor;
    private IntegerProperty selectedColorIndex;
    private Stage stage;

    private ChartCanvas canvas;
    private Palette palette;

    private static final int MIN_CHART_SIZE = 1;
    private static final int MAX_CHART_SIZE = 500;


    @Override
    public void start(Stage stage) {
        this.stage = stage;
        root = new BorderPane();
        KnittingChart chart = new KnittingChart(20,20);

        selectedType =
                new SimpleObjectProperty<>(null);

        palette = new Palette();

        selectedColor =
                new SimpleObjectProperty<>(null);

        selectedColorIndex =
                new SimpleIntegerProperty(-1);

        //TOP Menu Bar
        MenuBar menuBar =
                new MenuBarBuilder(
                        this::createNewChart,
                        () -> saveChart(),
                        this::loadChart,
                        () -> canvas.undo(),
                        () -> canvas.redo(),
                        () -> canvas.resetView(),
                        this::exportPDF,
                        this::exportSVG
                ).createMenuBar();

        root.setTop(menuBar);

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


        symbolPanel.getChildren().addAll(
                symbolTitle,
                knitButton,
                purlButton,
                yarnButton,
                k2togButton,
                eraserButton
        );


// Palette

        PaletteView paletteView =
                new PaletteView(
                        palette,
                        selectedColor,
                        selectedColorIndex
                );


// Combine left side

        VBox leftPanel = new VBox();
        leftPanel.setSpacing(15);

        leftPanel.getChildren().addAll(
                symbolPanel,
                paletteView
        );

        root.setLeft(leftPanel);


// Default tool

        selectTool(
                null,
                knitButton,
                purlButton,
                yarnButton,
                k2togButton,
                eraserButton
        );


// Tool actions

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


// Create canvas

        setChart(chart);

        root.setBottom(new Label("Status: Ready"));


 //Create the scene
        Scene scene = new Scene(root, 800, 600);

        scene.setOnKeyPressed(event -> {

            switch (event.getCode()) {

                case Z:
                    if (event.isControlDown()) {
                        canvas.undo();
                    }
                    break;

                case Y:
                    if (event.isControlDown()) {
                        canvas.redo();
                    }
                    break;
            }

        });

        stage.setTitle("Knitting Chart Maker");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {

            if (!confirmDiscardChanges()) {

                event.consume();

            }

        });

        stage.show();
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

    private void exportPDF() {

        FileChooser chooser =
                new FileChooser();


        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "PDF (*.pdf)",
                        "*.pdf"
                )
        );


        File file =
                chooser.showSaveDialog(this.stage);


        if(file != null) {

            try {

                new PDFExporter(
                        canvas.getChart()
                ).export(
                        file.getAbsolutePath()
                );

            }
            catch(IOException e) {

                e.printStackTrace();

                new Alert(
                        Alert.AlertType.ERROR,
                        "PDF export failed"
                ).showAndWait();

            }
        }
    }
    private void exportSVG() {

        FileChooser chooser =
                new FileChooser();


        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "SVG Image (*.svg)",
                        "*.svg"
                )
        );


        File file =
                chooser.showSaveDialog(this.stage);


        if(file != null) {

            try {

                new SVGExporter(
                        canvas.getChart()
                ).export(
                        file.getAbsolutePath()
                );

            }
            catch(IOException e) {

                e.printStackTrace();

                new Alert(
                        Alert.AlertType.ERROR,
                        "SVG export failed"
                ).showAndWait();

            }
        }
    }
    private void createNewChart() {

        if (!confirmDiscardChanges()) {
            return;
        }


        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("New Chart");
        dialog.setHeaderText("Create a new knitting chart");


        Label rowsLabel = new Label("Rows:");
        TextField rowsField = new TextField("20");


        Label columnsLabel = new Label("Columns:");
        TextField columnsField = new TextField("20");


        GridPane layout = new GridPane();

        layout.setHgap(10);
        layout.setVgap(10);

        layout.add(rowsLabel, 0, 0);
        layout.add(rowsField, 1, 0);

        layout.add(columnsLabel, 0, 1);
        layout.add(columnsField, 1, 1);


        dialog.getDialogPane().setContent(layout);


        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        ButtonType.OK,
                        ButtonType.CANCEL
                );


        dialog.showAndWait().ifPresent(result -> {

            if (result == ButtonType.OK) {

                try {

                    int rows =
                            Integer.parseInt(
                                    rowsField.getText()
                            );

                    int columns =
                            Integer.parseInt(
                                    columnsField.getText()
                            );


                    if (rows < MIN_CHART_SIZE ||
                            columns < MIN_CHART_SIZE ||
                            rows > MAX_CHART_SIZE ||
                            columns > MAX_CHART_SIZE) {

                        throw new NumberFormatException();

                    }


                    KnittingChart newChartData =
                            new KnittingChart(
                                    rows,
                                    columns
                            );


                    setChart(newChartData);


                } catch (NumberFormatException e) {

                    Alert alert =
                            new Alert(
                                    Alert.AlertType.ERROR
                            );

                    alert.setTitle("Invalid Chart Size");
                    alert.setHeaderText(
                            "Please enter valid numbers"
                    );

                    alert.setContentText(
                            "Rows and columns must be positive numbers smaller than 500."
                    );

                    alert.showAndWait();

                }
            }

        });
    }

    private void loadChart() {

        if (!confirmDiscardChanges()) {
            return;
        }


        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Knitting Pattern");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Knitting Pattern (*.knit)",
                        "*.knit"
                )
        );


        File file =
                fileChooser.showOpenDialog(stage);


        if (file != null) {

            try {

                FileManager manager =
                        new FileManager();


                KnittingChart loadedChart =
                        manager.load(file);


                setChart(loadedChart);


            } catch (IOException e) {

                Alert alert =
                        new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Load Error");
                alert.setHeaderText("Could not load pattern");
                alert.setContentText(e.getMessage());

                alert.showAndWait();

            }
        }
    }
    private boolean saveChart() {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Save Knitting Pattern");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Knitting Pattern (*.knit)",
                        "*.knit"
                )
        );


        File file = fileChooser.showSaveDialog(this.stage);


        if (file == null) {
            return false;
        }


        try {

            FileManager manager = new FileManager();

            manager.save(
                    canvas.getChart(),
                    file
            );

            canvas.markSaved();

            return true;

        }
        catch (IOException e) {

            Alert alert = new Alert(
                    Alert.AlertType.ERROR
            );

            alert.setTitle("Save Error");
            alert.setHeaderText("Could not save pattern");
            alert.setContentText(e.getMessage());

            alert.showAndWait();

            return false;
        }
    }
    private boolean confirmDiscardChanges() {

        if (!canvas.isModified()) {
            return true;
        }


        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        alert.setTitle("Unsaved Changes");
        alert.setHeaderText(
                "Your knitting chart has unsaved changes."
        );
        alert.setContentText(
                "Do you want to save before continuing?"
        );


        ButtonType save =
                new ButtonType("Save");

        ButtonType discard =
                new ButtonType("Discard");

        ButtonType cancel =
                new ButtonType(
                        "Cancel",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );


        alert.getButtonTypes().setAll(
                save,
                discard,
                cancel
        );


        return alert.showAndWait()
                .map(result -> {

                    if (result == save) {

                        return saveChart();

                    }

                    if (result == discard) {

                        return true;

                    }

                    return false;

                })
                .orElse(false);
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


    private void setChart(KnittingChart chart) {

        canvas = new ChartCanvas(
                chart,
                selectedType,
                selectedColor,
                selectedColorIndex
        );

        root.setCenter(canvas);

    }


    public static void main(String[] args) {
        launch();
    }
}