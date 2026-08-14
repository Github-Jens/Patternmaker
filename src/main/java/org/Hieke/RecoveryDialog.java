package org.Hieke;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import java.io.File;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class RecoveryDialog {

    public static Optional<AutosaveData> show(
            List<AutosaveData> autosaves
    ) {

        Dialog<AutosaveData> dialog = new Dialog<>();

        dialog.setTitle("Recover Autosave");
        dialog.setHeaderText(
                "Unsaved work was found."
        );

        TableView<AutosaveData> table =
                new TableView<>();

        table.setItems(
                FXCollections.observableArrayList(autosaves)
        );

        TableColumn<AutosaveData, String> nameColumn =
                new TableColumn<>("Pattern");

        nameColumn.setCellValueFactory(cell -> {

            File original =
                    cell.getValue().getOriginalFile();

            String name =
                    original == null
                            ? "Unsaved Pattern"
                            : original.getName();

            return new javafx.beans.property.SimpleStringProperty(name);

        });

        TableColumn<AutosaveData, String> statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(cell -> {

            File original =
                    cell.getValue().getOriginalFile();

            String status;

            if (original == null) {

                status = "Never saved";

            }
            else if (original.exists()) {

                status = "Existing file";

            }
            else {

                status = "Original missing";

            }

            return new javafx.beans.property.SimpleStringProperty(status);

        });

        TableColumn<AutosaveData, String> timeColumn =
                new TableColumn<>("Autosaved");

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                        .withZone(ZoneId.systemDefault());

        timeColumn.setCellValueFactory(cell ->

                new javafx.beans.property.SimpleStringProperty(
                        formatter.format(
                                cell.getValue().getTimestamp()
                        )
                )

        );

        table.getColumns().addAll(
                nameColumn,
                statusColumn,
                timeColumn
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        dialog.getDialogPane().setContent(table);

        ButtonType recover =
                new ButtonType(
                        "Recover",
                        ButtonBar.ButtonData.OK_DONE
                );

        ButtonType discard =
                new ButtonType(
                        "Discard",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

        dialog.getDialogPane().getButtonTypes().setAll(
                recover,
                discard
        );

        dialog.setResultConverter(button -> {

            if (button == recover) {

                return table.getSelectionModel()
                        .getSelectedItem();

            }

            return null;

        });

        if (!autosaves.isEmpty()) {

            table.getSelectionModel().selectFirst();

        }

        return dialog.showAndWait();

    }

}