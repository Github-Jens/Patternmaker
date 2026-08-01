package org.Hieke;

import javafx.scene.paint.Color;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.util.Matrix;

import java.io.File;
import java.io.IOException;


public class PDFRenderContext implements RenderContext {

    private final PDPageContentStream content;
    private final PDType0Font font;


    public PDFRenderContext(
            PDPageContentStream content,
            PDDocument document
    ) {
        this.content = content;

        try {
            font = PDType0Font.load(
                    document,
                    new File("C:/Windows/Fonts/seguisym.ttf")
            );
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void setStroke(String color) {

        try {

            Color c =
                    Color.web(color);

            content.setStrokingColor(
                    (float)c.getRed(),
                    (float)c.getGreen(),
                    (float)c.getBlue()
            );

        }
        catch(Exception e) {

            throw new RuntimeException(e);

        }
    }


    @Override
    public void setFill(String color) {

        try {

            Color c =
                    Color.web(color);

            content.setNonStrokingColor(
                    (float)c.getRed(),
                    (float)c.getGreen(),
                    (float)c.getBlue()
            );

        }
        catch(Exception e) {

            throw new RuntimeException(e);

        }
    }


    @Override
    public void drawLine(
            double x1,
            double y1,
            double x2,
            double y2
    ) {

        try {

            content.moveTo(
                    (float)x1,
                    (float)y1
            );

            content.lineTo(
                    (float)x2,
                    (float)y2
            );

            content.stroke();

        }
        catch(IOException e) {

            throw new RuntimeException(e);

        }

    }


    @Override
    public void drawText(
            String text,
            double x,
            double y
    ) {

        try {

            content.beginText();

            content.setNonStrokingColor(
                    0,
                    0,
                    0
            );

            content.setFont(
                    font,
                    12
            );

            float fontHeight =
                    (font.getFontDescriptor().getCapHeight()
                            / 1000)
                            * 12;

            float centeredY =
                    (float)(y + fontHeight / 2);

            float textWidth =
                    font.getStringWidth(text) / 1000 * 12;


            float centeredX =
                    (float)(x - textWidth / 2);


            content.setTextMatrix(
                    new Matrix(
                            1,
                            0,
                            0,
                            -1,
                            centeredX,
                            centeredY
                    )
            );


            content.showText(text);

            content.endText();

        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void fillRect(
            Color color,
            double x,
            double y,
            double width,
            double height
    ) {

        try {

            content.setNonStrokingColor(
                    (float)color.getRed(),
                    (float)color.getGreen(),
                    (float)color.getBlue()
            );


            content.addRect(
                    (float)x,
                    (float)y,
                    (float)width,
                    (float)height
            );


            content.fill();

        }
        catch(IOException e) {

            throw new RuntimeException(e);

        }

    }
}