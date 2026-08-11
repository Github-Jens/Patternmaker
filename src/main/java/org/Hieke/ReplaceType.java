package org.Hieke;

public enum ReplaceType {

    SYMBOL_TO_SYMBOL("Symbol → Symbol"),
    SYMBOL_TO_COLOUR("Symbol → Colour"),
    COLOUR_TO_SYMBOL("Colour → Symbol"),
    COLOUR_TO_COLOUR("Colour → Colour");

    private final String displayName;

    ReplaceType(
            String displayName
    ) {

        this.displayName = displayName;

    }

    @Override
    public String toString() {

        return displayName;

    }

}