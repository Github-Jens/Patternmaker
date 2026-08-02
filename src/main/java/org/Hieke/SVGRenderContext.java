package org.Hieke;

import javafx.scene.paint.Color;

public class SVGRenderContext implements RenderContext {

    private final StringBuilder svg;
    private String strokeColor = "#000000";


    public SVGRenderContext(
            double width,
            double height
    ) {

        svg = new StringBuilder();

        svg.append(
                "<svg xmlns=\"http://www.w3.org/2000/svg\" "
        );

        svg.append(
                "width=\"" + width + "\" "
        );

        svg.append(
                "height=\"" + height + "\">"
        );

    }


    @Override
    public void setStroke(String color) {

        this.strokeColor = color;

    }


    @Override
    public void setFill(String color) {

        svg.append(
                "<!-- fill "
                        + color
                        + " -->"
        );

    }


    @Override
    public void drawLine(
            double x1,
            double y1,
            double x2,
            double y2
    ) {

        svg.append(
                "<line "
        );

        svg.append(
                "x1=\"" + x1 + "\" "
        );

        svg.append(
                "y1=\"" + y1 + "\" "
        );

        svg.append(
                "x2=\"" + x2 + "\" "
        );

        svg.append(
                "y2=\"" + y2 + "\" "
        );

        svg.append(
                "stroke=\"" + strokeColor + "\"/>"
        );

    }


    @Override
    public void drawText(
            String text,
            double x,
            double y
    ) {

        svg.append(
                "<text "
        );

        svg.append(
                "x=\"" + x + "\" "
        );

        svg.append(
                "y=\"" + y + "\" "
        );

        svg.append(
                "text-anchor=\"middle\" "
        );

        svg.append(
                "dominant-baseline=\"central\">"
        );

        svg.append(text);

        svg.append(
                "</text>"
        );

    }


    @Override
    public void fillRect(
            Color color,
            double x,
            double y,
            double width,
            double height
    ) {

        svg.append(
                "<rect "
        );

        svg.append(
                "x=\"" + x + "\" "
        );

        svg.append(
                "y=\"" + y + "\" "
        );

        svg.append(
                "width=\"" + width + "\" "
        );

        svg.append(
                "height=\"" + height + "\" "
        );

        svg.append(
                "fill=\"" + colorToHex(color) + "\"/>"
        );

    }


    public String finish() {

        svg.append("</svg>");

        return svg.toString();

    }
    private String colorToHex(Color color) {

        return String.format(
                "#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255)
        );
    }
}