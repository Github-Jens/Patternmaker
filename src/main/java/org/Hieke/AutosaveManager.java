package org.Hieke;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class AutosaveManager {

    private final File autosaveDirectory;
    private Timeline autosaveTimer;
    private BooleanSupplier modified;

    public AutosaveManager() {

        String appData =
                System.getenv("LOCALAPPDATA");

        autosaveDirectory =
                new File(
                        appData,
                        "PennyAndStitch/autosave"
                );
    }

    private void ensureAutosaveDirectory() {

        if (autosaveDirectory.exists()) {
            return;
        }

        if (!autosaveDirectory.mkdirs()) {
            throw new IllegalStateException(
                    "Could not create autosave directory."
            );
        }
    }
    private File getAutosaveFile(
            PatternDocument document
    ) {

        return new File(
                autosaveDirectory,
                document.getId() + ".autosave"
        );

    }

    private AutosaveData createAutosaveData(
            PatternDocument document
    ) {

        File autosaveFile =
                getAutosaveFile(document);

        File originalFile =
                document.getFile();

        return new AutosaveData(
                autosaveFile,
                originalFile,
                java.time.Instant.now()
        );
    }

    public AutosaveData autosave(
            PatternDocument document
    ) throws IOException {

        if (document == null) {
            return null;
        }

        ensureAutosaveDirectory();

        AutosaveData data =
                createAutosaveData(document);

        FileManager manager =
                new FileManager();

        manager.saveAutosave(
                document,
                data
        );

        return data;
    }
    public void deleteAutosave(
            PatternDocument document
    ) {

        if (document == null) {
            return;
        }

        File file =
                getAutosaveFile(document);

        if (file.exists()) {
            file.delete();
        }

    }

    public void deleteAutosave(
            AutosaveData data
    ) {

        if (data == null) {
            return;
        }

        File file =
                data.getAutosaveFile();

        if (file.exists()) {
            file.delete();
        }

    }

    public boolean hasAutosave(
            PatternDocument document
    ) {

        if (document == null) {
            return false;
        }

        return getAutosaveFile(document).exists();

    }

    public List<AutosaveData> findAutosaves() {

        List<AutosaveData> autosaves =
                new ArrayList<>();

        if (!autosaveDirectory.exists()) {
            return autosaves;
        }

        File[] files =
                autosaveDirectory.listFiles(
                        (dir, name) -> name.endsWith(".autosave")
                );

        if (files == null) {
            return autosaves;
        }

        FileManager manager =
                new FileManager();

        for (File file : files) {

            try {

                AutosaveData data =
                        manager.readAutosaveMetadata(file);

                if (data != null) {
                    autosaves.add(data);
                }

            }
            catch (IOException ignored) {
            }

        }

        return autosaves;

    }

    public void start(
            PatternDocument document,
            BooleanSupplier modified
    ) {

        this.modified = modified;

        stop();

        autosaveTimer =
                new Timeline(
                        new KeyFrame(
                                Duration.seconds(60),
                                event -> {

                                    if (!this.modified.getAsBoolean()) {
                                        return;
                                    }

                                    try {

                                        autosave(document);

                                    }
                                    catch (IOException e) {

                                        e.printStackTrace();

                                    }

                                }
                        )
                );

        autosaveTimer.setCycleCount(
                Timeline.INDEFINITE
        );

        autosaveTimer.play();

    }

    public void stop() {

        if (autosaveTimer != null) {

            autosaveTimer.stop();

            autosaveTimer = null;

        }

    }



}