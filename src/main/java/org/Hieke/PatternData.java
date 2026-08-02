package org.Hieke;

/*
 * Container class for all data that belongs to a knitting pattern.
 *
 * Currently a pattern consists of:
 * - The knitting chart (stitches, rows, columns)
 * - The colour palette used by the pattern
 *
 * In the future this class can also contain:
 * - Legend settings
 * - Pattern metadata
 * - Author information
 * - Notes
 * - Other project settings
 *
 * Keeping these together makes saving and loading the complete project easier.
 */
public class PatternData {

    // The actual knitting chart containing all stitches
    private final KnittingChart chart;

    // The colour palette used in the pattern
    private final Palette palette;


    /*
     * Creates a complete pattern data object.
     *
     * @param chart  the knitting chart data
     * @param palette the colours used by the pattern
     */
    public PatternData(
            KnittingChart chart,
            Palette palette
    ) {

        this.chart = chart;
        this.palette = palette;

    }


    /*
     * Returns the knitting chart.
     */
    public KnittingChart getChart() {

        return chart;

    }


    /*
     * Returns the colour palette.
     */
    public Palette getPalette() {

        return palette;

    }

}