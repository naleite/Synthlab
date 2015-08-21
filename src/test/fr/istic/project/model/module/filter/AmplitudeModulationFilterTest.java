package fr.istic.project.model.module.filter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AmplitudeModulationFilterTest {

    private AmplitudeModulationFilter ampMF;

    private double coef;

    /**
     * Generate an AmplitudeModulationFilter object and a double for the amplitude coefficient.
     */
    @Before
    public void setUp() {
        // for testGetAndSetCoeff
        this.coef = 2.0;
        ampMF = new AmplitudeModulationFilter(coef);

    }

    /**
     * Function that tests getter and setter for the AmplitudeModulationFilter object and
     * test if negative value for the aplitude is possible or not.
     *
     * @throws Exception
     */
    @Test
    public void testGetAndSetCoeff() throws Exception {
        // test Get
        assertTrue(ampMF.getCoeff() == 2.0);

        // test Set
        ampMF.setCoeff(4.0);
        assertTrue(ampMF.getCoeff() != 2.0);

        // test negative coefficient value
        ampMF.setCoeff(-6.0);
        assertTrue(ampMF.getCoeff() == -6.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotGenerate() {
        ampMF.generate(-1, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotGenerate2() {
        ampMF.generate(1, -5);
    }

}