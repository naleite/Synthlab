package fr.istic.project.model.module.filter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ControlVoltageFilterTest {
    
    private static final double EPSILON = 1e-15;
    private ControlVoltageFilter cvFilter;
    
    @Before
    public void setUp() {
        cvFilter = new ControlVoltageFilter();
    }

    @Test
    public void testGetSetOctave() throws Exception {
        cvFilter.setOctave(1);
        assertEquals(cvFilter.getOctave(), 1, EPSILON);
    }

    @Test
    public void testGetSetSemitone() throws Exception {
        cvFilter.setSemitone(5);
        assertEquals(cvFilter.getSemitone(), 5, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_NegativeStart() throws Exception {
        cvFilter.generate(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_NegativeLimit() throws Exception {
        cvFilter.generate(1, -1);
    }

    @Test
    public void testGenerate() throws Exception {
        cvFilter.setOctave(2);
        cvFilter.setSemitone(0);
        cvFilter.generate(0, 1);
        
        double[] outputs = cvFilter.output.getValues();
        for (int i = 0; i < 1; i++) {
            assertEquals(outputs[i], 1.0, EPSILON);
        }
    }
}
