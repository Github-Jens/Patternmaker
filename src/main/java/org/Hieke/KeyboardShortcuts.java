package org.Hieke;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class KeyboardShortcuts {

    public KeyboardShortcuts(
            Scene scene,
            Runnable newChart,
            Runnable save,
            Runnable saveAs,
            Runnable open,
            Runnable undo,
            Runnable redo,
            Runnable escape
    ) {

        scene.getAccelerators().put(

                new KeyCodeCombination(
                        KeyCode.N,
                        KeyCombination.CONTROL_DOWN
                ),

                newChart

        );


        scene.getAccelerators().put(

                new KeyCodeCombination(
                        KeyCode.S,
                        KeyCombination.CONTROL_DOWN
                ),

                save

        );


        scene.getAccelerators().put(

                new KeyCodeCombination(
                        KeyCode.S,
                        KeyCombination.CONTROL_DOWN,
                        KeyCombination.SHIFT_DOWN
                ),

                saveAs

        );


        scene.getAccelerators().put(

                new KeyCodeCombination(
                        KeyCode.O,
                        KeyCombination.CONTROL_DOWN
                ),

                open

        );


        scene.getAccelerators().put(

                new KeyCodeCombination(
                        KeyCode.Z,
                        KeyCombination.CONTROL_DOWN
                ),

                undo

        );


        scene.getAccelerators().put(

                new KeyCodeCombination(
                        KeyCode.Y,
                        KeyCombination.CONTROL_DOWN
                ),

                redo

        );


        scene.getAccelerators().put(

                new KeyCodeCombination(
                        KeyCode.ESCAPE
                ),

                escape

        );

    }

}