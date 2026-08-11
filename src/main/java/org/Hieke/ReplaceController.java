package org.Hieke;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class ReplaceController {

    private final ChartEditor editor;
    private final EditorState editorState;
    private final Palette palette;
    private final SymbolPalette symbolPalette;
    private final Runnable refresh;
    private final boolean wholeChart;

    public ReplaceController(
            ChartEditor editor,
            EditorState editorState,
            Palette palette,
            SymbolPalette symbolPalette,
            Runnable refresh,
            boolean wholeChart
    ) {

        this.editor = editor;
        this.editorState = editorState;
        this.palette = palette;
        this.symbolPalette = symbolPalette;
        this.refresh = refresh;
        this.wholeChart = wholeChart;

    }

        //makes a list of all Symbols used in the chart
        public ObservableList<StitchDefinition> getSymbolsUsedInChart() {

            ObservableList<StitchDefinition> result =
                    FXCollections.observableArrayList();

            KnittingChart chart =
                    editor.getChart();

            int startRow = 0;
            int endRow = chart.getRows() - 1;

            int startColumn = 0;
            int endColumn = chart.getColumns() - 1;


            if (!wholeChart) {

                ChartSelection selection =
                        editorState.getSelection();

                if (!selection.hasSelection()) {
                    return result;
                }

                startRow =
                        Math.min(
                                selection.getStartRow(),
                                selection.getEndRow()
                        );

                endRow =
                        Math.max(
                                selection.getStartRow(),
                                selection.getEndRow()
                        );

                startColumn =
                        Math.min(
                                selection.getStartColumn(),
                                selection.getEndColumn()
                        );

                endColumn =
                        Math.max(
                                selection.getStartColumn(),
                                selection.getEndColumn()
                        );

            }


            for (int row = startRow;
                 row <= endRow;
                 row++) {

                for (int column = startColumn;
                     column <= endColumn;
                     column++) {

                    Stitch stitch =
                            chart.getStitch(row, column);

                    StitchDefinition definition =
                            stitch.getDefinition();

                    if (definition != null
                            && !result.contains(definition)) {

                        result.add(definition);

                    }

                }

            }

            return result;

        }

    // Makes a list of all palette colours used in the chart
    public ObservableList<Integer> getColoursUsedInChart() {

        ObservableList<Integer> result =
                FXCollections.observableArrayList();

        KnittingChart chart =
                editor.getChart();

        int startRow = 0;
        int endRow = chart.getRows() - 1;

        int startColumn = 0;
        int endColumn = chart.getColumns() - 1;


        if (!wholeChart) {

            ChartSelection selection =
                    editorState.getSelection();

            if (!selection.hasSelection()) {
                return result;
            }

            startRow =
                    Math.min(
                            selection.getStartRow(),
                            selection.getEndRow()
                    );

            endRow =
                    Math.max(
                            selection.getStartRow(),
                            selection.getEndRow()
                    );

            startColumn =
                    Math.min(
                            selection.getStartColumn(),
                            selection.getEndColumn()
                    );

            endColumn =
                    Math.max(
                            selection.getStartColumn(),
                            selection.getEndColumn()
                    );

        }


        for (int row = startRow;
             row <= endRow;
             row++) {

            for (int column = startColumn;
                 column <= endColumn;
                 column++) {

                Stitch stitch =
                        chart.getStitch(row, column);

                if (stitch.getBackgroundColor() == null) {
                    continue;
                }

                Color colour =
                        stitch.getBackgroundColor();


                for (int index = 0;
                     index < palette.size();
                     index++) {

                    if (colour.equals(
                            palette.getColor(index)
                    )) {

                        if (!result.contains(index)) {

                            result.add(index);

                        }

                        break;

                    }

                }

            }

        }

        return result;

    }
   // also the index numbers for the colours
    public ObservableList<Integer> getPaletteColourIndices() {

        ObservableList<Integer> result =
                FXCollections.observableArrayList();

        for (int index = 0;
             index < palette.size();
             index++) {

            result.add(index);
        }

        return result;
    }

    public Color getPaletteColor(
            int index
    ) {

        return palette.getColor(index);

    }

    public void replaceSymbol(
            StitchDefinition source,
            StitchDefinition target
    ) {

        editor.replaceSymbol(
                getSelection(),
                source,
                target
        );


        editorState.getSelection()
                .clear();

        refresh.run();

    }

    public void replaceSymbolWithColour(
            StitchDefinition source,
            Color targetColour
    ) {

        editor.replaceSymbolWithColour(
                getSelection(),
                source,
                targetColour
        );


        editorState.getSelection()
                .clear();

        refresh.run();

    }
    public void replaceColourWithSymbol(
            Color sourceColour,
            StitchDefinition target
    ) {

        editor.replaceColourWithSymbol(
                getSelection(),
                sourceColour,
                target
        );


        editorState.getSelection()
                .clear();

        refresh.run();

    }

    public void replaceColour(
            Color sourceColour,
            Color targetColour
    ) {

        editor.replaceColour(
                getSelection(),
                sourceColour,
                targetColour
        );


        editorState.getSelection()
                .clear();

        refresh.run();

    }
    private ChartSelection getSelection() {

        if (!wholeChart) {

            return editorState.getSelection();

        }


        ChartSelection selection =
                new ChartSelection();

        KnittingChart chart =
                editor.getChart();

        selection.setStart(
                0,
                0
        );

        selection.setEnd(
                chart.getRows() - 1,
                chart.getColumns() - 1
        );

        return selection;

    }

}