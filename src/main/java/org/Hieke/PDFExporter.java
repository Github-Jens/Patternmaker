package org.Hieke;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;


public class PDFExporter {

    private final KnittingChart chart;
    private final ChartRenderer renderer;


    public PDFExporter(
            KnittingChart chart
    ) {

        this.chart = chart;
        this.renderer = new ChartRenderer(chart);

    }


    public void export(
            String filename
    ) throws IOException {


        RenderSettings settings =
                new RenderSettings(
                        30,
                        chart.getRows(),
                        chart.getColumns()
                );


        float width =
                (float) (chart.getColumns()
                                        * settings.cellSize
                                        + settings.rulerSize * 2);


        float height =
                (float) (chart.getRows()
                                        * settings.cellSize
                                        + settings.rulerSize * 2);


        PDDocument document =
                new PDDocument();


        PDPage page =
                new PDPage(
                        new PDRectangle(
                                width,
                                height
                        )
                );


        document.addPage(page);


        PDPageContentStream content =
                new PDPageContentStream(
                        document,
                        page
                );
        content.transform(
                new Matrix(
                        1,
                        0,
                        0,
                        -1,
                        0,
                        height
                )
        );


        PDFRenderContext context =
                new PDFRenderContext(content, document);


        renderer.render(
                context,
                settings,
                width,
                height
        );


        content.close();


        document.save(filename);

        document.close();

    }
}