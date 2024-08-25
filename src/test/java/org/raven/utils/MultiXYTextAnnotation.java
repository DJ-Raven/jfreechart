package org.raven.utils;

import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.api.RectangleInsets;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.internal.Args;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextUtils;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;

public class MultiXYTextAnnotation extends AbstractXYAnnotation {

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        Args.nullNotPermitted(font, "font");
        this.font = font;
        fireAnnotationChanged();
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        Args.nullNotPermitted(paint, "paint");
        this.paint = paint;
        fireAnnotationChanged();
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public void setBackgroundPaint(Paint backgroundPaint) {
        this.backgroundPaint = backgroundPaint;
        fireAnnotationChanged();
    }

    public boolean isOutlineVisible() {
        return outlineVisible;
    }

    public void setOutlineVisible(boolean outlineVisible) {
        this.outlineVisible = outlineVisible;
        fireAnnotationChanged();
    }

    public Paint getOutlinePaint() {
        return outlinePaint;
    }

    public void setOutlinePaint(Paint paint) {
        Args.nullNotPermitted(paint, "paint");
        this.outlinePaint = paint;
        fireAnnotationChanged();
    }

    public Paint getGridlinePaint() {
        return gridlinePaint;
    }

    public void setGridlinePaint(Paint paint) {
        Args.nullNotPermitted(paint, "paint");
        this.gridlinePaint = paint;
        fireAnnotationChanged();
    }

    public Stroke getOutlineStroke() {
        return outlineStroke;
    }

    public void setOutlineStroke(Stroke stroke) {
        Args.nullNotPermitted(stroke, "stroke");
        this.outlineStroke = stroke;
        fireAnnotationChanged();
    }

    public double getGap() {
        return gap;
    }

    public void setGap(double gap) {
        this.gap = gap;
        fireAnnotationChanged();
    }

    public double getVerticalTextGap() {
        return verticalTextGap;
    }

    public void setVerticalTextGap(double verticalTextGap) {
        this.verticalTextGap = verticalTextGap;
        fireAnnotationChanged();
    }

    public RectangleInsets getPadding() {
        return padding;
    }

    public void setPadding(RectangleInsets padding) {
        Args.nullNotPermitted(padding, "padding");
        this.padding = padding;
        fireAnnotationChanged();
    }

    public RectangleInsets getSeriesPadding() {
        return seriesPadding;
    }

    public void setSeriesPadding(RectangleInsets padding) {
        Args.nullNotPermitted(padding, "padding");
        this.seriesPadding = padding;
        fireAnnotationChanged();
    }

    public double getRound() {
        return round;
    }

    public void setRound(double round) {
        this.round = round;
        fireAnnotationChanged();
    }

    public double getSeriesSize() {
        return seriesSize;
    }

    public void setSeriesSize(double seriesSize) {
        this.seriesSize = seriesSize;
        fireAnnotationChanged();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        fireAnnotationChanged();
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        fireAnnotationChanged();
    }

    public Label[] getLabels() {
        return labels;
    }

    public void setLabels(Label[] labels) {
        this.labels = labels;
        fireAnnotationChanged();
    }

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        Args.nullNotPermitted(numberFormat, "numberFormat");
        this.numberFormat = numberFormat;
        fireAnnotationChanged();
    }

    private Font font;
    private Paint paint;
    private Paint backgroundPaint;
    private boolean outlineVisible;
    private Paint outlinePaint;
    private Paint gridlinePaint;
    private Stroke outlineStroke;
    private double gap;
    private double verticalTextGap;
    private RectangleInsets padding;
    private RectangleInsets seriesPadding;
    private double round;
    private double seriesSize;
    private double x;
    private double y;
    private Label[] labels;
    private NumberFormat numberFormat;

    public MultiXYTextAnnotation() {
        this.paint = XYTextAnnotation.DEFAULT_PAINT;
        this.font = XYTextAnnotation.DEFAULT_FONT;
        this.backgroundPaint = new Color(255, 255, 255, 200);
        this.outlinePaint = new Color(190, 190, 190);
        this.gridlinePaint = new Color(25, 104, 148);
        this.outlineVisible = true;
        this.numberFormat = NumberFormat.getNumberInstance();
        this.padding = new RectangleInsets(10, 10, 10, 10);
        this.seriesPadding = new RectangleInsets(3, 2, 3, 10);
        this.outlineStroke = new BasicStroke(0.5f);
        this.round = 10;
        this.seriesSize = 10;
        this.gap = 10;
        this.verticalTextGap = 5;
    }

    public void autoCalculateX(double x, XYDataset dataset) {
        if (this.x != x) {
            this.x = x;
            createValues(dataset);
        }
    }

    private void createValues(XYDataset dataset) {
        int seriesCount = dataset.getSeriesCount();
        Label[] labels = new Label[seriesCount];
        double value = DatasetUtils.findYValue(dataset, 0, this.x);
        double closestY = value;
        labels[0] = new Label(dataset.getSeriesKey(0).toString(), getNumberFormat().format(value));
        for (int i = 1; i < seriesCount; i++) {
            double y = DatasetUtils.findYValue(dataset, i, this.x);
            String v = getNumberFormat().format(y);
            String t = dataset.getSeriesKey(i).toString();
            labels[i] = new Label(t, v);
            closestY = Math.max(closestY, y);
        }
        y = closestY;
        this.labels = labels;
        fireAnnotationChanged();
    }

    @Override
    public void draw(Graphics2D g2, XYPlot plot, Rectangle2D dataArea, ValueAxis domainAxis, ValueAxis rangeAxis, int rendererIndex, PlotRenderingInfo info) {
        if (labels != null) {
            PlotOrientation orientation = plot.getOrientation();
            RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(plot.getDomainAxisLocation(), orientation);
            RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(plot.getRangeAxisLocation(), orientation);

            float anchorX = (float) domainAxis.valueToJava2D(this.getX(), dataArea, domainEdge);
            float anchorY = (float) rangeAxis.valueToJava2D(this.getY(), dataArea, rangeEdge);

            if (orientation == PlotOrientation.HORIZONTAL) {
                float tempAnchor = anchorX;
                anchorX = anchorY;
                anchorY = tempAnchor;
            }
            g2.setFont(getFont());
            Rectangle2D bgRec = getBackgroundRectangle(g2, anchorX, anchorY);
            // adjust annotation
            if (!dataArea.contains(bgRec)) {
                int shadow = 5;
                int space = 2;
                double x = bgRec.getX();
                double y = bgRec.getY();
                double width = bgRec.getWidth();
                double height = bgRec.getHeight();
                if (x < dataArea.getX() + space) {
                    x = dataArea.getX() + space;
                } else if (x + width > dataArea.getX() + dataArea.getWidth() - shadow) {
                    x = dataArea.getX() + dataArea.getWidth() - width - shadow;
                }
                if (y < dataArea.getY() + space) {
                    y = dataArea.getY() + space;
                } else if (y + height > dataArea.getY() + dataArea.getHeight() - shadow) {
                    y = dataArea.getY() + dataArea.getHeight() - height - shadow;
                }
                bgRec = new Rectangle2D.Double(x, y, width, height);
            }
            Shape shape = round > 0 ?
                    new RoundRectangle2D.Double(bgRec.getX(), bgRec.getY(), bgRec.getWidth(), bgRec.getHeight(), round, round) :
                    bgRec;

            g2.setStroke(plot.getDomainGridlineStroke());
            g2.setPaint(getGridlinePaint());
            g2.draw(new Line2D.Double(anchorX, dataArea.getY(), anchorX, dataArea.getY() + dataArea.getHeight()));

            if (getBackgroundPaint() != null) {
                g2.setPaint(getBackgroundPaint());
                g2.fill(shape);
            }
            float x = (float) (bgRec.getX() + getSeriesSizeWidth() + getPadding().getLeft());
            float y = (float) (bgRec.getY() + getPadding().getTop());
            float x2 = (float) (bgRec.getX() + bgRec.getWidth() - getPadding().getRight());
            float seriesX = (float) (bgRec.getX() + getPadding().getLeft());
            for (int i = 0; i < labels.length; i++) {
                Label label = labels[i];
                float size = (float) drawLabel(g2, x, y, x2, label);
                drawSeries(g2, seriesX, y, size, plot.getRenderer().getSeriesPaint(i));
                y += size + getVerticalTextGap();
            }
            if (isOutlineVisible()) {
                g2.setStroke(getOutlineStroke());
                g2.setPaint(getOutlinePaint());
                g2.draw(shape);
            }
            String toolTip = getToolTipText();
            String url = getURL();
            if (toolTip != null || url != null) {
                addEntity(info, shape, rendererIndex, toolTip, url);
            }
        }
    }

    protected double drawLabel(Graphics2D g2, float x, float y, float x2, Label label) {
        g2.setPaint(getPaint());
        double textHeight = TextUtils.drawAlignedString(label.getText(), g2, x, y, TextAnchor.TOP_LEFT).getHeight();
        double valueHeight = TextUtils.drawAlignedString(label.getValue(), g2, x2, y, TextAnchor.TOP_RIGHT).getHeight();
        return Math.max(textHeight, valueHeight);
    }

    protected void drawSeries(Graphics2D g2, float x, float y, float height, Paint paint) {
        double size = Math.min(height - (getSeriesPadding().getTop() + getSeriesPadding().getBottom()), getSeriesSize());
        double lx = x + (getSeriesSize() - size) / 2f;
        double ly = y + ((height - size) / 2);
        g2.setPaint(paint);
        g2.fill(new Ellipse2D.Double(lx, ly, size, size));
    }

    protected Rectangle2D getBackgroundRectangle(Graphics2D g2, float anchorX, float anchorY) {
        if (labels == null || labels.length == 0) return null;

        double textWidth = 0, valueWidth = 0, totalHeight = 0;
        FontMetrics fm = g2.getFontMetrics();
        for (int i = 0; i < labels.length; i++) {
            Label label = labels[i];
            Rectangle2D textBounds = TextUtils.getTextBounds(label.getText(), g2, fm);
            Rectangle2D valueBounds = TextUtils.getTextBounds(label.getValue(), g2, fm);
            totalHeight += Math.max(textBounds.getHeight(), valueBounds.getHeight());
            textWidth = Math.max(textWidth, textBounds.getWidth());
            valueWidth = Math.max(valueWidth, valueBounds.getWidth());
        }
        if (labels.length > 1) {
            totalHeight += (getVerticalTextGap() * (labels.length - 1));
        }
        totalHeight += getPadding().getTop() + getPadding().getBottom();
        double totalWidth = textWidth + valueWidth + getSeriesSizeWidth() + getGap() + getPadding().getLeft() + getPadding().getRight();
        double space = 10;
        return new Rectangle2D.Double(anchorX - space, anchorY - totalHeight - space, totalWidth, totalHeight);
    }

    private double getSeriesSizeWidth() {
        return getSeriesSize() + getSeriesPadding().getLeft() + getSeriesPadding().getRight();
    }

    public static class Label {

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Label(String text, String value) {
            this.text = text;
            this.value = value;
        }

        private String text;
        private String value;
    }
}
