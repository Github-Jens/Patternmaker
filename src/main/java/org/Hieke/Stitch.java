package org.Hieke;

public class Stitch {

    private StitchType type;
    private int row;
    private int column;

    public Stitch(StitchType type, int row, int column) {
        this.type = type;
        this.row = row;
        this.column = column;
    }

    public void setType(StitchType type) {
        this.type = type;
    }
    public StitchType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Stitch " + type +
                " at row " + row +
                " column " + column;
    }

    public String getSymbol() {
        return type.getSymbol();
    }
}