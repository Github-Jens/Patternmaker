package org.Hieke;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private BorderPane root;
    private ObjectProperty<StitchType> selectedType;
    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        KnittingChart chart = new KnittingChart(20,20);

        selectedType =
                new SimpleObjectProperty<>(StitchType.KNIT);

        GridPane grid = createChartGrid(chart, selectedType);

        //TOP Menu Bar
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");

        //Create new chart here
        MenuItem newChart = new MenuItem("New Chart");
        newChart.setOnAction(event -> {
            //Dialog asking for size
            Dialog<ButtonType> dialog = new Dialog<>();

            dialog.setTitle("New Chart");
            dialog.setHeaderText("Create a new knitting chart");


            Label rowsLabel = new Label("Rows:");
            TextField rowsField = new TextField("20");


            Label columnsLabel = new Label("Columns:");
            TextField columnsField = new TextField("20");


            GridPane layout = new GridPane();

            layout.setHgap(10);
            layout.setVgap(10);

            layout.add(rowsLabel, 0, 0);
            layout.add(rowsField, 1, 0);

            layout.add(columnsLabel, 0, 1);
            layout.add(columnsField, 1, 1);


            dialog.getDialogPane().setContent(layout);


            dialog.getDialogPane()
                    .getButtonTypes()
                    .addAll(ButtonType.OK, ButtonType.CANCEL);


            dialog.showAndWait().ifPresent(result -> {
                //check for invalid options
                if (result == ButtonType.OK) {

                    try {

                        int rows = Integer.parseInt(rowsField.getText());
                        int columns = Integer.parseInt(columnsField.getText());


                        if (rows <= 0 || columns <= 0) {

                            throw new NumberFormatException();

                        }


                        KnittingChart newChartData =
                                new KnittingChart(rows, columns);


                        GridPane newGrid =
                                createChartGrid(newChartData, selectedType);


                        root.setCenter(newGrid);


                    } catch (NumberFormatException e) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);

                        alert.setTitle("Invalid Chart Size");
                        alert.setHeaderText("Please enter valid numbers");
                        alert.setContentText(
                                "Rows and columns must be positive numbers."
                        );

                        alert.showAndWait();

                    }
                }

            });

        });

        fileMenu.getItems().add(newChart);

        menuBar.getMenus().add(fileMenu);

        root.setTop(menuBar);

        //Left VBOX

        VBox symbolPanel = new VBox();

        Label symbolTitle = new Label("Symbols");

        Button knitButton = new Button("K - Knit");
        Button purlButton = new Button("P - Purl");
        Button yarnButton = new Button("O - Yarn Over");
        Button k2togButton = new Button("/ - K2tog");

        symbolPanel.getChildren().addAll(
                symbolTitle,
                knitButton,
                purlButton,
                yarnButton,
                k2togButton
        );

        root.setLeft(symbolPanel);

        root.setCenter(grid);
        root.setBottom(new Label("Status: Ready"));

        knitButton.setOnAction(event -> {
            selectedType.set(StitchType.KNIT);
        });

        purlButton.setOnAction(event -> {
            selectedType.set(StitchType.PURL);
        });

        yarnButton.setOnAction(event -> {
            selectedType.set(StitchType.YARN_OVER);
        });

        k2togButton.setOnAction(event -> {
            selectedType.set(StitchType.K2TOG);
        });



        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Knitting Chart Maker");
        stage.setScene(scene);
        stage.show();
    }


    private GridPane createChartGrid(
            KnittingChart chart,
            ObjectProperty<StitchType> selectedType) {

        GridPane grid = new GridPane();

        for (int row = 0; row < chart.getRows(); row++) {

            for (int column = 0; column < chart.getColumns(); column++) {

                Stitch currentStitch = chart.getStitch(row, column);

                Button button = new Button(currentStitch.getSymbol());

                button.setPrefSize(30, 30);

                button.setUserData(currentStitch);

                button.setOnAction(event -> {

                    Stitch stitch = (Stitch) button.getUserData();

                    stitch.setType(selectedType.get());

                    button.setText(stitch.getSymbol());

                });

                grid.add(button, column, row);
            }
        }

        return grid;
    }

    public static void main(String[] args) {
        launch();
    }
}