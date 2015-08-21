package fr.istic.project.model.module.seq;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SequenceGeneratorTest {
    
    private static final double EPSILON = 1e-15;
    private SequenceGenerator seqGen;
    
    @Before
    public void setUp() {
        double[] values = new double[] {0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
        seqGen = new SequenceGenerator(values);
    }

    @Test
    public void testGetValue() throws Exception {
        assertEquals(seqGen.getValue(0), 0.5, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValue_OverUpperBound() throws Exception {
        seqGen.setValue(0, Seq.MAX_VALUE + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValue_UnderLowerBound() throws Exception {
        seqGen.setValue(0, Seq.MIN_VALUE - 1);
    }
}
