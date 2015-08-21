package fr.istic.project.model.module.filter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VCAFilterTest {

    private static final double EPSILON = 1e-15;
    private VCAFilter vcaFilter;

    @Before
    public void setUp() throws Exception {
        vcaFilter = new VCAFilter(0);
    }

    @Test
    public void testSetAmplitudeCoef() throws Exception {
        assertEquals(vcaFilter.getAmplitudeCoef(), 0, EPSILON);

        vcaFilter.setAmplitudeCoef(5);
        assertEquals(vcaFilter.getAmplitudeCoef(), 5, EPSILON);

        vcaFilter.setAmplitudeCoef(10);
        assertEquals(vcaFilter.getAmplitudeCoef(), 10, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotGenerate() throws Exception {
        vcaFilter.generate(-1,5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotGenerate2() throws Exception {
        vcaFilter.generate(1,-5);
    }
}