package org.Hieke;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SymbolPalette {


    private final ObservableList<StitchType> symbols =
            FXCollections.observableArrayList();


    public SymbolPalette() {

        symbols.add(StitchType.KNIT);
        symbols.add(StitchType.PURL);
        symbols.add(StitchType.YARN_OVER);
        symbols.add(StitchType.K2TOG);

    }


    public ObservableList<StitchType> getSymbols() {

        return symbols;

    }


    public int size() {

        return symbols.size();

    }


    public StitchType getSymbol(int index) {

        return symbols.get(index);

    }


    public void addSymbol(
            StitchType symbol
    ) {

        symbols.add(symbol);

    }


    public void removeSymbol(
            int index
    ) {

        symbols.remove(index);

    }

}