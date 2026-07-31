package org.Hieke;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Palette {

    private final List<Color> colors;


    public Palette() {

        colors = new ArrayList<>();

        colors.add(null);
        colors.add(Color.WHITE);
        colors.add(Color.RED);
        colors.add(Color.BLACK);

    }


    public List<Color> getColors() {

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
}