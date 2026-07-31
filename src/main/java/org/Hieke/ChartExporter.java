package org.Hieke;

public class ChartExporter {

    private final KnittingChart chart;
    private final ChartRenderer renderer;


    public ChartExporter(KnittingChart chart) {

        this.chart = chart;
        this.renderer = new ChartRenderer(chart);

    }


    public RenderSettings createFullChartSettings() {

        return new RenderSettings(
                30,
                chart.getRows(),
                chart.getColumns()
        );
    }
}