package fr.istic.project.model.module.vcfb;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.UnitGenerator;
import fr.istic.project.model.memento.Memento;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSynVCFBTest {
    
    private static final double EPSILON = 1e-15;
    private VCFB vcfb;
    
    @Before
    public void setUp() {
        vcfb = new JSynVCFB();
        Synthesizer synthesizer = JSyn.createSynthesizer();
        for (UnitGenerator u : vcfb.getCircuit()) {
            synthesizer.add(u);
        }
        synthesizer.start();
        vcfb.setActivated(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCutoffFrequency_OverUpperBound() throws Exception {
        vcfb.setCutoffFrequency(VCFB.MAX_FREQ + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCutoffFrequency_UnderLowerBound() throws Exception {
        vcfb.setCutoffFrequency(VCFB.MIN_FREQ - 1);
    }

    @Test
    public void testGetMemento() throws Exception {
        Memento memento = vcfb.getMemento();
        assertEquals((double) memento.getData().get("cutoffFreq"), vcfb.getCutoffFrequency(), EPSILON);
    }

    @Test
    public void testSetMemento() throws Exception {
        Memento memento = vcfb.getMemento();
        memento.getData().put("x", 10.0);
        memento.getData().put("y", 10.0);
        memento.getData().put("cutoffFreq", 100.0);
        vcfb.setMemento(memento);
        
        assertEquals(vcfb.getPosition().getX(), 10.0, EPSILON);
        assertEquals(vcfb.getPosition().getY(), 10.0, EPSILON);
        assertEquals(vcfb.getCutoffFrequency(), 100.0, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMemento_Null() throws Exception {
        vcfb.setMemento(null);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(vcfb.toString(), "VCFB");
    }
}
