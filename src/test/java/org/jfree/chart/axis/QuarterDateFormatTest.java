/* ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * Project Info:  https://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * --------------------------
 * QuarterDateFormatTest.java
 * --------------------------
 * (C) Copyright 2005-present, by David Gilbert and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   -;
 *
 */

package org.jfree.chart.axis;

import java.util.TimeZone;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link QuarterDateFormat} class.
 */
public class QuarterDateFormatTest {

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        QuarterDateFormat qf1 = new QuarterDateFormat(TimeZone.getTimeZone(
                "GMT"), new String[] {"1", "2", "3", "4"});
        QuarterDateFormat qf2 = new QuarterDateFormat(TimeZone.getTimeZone(
                "GMT"), new String[] {"1", "2", "3", "4"});
        assertEquals(qf1, qf2);
        assertEquals(qf2, qf1);

        qf1 = new QuarterDateFormat(TimeZone.getTimeZone("PST"),
                new String[] {"1", "2", "3", "4"});
        assertNotEquals(qf1, qf2);
        qf2 = new QuarterDateFormat(TimeZone.getTimeZone("PST"),
                new String[] {"1", "2", "3", "4"});
        assertEquals(qf1, qf2);

        qf1 = new QuarterDateFormat(TimeZone.getTimeZone("PST"),
                new String[] {"A", "2", "3", "4"});
        assertNotEquals(qf1, qf2);
        qf2 = new QuarterDateFormat(TimeZone.getTimeZone("PST"),
                new String[] {"A", "2", "3", "4"});
        assertEquals(qf1, qf2);

        qf1 = new QuarterDateFormat(TimeZone.getTimeZone("PST"),
                new String[] {"A", "2", "3", "4"}, true);
        assertNotEquals(qf1, qf2);
        qf2 = new QuarterDateFormat(TimeZone.getTimeZone("PST"),
                new String[] {"A", "2", "3", "4"}, true);
        assertEquals(qf1, qf2);
    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashCode() {
        QuarterDateFormat qf1 = new QuarterDateFormat(TimeZone.getTimeZone(
                "GMT"), new String[] {"1", "2", "3", "4"});
        QuarterDateFormat qf2 = new QuarterDateFormat(TimeZone.getTimeZone(
                "GMT"), new String[] {"1", "2", "3", "4"});
        assertEquals(qf1, qf2);
        int h1 = qf1.hashCode();
        int h2 = qf2.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() {
        QuarterDateFormat qf1 = new QuarterDateFormat(TimeZone.getTimeZone(
                "GMT"), new String[] {"1", "2", "3", "4"});
        QuarterDateFormat qf2 = null;
        qf2 = (QuarterDateFormat) qf1.clone();
        assertNotSame(qf1, qf2);
        assertSame(qf1.getClass(), qf2.getClass());
        assertEquals(qf1, qf2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() {
        QuarterDateFormat qf1 = new QuarterDateFormat(TimeZone.getTimeZone(
                "GMT"), new String[] {"1", "2", "3", "4"});
        QuarterDateFormat qf2 = TestUtils.serialised(qf1);
        assertEquals(qf1, qf2);
    }

}
