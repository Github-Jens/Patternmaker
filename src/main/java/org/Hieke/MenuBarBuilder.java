package org.Hieke;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MenuBarBuilder {

    private final Runnable newChartAction;
    private final Runnable saveAction;
    private final Runnable saveAsAction;
    private final Runnable loadAction;

    private final Runnable undoAction;
    private final Runnable redoAction;

    private final Runnable resetViewAction;

    private final Runnable exportPDFAction;
    private final Runnable exportSVGAction;

    private final Runnable modifyChartAction;
    private final Runnable replaceChartAction;

    private final Supplier<List<File>> recentFilesSupplier;
    private final Consumer<File> openRecentAction;


    public MenuBarBuilder(
            Runnable newChartAction,
            Runnable saveAction,
            Runnable saveAsAction,
            Runnable loadAction,
            Runnable undoAction,
            Runnable redoAction,
            Runnable resetViewAction,
            Runnable exportPDFAction,
            Runnable exportSVGAction,
            Runnable modifyChartAction,
            Runnable replaceChartAction,
            Supplier<List<File>> recentFilesSupplier,
            Consumer<File> openRecentAction

    ) {

        this.newChartAction = newChartAction;
        this.saveAction = saveAction;
        this.saveAsAction =
                saveAsAction;
        this.loadAction = loadAction;

        this.undoAction = undoAction;
        this.redoAction = redoAction;

        this.resetViewAction = resetViewAction;

        this.exportPDFAction = exportPDFAction;
        this.exportSVGAction = exportSVGAction;

        this.modifyChartAction = modifyChartAction;
        this.replaceChartAction = replaceChartAction;

        this.recentFilesSupplier = recentFilesSupplier;
        this.openRecentAction =
                openRecentAction;
    }



    private Menu createViewMenu() {

        Menu viewMenu = new Menu("View");

        MenuItem resetView =
                new MenuItem("Reset View");


        resetView.setOnAction(event ->
                resetViewAction.run()
        );


        viewMenu.getItems().add(
                resetView
        );


        return viewMenu;
    }

    private Menu createExportMenu() {

        Menu exportMenu = new Menu("Export");


        MenuItem exportSVG =
                new MenuItem("Export SVG");

        MenuItem exportPDF =
                new MenuItem("Export PDF");


        exportSVG.setOnAction(event ->
                exportSVGAction.run()
        );


        exportPDF.setOnAction(event ->
                exportPDFAction.run()
        );


        exportMenu.getItems().addAll(
                exportSVG,
                exportPDF
        );


        return exportMenu;
    }

    private Menu createEditMenu() {

        Menu editMenu = new Menu("Edit");


        MenuItem undo =
                new MenuItem("Undo");

        MenuItem redo =
                new MenuItem("Redo");


        undo.setOnAction(event ->
                undoAction.run()
        );


        redo.setOnAction(event ->
                redoAction.run()
        );


        editMenu.getItems().addAll(
                undo,
                redo
        );


        return editMenu;
    }

    private Menu createFileMenu() {

        Menu fileMenu = new Menu("File");

        MenuItem newChart =
                new MenuItem("New Chart");

        MenuItem savePattern =
                new MenuItem("Save Pattern");

        MenuItem savePatternAs =
                new MenuItem("Save Pattern As...");

        MenuItem loadPattern =
                new MenuItem("Load Pattern");

        Menu recentMenu =
                new Menu("Open Recent Pattern");

        MenuItem loading =
                new MenuItem("No recent files");

        loading.setDisable(true);

        recentMenu.getItems().add(
                loading
        );


        newChart.setOnAction(event ->
                newChartAction.run()
        );

        savePattern.setOnAction(event ->
                saveAction.run()
        );

        savePatternAs.setOnAction(event ->
                saveAsAction.run()
        );

        loadPattern.setOnAction(event ->
                loadAction.run()
        );


        recentMenu.setOnShowing(event -> {

            recentMenu.getItems().clear();

            List<File> recentFiles =
                    recentFilesSupplier.get();

            if (recentFiles.isEmpty()) {

                MenuItem empty =
                        new MenuItem(
                                "No recent files"
                        );

                empty.setDisable(true);

                recentMenu.getItems().add(
                        empty
                );

            }
            else {

                for (File file : recentFiles) {

                    MenuItem item =
                            new MenuItem(
                                    file.getName()
                            );

                    item.setOnAction(actionEvent ->
                            openRecentAction.accept(file)
                    );

                    recentMenu.getItems().add(
                            item
                    );

                }

            }

        });


        fileMenu.getItems().addAll(
                newChart,
                savePattern,
                savePatternAs,
                loadPattern,
                recentMenu
        );

        return fileMenu;

    }

    private Menu createChartMenu() {

        Menu chartMenu = new Menu("Chart");

        MenuItem resizeChart =
                new MenuItem(
                        "Resize Chart..."
                );

        MenuItem replaceChart =
                new MenuItem("Replace...");

        resizeChart.setOnAction(event ->
                modifyChartAction.run()
        );
        replaceChart.setOnAction(event ->
                replaceChartAction.run()
        );

        chartMenu.getItems().add(
                resizeChart
        );
        chartMenu.getItems().add(
                replaceChart
        );

        return chartMenu;
    }

    public MenuBar createMenuBar() {

        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().addAll(
                createFileMenu(),
                createEditMenu(),
                createViewMenu(),
                createExportMenu(),
                createChartMenu()
        );

        return menuBar;
    }

}