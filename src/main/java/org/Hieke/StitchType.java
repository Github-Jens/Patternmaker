package org.Hieke;

public enum StitchType {

    KNIT("K"),
    PURL("P"),
    YARN_OVER("O"),
    K2TOG("/");

    private String symbol;

    StitchType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}