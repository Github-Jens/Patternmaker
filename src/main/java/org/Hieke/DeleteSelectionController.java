package org.Hieke;

public class DeleteSelectionController {

    private final ChartEditor editor;
    private final EditorState editorState;
    private final Runnable refresh;
    private final Runnable resizeCanvas;


    public DeleteSelectionController(
            ChartEditor editor,
            EditorState editorState,
            Runnable refresh,
            Runnable resizeCanvas
    ) {

        this.editor = editor;
        this.editorState = editorState;
        this.refresh = refresh;
        this.resizeCanvas = resizeCanvas;

    }


    public void delete(
            boolean colour,
            boolean symbol,
            boolean frame
    ) {

        editor.deleteSelectionParts(
                editorState.getSelection(),
                colour,
                symbol,
                frame
        );


        editorState.getSelection()
                .clear();

        resizeCanvas.run();
        refresh.run();

    }

}