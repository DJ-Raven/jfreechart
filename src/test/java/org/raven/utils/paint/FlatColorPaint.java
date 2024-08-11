package org.raven.utils.paint;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.ColorFunctions;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

public class FlatColorPaint implements Paint {

    private Color light;
    private Color dark;
    private String colorUIKey;
    private float alpha;

    public FlatColorPaint(FlatColorPaint flatColorPaint) {
        if (flatColorPaint.colorUIKey == null) {
            light = flatColorPaint.light;
            dark = flatColorPaint.dark;
        } else {
            colorUIKey = flatColorPaint.colorUIKey;
        }
        alpha = flatColorPaint.alpha;
    }

    public FlatColorPaint(Color light, Color dark) {
        this(light, dark, 1f);
    }

    public FlatColorPaint(Color light, Color dark, float alpha) {
        this.light = light;
        this.dark = dark;
        this.alpha = alpha;
    }

    public FlatColorPaint(String colorUIKey) {
        this(colorUIKey, 1f);
    }

    public FlatColorPaint(String colorUIKey, float alpha) {
        this.colorUIKey = colorUIKey;
        this.alpha = alpha;
    }

    public FlatColorPaint alpha(float alpha) {
        FlatColorPaint obj = new FlatColorPaint(this);
        obj.alpha = alpha;
        return obj;
    }

    @Override
    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
        return getColor().createContext(cm, deviceBounds, userBounds, xform, hints);
    }

    @Override
    public int getTransparency() {
        return getColor().getTransparency();
    }

    private Color getColor() {
        Color color;
        if (colorUIKey != null) {
            color = FlatUIUtils.getUIColor(colorUIKey,"");
        } else {
            color = FlatLaf.isLafDark() ? dark : light;
        }
        if (alpha < 1f) {
            color = ColorFunctions.fade(color, alpha);
        }
        return color;
    }
}
