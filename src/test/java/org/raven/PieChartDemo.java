package org.raven;

import com.formdev.flatlaf.FlatIntelliJLaf;
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
import org.raven.utils.ThemesUtils;

import javax.swing.*;
import java.awt.*;

public class PieChartDemo extends JFrame {

    public PieChartDemo() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("al center center"));
        ThemesUtils.initThemes();
        add(createChartPanel());
    }

    private JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Product Sales Distribution",
                dataset,
                true,
                true,
                false);

        Color foreground = UIManager.getColor("Label.foreground");
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Laptop", Color.decode("#fb7185"));
        plot.setSectionPaint("Smartphone", Color.decode("#60a5fa"));
        plot.setSectionPaint("Tablet", Color.decode("#2dd4bf"));
        plot.setSectionPaint("Smartwatch", Color.decode("#fb923c"));
        plot.setLabelPaint(foreground);
        plot.setLabelBackgroundPaint(new Color(234, 234, 234));
        plot.setLabelLinkStyle(PieLabelLinkStyle.STANDARD);
        plot.setLabelLinkPaint(new Color(130, 130, 130));
        plot.setLabelOutlinePaint(null);
        plot.setLabelPadding(new RectangleInsets(5, 5, 5, 5));
        plot.setExplodePercent("Tablet", 0.2);
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
        FlatIntelliJLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new PieChartDemo().setVisible(true));
    }
}
