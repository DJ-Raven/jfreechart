package org.raven.renderer;

import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.OHLCDataset;

import java.awt.*;

/**
 * This class that customizes the appearance of candlesticks
 * in a candlestick chart by ensuring the body and border colors are the same.
 *
 * @author Raven
 */
public class CustomCandlestickRenderer extends CandlestickRenderer {

    @Override
    public Paint getItemPaint(int row, int column) {
        OHLCDataset highLowData = (OHLCDataset) getPlot().getDataset();
        double yOpen = highLowData.getOpenValue(row, column);
        double yClose = highLowData.getCloseValue(row, column);
        boolean isUpCandle = yClose > yOpen;
        if (isUpCandle) {
            return getUpPaint();
        } else {
            return getDownPaint();
        }
    }
}
