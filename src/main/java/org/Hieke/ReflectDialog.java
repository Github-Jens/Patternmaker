package org.Hieke;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReflectDialog extends Dialog<ReflectDirection> {

    public ReflectDialog(

    ) {

        setTitle("Reflect Selection");
        setHeaderText("Choose reflection direction");

        initModality(Modality.APPLICATION_MODAL);


        ToggleGroup group =
                new ToggleGroup();


        RadioButton left =
                new RadioButton("Left");

        RadioButton right =
                new RadioButton("Right");

        RadioButton up =
                new RadioButton("Up");

        RadioButton down =
                new RadioButton("Down");


        left.setToggleGroup(group);
        right.setToggleGroup(group);
        up.setToggleGroup(group);
        down.setToggleGroup(group);


        right.setSelected(true);


        VBox content =
                new VBox(
                        8,
                        left,
                        right,
                        up,
                        down
                );

        content.setPadding(
                new Insets(10)
        );

        content.setAlignment(
                Pos.CENTER_LEFT
        );


        getDialogPane().setContent(
                content
        );


        ButtonType cancelButton =
                new ButtonType(
                        "Cancel",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

        ButtonType reflectButton =
                new ButtonType(
                        "Reflect",
                        ButtonBar.ButtonData.OK_DONE
                );


        getDialogPane().getButtonTypes().addAll(
                cancelButton,
                reflectButton
        );


        setResultConverter(button -> {

            if (button != reflectButton) {
                return null;
            }


            if (left.isSelected()) {
                return ReflectDirection.LEFT;
            }

            if (right.isSelected()) {
                return ReflectDirection.RIGHT;
            }

            if (up.isSelected()) {
                return ReflectDirection.UP;
            }

            if (down.isSelected()) {
                return ReflectDirection.DOWN;
            }


            return null;

        });

    }

}