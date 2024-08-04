package org.raven.utils;

import java.awt.*;

public class Utils {

    public static Color alphaColor(Color color, float alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255f));
    }
}
