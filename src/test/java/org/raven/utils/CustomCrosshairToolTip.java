package org.raven.utils;

import org.jfree.chart.api.RectangleInsets;
import org.jfree.chart.plot.Crosshair;
import org.raven.utils.paint.FlatColorPaint;

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
        FlatColorPaint background = new FlatColorPaint("ToolTip.background");
        FlatColorPaint foreground = new FlatColorPaint("ToolTip.foreground");
        FlatColorPaint border = new FlatColorPaint("Component.borderColor");
        Font font = UIManager.getFont("ToolTip.font");
        setLabelBackgroundPaint(background.alpha(0.7f));
        setLabelPaint(foreground);
        setLabelOutlinePaint(border);
        setLabelFont(font);
        setLabelPadding(new RectangleInsets(3, 5, 3, 5));
        setPaint(foreground.alpha(0.5f));
        setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0));
    }
}
