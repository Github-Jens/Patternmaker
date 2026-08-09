package org.Hieke;

import javafx.scene.control.*;

public class EditorToolbar extends ToolBar {

    public EditorToolbar(
            EditorState editorState,
            Runnable newFile,
            Runnable undo,
            Runnable redo,
            Runnable resetView,
            Runnable addColumn,
            Runnable addRow
    )  {

        ToggleGroup group = new ToggleGroup();

        ToggleButton drawButton =
                new ToggleButton("Draw");

        ToggleButton selectButton =
                new ToggleButton("Select");

        ToggleButton eraserButton =
                new ToggleButton("Eraser");

        Button newFileButton =
                new Button("New");

        Button undoButton =
                new Button("Undo");

        Button redoButton =
                new Button("Redo");

        Button resetViewButton =
                new Button("Reset View");

        Button addColumnButton =
                new Button("Add Column");

        Button addRowButton =
                new Button("Add Row");


        drawButton.setToggleGroup(group);
        selectButton.setToggleGroup(group);
        eraserButton.setToggleGroup(group);

        drawButton.setToggleGroup(group);
        selectButton.setToggleGroup(group);
        eraserButton.setToggleGroup(group);

        editorState.activeToolProperty().addListener(
                (observable, oldTool, newTool) -> {

                    drawButton.setSelected(
                            newTool == Tool.DRAW
                    );

                    selectButton.setSelected(
                            newTool == Tool.SELECT
                    );

                    eraserButton.setSelected(
                            newTool == Tool.ERASE
                    );

                }
        );

        drawButton.setFocusTraversable(false);
        selectButton.setFocusTraversable(false);
        eraserButton.setFocusTraversable(false);
        newFileButton.setFocusTraversable(false);
        undoButton.setFocusTraversable(false);
        redoButton.setFocusTraversable(false);
        resetViewButton.setFocusTraversable(false);
        addColumnButton.setFocusTraversable(false);
        addRowButton.setFocusTraversable(false);

        // Default tool
        drawButton.setSelected(true);


        drawButton.setOnAction(event ->
                editorState.activeToolProperty()
                        .set(Tool.DRAW)
        );

        selectButton.setOnAction(event ->
                editorState.activeToolProperty()
                        .set(Tool.SELECT)
        );

        eraserButton.setOnAction(event ->
                editorState.activeToolProperty()
                        .set(Tool.ERASE)
        );

        newFileButton.setOnAction(event ->
                newFile.run()
        );

        undoButton.setOnAction(event ->
                undo.run()
        );

        redoButton.setOnAction(event ->
                redo.run()
        );

        resetViewButton.setOnAction(event ->
                resetView.run()
        );

        addColumnButton.setOnAction(event ->
                addColumn.run()
        );

        addRowButton.setOnAction(event ->
                addRow.run()
        );


        getItems().addAll(
                newFileButton,

                new Separator(),

                drawButton,
                selectButton,
                eraserButton,

                new Separator(),

                undoButton,
                redoButton,
                resetViewButton,
                addColumnButton,
                addRowButton


        );
    }
}