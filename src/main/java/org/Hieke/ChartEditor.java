package org.Hieke;

import javafx.scene.paint.Color;

import java.util.Stack;

public class ChartEditor {

    private final KnittingChart chart;

    private final Stack<Stroke> undoStack = new Stack<>();
    private final Stack<Stroke> redoStack = new Stack<>();
    private Stroke currentStroke;
    private final ChartClipboard clipboard =
            new ChartClipboard();
    private final SelectionTransformer transformer =
            new SelectionTransformer();

    private boolean modified = false;

    public ChartEditor(
            KnittingChart chart
    ) {
        this.chart = chart;
    }

    public void paintCell(
            int row,
            int column,
            Tool activeTool,
            StitchDefinition selectedStitch,
            Color selectedColor,
            int selectedColorIndex
    ) {

        Stitch stitch =
                chart.getStitch(row, column);


        StitchDefinition oldStitch =
                stitch.getDefinition();

        Color oldBackground =
                stitch.getBackgroundColor();

        Color oldTopBorder =
                stitch.getTopBorderColor();

        Color oldRightBorder =
                stitch.getRightBorderColor();

        Color oldBottomBorder =
                stitch.getBottomBorderColor();

        Color oldLeftBorder =
                stitch.getLeftBorderColor();


        StitchDefinition newStitch = oldStitch;

        Color newBackground = oldBackground;
        Color newTopBorder = oldTopBorder;
        Color newRightBorder = oldRightBorder;
        Color newBottomBorder = oldBottomBorder;
        Color newLeftBorder = oldLeftBorder;


        // Stitch handling
        if (activeTool == Tool.ERASE) {

            newStitch = null;
            newBackground = null;

        }
        else if (selectedStitch != null) {

            newStitch = selectedStitch;

        }

        // Colour handling
        if (activeTool == Tool.ERASE) {

            newBackground = null;

        }
        else if (selectedColorIndex == 0) {

            // Null palette colour selected
            newBackground = null;

        }
        else if (selectedColorIndex > 0) {

            // Normal colour selected
            newBackground = selectedColor;

        }



        StitchChange change =
                new StitchChange(
                        stitch,
                        oldStitch,
                        newStitch,
                        oldBackground,
                        newBackground,

                        oldTopBorder,
                        newTopBorder,

                        oldRightBorder,
                        newRightBorder,

                        oldBottomBorder,
                        newBottomBorder,

                        oldLeftBorder,
                        newLeftBorder
                );


        change.redo();
        modified = true;


        if (currentStroke != null) {

            currentStroke.addChange(change);

        }

    }

    private void changeBorders(
            Stitch stitch,
            Color top,
            Color right,
            Color bottom,
            Color left
    ) {

        Color oldTop =
                stitch.getTopBorderColor();

        Color oldRight =
                stitch.getRightBorderColor();

        Color oldBottom =
                stitch.getBottomBorderColor();

        Color oldLeft =
                stitch.getLeftBorderColor();


        StitchChange change =
                new StitchChange(
                        stitch,

                        stitch.getDefinition(),
                        stitch.getDefinition(),

                        stitch.getBackgroundColor(),
                        stitch.getBackgroundColor(),

                        oldTop,
                        top,

                        oldRight,
                        right,

                        oldBottom,
                        bottom,

                        oldLeft,
                        left
                );


        change.redo();


        if (currentStroke != null) {

            currentStroke.addChange(change);

        }

    }

    public void frameSelection(
            ChartSelection selection,
            Color color
    ) {

        if (!selection.hasSelection()) {

            return;

        }


        int startColumn =
                Math.min(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );

        int endColumn =
                Math.max(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );


        int startRow =
                Math.min(
                        selection.getStartRow(),
                        selection.getEndRow()
                );

        int endRow =
                Math.max(
                        selection.getStartRow(),
                        selection.getEndRow()
                );


        beginStroke();


        for (int row = startRow; row <= endRow; row++) {

            for (int column = startColumn;
                 column <= endColumn;
                 column++) {


                Stitch stitch =
                        chart.getStitch(
                                row,
                                column
                        );


                Color top =
                        row == startRow
                                ? color
                                : stitch.getTopBorderColor();


                Color bottom =
                        row == endRow
                                ? color
                                : stitch.getBottomBorderColor();


                Color left =
                        column == startColumn
                                ? color
                                : stitch.getLeftBorderColor();


                Color right =
                        column == endColumn
                                ? color
                                : stitch.getRightBorderColor();


                changeBorders(
                        stitch,
                        top,
                        right,
                        bottom,
                        left
                );

            }

        }


        endStroke();

    }

    public void fillSelection(
            ChartSelection selection,
            Tool activeTool,
            StitchDefinition definition,
            Color selectedColor,
            int selectedColorIndex
    ){


        if (!selection.hasSelection()) {

            return;

        }


        int startColumn =
                Math.min(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );


        int endColumn =
                Math.max(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );


        int startRow =
                Math.min(
                        selection.getStartRow(),
                        selection.getEndRow()
                );


        int endRow =
                Math.max(
                        selection.getStartRow(),
                        selection.getEndRow()
                );


        beginStroke();


        for (int row = startRow; row <= endRow; row++) {


            for (int column = startColumn;
                 column <= endColumn;
                 column++) {


                paintCell(
                        row,
                        column,
                        activeTool,
                        definition,
                        selectedColor,
                        selectedColorIndex
                );

            }

        }


        endStroke();

    }

    public void clearSelection(
            ChartSelection selection
    ) {

        if (!selection.hasSelection()) {

            return;

        }


        int startColumn =
                Math.min(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );

        int endColumn =
                Math.max(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );


        int startRow =
                Math.min(
                        selection.getStartRow(),
                        selection.getEndRow()
                );

        int endRow =
                Math.max(
                        selection.getStartRow(),
                        selection.getEndRow()
                );


        beginStroke();


        for (int row = startRow; row <= endRow; row++) {

            for (int column = startColumn;
                 column <= endColumn;
                 column++) {


                paintCell(
                        row,
                        column,
                        Tool.ERASE,
                        null,
                        null,
                        -1
                );

            }
        }


        endStroke();

    }

    public void copySelection(
            ChartSelection selection
    ) {

        if (!selection.hasSelection()) {
            return;
        }


        int startRow =
                Math.min(
                        selection.getStartRow(),
                        selection.getEndRow()
                );

        int endRow =
                Math.max(
                        selection.getStartRow(),
                        selection.getEndRow()
                );


        int startColumn =
                Math.min(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );

        int endColumn =
                Math.max(
                        selection.getStartColumn(),
                        selection.getEndColumn()
                );


        ChartClipboardData[][] data =
                new ChartClipboardData
                        [endRow - startRow + 1]
                        [endColumn - startColumn + 1];


        for (int row = startRow; row <= endRow; row++) {

            for (int column = startColumn; column <= endColumn; column++) {

                Stitch stitch =
                        chart.getStitch(row,column);


                data[row-startRow][column-startColumn] =
                        new ChartClipboardData(
                                stitch.getDefinition(),
                                stitch.getBackgroundColor()
                        );

            }

        }


        clipboard.copy(data);

    }



    public void paste(
            int targetRow,
            int targetColumn
    ) {

        if (!clipboard.hasData()) {
            return;
        }


        ChartClipboardData[][] data =
                clipboard.getData();


        beginStroke();


        for (int row = 0; row < data.length; row++) {

            for (int column = 0; column < data[row].length; column++) {


                int chartRow =
                        targetRow + row;


                int chartColumn =
                        targetColumn + column;


                if (chartRow < 0 ||
                        chartColumn < 0 ||
                        chartRow >= chart.getRows() ||
                        chartColumn >= chart.getColumns()) {

                    continue;

                }


                ChartClipboardData cell =
                        data[row][column];


                paintCell(
                        chartRow,
                        chartColumn,
                        Tool.DRAW,
                        cell.getDefinition(),
                        cell.getColor(),
                        cell.getColor() == null ? 0 : 1
                );

            }

        }


        endStroke();

    }

    public void paint(
            int row,
            int column,
            EditorState state
    ) {

        paintCell(
                row,
                column,
                state.activeToolProperty().get(),
                state.selectedStitchProperty().get(),
                state.selectedColorProperty().get(),
                state.selectedColorIndexProperty().get()
        );

    }

    public void mirrorHorizontal(
            ChartSelection selection
    ) {

        if (!selection.hasSelection()) {
            return;
        }


        int startRow =
                getSelectionStartRow(selection);

        int startColumn =
                getSelectionStartColumn(selection);


        SelectionSnapshot before =
                transformer.createSnapshot(
                        chart,
                        selection
                );


        SelectionSnapshot after =
                transformer.mirrorHorizontal(
                        before
                );


        beginStroke();


        transformer.applySnapshot(
                chart,
                startRow,
                startColumn,
                after
        );


        currentStroke.addChange(
                new TransformationChange(
                        transformer,
                        chart,
                        startRow,
                        startColumn,
                        before,
                        after
                )
        );


        endStroke();

        modified = true;

    }

    public void mirrorVertical(
            ChartSelection selection
    ) {

        if (!selection.hasSelection()) {
            return;
        }


        int startRow =
                getSelectionStartRow(selection);

        int startColumn =
                getSelectionStartColumn(selection);


        SelectionSnapshot before =
                transformer.createSnapshot(
                        chart,
                        selection
                );


        SelectionSnapshot after =
                transformer.mirrorVertical(
                        before
                );


        beginStroke();


        transformer.applySnapshot(
                chart,
                startRow,
                startColumn,
                after
        );


        currentStroke.addChange(
                new TransformationChange(
                        transformer,
                        chart,
                        startRow,
                        startColumn,
                        before,
                        after
                )
        );


        endStroke();

        modified = true;

    }

    public void startRotation(
            ChartSelection selection,
            EditorState state
    ) {

        if (!selection.hasSelection()) {
            return;
        }


        int startRow =
                getSelectionStartRow(selection);


        int startColumn =
                getSelectionStartColumn(selection);


        SelectionSnapshot before =
                transformer.createSnapshot(
                        chart,
                        selection
                );


        transformer.clearArea(
                chart,
                startRow,
                startColumn,
                before.getRows(),
                before.getColumns()
        );


        state.setFloatingSelection(
                new FloatingSelection(
                        before,
                        before,
                        startRow,
                        startColumn
                )
        );

        state.setMode(
                EditorMode.ROTATION
        );

    }

    public void startMove(
            ChartSelection selection,
            EditorState state
    ) {

        if (!selection.hasSelection()) {
            return;
        }


        int startRow =
                getSelectionStartRow(selection);


        int startColumn =
                getSelectionStartColumn(selection);


        SelectionSnapshot snapshot =
                transformer.createSnapshot(
                        chart,
                        selection
                );


        transformer.clearArea(
                chart,
                startRow,
                startColumn,
                snapshot.getRows(),
                snapshot.getColumns()
        );


        state.setFloatingSelection(
                new FloatingSelection(
                        snapshot,
                        snapshot,
                        startRow,
                        startColumn
                )
        );


        state.setMode(
                EditorMode.FLOATING_SELECTION
        );

    }

    public void rotateFloating90(
            EditorState state
    ) {

        FloatingSelection floating =
                state.getFloatingSelection();


        if (floating == null) {

            return;

        }


        floating.rotate90(
                transformer
        );

    }

    public void rotateFloatingCounterClockwise90(
            EditorState state
    ) {

        FloatingSelection floating =
                state.getFloatingSelection();


        if (floating == null) {

            return;

        }


        floating.rotateCounterClockwise90(
                transformer
        );

    }

    // for roation placement

    public void placeFloatingSelection(
            EditorState state
    ) {

        FloatingSelection floating =
                state.getFloatingSelection();

        if (floating == null) {
            return;
        }


        int startRow =
                floating.getRow();

        int startColumn =
                floating.getColumn();


        SelectionSnapshot before =
                floating.getOriginalSnapshot();

        SelectionSnapshot after =
                floating.getSnapshot();


        beginStroke();


        FloatingSelectionPlacementChange change =
                new FloatingSelectionPlacementChange(
                        transformer,
                        chart,
                        floating.getOriginalRow(),
                        floating.getOriginalColumn(),
                        startRow,
                        startColumn,
                        before,
                        after
                );


        change.redo();


        currentStroke.addChange(change);


        endStroke();


        state.setFloatingSelection(null);
        state.getSelection().clear();
        state.setMode(
                EditorMode.NORMAL
        );

        modified = true;

    }

    //place the floating selection when in move mode

    public void commitFloatingSelectionMove(
            EditorState state
    ) {

        FloatingSelection floating =
                state.getFloatingSelection();

        if (floating == null) {
            return;
        }

        int row =
                floating.getRow();

        int column =
                floating.getColumn();

        SelectionSnapshot before =
                floating.getOriginalSnapshot();

        SelectionSnapshot after =
                floating.getSnapshot();

        beginStroke();

        FloatingSelectionPlacementChange change =
                new FloatingSelectionPlacementChange(
                        transformer,
                        chart,
                        floating.getOriginalRow(),
                        floating.getOriginalColumn(),
                        row,
                        column,
                        before,
                        after
                );

        change.redo();

        currentStroke.addChange(change);

        endStroke();

        modified = true;
    }

    public void moveFloatingSelection(
            EditorState state,
            int row,
            int column
    ) {

        FloatingSelection floating =
                state.getFloatingSelection();

        if (floating == null) {
            return;
        }

        floating.move(
                row,
                column
        );

    }

    public void cancelFloatingSelection(
            EditorState state
    ) {

        FloatingSelection floating =
                state.getFloatingSelection();


        if (floating == null) {
            return;
        }


        transformer.applySnapshot(
                chart,
                floating.getRow(),
                floating.getColumn(),
                floating.getOriginalSnapshot()
        );


        state.setFloatingSelection(null);

        state.getSelection().clear();

        state.setMode(
                EditorMode.NORMAL
        );

    }
    public void startPainting() {

        beginStroke();

    }

    public void finishPainting() {

        endStroke();

    }

    public void beginStroke() {

        currentStroke = new Stroke();

    }


    public void endStroke() {

        if (currentStroke != null) {

            undoStack.push(currentStroke);

            redoStack.clear();

            currentStroke = null;

        }

    }

    private void addChange(UndoableChange change) {

        if (currentStroke != null) {

            currentStroke.addChange(change);

        }

    }

    public void undo() {

        if (!undoStack.isEmpty()) {

            Stroke stroke = undoStack.pop();

            stroke.undo();

            redoStack.push(stroke);

            modified = true;

        }

    }


    public void redo() {

        if (!redoStack.isEmpty()) {

            Stroke stroke = redoStack.pop();

            stroke.redo();

            undoStack.push(stroke);

            modified = true;

        }

    }


    public void insertRows(
            int index,
            int amount
    ) {

        if (!canInsertRows(amount)) {
            return;
        }

        if (amount <= 0) {
            return;
        }


        beginStroke();


        for (int i = 0; i < amount; i++) {

            chart.insertRow(index);

        }


        currentStroke.addChange(
                new InsertRowChange(
                        chart,
                        index,
                        amount
                )
        );


        endStroke();

        modified = true;

    }

    public void insertColumns(
            int index,
            int amount
    ) {

        if (!canInsertColumns(amount)) {
            return;
        }

        if (amount <= 0) {
            return;
        }


        beginStroke();


        for (int i = 0; i < amount; i++) {

            chart.insertColumn(index);

        }


        currentStroke.addChange(
                new InsertColumnChange(
                        chart,
                        index,
                        amount
                )
        );


        endStroke();

        modified = true;

    }

    public void deleteRows(
            int startRow,
            int endRow
    ) {

        int amount = endRow - startRow + 1;

        if (!canDeleteRows(amount)) {
            return;
        }

        beginStroke();


        for (int row = endRow; row >= startRow; row--) {

            deleteRowInternal(row);

        }


        endStroke();

        modified = true;

    }


    public void deleteColumns(
            int startColumn,
            int endColumn
    ) {

        int amount = endColumn - startColumn + 1;

        if (!canDeleteColumns(amount)) {
            return;
        }

        beginStroke();


        for (int column = endColumn; column >= startColumn; column--) {

            deleteColumnInternal(column);

        }


        endStroke();

        modified = true;

    }

    private void deleteRowInternal(int index) {

        Stitch[] deletedRow =
                new Stitch[chart.getColumns()];


        for (int column = 0; column < chart.getColumns(); column++) {

            deletedRow[column] =
                    chart.getStitch(index, column);

        }


        chart.deleteRow(index);


        addChange(
                new DeleteRowChange(
                        chart,
                        deletedRow,
                        index
                )
        );

    }

    private void deleteColumnInternal(int index) {

        Stitch[] deletedColumn =
                new Stitch[chart.getRows()];


        for (int row = 0; row < chart.getRows(); row++) {

            deletedColumn[row] =
                    chart.getStitch(row, index);

        }


        chart.deleteColumn(index);


        addChange(
                new DeleteColumnChange(
                        chart,
                        deletedColumn,
                        index
                )
        );

    }

    public boolean canInsertRows(int amount) {

        return amount > 0 &&
                chart.getRows() + amount <= KnittingChart.MAX_ROWS;

    }


    public boolean canInsertColumns(int amount) {

        return amount > 0 &&
                chart.getColumns() + amount <= KnittingChart.MAX_COLUMNS;

    }


    public boolean canDeleteRows(int amount) {

        return amount > 0 &&
                chart.getRows() - amount >= KnittingChart.MIN_ROWS;

    }


    public boolean canDeleteColumns(int amount) {

        return amount > 0 &&
                chart.getColumns() - amount >= KnittingChart.MIN_COLUMNS;

    }



    public KnittingChart getChart() {
        return chart;
    }

    public boolean hasClipboardData() {

        return clipboard.hasData();

    }

    public boolean isModified() {

        return modified;

    }


    public void markSaved() {

        modified = false;

    }

    private int getSelectionStartRow(
            ChartSelection selection
    ) {

        return Math.min(
                selection.getStartRow(),
                selection.getEndRow()
        );

    }


    private int getSelectionStartColumn(
            ChartSelection selection
    ) {

        return Math.min(
                selection.getStartColumn(),
                selection.getEndColumn()
        );

    }

}