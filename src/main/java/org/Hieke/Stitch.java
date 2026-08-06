package org.Hieke;

import javafx.scene.paint.Color;

public class Stitch {

    private StitchDefinition definition;

    private Color backgroundColor;

    private int row;
    private int column;

    private Color topBorderColor;
    private Color rightBorderColor;
    private Color bottomBorderColor;
    private Color leftBorderColor;


    public Stitch(
            StitchDefinition definition,
            int row,
            int column
    ) {

        this.definition = definition;
        this.row = row;
        this.column = column;

        this.backgroundColor = null;

        this.topBorderColor = null;
        this.rightBorderColor = null;
        this.bottomBorderColor = null;
        this.leftBorderColor = null;
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

    public Color getTopBorderColor() {
        return topBorderColor;
    }

    public void setTopBorderColor(Color color) {
        this.topBorderColor = color;
    }

    public Color getRightBorderColor() {
        return rightBorderColor;
    }

    public void setRightBorderColor(Color color) {
        this.rightBorderColor = color;
    }

    public Color getBottomBorderColor() {
        return bottomBorderColor;
    }

    public void setBottomBorderColor(Color color) {
        this.bottomBorderColor = color;
    }

    public Color getLeftBorderColor() {
        return leftBorderColor;
    }

    public void setLeftBorderColor(Color color) {
        this.leftBorderColor = color;
    }

    public void setPosition(
            int row,
            int column
    ) {

        this.row = row;
        this.column = column;

    }

    // use copy() to create a temporary snapshot of the selected area and copyFrom() to write the transformed stitches back into the chart.

    public Stitch copy() {

        Stitch copy =
                new Stitch(
                        definition,
                        row,
                        column
                );

        copy.setBackgroundColor(
                backgroundColor
        );

        copy.setTopBorderColor(
                topBorderColor
        );

        copy.setRightBorderColor(
                rightBorderColor
        );

        copy.setBottomBorderColor(
                bottomBorderColor
        );

        copy.setLeftBorderColor(
                leftBorderColor
        );

        return copy;

    }

    public void copyFrom(
            Stitch other
    ) {

        setDefinition(
                other.getDefinition()
        );

        setBackgroundColor(
                other.getBackgroundColor()
        );

        setTopBorderColor(
                other.getTopBorderColor()
        );

        setRightBorderColor(
                other.getRightBorderColor()
        );

        setBottomBorderColor(
                other.getBottomBorderColor()
        );

        setLeftBorderColor(
                other.getLeftBorderColor()
        );

    }

}