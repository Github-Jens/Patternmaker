package org.Hieke;

public class ReplaceSymbolOption {

    private final String displayName;
    private final StitchDefinition definition;

    public ReplaceSymbolOption(
            String displayName,
            StitchDefinition definition
    ) {

        this.displayName = displayName;
        this.definition = definition;

    }

    public String getDisplayName() {

        return displayName;

    }

    public StitchDefinition getDefinition() {

        return definition;

    }

}