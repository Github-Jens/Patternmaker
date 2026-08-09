package org.Hieke;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class DeleteSelectionPopup extends Popup {

    private final Runnable closePopup;

    private final DeleteSelectionController controller;

    public DeleteSelectionPopup(
            DeleteSelectionController controller,
            Runnable closePopup
    ) {

        this.controller = controller;
        this.closePopup = closePopup;

        CheckBox colourCheckBox =
                new CheckBox("Colour");

        CheckBox symbolCheckBox =
                new CheckBox("Symbol");

        CheckBox frameCheckBox =
                new CheckBox("Frame");


        Button apply =
                new Button("Apply");

        Button cancel =
                new Button("Cancel");


        apply.setOnAction(event -> {

            controller.delete(
                    colourCheckBox.isSelected(),
                    symbolCheckBox.isSelected(),
                    frameCheckBox.isSelected()
            );

            hide();
            closePopup.run();

        });


        cancel.setOnAction(event -> {

            hide();
            closePopup.run();

        });


        HBox buttons =
                new HBox(
                        10,
                        apply,
                        cancel
                );


        VBox content =
                new VBox(
                        10,
                        new Label("Delete:"),
                        colourCheckBox,
                        symbolCheckBox,
                        frameCheckBox,
                        buttons
                );

        content.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #999999;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 10;"
        );


        getContent().add(content);

    }

}