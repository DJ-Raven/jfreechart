package org.raven.utils;

import org.jfree.chart.api.RectangleInsets;
import org.jfree.chart.plot.Crosshair;

import javax.swing.*;
import java.awt.*;

/**
 * This class custom crosshair base on the flatlaf toolTip style
 *
 * @author Raven
 */
public class CustomCrosshairToolTip extends Crosshair {

    public CustomCrosshairToolTip() {
        init();
    }

    private void init() {
        Color background = UIManager.getColor("ToolTip.background");
        Color foreground = UIManager.getColor("ToolTip.foreground");
        Font font = UIManager.getFont("ToolTip.font");
        setLabelBackgroundPaint(new Color(background.getRed(), background.getGreen(), background.getBlue(), 200));
        setLabelPaint(foreground);
        setLabelOutlineVisible(false);
        setLabelFont(font);
        setLabelPadding(new RectangleInsets(3, 5, 3, 5));
        setPaint(new Color(foreground.getRed(), foreground.getGreen(), foreground.getBlue(), 150));
        setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0));
    }
}
