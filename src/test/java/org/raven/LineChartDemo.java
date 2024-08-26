package org.raven;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.api.RectangleInsets;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBezierRenderer;
import org.jfree.chart.swing.ChartMouseEvent;
import org.jfree.chart.swing.ChartMouseListener;
import org.jfree.chart.swing.ChartPanel;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.raven.data.SampleData;
import org.raven.utils.Utils;
import org.raven.utils.paint.FlatColorPaint;
import org.raven.utils.MultiXYTextAnnotation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LineChartDemo extends DemoFrame {

    public LineChartDemo() {
        setLayout(new MigLayout("insets 20,al center center"));
        setSize(new Dimension(800, 400));
        add(createChartPanel());
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Legal & General Unit Trust Prices",   // title
                null,                                       // x-axis label
                null,                                       // y-axis label
                dataset);

        Color foreground = UIManager.getColor("Label.foreground");
        XYPlot plot = (XYPlot) chart.getPlot();

        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        DateAxis domain = (DateAxis) plot.getDomainAxis();
        range.setAxisLineVisible(false);
        range.setTickMarksVisible(false);
        range.setUpperMargin(0.2);
        range.setLowerMargin(0.1);

        domain.setAxisLineVisible(false);
        domain.setTickMarksVisible(false);

        plot.setDomainPannable(true);
        float dash[] = {4f};
        plot.setDomainGridlineStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5f, dash, 0f));
        plot.setRangeGridlinesVisible(false);
        plot.setInsets(new RectangleInsets(4, 8, 15, 8));

        XYBezierRenderer renderer = new XYBezierRenderer(10, 25);
        initSeriesStyle(renderer, 0, Color.decode("#10b981"));
        initSeriesStyle(renderer, 1, Color.decode("#737373"));

        renderer.setDefaultItemLabelPaint(foreground);
        plot.setRenderer(renderer);
        return chart;
    }

    private void initSeriesStyle(XYBezierRenderer renderer, int series, Color color) {
        renderer.setSeriesPaint(series, color);
        renderer.setSeriesStroke(series, new BasicStroke(1.5f));
        renderer.setSeriesShape(series, new Ellipse2D.Float(-4, -4, 8, 8));
        renderer.setSeriesFillPaint(series, createGradient(color));
        renderer.setSeriesOutlinePaint(series, new Color(255, 255, 255, 180));
        renderer.setSeriesOutlineStroke(series, new BasicStroke(2f));
        renderer.setUseOutlinePaint(true);
    }

    private Paint createGradient(Color color) {
        Color startColor = Utils.alphaColor(color, 0.5f);
        Color endColor = Utils.alphaColor(color, 0);
        return new GradientPaint(0f, 0f, startColor, 0f, 1f, endColor);
    }

    private JPanel createChartPanel() {
        JFreeChart chart = createChart(SampleData.createTimeSeriesDataset());
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setFillZoomRectangle(false);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setRangeZoomable(false);
        createAnnotation(chartPanel);
        chartPanel.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:15,5,5,5,$Component.borderColor,1,20");
        return chartPanel;
    }

    private void createAnnotation(ChartPanel chartPanel) {
        XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
        MultiXYTextAnnotation annotation = new MultiXYTextAnnotation();

        DateFormat titleFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        FlatColorPaint background = new FlatColorPaint("Panel.background");
        FlatColorPaint foreground = new FlatColorPaint("Label.foreground");
        FlatColorPaint border = new FlatColorPaint("Component.borderColor");
        Font font = UIManager.getFont("Label.font");
        annotation.setBackgroundPaint(background);
        annotation.setDefaultPaint(foreground);
        annotation.setFont(font);
        annotation.setOutlinePaint(border);
        annotation.setTitleLinePain(border);
        annotation.setTitleGenerator(xValue -> titleFormat.format(new Date((long) xValue)));

        plot.addAnnotation(annotation);
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                Rectangle2D dataArea = chartPanel.getScreenDataArea();
                if (!dataArea.contains(event.getTrigger().getPoint())) {
                    annotation.setLabels(null);
                    return;
                }
                double x = plot.getDomainAxis().java2DToValue(event.getTrigger().getX(), dataArea, plot.getDomainAxisEdge());
                TimeSeriesCollection dataset = (TimeSeriesCollection) plot.getDataset();
                int seriesCount = plot.getSeriesCount();

                double minDistance = Double.MAX_VALUE;
                double closestX = 0;
                boolean found = false;

                for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                    for (int itemIndex = 0; itemIndex < plot.getDataset().getItemCount(seriesIndex); itemIndex++) {
                        double dataX = dataset.getXValue(seriesIndex, itemIndex);
                        double distance = Math.abs(x - dataX);
                        if (distance < minDistance) {
                            minDistance = distance;
                            closestX = dataX;
                            found = true;
                        }
                    }
                }
                if (found) {
                    annotation.autoCalculateX(closestX, dataset);
                }
            }
        });
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatIntelliJLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new LineChartDemo().setVisible(true));
    }
}
