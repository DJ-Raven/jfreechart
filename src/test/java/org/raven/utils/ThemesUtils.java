package org.raven.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;
import org.raven.utils.paint.FlatColorPaint;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public class ThemesUtils {

    public static void initThemes() {
        StandardChartTheme theme = new StandardChartTheme("JFree/Shadow", true);

        // init value
        FlatColorPaint background = new FlatColorPaint("Panel.background");
        FlatColorPaint foreground = new FlatColorPaint("Label.foreground");
        FlatColorPaint border = new FlatColorPaint("Component.borderColor");
        Font font = UIManager.getFont("Label.font");

        // chart
        theme.setChartBackgroundPaint(background);
        theme.setBaselinePaint(Color.red);

        // plot
        theme.setPlotBackgroundPaint(background);
        theme.setPlotOutlinePaint(background);

        theme.setDomainGridlinePaint(border);
        theme.setRangeGridlinePaint(border);

        theme.setAxisLabelPaint(foreground);
        theme.setTickLabelPaint(foreground);
        theme.setTitlePaint(foreground);

        // legend
        theme.setLegendItemPaint(foreground);
        theme.setLegendBackgroundPaint(background);

        // font
        theme.setExtraLargeFont(font.deriveFont(20f));
        theme.setLargeFont(font.deriveFont(14f));
        theme.setRegularFont(font);
        theme.setSmallFont(font.deriveFont(10f));

        ChartFactory.setChartTheme(theme);
    }
}
