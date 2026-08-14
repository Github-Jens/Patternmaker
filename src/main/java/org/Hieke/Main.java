package org.Hieke;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Main extends Application {

    private BorderPane root;

    private Stage stage;

    private ChartCanvas canvas;
    private Palette palette;
    private ScrollPane scrollPane;
    private EditorState editorState;
    private StitchLibrary stitchLibrary;
    private DocumentManager documentManager;

    private ChartModificationController modificationController;
    private ReplaceController replaceController;
    private AutosaveManager autosaveManager;

    private static final int MIN_CHART_SIZE = 1;
    private static final int MAX_CHART_SIZE = 300;


    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.getIcons().add(
                new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("/icons/P&S.JPG"))
                )
        );

        root = new BorderPane();
        KnittingChart chart =
                new KnittingChart(
                        20,
                        20
                );


        stitchLibrary =
                new StitchLibrary();

        documentManager =
                new DocumentManager(
                        stage,
                        stitchLibrary
                );
        autosaveManager =
                new AutosaveManager();


        editorState =
                new EditorState(
                        stitchLibrary
                );


        palette =
                new Palette();

        documentManager.createNewDocument(
                chart,
                palette,
                editorState.getSymbolPalette()
        );

// Create canvas

        setChart(chart);

        startAutosave();

        // TOP Menu Bar + Toolbar

        MenuBar menuBar =
                new MenuBarBuilder(
                        this::createNewChart,
                        this::saveChart,
                        this::saveChartAs,
                        this::loadChart,
                        () -> canvas.undo(),
                        () -> canvas.redo(),
                        () -> canvas.resetView(),
                        this::exportPDF,
                        this::exportSVG,
                        this::modifyChart,
                        this::replaceChart,
                        documentManager::getRecentFiles,
                        this::openRecentChart
                ).createMenuBar();


        EditorToolbar toolbar =
                new EditorToolbar(
                        editorState,
                        this::createNewChart,
                        this::saveChart,
                        this::undo,
                        this::redo,
                        this::resetView,
                        this::addColumn,
                        this::addRow
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
                        editorState,
                        stitchLibrary
                );

        root.setLeft(toolPanel);



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

        stage.setTitle("Penny and Stitch Chartmaker");
        stage.setMaximized(true);
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {

            if (!confirmDiscardChanges()) {

                event.consume();

            }

        });

        stage.show();
        checkForAutosaves();
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

                    documentManager.createNewDocument(
                            newChartData,
                            palette,
                            editorState.getSymbolPalette()
                    );

                    setChart(newChartData);

                    startAutosave();


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

        if (!documentManager.load()) {
            return;
        }

        PatternDocument loadedDocument =
                documentManager.getDocument();

        palette.replaceColors(
                loadedDocument.getPalette().getColors()
        );

        editorState.getSymbolPalette()
                .getSymbols()
                .setAll(
                        loadedDocument.getSymbolPalette()
                                .getSymbols()
                );

        setChart(
                loadedDocument.getChart()
        );
        startAutosave();
    }

    private boolean saveChart() {

        boolean saved =
                documentManager.save();

        if (saved) {

            autosaveManager.deleteAutosave(
                    documentManager.getDocument()
            );

            canvas.markSaved();

        }

        return saved;
    }

    private void modifyChart() {

        modificationController.modifyChart();

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

        editorState.getSelection().clear();
        editorState.activeToolProperty().set(Tool.DRAW);

        editorState.selectedColorProperty().set(null);
        editorState.selectedColorIndexProperty().set(-1);

        editorState.selectedStitchProperty().set(null);


        scrollPane = new ScrollPane();


        canvas = new ChartCanvas(
                chart,
                editorState,
                palette,
                scrollPane
        );

        modificationController =
                new ChartModificationController(
                        canvas.getEditor(),
                        canvas::refresh
                );

        replaceController =
                new ReplaceController(
                        canvas.getEditor(),
                        editorState,
                        palette,
                        editorState.getSymbolPalette(),
                        canvas::refresh,
                        true

                );


        scrollPane.setContent(canvas);


        canvas.setScrollPane(scrollPane);


        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setPannable(false);


        root.setCenter(scrollPane);

    }

    private void replaceChart() {

        ReplacePopup popup =
                new ReplacePopup(
                        replaceController,
                        editorState.getSymbolPalette(),
                        () -> {
                        }
                );

        popup.show(
                stage,
                stage.getX() + 100,
                stage.getY() + 100
        );

    }

    private void startAutosave() {

        autosaveManager.start(
                documentManager.getDocument(),
                canvas::isModified
        );

    }
    private void checkForAutosaves() {

        List<AutosaveData> autosaves =
                autosaveManager.findAutosaves();

        if (autosaves.isEmpty()) {
            return;
        }

        Optional<AutosaveData> recovered =
                RecoveryDialog.show(autosaves);

        if (recovered.isPresent()) {

            recoverAutosave(
                    recovered.get()
            );

        }
        else {

            for (AutosaveData data : autosaves) {

                autosaveManager.deleteAutosave(
                        data
                );

            }

        }

    }

    private void recoverAutosave(
            AutosaveData data
    ) {

        try {

            FileManager manager =
                    new FileManager();

            PatternDocument recovered =
                    manager.loadAutosave(
                            data.getAutosaveFile(),
                            stitchLibrary
                    );

            documentManager.setDocument(recovered);

            palette.replaceColors(
                    recovered.getPalette().getColors()
            );

            editorState.getSymbolPalette()
                    .getSymbols()
                    .setAll(
                            recovered.getSymbolPalette()
                                    .getSymbols()
                    );

            setChart(
                    recovered.getChart()
            );

            startAutosave();

        }
        catch (IOException e) {

            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Recovery Failed");
            alert.setHeaderText(
                    "Could not recover autosave"
            );
            alert.setContentText(
                    e.getMessage()
            );

            alert.showAndWait();

        }

    }

    private void openRecentChart(File file) {

        if (!confirmDiscardChanges()) {
            return;
        }

        if (!documentManager.load(file)) {
            return;
        }

        PatternDocument document =
                documentManager.getDocument();

        palette.replaceColors(
                document.getPalette().getColors()
        );

        editorState.getSymbolPalette()
                .getSymbols()
                .setAll(
                        document.getSymbolPalette()
                                .getSymbols()
                );

        setChart(
                document.getChart()
        );

    }

    private void saveChartAs() {

        if (documentManager.saveAs()) {

            canvas.markSaved();

        }

    }

    private void undo() {
        canvas.undo();
    }

    private void redo() {
        canvas.redo();
    }

    private void resetView() {
        canvas.resetView();
    }

    private void addColumn() {
        modificationController.addColumn();
    }

    private void addRow() {
        modificationController.addRow();
    }


    public static void main(String[] args) {
        launch();
    }
}