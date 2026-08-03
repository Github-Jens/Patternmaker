package org.Hieke;

public enum StitchType {

    PURL("P"),
    YARN_OVER("O"),
    K2TOG("/"),
    NORMAL("");

    private String symbol;

    StitchType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}