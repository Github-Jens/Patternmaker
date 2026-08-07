package org.Hieke;

public class FloatingSelectionMenuController {


    private final EditorState editorState;
    private final ChartEditor editor;
    private final Runnable refresh;


    public FloatingSelectionMenuController(
            EditorState editorState,
            ChartEditor editor,
            Runnable refresh
    ) {

        this.editorState = editorState;
        this.editor = editor;
        this.refresh = refresh;

    }


    public void rotateClockwise() {

        editor.rotateFloating90(
                editorState
        );

        refresh.run();

    }


    public void rotateCounterClockwise() {

        editor.rotateFloatingCounterClockwise90(
                editorState
        );

        refresh.run();

    }


    public void apply() {

        editor.placeFloatingSelection(
                editorState
        );

        refresh.run();

    }


    public void cancel() {

        editor.cancelFloatingSelection(
                editorState
        );

        refresh.run();

    }

}