package fr.istic.project.model.module.filter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KeyBoardGateFilterTest {
    
    private static final double EPSILON = 1e-15;
    private KeyBoardGateFilter gateFilter;
    
    @Before
    public void setUp() {
        gateFilter = new KeyBoardGateFilter(false);
    }

    @Test
    public void testIsNoteOn() throws Exception {
        assertFalse(gateFilter.isNoteOn());
        assertEquals(gateFilter.getTension(), 0, EPSILON);
    }

    @Test
    public void testSetNoteOn_True() throws Exception {
        gateFilter.setNoteOn(true);
        assertTrue(gateFilter.isNoteOn());
        assertEquals(gateFilter.getTension(), 5, EPSILON);
    }

    @Test
    public void testSetNoteOn_False() throws Exception {
        gateFilter.setNoteOn(false);
        assertFalse(gateFilter.isNoteOn());
        assertEquals(gateFilter.getTension(), -5, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_NegativeStart() throws Exception {
        gateFilter.generate(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_NegativeLimit() throws Exception {
        gateFilter.generate(1, -1);
    }

    @Test
    public void testGenerate_NoteOn() throws Exception {
        gateFilter.setNoteOn(true);
        gateFilter.generate(0, 1);
        
        double[] outputs = gateFilter.output.getValues();
        for (int i = 0; i < 1; i++) {
            assertEquals(outputs[i], 5, EPSILON);
        }
    }

    @Test
    public void testGenerate_NoteOff() throws Exception {
        gateFilter.setNoteOn(false);
        gateFilter.generate(0, 1);

        double[] outputs = gateFilter.output.getValues();
        for (int i = 0; i < 1; i++) {
            assertEquals(outputs[i], -5, EPSILON);
        }
    }
}
