package org.Hieke;

import javafx.scene.paint.Color;

public class ChartClipboard {

    private ChartClipboardData[][] clipboard;


    public void copy(ChartClipboardData[][] data) {

        clipboard = data;

    }


    public boolean hasData() {

        return clipboard != null;

    }


    public ChartClipboardData[][] getData() {

        return clipboard;

    }


    public void clear() {

        clipboard = null;

    }

}