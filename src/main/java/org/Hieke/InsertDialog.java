package org.Hieke;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class InsertDialog {

    public enum InsertType {
        ROW,
        COLUMN
    }


    public enum Direction {
        BEFORE,
        AFTER
    }


    private final InsertType type;
    private final Direction direction;
    private final int amount;


    public InsertDialog(
            InsertType type,
            Direction direction,
            int amount
    ) {

        this.type = type;
        this.direction = direction;
        this.amount = amount;

    }


    public InsertType getType() {
        return type;
    }


    public Direction getDirection() {
        return direction;
    }


    public int getAmount() {
        return amount;
    }



    public static Optional<InsertDialog> show() {

        Dialog<InsertDialog> dialog =
                new Dialog<>();


        dialog.setTitle(
                "Insert"
        );


        ButtonType insertButton =
                new ButtonType(
                        "Insert",
                        ButtonBar.ButtonData.OK_DONE
                );


        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        insertButton,
                        ButtonType.CANCEL
                );



        RadioButton rows =
                new RadioButton(
                        "Rows"
                );


        RadioButton columns =
                new RadioButton(
                        "Columns"
                );


        ToggleGroup typeGroup =
                new ToggleGroup();


        rows.setToggleGroup(typeGroup);
        columns.setToggleGroup(typeGroup);


        rows.setSelected(true);



        TextField amountField =
                new TextField(
                        "1"
                );


        amountField.textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {

                            if (!newValue.matches("\\d*")) {

                                amountField.setText(
                                        oldValue
                                );

                            }

                        }
                );



        RadioButton before =
                new RadioButton(
                        "Above / Left"
                );


        RadioButton after =
                new RadioButton(
                        "Below / Right"
                );


        ToggleGroup directionGroup =
                new ToggleGroup();


        before.setToggleGroup(directionGroup);
        after.setToggleGroup(directionGroup);


        before.setSelected(true);



        columns.selectedProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {

                            if (newValue) {

                                before.setText(
                                        "Left"
                                );

                                after.setText(
                                        "Right"
                                );

                            }

                        }
                );


        rows.selectedProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {

                            if (newValue) {

                                before.setText(
                                        "Above"
                                );

                                after.setText(
                                        "Below"
                                );

                            }

                        }
                );



        GridPane grid =
                new GridPane();


        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(
                new Insets(10)
        );


        grid.add(
                new Label("Insert:"),
                0,
                0
        );

        grid.add(
                rows,
                1,
                0
        );

        grid.add(
                columns,
                2,
                0
        );


        grid.add(
                new Label("Amount:"),
                0,
                1
        );

        grid.add(
                amountField,
                1,
                1
        );


        grid.add(
                new Label("Direction:"),
                0,
                2
        );


        grid.add(
                before,
                1,
                2
        );


        grid.add(
                after,
                2,
                2
        );



        dialog.getDialogPane()
                .setContent(
                        grid
                );



        dialog.setResultConverter(
                button -> {

                    if (button == insertButton) {


                        int amount;


                        try {

                            amount =
                                    Integer.parseInt(
                                            amountField.getText()
                                    );

                        }
                        catch (NumberFormatException e) {

                            return null;

                        }



                        return new InsertDialog(
                                rows.isSelected()
                                        ? InsertType.ROW
                                        : InsertType.COLUMN,

                                before.isSelected()
                                        ? Direction.BEFORE
                                        : Direction.AFTER,

                                amount
                        );

                    }


                    return null;

                }
        );


        return dialog.showAndWait();

    }

}