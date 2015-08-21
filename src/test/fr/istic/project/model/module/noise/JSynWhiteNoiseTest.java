package fr.istic.project.model.module.noise;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSynWhiteNoiseTest {

    private JSynWhiteNoise noise;

    @Before
    public void setUp() throws Exception {
        noise = new JSynWhiteNoise();
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(noise.toString(), "WhiteNoise");
    }
}