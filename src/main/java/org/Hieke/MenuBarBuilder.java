package org.Hieke;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuBarBuilder {

    private final Runnable newChartAction;
    private final Runnable saveAction;
    private final Runnable loadAction;

    private final Runnable undoAction;
    private final Runnable redoAction;

    private final Runnable resetViewAction;

    private final Runnable exportPDFAction;
    private final Runnable exportSVGAction;

    private final Runnable modifyChartAction;
    private final Runnable replaceChartAction;


    public MenuBarBuilder(
            Runnable newChartAction,
            Runnable saveAction,
            Runnable loadAction,
            Runnable undoAction,
            Runnable redoAction,
            Runnable resetViewAction,
            Runnable exportPDFAction,
            Runnable exportSVGAction,
            Runnable modifyChartAction,
            Runnable replaceChartAction
    ) {

        this.newChartAction = newChartAction;
        this.saveAction = saveAction;
        this.loadAction = loadAction;

        this.undoAction = undoAction;
        this.redoAction = redoAction;

        this.resetViewAction = resetViewAction;

        this.exportPDFAction = exportPDFAction;
        this.exportSVGAction = exportSVGAction;

        this.modifyChartAction = modifyChartAction;
        this.replaceChartAction = replaceChartAction;
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

        MenuItem loadPattern =
                new MenuItem("Load Pattern");


        newChart.setOnAction(event ->
                newChartAction.run()
        );


        savePattern.setOnAction(event ->
                saveAction.run()
        );


        loadPattern.setOnAction(event ->
                loadAction.run()
        );


        fileMenu.getItems().addAll(
                newChart,
                savePattern,
                loadPattern
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