package org.Hieke;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;

public class FloatingSelectionMenu extends Popup {


    private final FloatingSelectionMenuController controller;


    public FloatingSelectionMenu(
            FloatingSelectionMenuController controller
    ) {

        this.controller = controller;


        Button rotateCW =
                new Button(
                        "Rotate Clockwise"
                );


        Button rotateCCW =
                new Button(
                        "Rotate Counter-clockwise"
                );


        Button apply =
                new Button(
                        "Apply"
                );


        Button cancel =
                new Button(
                        "Cancel"
                );


        rotateCW.setOnAction(event -> {

            controller.rotateClockwise();

        });


        rotateCCW.setOnAction(event -> {

            controller.rotateCounterClockwise();

        });


        apply.setOnAction(event -> {

            controller.apply();

            hide();

        });


        cancel.setOnAction(event -> {

            controller.cancel();

            hide();

        });


        javafx.scene.layout.VBox container =
                new javafx.scene.layout.VBox(
                        5,
                        rotateCW,
                        rotateCCW,
                        apply,
                        cancel
                );


        container.setStyle("""
        -fx-background-color: white;
        -fx-border-color: black;
        -fx-border-width: 1;
        -fx-padding: 8;
        """);


        getContent()
                .add(container);


        setAutoHide(false);

    }

    public void setDragging(boolean dragging) {

        getContent().forEach(node ->
                node.setOpacity(
                        dragging ? 0.5 : 1.0
                )
        );

    }

}