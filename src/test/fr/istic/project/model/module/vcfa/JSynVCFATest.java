package fr.istic.project.model.module.vcfa;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.UnitGenerator;
import fr.istic.project.model.memento.Memento;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSynVCFATest {
    
    private static final double EPSILON = 1e-15;
    private VCFA vcfa;
    
    @Before
    public void setUp() {
        vcfa = new JSynVCFA();
        Synthesizer synthesizer = JSyn.createSynthesizer();
        for (UnitGenerator u : vcfa.getCircuit()) {
            synthesizer.add(u);
        }
        synthesizer.start();
        vcfa.setActivated(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCutoffFrequency_OverUpperBound() throws Exception {
        vcfa.setCutoffFrequency(VCFA.MAX_FREQ + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCutoffFrequency_UnderLowerBound() throws Exception {
        vcfa.setCutoffFrequency(VCFA.MIN_FREQ - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGain_OverUpperBound() throws Exception {
        vcfa.setGain(VCFA.MAX_GAIN + 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGain_UnderLowerBound() throws Exception {
        vcfa.setGain(VCFA.MIN_GAIN - 0.01);
    }

    @Test
    public void testGetMemento() throws Exception {
        Memento memento = vcfa.getMemento();
        assertEquals((double) memento.getData().get("cutoffFreq"), vcfa.getCutoffFrequency(), EPSILON);
        assertEquals((double) memento.getData().get("gain"), vcfa.getGain(), EPSILON);
    }

    @Test
    public void testSetMemento() throws Exception {
        Memento memento = vcfa.getMemento();
        memento.getData().put("x", 10.0);
        memento.getData().put("y", 10.0);
        memento.getData().put("cutoffFreq", 100.0);
        memento.getData().put("gain", 1.1);
        vcfa.setMemento(memento);
        
        assertEquals(vcfa.getPosition().getX(), 10.0, EPSILON);
        assertEquals(vcfa.getPosition().getY(), 10.0, EPSILON);
        assertEquals(vcfa.getCutoffFrequency(), 100.0, EPSILON);
        assertEquals(vcfa.getGain(), 1.1, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMemento_Null() throws Exception {
        vcfa.setMemento(null);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(vcfa.toString(), "VCFA");
    }
}
