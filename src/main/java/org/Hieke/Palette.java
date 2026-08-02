package org.Hieke;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Palette {

    private final ObservableList<Color> colors;


    public Palette() {

        colors = FXCollections.observableArrayList();

        colors.add(null);
        colors.add(Color.WHITE);
        colors.add(Color.RED);
        colors.add(Color.BLACK);

    }


    public ObservableList<Color> getColors() {

        return colors;

    }


    public Color getColor(int index) {

        return colors.get(index);

    }


    public void setColor(
            int index,
            Color color
    ) {

        colors.set(
                index,
                color
        );

    }


    public void addColor() {

        colors.add(Color.WHITE);

    }


    public int size() {

        return colors.size();

    }
    public void replaceColors(
            Collection<Color> newColors
    ) {

        colors.setAll(newColors);

    }
}