package org.Hieke;

import javafx.scene.paint.Color;

import java.io.*;

public class FileManager {

    public void save(
            KnittingChart chart,
            Palette palette,
            SymbolPalette symbolPalette,
            File file
    )throws IOException {


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

            writer.println("SYMBOL_PALETTE");


            for (StitchDefinition symbol : symbolPalette.getSymbols()) {

                writer.println(
                        symbol.getId()
                );

            }


            writer.println("END_SYMBOL_PALETTE");


            // Save stitches
            for (int row = 0; row < chart.getRows(); row++) {


                for (int column = 0; column < chart.getColumns(); column++) {


                    Stitch stitch =
                            chart.getStitch(row, column);


                    // Save stitch type
// Null means the stitch has been erased
                    if (stitch.getDefinition() == null) {

                        writer.print("null");

                    }
                    else {

                        writer.print(
                                stitch.getDefinition().getId()
                        );

                    }


                    writer.print(":");

                    writer.print(
                            stitch.getBackgroundColor() == null
                                    ? "null"
                                    : toHex(stitch.getBackgroundColor())
                    );

                    writer.print(":");

                    writer.print(
                            colorToString(stitch.getTopBorderColor())
                    );

                    writer.print(":");

                    writer.print(
                            colorToString(stitch.getRightBorderColor())
                    );

                    writer.print(":");

                    writer.print(
                            colorToString(stitch.getBottomBorderColor())
                    );

                    writer.print(":");

                    writer.print(
                            colorToString(stitch.getLeftBorderColor())
                    );


                    if (column < chart.getColumns() - 1) {
                        writer.print(",");
                    }

                }


                writer.println();

            }

        }
    }


    public PatternData load(
            File file,
            StitchLibrary stitchLibrary
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


            Palette palette =
                    new Palette();


            SymbolPalette symbolPalette =
                    new SymbolPalette(
                            stitchLibrary
                    );


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
            String symbolHeader =
                    reader.readLine();


            if ("SYMBOL_PALETTE".equals(symbolHeader)) {

                symbolPalette.getSymbols().clear();


                String line;


                while (!(line = reader.readLine())
                        .equals("END_SYMBOL_PALETTE")) {


                    for (StitchDefinition stitch :
                            stitchLibrary.getStitches()) {


                        if (stitch.getId().equals(line)) {

                            symbolPalette.getSymbols()
                                    .add(stitch);

                            break;

                        }

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
                            stitchData.split(":", -1);


                    StitchDefinition definition;

                    if (parts[0].equals("null")) {

                        definition = null;

                    }
                    else {

                        definition =
                                stitchLibrary.findById(
                                        parts[0]
                                );

                    }


                    Stitch stitch =
                            chart.getStitch(
                                    row,
                                    column
                            );


                    stitch.setDefinition(definition);


                    // Load background colour if present
                    if (parts.length > 1) {

                        stitch.setBackgroundColor(
                                parseColor(parts[1])
                        );

                    }
                    else {

                        stitch.setBackgroundColor(null);

                    }

                    stitch.setTopBorderColor(null);
                    stitch.setRightBorderColor(null);
                    stitch.setBottomBorderColor(null);
                    stitch.setLeftBorderColor(null);


// Load border colours if present
                    if (parts.length > 2) {

                        stitch.setTopBorderColor(
                                parseColor(parts[2])
                        );

                    }

                    if (parts.length > 3) {

                        stitch.setRightBorderColor(
                                parseColor(parts[3])
                        );

                    }

                    if (parts.length > 4) {

                        stitch.setBottomBorderColor(
                                parseColor(parts[4])
                        );

                    }

                    if (parts.length > 5) {

                        stitch.setLeftBorderColor(
                                parseColor(parts[5])
                        );

                    }

                }

            }


            return new PatternData(
                    chart,
                    palette,
                    symbolPalette
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

    private String colorToString(Color color) {

        if (color == null) {
            return "null";
        }

        return toHex(color);
    }

    private Color parseColor(String value) {

        if (value == null || value.equals("null")) {

            return null;

        }

        return Color.web(value);

    }

}