package org.Hieke;

import javafx.scene.paint.Color;

import java.io.*;

public class FileManager {

    public void save(
            KnittingChart chart,
            Palette palette,
            File file
    ) throws IOException {


        try (PrintWriter writer = new PrintWriter(file)) {


            // Save size
            writer.println(chart.getRows());
            writer.println(chart.getColumns());

            // Save palette

            writer.println("PALETTE");


            for (Color color : palette.getColors()) {

                if (color == null) {

                    writer.println("null");

                }
                else {

                    writer.println(
                            toHex(color)
                    );

                }

            }


            writer.println("END_PALETTE");


            // Save stitches
            for (int row = 0; row < chart.getRows(); row++) {


                for (int column = 0; column < chart.getColumns(); column++) {


                    Stitch stitch =
                            chart.getStitch(row, column);


                    writer.print(
                            stitch.getType().name()
                    );


                    if (stitch.getBackgroundColor() != null) {

                        writer.print(":");

                        writer.print(
                                toHex(
                                        stitch.getBackgroundColor()
                                )
                        );

                    }


                    if (column < chart.getColumns() - 1) {
                        writer.print(",");
                    }

                }


                writer.println();

            }

        }
    }


    public PatternData load(
            File file
    ) throws IOException {


        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(file)
                     )) {


            int rows =
                    Integer.parseInt(
                            reader.readLine()
                    );


            int columns =
                    Integer.parseInt(
                            reader.readLine()
                    );


            KnittingChart chart =
                    new KnittingChart(
                            rows,
                            columns
                    );

            Palette palette = new Palette();


// Load palette

            String paletteHeader =
                    reader.readLine();


            if ("PALETTE".equals(paletteHeader)) {

                palette.getColors().clear();


                String line;


                while (!(line = reader.readLine())
                        .equals("END_PALETTE")) {


                    if (line.equals("null")) {

                        palette.getColors()
                                .add(null);

                    }
                    else {

                        palette.getColors()
                                .add(
                                        Color.web(line)
                                );

                    }

                }

            }


// Load stitches

            for (int row = 0; row < rows; row++) {


                String line =
                        reader.readLine();


                String[] stitches =
                        line.split(",");


                for (int column = 0; column < columns; column++) {


                    String stitchData =
                            stitches[column];


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


                    stitch.setType(type);


                    // Load colour if present
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


            return new PatternData(
                    chart,
                    palette
            );

        }

    }


    private String toHex(Color color) {

        return String.format(
                "#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255)
        );

    }

}