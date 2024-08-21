package org.raven;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCrosshairLabelGenerator;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBezierRenderer;
import org.jfree.chart.swing.ChartMouseEvent;
import org.jfree.chart.swing.ChartMouseListener;
import org.jfree.chart.swing.ChartPanel;
import org.jfree.chart.swing.CrosshairOverlay;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.raven.data.SampleData;
import org.raven.utils.CustomCrosshairToolTip;
import org.raven.utils.lebel.DateCrosshairLabelGenerator;
import org.raven.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class Demo extends DemoFrame {

    public Demo() {
        setLayout(new MigLayout("al center center"));
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
        range.setLowerMargin(0.15);
        domain.setUpperMargin(0.1);

        plot.setDomainPannable(true);
        XYBezierRenderer renderer = new XYBezierRenderer(1, 25, XYBezierRenderer.FillType.TO_ZERO);
        initSeriesStyle(renderer, 0, new Color(37, 148, 107));
        initSeriesStyle(renderer, 1, new Color(37, 93, 148));

        renderer.setDefaultItemLabelPaint(foreground);
        plot.setRenderer(renderer);
        return chart;
    }

    private void initSeriesStyle(XYBezierRenderer renderer, int series, Color color) {
        renderer.setSeriesPaint(series, color);
        renderer.setSeriesStroke(series, new BasicStroke(2.5f));
        renderer.setSeriesShape(series, new Ellipse2D.Float(-5, -5, 10, 10));
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
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setRangeZoomable(false);
        chartPanel.addOverlay(createXCrosshair(chartPanel));
        return chartPanel;
    }

    private CrosshairOverlay createXCrosshair(ChartPanel chartPanel) {
        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        CustomCrosshairToolTip xCrosshair = new CustomCrosshairToolTip();
        xCrosshair.setLabelAnchor(RectangleAnchor.BOTTOM);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        xCrosshair.setLabelGenerator(new DateCrosshairLabelGenerator("{0}", dateFormat));
        xCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);

        XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
        TimeSeriesCollection dataset = (TimeSeriesCollection) plot.getDataset();
        int seriesCount = plot.getSeriesCount();

        Crosshair[] yCrosshirs = new Crosshair[seriesCount];
        for (int i = 0; i < yCrosshirs.length; i++) {
            yCrosshirs[i] = new CustomCrosshairToolTip();
            yCrosshirs[i].setLabelVisible(true);
            yCrosshirs[i].setLabelAnchor(RectangleAnchor.RIGHT);
            yCrosshirs[i].setLabelGenerator(new StandardCrosshairLabelGenerator("{0}", NumberFormat.getNumberInstance()));
            crosshairOverlay.addRangeCrosshair(yCrosshirs[i]);
        }

        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent chartMouseEvent) {
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                Rectangle2D dataArea = chartPanel.getScreenDataArea();
                if (!dataArea.contains(event.getTrigger().getPoint())) {
                    return;
                }
                double x = plot.getDomainAxis().java2DToValue(event.getTrigger().getX(), dataArea, plot.getDomainAxisEdge());

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
                    xCrosshair.setValue(closestX);
                    for (int i = 0; i < seriesCount; i++) {
                        double y = DatasetUtils.findYValue(plot.getDataset(), i, closestX);
                        yCrosshirs[i].setValue(y);
                    }
                }
            }
        });
        return crosshairOverlay;
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatDarculaLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new Demo().setVisible(true));
    }
}
