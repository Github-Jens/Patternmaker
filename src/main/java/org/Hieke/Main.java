package org.Hieke;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    private BorderPane root;
    private ObjectProperty<StitchType> selectedType;

    private ChartCanvas canvas;

    private static final int MIN_CHART_SIZE = 1;
    private static final int MAX_CHART_SIZE = 50;

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        KnittingChart chart = new KnittingChart(20,20);

        selectedType =
                new SimpleObjectProperty<>(StitchType.KNIT);

        //TOP Menu Bar
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        Menu viewMenu = new Menu("View");
        Menu exportMenu = new Menu("Export");
        MenuItem resetView = new MenuItem("Reset View");
        MenuItem exportSVG =
                new MenuItem("Export SVG");

        resetView.setOnAction(event -> {

            canvas.resetView();

        });

        exportSVG.setOnAction(event ->
                exportSVG(stage)
        );
        exportMenu.getItems().add(
                exportSVG
        );
        viewMenu.getItems().add(
                resetView
        );

                //Create new chart here
        MenuItem savePattern = new MenuItem("Save Pattern");
        MenuItem newChart = new MenuItem("New Chart");
        newChart.setOnAction(event -> {
            //Dialog asking for size
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
                    .addAll(ButtonType.OK, ButtonType.CANCEL);


            dialog.showAndWait().ifPresent(result -> {
                //check for invalid options
                if (result == ButtonType.OK) {

                    try {

                        int rows = Integer.parseInt(rowsField.getText());
                        int columns = Integer.parseInt(columnsField.getText());


                        if (rows < MIN_CHART_SIZE ||
                                columns < MIN_CHART_SIZE ||
                                rows > MAX_CHART_SIZE ||
                                columns > MAX_CHART_SIZE) {

                            throw new NumberFormatException();

                        }


                        KnittingChart newChartData =
                                new KnittingChart(rows, columns);


                        canvas = new ChartCanvas(newChartData, selectedType);

                        root.setCenter(canvas);


                    } catch (NumberFormatException e) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);

                        alert.setTitle("Invalid Chart Size");
                        alert.setHeaderText("Please enter valid numbers");
                        alert.setContentText(
                                "Rows and columns must be positive numbers smaller than 50."
                        );

                        alert.showAndWait();

                    }
                }

            });

        });

        savePattern.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Save Knitting Pattern");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Knitting Pattern (*.knit)",
                            "*.knit"
                    )
            );


            File file = fileChooser.showSaveDialog(stage);


            if (file != null) {

                try {

                    FileManager manager =
                            new FileManager();


                    manager.save(
                            canvas.getChart(),
                            file
                    );


                } catch (IOException e) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Save Error");
                    alert.setHeaderText("Could not save pattern");
                    alert.setContentText(e.getMessage());

                    alert.showAndWait();

                }

            }

        });

        MenuItem loadPattern = new MenuItem("Load Pattern");
        loadPattern.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Open Knitting Pattern");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Knitting Pattern (*.knit)",
                            "*.knit"
                    )
            );


            File file = fileChooser.showOpenDialog(stage);


            if (file != null) {

                try {

                    FileManager manager =
                            new FileManager();


                    KnittingChart loadedChart =
                            manager.load(file);


                    canvas =
                            new ChartCanvas(
                                    loadedChart,
                                    selectedType
                            );


                    root.setCenter(canvas);


                } catch (IOException e) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Load Error");
                    alert.setHeaderText("Could not load pattern");
                    alert.setContentText(e.getMessage());

                    alert.showAndWait();

                }

            }

        });



        fileMenu.getItems().addAll(
                newChart,
                savePattern,
                loadPattern
        );


// Edit Menu
        Menu editMenu = new Menu("Edit");

        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");


        undoItem.setOnAction(event -> {
            canvas.undo();
        });


        redoItem.setOnAction(event -> {
            canvas.redo();
        });


        editMenu.getItems().addAll(
                undoItem,
                redoItem
        );


        menuBar.getMenus().addAll(
                fileMenu,
                editMenu,
                viewMenu,
                exportMenu
        );


        root.setTop(menuBar);

        //Left VBOX

        VBox symbolPanel = new VBox();

        Label symbolTitle = new Label("Symbols");
        //Create Buttons for Symbols here
        Button eraserButton = new Button("Eraser");
        Button knitButton = new Button("K - Knit");
        Button purlButton = new Button("P - Purl");
        Button yarnButton = new Button("O - Yarn Over");
        Button k2togButton = new Button("/ - K2tog");

        symbolPanel.getChildren().addAll(
                symbolTitle,
                knitButton,
                purlButton,
                yarnButton,
                k2togButton,
                eraserButton
        );

        root.setLeft(symbolPanel);

        selectTool(
                knitButton,
                knitButton,
                purlButton,
                yarnButton,
                k2togButton,
                eraserButton
        );

        //create the chart with this method
        canvas = new ChartCanvas(chart, selectedType);

        root.setCenter(canvas);

        root.setBottom(new Label("Status: Ready"));
        //Make the buttons do things
        knitButton.setOnAction(event -> {

            selectedType.set(StitchType.KNIT);

            selectTool(
                    knitButton,
                    knitButton,
                    purlButton,
                    yarnButton,
                    k2togButton,
                    eraserButton
            );

        });

        purlButton.setOnAction(event -> {

            selectedType.set(StitchType.PURL);

            selectTool(
                    purlButton,
                    knitButton,
                    purlButton,
                    yarnButton,
                    k2togButton,
                    eraserButton
            );

        });

        yarnButton.setOnAction(event -> {

            selectedType.set(StitchType.YARN_OVER);

            selectTool(
                    yarnButton,
                    knitButton,
                    purlButton,
                    yarnButton,
                    k2togButton,
                    eraserButton
            );

        });

        k2togButton.setOnAction(event -> {

            selectedType.set(StitchType.K2TOG);

            selectTool(
                    k2togButton,
                    knitButton,
                    purlButton,
                    yarnButton,
                    k2togButton,
                    eraserButton
            );

        });
        eraserButton.setOnAction(event -> {

            selectedType.set(StitchType.EMPTY);

            selectTool(
                    eraserButton,
                    knitButton,
                    purlButton,
                    yarnButton,
                    k2togButton,
                    eraserButton
            );

        });


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
        stage.show();
    }

    private void selectTool(Button selectedButton, Button... buttons) {

        for (Button button : buttons) {

            button.setStyle("");

        }
        //change button style here
        selectedButton.setStyle(
                "-fx-background-color: lightblue;"
        );
    }
    private void exportSVG(Stage stage) {

        FileChooser chooser =
                new FileChooser();


        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "SVG Image (*.svg)",
                        "*.svg"
                )
        );


        File file =
                chooser.showSaveDialog(stage);


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


    public static void main(String[] args) {
        launch();
    }
}