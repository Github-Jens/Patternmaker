package org.Hieke;

import java.io.File;
import java.util.UUID;

public class PatternDocument {

    private final KnittingChart chart;
    private final Palette palette;
    private final SymbolPalette symbolPalette;
    private File file;
    private final UUID id;

    public PatternDocument(
            KnittingChart chart,
            Palette palette,
            SymbolPalette symbolPalette
    ) {

        this.chart = chart;
        this.palette = palette;
        this.symbolPalette = symbolPalette;
        this.id = UUID.randomUUID();

    }

    public KnittingChart getChart() {
        return chart;
    }

    public Palette getPalette() {
        return palette;
    }

    public SymbolPalette getSymbolPalette() {
        return symbolPalette;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean hasFile() {
        return file != null;
    }
    public UUID getId() {

        return id;

    }

}