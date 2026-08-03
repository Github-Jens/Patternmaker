package org.Hieke;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class StitchLibraryDialog extends Dialog<StitchDefinition> {


    private final StitchLibrary library;


    private final GridPane grid =
            new GridPane();


    private final TextField searchField =
            new TextField();


    private final ComboBox<String> categoryBox =
            new ComboBox<>();


    public StitchLibraryDialog(
            StitchLibrary library
    ) {

        this.library = library;


        setTitle("Stitch Library");
        setHeaderText("Choose a stitch");


        grid.setHgap(10);
        grid.setVgap(10);


        searchField.setPromptText(
                "Search stitch..."
        );


        categoryBox.getItems()
                .add("All");


        library.getStitches()
                .stream()
                .map(StitchDefinition::getCategory)
                .distinct()
                .sorted()
                .forEach(
                        categoryBox.getItems()::add
                );


        categoryBox.getSelectionModel()
                .selectFirst();



        searchField.textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> refresh()
                );


        categoryBox.valueProperty()
                .addListener(
                        (observable, oldValue, newValue) -> refresh()
                );



        VBox content =
                new VBox(
                        10,
                        searchField,
                        categoryBox,
                        grid
                );


        getDialogPane()
                .setContent(content);



        getDialogPane()
                .getButtonTypes()
                .add(
                        ButtonType.CANCEL
                );


        setResultConverter(buttonType -> {

            if (buttonType == ButtonType.CANCEL) {

                return null;

            }

            return null;

        });


        refresh();

    }



    private void refresh() {


        grid.getChildren()
                .clear();


        String search =
                searchField.getText()
                        .toLowerCase();


        String selectedCategory =
                categoryBox.getValue();



        int index = 0;



        for (StitchDefinition definition :
                library.getStitches()) {


            boolean matchesSearch =
                    search.isEmpty()
                            ||
                            definition.getName()
                                    .toLowerCase()
                                    .contains(search);



            boolean matchesCategory =
                    selectedCategory.equals("All")
                            ||
                            definition.getCategory()
                                    .equals(selectedCategory);



            if (!matchesSearch ||
                    !matchesCategory) {

                continue;

            }



            Button button =
                    createButton(definition);



            grid.add(
                    button,
                    index % 5,
                    index / 5
            );


            index++;

        }

    }



    private Button createButton(
            StitchDefinition definition
    ) {


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



        return button;

    }

}