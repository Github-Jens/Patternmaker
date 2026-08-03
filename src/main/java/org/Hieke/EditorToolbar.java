package org.Hieke;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;

public class EditorToolbar extends ToolBar {

    public EditorToolbar(EditorState editorState) {

        ToggleGroup group = new ToggleGroup();

        ToggleButton drawButton =
                new ToggleButton("Draw");

        ToggleButton selectButton =
                new ToggleButton("Select");

        ToggleButton eraserButton =
                new ToggleButton("Eraser");


        drawButton.setToggleGroup(group);
        selectButton.setToggleGroup(group);
        eraserButton.setToggleGroup(group);


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


        getItems().addAll(
                drawButton,
                selectButton,
                eraserButton
        );
    }
}