package org.Hieke;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class StitchLibrary {


    private final ObservableList<StitchDefinition> stitches =
            FXCollections.observableArrayList();



    public StitchLibrary() {


        stitches.add(
                new StitchDefinition(
                        "normal",
                        "Knit Stitch",
                        "",
                        1,
                        1
                )
        );


        stitches.add(
                new StitchDefinition(
                        "purl",
                        "Purl",
                        "●",
                        1,
                        1
                )
        );


        stitches.add(
                new StitchDefinition(
                        "yarn_over",
                        "Yarn Over",
                        "○",
                        1,
                        1
                )
        );


        stitches.add(
                new StitchDefinition(
                        "k2tog",
                        "Knit Two Together",
                        "⟋",
                        1,
                        1
                )
        );

    }


    public ObservableList<StitchDefinition> getStitches() {

        return stitches;

    }


    public void addStitch(
            StitchDefinition stitch
    ) {

        stitches.add(stitch);

    }
    public StitchDefinition findById(
            String id
    ) {

        for (StitchDefinition stitch : stitches) {

            if (stitch.getId().equals(id)) {

                return stitch;

            }

        }

        return null;

    }

}