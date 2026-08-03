package org.Hieke;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StitchDefinition {


    private final String id;

    private final String name;

    private final String symbol;


    // Future multi-cell support
    private final int width;
    private final int height;

    private final String category;
    private final String svg;


    @JsonCreator
    public StitchDefinition(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("category") String category,
            @JsonProperty("symbol") String symbol,
            @JsonProperty("svg") String svg,
            @JsonProperty("width") int width,
            @JsonProperty("height") int height
    ){

        this.id = id;
        this.name = name;
        this.category = category;
        this.symbol = symbol;
        this.svg = svg;
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
    public String getCategory() {

        return category;

    }
    public String getSvg() {

        return svg;

    }

}