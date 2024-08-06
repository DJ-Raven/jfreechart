package org.raven;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.swing.ChartMouseEvent;
import org.jfree.chart.swing.ChartMouseListener;
import org.jfree.chart.swing.ChartPanel;
import org.jfree.data.category.CategoryDataset;
import org.raven.data.SampleData;
import org.raven.utils.ThemesUtils;
import org.raven.utils.Utils;

import javax.swing.*;
import java.awt.*;

public class BarChartDemo extends JFrame {

    public BarChartDemo() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 400));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("al center center"));
        ThemesUtils.initThemes();
        add(createChartPanel());
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Antidepressant Medication Usage",     // title
                null,                                       // x-axis label
                null,                                       // y-axis label
                dataset);

        Color foreground = UIManager.getColor("Label.foreground");
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRangePannable(true);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        initSeriesStyle(renderer, 0, Color.decode("#10b981"));
        initSeriesStyle(renderer, 1, Color.decode("#0ea5e9"));
        initSeriesStyle(renderer, 2, Color.decode("#6366f1"));
        initSeriesStyle(renderer, 3, Color.decode("#ec4899"));

        renderer.setBarPainter(new StandardBarPainter());
        renderer.setMaximumBarWidth(0.02);
        renderer.setDefaultItemLabelPaint(foreground);
        renderer.setDefaultToolTipGenerator(null);

        plot.setRenderer(renderer);
        return chart;
    }

    private void initSeriesStyle(BarRenderer renderer, int series, Color color) {
        renderer.setSeriesPaint(series, createGradient(Utils.alphaColor(color, 0.7f)));
        renderer.setSeriesOutlinePaint(series, color);
        renderer.setDrawBarOutline(true);
    }

    private Paint createGradient(Color color) {
        Color endColor = Utils.alphaColor(color, 0.3f);
        return new GradientPaint(0f, 0f, endColor, 0f, 1f, color);
    }

    private JPanel createChartPanel() {
        JFreeChart chart = createChart(SampleData.getCategoryDataset());
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        createAnnotation(chartPanel);
        return chartPanel;
    }

    private void createAnnotation(ChartPanel chartPanel) {

        CategoryPlot plot = (CategoryPlot) chartPanel.getChart().getPlot();

        chartPanel.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                ChartEntity entity = event.getEntity();
                if (entity instanceof CategoryItemEntity) {
                    CategoryItemEntity categoryEntity = (CategoryItemEntity) entity;
                    String category = categoryEntity.getColumnKey().toString();
                    double value = categoryEntity.getDataset().getValue(categoryEntity.getRowKey(), categoryEntity.getColumnKey()).doubleValue();
                    String text = String.format("%s: %.2f", categoryEntity.getRowKey().toString(), value);
                    annotation = initAnnotation(text, category, value);
                }
            }

            private CategoryTextAnnotation annotation;

            private CategoryTextAnnotation initAnnotation(String text, Comparable category, double value) {
                if (annotation == null) {
                    annotation = new CategoryTextAnnotation(text, category, value);
                    annotation.setFont(UIManager.getFont("Label.font"));
                    annotation.setPaint(UIManager.getColor("Label.foreground"));
                    plot.addAnnotation(annotation);
                } else {
                    annotation.setText(text);
                    annotation.setCategory(category);
                    annotation.setValue(value);
                }
                return annotation;
            }
        });
    }


    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatIntelliJLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new BarChartDemo().setVisible(true));
    }
}
