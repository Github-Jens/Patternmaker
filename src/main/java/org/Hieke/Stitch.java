package org.Hieke;

import javafx.scene.paint.Color;

public class Stitch {

    private StitchDefinition definition;

    private Color backgroundColor;

    private int row;
    private int column;


    public Stitch(
            StitchDefinition definition,
            int row,
            int column
    ) {

        this.definition = definition;
        this.row = row;
        this.column = column;

        this.backgroundColor = null;
    }


    public void setDefinition(
            StitchDefinition definition
    ) {

        this.definition = definition;

    }


    public StitchDefinition getDefinition() {

        return definition;

    }


    public Color getBackgroundColor() {

        return backgroundColor;

    }


    public void setBackgroundColor(
            Color backgroundColor
    ) {

        this.backgroundColor = backgroundColor;

    }


    public String getSymbol() {

        if (definition == null) {

            return "";

        }

        return definition.getSymbol();

    }


    public int getRow() {

        return row;

    }


    public int getColumn() {

        return column;

    }


    @Override
    public String toString() {

        return "Stitch " + definition +
                " at row " + row +
                " column " + column +
                " background " + backgroundColor;

    }

}