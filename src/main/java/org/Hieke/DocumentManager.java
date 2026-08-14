package org.Hieke;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class DocumentManager {

    private final Stage stage;
    private final StitchLibrary stitchLibrary;

    private PatternDocument document;

    private static final int MAX_RECENT_FILES = 10;

    private final List<File> recentFiles;
    private final File recentDirectory;
    private final File recentFile;

    public DocumentManager(
            Stage stage,
            StitchLibrary stitchLibrary
    ) {

        this.stage = stage;
        this.stitchLibrary = stitchLibrary;

        recentFiles =
                new ArrayList<>();

        String appData =
                System.getenv("LOCALAPPDATA");

        recentDirectory =
                new File(
                        appData,
                        "PennyAndStitch"
                );

        recentFile =
                new File(
                        recentDirectory,
                        "recent.txt"
                );

        loadRecentFiles();

    }

    public void createNewDocument(
            KnittingChart chart,
            Palette palette,
            SymbolPalette symbolPalette
    ) {

        document =
                new PatternDocument(
                        chart,
                        palette,
                        symbolPalette
                );

    }

    public PatternDocument getDocument() {

        return document;

    }

    public void setDocument(
            PatternDocument document
    ) {

        this.document = document;

    }
    public void save(File file) throws IOException {

        if (document == null) {
            throw new IllegalStateException(
                    "No document is currently open."
            );
        }

        FileManager manager =
                new FileManager();

        manager.save(
                document,
                file
        );

        document.setFile(file);
        addRecentFile(file);
    }

    public boolean save() {

        if (document == null) {
            return false;
        }

        File file = document.getFile();

        if (file == null) {

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Save Knitting Pattern"
            );

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Knitting Pattern (*.knit)",
                            "*.knit"
                    )
            );

            file =
                    fileChooser.showSaveDialog(
                            stage
                    );

            if (file == null) {
                return false;
            }
        }

        try {

            save(file);

            return true;

        }
        catch (IOException e) {

            Alert alert =
                    new Alert(
                            Alert.AlertType.ERROR
                    );

            alert.setTitle("Save Error");
            alert.setHeaderText(
                    "Could not save pattern"
            );
            alert.setContentText(
                    e.getMessage()
            );

            alert.showAndWait();

            return false;
        }
    }

    public boolean load() {

        FileChooser fileChooser =
                new FileChooser();

        fileChooser.setTitle(
                "Open Knitting Pattern"
        );

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Knitting Pattern (*.knit)",
                        "*.knit"
                )
        );

        File file =
                fileChooser.showOpenDialog(stage);

        if (file == null) {
            return false;
        }

        return load(file);

    }
    public boolean load(
            File file
    ) {

        if (file == null) {
            return false;
        }

        try {

            FileManager manager =
                    new FileManager();

            PatternDocument loadedDocument =
                    manager.load(
                            file,
                            stitchLibrary
                    );

            loadedDocument.setFile(file);

            document =
                    loadedDocument;

            addRecentFile(file);

            return true;

        }
        catch (IOException e) {

            Alert alert =
                    new Alert(
                            Alert.AlertType.ERROR
                    );

            alert.setTitle("Load Error");
            alert.setHeaderText(
                    "Could not load pattern"
            );
            alert.setContentText(
                    e.getMessage()
            );

            alert.showAndWait();

            return false;

        }

    }

    public List<File> getRecentFiles() {

        cleanRecentFiles();

        return List.copyOf(
                recentFiles
        );

    }

    private void addRecentFile(File file) {

        if (file == null) {
            return;
        }

        recentFiles.remove(file);
        recentFiles.add(0, file);

        if (recentFiles.size() > MAX_RECENT_FILES) {
            recentFiles.remove(recentFiles.size() - 1);
        }

        saveRecentFiles();
    }

    private void cleanRecentFiles() {

        recentFiles.removeIf(file -> !file.exists());

    }

    private void loadRecentFiles() {

        recentFiles.clear();

        if (!recentFile.exists()) {
            return;
        }

        try {

            for (String line : Files.readAllLines(recentFile.toPath())) {

                File file = new File(line);

                if (file.exists()) {
                    recentFiles.add(file);
                }

            }

        }
        catch (IOException ignored) {
        }

    }

    private void saveRecentFiles() {

        try {

            if (!recentDirectory.exists()) {
                recentDirectory.mkdirs();
            }

            Files.write(
                    recentFile.toPath(),
                    recentFiles.stream()
                            .map(File::getAbsolutePath)
                            .toList()
            );

        }
        catch (IOException ignored) {
        }

    }




}