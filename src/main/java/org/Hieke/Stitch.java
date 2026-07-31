package org.Hieke;

import javafx.scene.paint.Color;

public class Stitch {

    private StitchType type;

    private Color backgroundColor;

    private int row;
    private int column;


    public Stitch(
            StitchType type,
            int row,
            int column
    ) {

        this.type = type;
        this.row = row;
        this.column = column;

        this.backgroundColor = null;
    }


    public void setType(StitchType type) {
        this.type = type;
    }


    public StitchType getType() {
        return type;
    }


    public Color getBackgroundColor() {
        return backgroundColor;
    }


    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }


    public String getSymbol() {

        if (type == null) {
            return "";
        }

        return type.getSymbol();
    }


    public int getRow() {
        return row;
    }


    public int getColumn() {
        return column;
    }


    @Override
    public String toString() {

        return "Stitch " + type +
                " at row " + row +
                " column " + column +
                " background " + backgroundColor;
    }
}