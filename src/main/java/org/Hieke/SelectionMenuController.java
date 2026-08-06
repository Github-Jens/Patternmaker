package org.Hieke;

import java.util.Optional;

public class SelectionMenuController {

    private final ChartEditor editor;
    private final EditorState editorState;
    private final Palette palette;
    private final SymbolPalette symbolPalette;
    private final Runnable refresh;
    private final Runnable resizeCanvas;

    private int cursorRow = -1;
    private int cursorColumn = -1;

    public SelectionMenuController(
            ChartEditor editor,
            EditorState editorState,
            Palette palette,
            SymbolPalette symbolPalette,
            Runnable refresh,
            Runnable resizeCanvas
    ) {

        this.editor = editor;
        this.editorState = editorState;
        this.palette = palette;
        this.symbolPalette = symbolPalette;
        this.refresh = refresh;
        this.resizeCanvas = resizeCanvas;

    }


    private void finishAction() {

        editorState.getSelection()
                .clear();

        resizeCanvas.run();

        refresh.run();

    }

    public void fillWithSymbol(StitchDefinition definition) {

        editor.fillSelection(
                editorState.getSelection(),
                Tool.DRAW,
                definition,
                null,
                -1
        );

        finishAction();

    }

    public void fillWithColour(int index) {

        editor.fillSelection(
                editorState.getSelection(),
                Tool.DRAW,
                null,
                palette.getColor(index),
                index
        );

        finishAction();

    }

    public void frameWithColour(int index) {

        editor.frameSelection(
                editorState.getSelection(),
                palette.getColor(index)
        );

        finishAction();

    }

    public void copy() {

        editor.copySelection(
                editorState.getSelection()
        );

        finishAction();

    }

    public void cut() {

        editor.copySelection(
                editorState.getSelection()
        );

        editor.clearSelection(
                editorState.getSelection()
        );

        finishAction();

    }

    public void paste() {

        if (cursorRow < 0 ||
                cursorColumn < 0) {

            return;

        }

        editor.paste(
                cursorRow,
                cursorColumn
        );

        finishAction();

    }

    public void clear() {

        editor.clearSelection(
                editorState.getSelection()
        );

        finishAction();

    }

    public void deleteRows() {

        if (!editorState.getSelection().hasSelection()) {
            return;
        }

        ChartSelection selection =
                editorState.getSelection();


        int startRow =
                Math.min(
                        selection.getStartRow(),
                        selection.getEndRow()
                );

        int endRow =
                Math.max(
                        selection.getStartRow(),
                        selection.getEndRow()
                );


        editor.deleteRows(
                startRow,
                endRow
        );


        finishAction();

    }

    public void deleteColumns() {

        if (!editorState.getSelection().hasSelection()) {
            return;
        }

        ChartSelection selection =
                editorState.getSelection();


        int startColumn =
                Math.min(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );

        int endColumn =
                Math.max(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );


        editor.deleteColumns(
                startColumn,
                endColumn
        );


        finishAction();

    }

    public void insert() {

        ChartSelection selection =
                editorState.getSelection();


        if (!selection.hasSelection()) {
            return;
        }


        Optional<InsertDialog> result =
                InsertDialog.show();


        result.ifPresent(request -> {

            int amount =
                    request.getAmount();


            if (request.getType()
                    == InsertDialog.InsertType.ROW) {


                int row =
                        request.getDirection()
                                == InsertDialog.Direction.BEFORE

                                ? Math.min(
                                selection.getStartRow(),
                                selection.getEndRow()
                        )

                                : Math.max(
                                selection.getStartRow(),
                                selection.getEndRow()
                        ) + 1;


                editor.insertRows(
                        row,
                        amount
                );

            }
            else {


                int column =
                        request.getDirection()
                                == InsertDialog.Direction.BEFORE

                                ? Math.min(
                                selection.getStartColumn(),
                                selection.getEndColumn()
                        )

                                : Math.max(
                                selection.getStartColumn(),
                                selection.getEndColumn()
                        ) + 1;


                editor.insertColumns(
                        column,
                        amount
                );

            }


            finishAction();

        });

    }

    public boolean hasClipboardData() {

        return editor.hasClipboardData();

    }

    public void setCursorPosition(
            int row,
            int column
    ) {

        this.cursorRow = row;
        this.cursorColumn = column;

    }

    public void updateCursorPosition(
            int row,
            int column
    ) {

        this.cursorRow = row;
        this.cursorColumn = column;

    }

}