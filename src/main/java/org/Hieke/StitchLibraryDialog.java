package org.Hieke;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;


public class StitchLibraryDialog extends Dialog<StitchDefinition> {


    private final StitchLibrary library;


    public StitchLibraryDialog(
            StitchLibrary library
    ) {

        this.library = library;


        setTitle("Stitch Library");
        setHeaderText("Choose a stitch");


        GridPane grid =
                new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);


        for (int i = 0; i < library.getStitches().size(); i++) {


            StitchDefinition definition =
                    library.getStitches()
                            .get(i);


            Button button =
                    new Button(
                            definition.getSymbol()
                    );


            button.setPrefSize(
                    50,
                    50
            );


            button.setOnAction(event -> {

                setResult(
                        definition
                );

                close();

            });


            grid.add(
                    button,
                    i % 5,
                    i / 5
            );

        }


        getDialogPane()
                .setContent(grid);


        getDialogPane()
                .getButtonTypes()
                .add(
                        ButtonType.CANCEL
                );

    }

}