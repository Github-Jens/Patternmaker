package org.Hieke;

import java.io.*;

public class FileManager {

    public void save(KnittingChart chart, File file)
            throws IOException {


        PrintWriter writer = new PrintWriter(file);


        // Save size
        writer.println(chart.getRows());
        writer.println(chart.getColumns());


        // Save stitches
        for (int row = 0; row < chart.getRows(); row++) {


            for (int column = 0; column < chart.getColumns(); column++) {


                Stitch stitch = chart.getStitch(row, column);


                writer.print(stitch.getType().name());


                if (column < chart.getColumns() - 1) {
                    writer.print(",");
                }

            }


            writer.println();
        }


        writer.close();
    }

    public KnittingChart load(File file)
            throws IOException {


        BufferedReader reader =
                new BufferedReader(
                        new FileReader(file)
                );


        int rows = Integer.parseInt(reader.readLine());

        int columns = Integer.parseInt(reader.readLine());


        KnittingChart chart =
                new KnittingChart(rows, columns);


        for (int row = 0; row < rows; row++) {

            String line = reader.readLine();

            String[] stitches = line.split(",");


            for (int column = 0; column < columns; column++) {

                StitchType type =
                        StitchType.valueOf(stitches[column]);


                chart.getStitch(row, column)
                        .setType(type);

            }
        }


        reader.close();


        return chart;
    }
}