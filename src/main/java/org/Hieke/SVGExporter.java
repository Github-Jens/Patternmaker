package org.Hieke;

import java.io.FileWriter;
import java.io.IOException;

public class SVGExporter {

    private final KnittingChart chart;
    private final ChartRenderer renderer;



    public SVGExporter(KnittingChart chart) {

        this.chart = chart;
        this.renderer = new ChartRenderer(chart);


    }


    public void export(String filename) throws IOException {


        RenderSettings settings =
                new RenderSettings(
                        30,
                        chart.getRows(),
                        chart.getColumns()
                );


        double width =
                chart.getColumns() * settings.cellSize
                        + settings.rulerSize;


        double height =
                chart.getRows() * settings.cellSize
                        + settings.rulerSize;



        SVGRenderContext context =
                new SVGRenderContext(
                        width,
                        height
                );

        renderer.render(
                context,
                settings,
                width,
                height
        );


        try(FileWriter writer =
                    new FileWriter(filename)) {

            writer.write(
                    context.finish()
            );

        }
    }
}