package org.raven;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.api.RectangleInsets;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.pie.PieLabelLinkStyle;
import org.jfree.chart.plot.pie.PiePlot;
import org.jfree.chart.swing.ChartPanel;
import org.jfree.data.general.PieDataset;
import org.raven.data.SampleData;
import org.raven.utils.paint.FlatColorPaint;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class PieChartDemo extends DemoFrame {

    public PieChartDemo() {
        setLayout(new MigLayout("al center center"));
        add(createChartPanel());
    }

    private JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Product Sales Distribution",
                dataset,
                true,
                true,
                false);

        FlatColorPaint foreground = new FlatColorPaint("Label.foreground");
        FlatColorPaint borderColor = new FlatColorPaint("Component.borderColor");
        FlatColorPaint labelBackground = new FlatColorPaint("Button.background");
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Laptop", Color.decode("#fb7185"));
        plot.setSectionPaint("Smartphone", Color.decode("#60a5fa"));
        plot.setSectionPaint("Tablet", Color.decode("#2dd4bf"));
        plot.setSectionPaint("Smartwatch", Color.decode("#fb923c"));
        plot.setLabelPaint(foreground);
        plot.setLabelBackgroundPaint(labelBackground);
        plot.setLabelLinkStyle(PieLabelLinkStyle.STANDARD);
        plot.setLabelLinkPaint(foreground.alpha(0.5f));
        plot.setLabelOutlinePaint(borderColor);
        plot.setLabelPadding(new RectangleInsets(5, 5, 5, 5));
        plot.setExplodePercent("Tablet", 0.2);
        plot.setLegendItemShape(new Ellipse2D.Float(0, 0, 12, 12));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({1}, {2})"));
        plot.setToolTipGenerator(null);
        plot.setSectionOutlinesVisible(false);
        return chart;
    }

    private JPanel createChartPanel() {
        JFreeChart chart = createChart(SampleData.createPieDataset());
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatDarculaLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new PieChartDemo().setVisible(true));
    }
}
