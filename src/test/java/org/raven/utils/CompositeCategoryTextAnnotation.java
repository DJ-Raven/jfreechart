package org.raven.utils;

import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CompositeCategoryTextAnnotation extends CategoryTextAnnotation {

    private CategoryTextAnnotation[] annotations;

    public CompositeCategoryTextAnnotation(Value[] values, CategoryTextAnnotation... annotations) {
        super(values[0].getText(), values[0].getCategory(), values[0].getValue());
        this.annotations = annotations;
        initAnnotation(values);
    }

    @Override
    public void draw(Graphics2D g2, CategoryPlot plot, Rectangle2D dataArea, CategoryAxis domainAxis, ValueAxis rangeAxis) {
        super.draw(g2, plot, dataArea, domainAxis, rangeAxis);
        if (annotations != null) {
            for (CategoryTextAnnotation annotation : annotations) {
                annotation.draw(g2, plot, dataArea, domainAxis, rangeAxis);
            }
        }
    }

    public void setValue(Value... values) {
        Value value = values[0];
        setText(value.getText());
        setCategory(value.getCategory());
        setValue(value.getValue());
        initAnnotation(values);
    }

    private void initAnnotation(Value... values) {
        for (int i = 1; i < values.length; i++) {
            Value value = values[i];
            annotations[i - 1].setText(value.getText());
            annotations[i - 1].setCategory(value.getCategory());
            annotations[i - 1].setValue(value.getValue());
        }
    }

    public static class Value {

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Comparable getCategory() {
            return category;
        }

        public void setCategory(Comparable category) {
            this.category = category;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public Value(String text, Comparable category, double value) {
            this.text = text;
            this.category = category;
            this.value = value;
        }

        private String text;
        private Comparable category;
        private double value;
    }
}
