package org.Hieke;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SymbolPalette {


    private final ObservableList<StitchDefinition> symbols =
            FXCollections.observableArrayList();


    public SymbolPalette(
            StitchLibrary library
    ) {

        symbols.add(
                library.getStitches().get(0)
        );

        symbols.add(
                library.getStitches().get(1)
        );

        symbols.add(
                library.getStitches().get(2)
        );

        symbols.add(
                library.getStitches().get(3)
        );

    }


    public ObservableList<StitchDefinition> getSymbols() {

        return symbols;

    }


    public int size() {

        return symbols.size();

    }


    public StitchDefinition getSymbol(int index) {

        return symbols.get(index);

    }


    public void addSymbol(
            StitchDefinition symbol
    ) {
            symbols.add(symbol);



    }


    public void removeSymbol(
            int index
    ) {

        symbols.remove(index);

    }

}