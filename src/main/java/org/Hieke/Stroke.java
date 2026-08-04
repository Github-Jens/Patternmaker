package org.Hieke;

import java.util.ArrayList;
import java.util.List;

public class Stroke {

    private final List<UndoableChange> changes =
            new ArrayList<>();


    public void addChange(UndoableChange change) {

        changes.add(change);

    }


    public void undo() {

        for (int i = changes.size() - 1; i >= 0; i--) {

            changes.get(i).undo();

        }

    }


    public void redo() {

        for (UndoableChange change : changes) {

            change.redo();

        }

    }

}