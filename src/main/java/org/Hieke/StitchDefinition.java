package org.Hieke;

public class StitchDefinition {


    private final String id;

    private final String name;

    private final String symbol;


    // Future multi-cell support
    private final int width;
    private final int height;

    public StitchDefinition(
            String id,
            String name,
            String symbol,
            int width,
            int height
    ) {

        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.width = width;
        this.height = height;


    }


    public String getId() {

        return id;

    }


    public String getName() {

        return name;

    }


    public String getSymbol() {

        return symbol;

    }


    public int getWidth() {

        return width;

    }


    public int getHeight() {

        return height;

    }

}