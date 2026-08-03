package org.Hieke;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


public class SelectionMenu extends ContextMenu {


    public SelectionMenu() {


        MenuItem fillSymbol =
                new MenuItem(
                        "Fill with Symbol..."
                );


        MenuItem fillColour =
                new MenuItem(
                        "Fill with Colour..."
                );


        MenuItem copy =
                new MenuItem(
                        "Copy"
                );


        MenuItem cut =
                new MenuItem(
                        "Cut"
                );


        MenuItem cancel =
                new MenuItem(
                        "Cancel"
                );


        getItems()
                .addAll(
                        fillSymbol,
                        fillColour,
                        new SeparatorMenuItem(),
                        copy,
                        cut,
                        new SeparatorMenuItem(),
                        cancel
                );

    }

}