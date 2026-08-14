package org.Hieke;

import java.io.File;
import java.time.Instant;

public class AutosaveData {

    private final File autosaveFile;
    private final File originalFile;
    private final Instant timestamp;

    public AutosaveData(
            File autosaveFile,
            File originalFile,
            Instant timestamp
    ) {

        this.autosaveFile = autosaveFile;
        this.originalFile = originalFile;
        this.timestamp = timestamp;
    }

    public File getAutosaveFile() {
        return autosaveFile;
    }

    public File getOriginalFile() {
        return originalFile;
    }

    public String getOriginalFilePath() {

        if (originalFile == null) {
            return null;
        }

        return originalFile.getAbsolutePath();
    }

    public Instant getTimestamp() {
        return timestamp;
    }

}