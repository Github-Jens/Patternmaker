package org.Hieke;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class StitchLibrary {


    private final ObservableList<StitchDefinition> stitches =
            FXCollections.observableArrayList();



    public StitchLibrary() {

        loadLibrary();

    }

    private void loadLibrary() {

        ObjectMapper mapper =
                new ObjectMapper();


        try (InputStream input =
                     getClass()
                             .getResourceAsStream("/stitches.json")) {


            if (input == null) {

                throw new RuntimeException(
                        "stitches.json not found"
                );

            }


            List<StitchDefinition> loaded =
                    mapper.readValue(
                            input,
                            new TypeReference<List<StitchDefinition>>() {}
                    );


            stitches.addAll(
                    loaded
            );


        }
        catch (IOException e) {

            throw new RuntimeException(
                    "Could not load stitch library",
                    e
            );

        }

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