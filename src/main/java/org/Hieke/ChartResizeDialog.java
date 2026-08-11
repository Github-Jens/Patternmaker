package org.Hieke;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class ChartResizeDialog {

    public enum Action {
        INSERT,
        DELETE
    }


    public enum Type {
        ROW,
        COLUMN
    }


    private final Action action;
    private final Type type;
    private final int amount;


    public ChartResizeDialog(
            Action action,
            Type type,
            int amount
    ) {

        this.action = action;
        this.type = type;
        this.amount = amount;

    }


    public Action getAction() {
        return action;
    }


    public Type getType() {
        return type;
    }


    public int getAmount() {
        return amount;
    }



    public static Optional<ChartResizeDialog> show() {

        Dialog<ChartResizeDialog> dialog =
                new Dialog<>();


        dialog.setTitle(
                "Resize Chart"
        );


        ButtonType applyButton =
                new ButtonType(
                        "Apply",
                        ButtonBar.ButtonData.OK_DONE
                );


        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        applyButton,
                        ButtonType.CANCEL
                );


        RadioButton insert =
                new RadioButton(
                        "Insert"
                );


        RadioButton delete =
                new RadioButton(
                        "Delete"
                );


        ToggleGroup actionGroup =
                new ToggleGroup();


        insert.setToggleGroup(actionGroup);
        delete.setToggleGroup(actionGroup);


        insert.setSelected(true);



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

                                amountField.setText(oldValue);

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
                new Label("Action:"),
                0,
                0
        );

        grid.add(
                insert,
                1,
                0
        );

        grid.add(
                delete,
                2,
                0
        );


        grid.add(
                new Label("Type:"),
                0,
                1
        );

        grid.add(
                rows,
                1,
                1
        );

        grid.add(
                columns,
                2,
                1
        );


        grid.add(
                new Label("Amount:"),
                0,
                2
        );

        grid.add(
                amountField,
                1,
                2
        );



        dialog.getDialogPane()
                .setContent(grid);



        dialog.setResultConverter(
                button -> {

                    if (button == applyButton) {


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


                        return new ChartResizeDialog(
                                insert.isSelected()
                                        ? Action.INSERT
                                        : Action.DELETE,

                                rows.isSelected()
                                        ? Type.ROW
                                        : Type.COLUMN,

                                amount
                        );

                    }


                    return null;

                }
        );


        return dialog.showAndWait();

    }

}