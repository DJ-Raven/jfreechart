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
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.swing.ChartMouseEvent;
import org.jfree.chart.swing.ChartMouseListener;
import org.jfree.chart.swing.ChartPanel;
import org.jfree.chart.swing.CrosshairOverlay;
import org.jfree.data.xy.*;
import org.raven.data.SampleData;
import org.raven.renderer.CustomCandlestickRenderer;
import org.raven.utils.CustomCrosshairToolTip;
import org.raven.utils.lebel.DateCrosshairLabelGenerator;
import org.raven.utils.ThemesUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class DemoCandlestickChart extends JFrame {

    public DemoCandlestickChart() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(800, 400));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("al center center"));
        ThemesUtils.initThemes();
        add(createChartPanel());
    }

    private JFreeChart createChart(OHLCDataset dataset) {
        JFreeChart chart = ChartFactory.createCandlestickChart(
                "Candlestick",   // title
                null,                 // x-axis label
                null,                 // y-axis label
                dataset,
                false);

        XYPlot plot = (XYPlot) chart.getPlot();
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        DateAxis domain = (DateAxis) plot.getDomainAxis();
        range.setAutoRangeIncludesZero(false);
        range.setLowerMargin(0.15);
        domain.setUpperMargin(0.1);
        CandlestickRenderer renderer = new CustomCandlestickRenderer();

        renderer.setDownPaint(new Color(241, 89, 89));
        renderer.setUpPaint(new Color(37, 176, 127));
        renderer.setCandleWidth(8);
        renderer.setDefaultToolTipGenerator(null);
        renderer.setDrawVolume(false);
        plot.setRenderer(renderer);
        return chart;
    }

    private JPanel createChartPanel() {
        OHLCDataset dataset = SampleData.createOhlcDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setFillZoomRectangle(false);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.addOverlay(createXCrosshair(chartPanel, dataset));
        return chartPanel;
    }

    private CrosshairOverlay createXCrosshair(ChartPanel chartPanel, OHLCDataset dataset) {
        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        CustomCrosshairToolTip xCrosshair = new CustomCrosshairToolTip();
        CustomCrosshairToolTip yCrosshair = new CustomCrosshairToolTip();
        xCrosshair.setLabelAnchor(RectangleAnchor.BOTTOM);
        yCrosshair.setLabelAnchor(RectangleAnchor.RIGHT);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        xCrosshair.setLabelGenerator(new DateCrosshairLabelGenerator("{0}", dateFormat));
        yCrosshair.setLabelGenerator(new StandardCrosshairLabelGenerator("{0}", NumberFormat.getNumberInstance()));
        xCrosshair.setLabelVisible(true);
        yCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);

        XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
        int seriesCount = plot.getSeriesCount();

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
                JFreeChart chart = chartPanel.getChart();
                XYPlot plot = (XYPlot) chart.getPlot();
                double x = plot.getDomainAxis().java2DToValue(event.getTrigger().getX(), dataArea, plot.getDomainAxisEdge());
                double y = plot.getRangeAxis().java2DToValue(event.getTrigger().getY(), dataArea, plot.getRangeAxisEdge());

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
                }
                yCrosshair.setValue(y);
            }
        });
        return crosshairOverlay;
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatDarculaLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        EventQueue.invokeLater(() -> new DemoCandlestickChart().setVisible(true));
    }
}
