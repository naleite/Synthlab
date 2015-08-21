package fr.istic.project.model.module.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class FilterAttenuationTest {

    private FilterAttenuation filterAttenuation;

    @Before
    public void setUp() {
        filterAttenuation = new FilterAttenuation(0);

    }
    @Test
    public void testInitialisation()
    {
        Assert.assertNotNull(filterAttenuation.getAttenuation());
        Assert.assertEquals(filterAttenuation.getAttenuation(), 1.0, 0.0); //car 0db <-> 1ampl
    }

    @Test
    public void testSetAttenuation()
    {
        filterAttenuation.setAttenuationDB(6);
        Assert.assertEquals(2,filterAttenuation.getAttenuation(), 0.1);// 6db <-> 2ampl

        filterAttenuation.setAttenuationDB(-6);
        Assert.assertEquals(0.5,filterAttenuation.getAttenuation(), 0.1);//6db <-> 0.5ampl

        filterAttenuation.setAttenuation(1.0);
        Assert.assertEquals(1.0,filterAttenuation.getAttenuation(), 0.1);

        filterAttenuation.setAttenuation(2.0);
        Assert.assertEquals(2.0,filterAttenuation.getAttenuation(), 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotGenerate() {
        filterAttenuation.generate(-1, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotGenerate2() {
        filterAttenuation.generate(1, -5);
    }

}