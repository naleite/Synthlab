package fr.istic.project.model.module.filter;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FrequencyModulationFilterTest {

    // FrequencyModulationFilter declaration
    private FrequencyModulationFilter freqMF;

    // Coefficient
    private double k;

    // Frequency
    private double fo;

    /**
     * FrequencyModulationFilter instantiation
     */
    @Before
    public void setUp() {
        this.k = 1.0;
        this.fo = 500.0;

        this.freqMF = new FrequencyModulationFilter();
        this.freqMF.setK(this.k);
        this.freqMF.setRefFrequency(this.fo);
    }

    /**
     * GetK() function test
     *
     * @throws Exception
     */
    @Test
    public void testGetK() throws Exception {
        assertEquals(this.freqMF.getK(), 1.0, 0.1);
    }

    /**
     * GetFo() function test
     *
     * @throws Exception
     */
    @Test
    public void testGetFo() throws Exception {
        assertTrue(this.freqMF.getRefFrequency() == 500.0);
    }

    /**
     * setFo(double value) function test
     *
     * @throws Exception
     */
    @Test
    public void testSetFo() throws Exception {
        this.freqMF.setRefFrequency(300.0);
        assertTrue(this.freqMF.getRefFrequency() != 500.0);
        assertTrue(this.freqMF.getRefFrequency() == 300.0);
    }

    /**
     * setFo(double value) function test
     *
     * @throws Exception
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSetFoThrow() throws Exception {

        this.freqMF.setRefFrequency(-100.0);
        assertTrue(this.freqMF.getRefFrequency() != 300.0);
        assertTrue(this.freqMF.getRefFrequency() == -100.0);
    }

    /**
     * setK(double value) function  test
     *
     * @throws Exception
     */
    @Test
    public void testSetK() throws Exception {
        this.freqMF.setK(2.0);
        assertTrue(this.freqMF.getK() != 1.0);
        assertTrue(this.freqMF.getK() == 2.0);

        this.freqMF.setK(-15.0);
        assertTrue(this.freqMF.getK() != 2.0);
        assertTrue(this.freqMF.getK() == -15.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotGenerate() {
        freqMF.generate(-1, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotGenerate2() {
        freqMF.generate(1, -5);
    }
}