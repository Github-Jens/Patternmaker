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

    private Stage stage;

    private ChartCanvas canvas;
    private Palette palette;
    private ScrollPane scrollPane;
    private EditorState editorState;

    private static final int MIN_CHART_SIZE = 1;
    private static final int MAX_CHART_SIZE = 300;


    @Override
    public void start(Stage stage) {
        this.stage = stage;
        root = new BorderPane();
        KnittingChart chart = new KnittingChart(20, 20);

        editorState = new EditorState();

        palette = new Palette();

        // TOP Menu Bar + Toolbar

        MenuBar menuBar =
                new MenuBarBuilder(
                        this::createNewChart,
                        this::saveChart,
                        this::loadChart,
                        () -> canvas.undo(),
                        () -> canvas.redo(),
                        () -> canvas.resetView(),
                        this::exportPDF,
                        this::exportSVG
                ).createMenuBar();


        EditorToolbar toolbar =
                new EditorToolbar(
                        editorState
                );


        VBox topArea =
                new VBox(
                        menuBar,
                        toolbar
                );


        root.setTop(
                topArea
        );

        //left VBox
        ToolPanel toolPanel =
                new ToolPanel(
                        palette,
                        editorState
                );

        root.setLeft(toolPanel);

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
        stage.setMaximized(true);
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {

            if (!confirmDiscardChanges()) {

                event.consume();

            }

        });

        stage.show();
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
                            "Rows and columns must be positive numbers smaller than 300."
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


                PatternData data =
                        manager.load(file);


                palette.replaceColors(
                        data.getPalette().getColors()
                );

                setChart(
                        data.getChart()
                );


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
                    palette,
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

    private void setChart(KnittingChart chart) {


        scrollPane = new ScrollPane();


        canvas = new ChartCanvas(
                chart,
                editorState,
                palette,
                scrollPane
        );


        scrollPane.setContent(canvas);


        canvas.setScrollPane(scrollPane);


        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setPannable(false);


        root.setCenter(scrollPane);

    }


    public static void main(String[] args) {
        launch();
    }
}