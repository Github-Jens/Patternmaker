package org.Hieke;

import javafx.scene.paint.Color;

import java.io.*;

public class FileManager {


    /**
     * Saves a knitting chart into a custom .knit file.
     *
     * File format:
     *
     * Line 1: Number of rows
     * Line 2: Number of columns
     *
     * Following lines contain the stitches row by row.
     *
     * Example:
     * KNIT:#FF0000,PURL,YARN_OVER
     *
     * The colour is optional. This keeps compatibility
     * with older pattern files.
     */
    public void save(
            KnittingChart chart,
            File file
    ) throws IOException {


        /*
         * Try-with-resources automatically closes the writer.
         * This prevents file handles staying open if an error occurs.
         */
        try (PrintWriter writer = new PrintWriter(file)) {


            // Store chart dimensions first so the loader
            // knows how large the chart needs to be recreated.
            writer.println(chart.getRows());
            writer.println(chart.getColumns());


            /*
             * Save every stitch row by row.
             *
             * The order is important:
             * The loader reads the stitches back in exactly
             * the same row/column order.
             */
            for (int row = 0; row < chart.getRows(); row++) {


                for (int column = 0;
                     column < chart.getColumns();
                     column++) {


                    Stitch stitch =
                            chart.getStitch(row, column);


                    // Always save the stitch type.
                    writer.print(
                            stitch.getType().name()
                    );


                    /*
                     * If the stitch has a background colour,
                     * append it after a colon.
                     *
                     * Example:
                     * KNIT:#FF0000
                     *
                     * If no colour exists, nothing is added.
                     */
                    if (stitch.getBackgroundColor() != null) {

                        writer.print(":");

                        writer.print(
                                toHex(
                                        stitch.getBackgroundColor()
                                )
                        );

                    }


                    // Separate stitches in the same row.
                    if (column < chart.getColumns() - 1) {
                        writer.print(",");
                    }

                }


                // Move to the next row in the file.
                writer.println();

            }

        }
    }


    /**
     * Loads a knitting chart from a .knit file.
     *
     * Supports both:
     *
     * Old format:
     * KNIT
     *
     * New format:
     * KNIT:#FF0000
     *
     * This means old patterns without colours
     * can still be opened.
     */
    public KnittingChart load(
            File file
    ) throws IOException {


        /*
         * Automatically closes the reader when finished.
         */
        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(file)
                     )) {


            // Read chart dimensions.
            int rows =
                    Integer.parseInt(
                            reader.readLine()
                    );


            int columns =
                    Integer.parseInt(
                            reader.readLine()
                    );


            // Create an empty chart with the correct size.
            KnittingChart chart =
                    new KnittingChart(
                            rows,
                            columns
                    );


            /*
             * Read every row from the file.
             */
            for (int row = 0; row < rows; row++) {


                String line =
                        reader.readLine();


                /*
                 * Split the row into individual stitches.
                 *
                 * Example:
                 *
                 * KNIT:#FF0000,PURL,EMPTY
                 *
                 * becomes:
                 *
                 * [
                 *   KNIT:#FF0000,
                 *   PURL,
                 *   EMPTY
                 * ]
                 */
                String[] stitches =
                        line.split(",");


                for (int column = 0;
                     column < columns;
                     column++) {


                    String stitchData =
                            stitches[column];


                    /*
                     * Split stitch type and colour.
                     *
                     * Example:
                     *
                     * KNIT:#FF0000
                     *
                     * becomes:
                     *
                     * parts[0] = KNIT
                     * parts[1] = #FF0000
                     */
                    String[] parts =
                            stitchData.split(":");


                    StitchType type =
                            StitchType.valueOf(
                                    parts[0]
                            );


                    Stitch stitch =
                            chart.getStitch(
                                    row,
                                    column
                            );


                    // Restore stitch symbol.
                    stitch.setType(type);


                    /*
                     * Restore background colour if one exists.
                     *
                     * Older files do not have this value,
                     * therefore the check prevents errors.
                     */
                    if (parts.length > 1 &&
                            !parts[1].isEmpty()) {


                        stitch.setBackgroundColor(
                                Color.web(
                                        parts[1]
                                )
                        );

                    }

                }

            }


            return chart;

        }

    }


    /**
     * Converts a JavaFX Color into a hexadecimal string.
     *
     * Example:
     *
     * Color.RED
     *
     * becomes:
     *
     * #FF0000
     */
    private String toHex(
            Color color
    ) {

        return String.format(
                "#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255)
        );

    }

}