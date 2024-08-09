package org.raven;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.swing.ChartMouseEvent;
import org.jfree.chart.swing.ChartMouseListener;
import org.jfree.chart.swing.ChartPanel;
import org.jfree.chart.text.TextAnchor;
import org.jfree.data.category.CategoryDataset;
import org.raven.data.SampleData;
import org.raven.utils.CompositeCategoryTextAnnotation;
import org.raven.utils.Utils;

import javax.swing.*;
import java.awt.*;

public class BarChartDemo extends DemoFrame {

    public BarChartDemo() {
        setLayout(new MigLayout("al center center"));
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

                    annotation = initAnnotation(getValues(categoryEntity));
                }
            }

            private CompositeCategoryTextAnnotation.Value[] getValues(CategoryItemEntity entity) {
                CategoryDataset dataset = entity.getDataset();
                int count = dataset.getRowCount();
                CompositeCategoryTextAnnotation.Value[] values = new CompositeCategoryTextAnnotation.Value[count];
                for (int i = 0; i < count; i++) {
                    Comparable row = dataset.getRowKey(i);
                    Comparable category = entity.getColumnKey();
                    double value = dataset.getValue(row, category).doubleValue();
                    String text = String.format("%s: %.2f", row.toString(), value);
                    values[i] = new CompositeCategoryTextAnnotation.Value(text, category.toString(), value);
                }
                return values;
            }

            private CompositeCategoryTextAnnotation annotation;

            private CompositeCategoryTextAnnotation initAnnotation(CompositeCategoryTextAnnotation.Value[] values) {
                if (annotation == null) {
                    CategoryTextAnnotation[] annotations = new CategoryTextAnnotation[values.length - 1];
                    for (int i = 0; i < annotations.length; i++) {
                        annotations[i] = new CategoryTextAnnotation(values[i + 1].getText(), values[i + 1].getCategory(), values[i + 1].getValue());
                        applyAnnotationStyle(annotations[i]);
                    }
                    annotation = new CompositeCategoryTextAnnotation(values, annotations);
                    applyAnnotationStyle(annotation);
                    plot.addAnnotation(annotation);
                } else {
                    annotation.setValue(values);
                }
                return annotation;
            }

            private void applyAnnotationStyle(CategoryTextAnnotation annotation) {
                annotation.setFont(UIManager.getFont("Label.font"));
                annotation.setPaint(UIManager.getColor("Label.foreground"));
                annotation.setTextAnchor(TextAnchor.BASELINE_LEFT);
                annotation.setCategoryAnchor(CategoryAnchor.START);
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
