package fr.istic.project.model.module.mix;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.PassThrough;
import com.jsyn.unitgen.UnitGenerator;
import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.port.JSynPort;
import fr.istic.project.model.module.vcoa.JSynVCOA;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSynMixTest {

    private static final double EPSILON = 1e-15;
    private JSynMix jsynMix;

    /**
     * Initialisation before each test
     */
    @Before
    public void setUp() {
        try {
            jsynMix = new JSynMix();
            
            // Connect in1 with a test output
            JSynPort p = new JSynPort(new JSynVCOA(), "testOutput", new PassThrough().output);
            jsynMix.getInputByLabel("in1").connect(p);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        Synthesizer synthesizer = JSyn.createSynthesizer();
        for (UnitGenerator u : jsynMix.getCircuit()) {
            synthesizer.add(u);
        }
        synthesizer.start();
        jsynMix.setActivated(true);
    }

    @Test
    public void testGainIn1_Accessor() {
        jsynMix.setGainIn1(4.0);
        assertEquals(jsynMix.getGainIn1(), 4.0, EPSILON);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetGainIn1_OverUpperBound() {
        jsynMix.setGainIn1(Mix.MAX_ATTENUATION + 1);
    }

    @Test
    public void testGainIn2_Accessor() {
        jsynMix.setGainIn2(4.0);
        assertEquals(jsynMix.getGainIn2(), 4.0, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGainIn2_OverUpperBound() {
        jsynMix.setGainIn2(Mix.MAX_ATTENUATION + 1);
    }
    
    @Test
    public void testGainIn3_Accessor() {
        jsynMix.setGainIn3(4.0);
        assertEquals(jsynMix.getGainIn3(), 4.0, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGainIn3_OverUpperBound() {
        jsynMix.setGainIn3(Mix.MAX_ATTENUATION + 1);
    }

    @Test
    public void testGainIn4_Accessor() {
        jsynMix.setGainIn4(4.0);
        assertEquals(jsynMix.getGainIn4(), 4.0, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGainIn4_OverUpperBound() {
        jsynMix.setGainIn4(Mix.MAX_ATTENUATION + 1);
    }

    @Test
    public void testGetOut_InitialValue() {
        assertEquals(jsynMix.getOut(), 0, EPSILON);
    }

    @Test
    public void testOut_Accessor() {
        jsynMix.setOut(10);
        assertEquals(jsynMix.getOut(), 10, EPSILON);
    }

    @Test
    public void testGetMemento() {
        Memento memento = jsynMix.getMemento();
        assertEquals((double) memento.getData().get("x"), jsynMix.getPosition().getX(), EPSILON);
        assertEquals((double) memento.getData().get("y"), jsynMix.getPosition().getY(), EPSILON);
        assertEquals((double) memento.getData().get("gainIn1"), jsynMix.getGainIn1(), EPSILON);
        assertEquals((double) memento.getData().get("gainIn2"), jsynMix.getGainIn2(), EPSILON);
        assertEquals((double) memento.getData().get("gainIn3"), jsynMix.getGainIn3(), EPSILON);
        assertEquals((double) memento.getData().get("gainIn4"), jsynMix.getGainIn4(), EPSILON);
        assertEquals((double) memento.getData().get("out"), jsynMix.getOut(), EPSILON);
    }

    @Test
    public void testSetMemento() {
        Memento memento = jsynMix.getMemento();
        memento.getData().put("x", 10.0);
        memento.getData().put("y", 10.0);
        memento.getData().put("gainIn1", 1.0);
        memento.getData().put("gainIn2", 2.0);
        memento.getData().put("gainIn3", 3.0);
        memento.getData().put("gainIn4", 4.0);
        memento.getData().put("out", 5.0);
        jsynMix.setMemento(memento);
        
        assertEquals(jsynMix.getPosition().getX(), 10.0, EPSILON);
        assertEquals(jsynMix.getPosition().getY(), 10.0, EPSILON);
        assertEquals(jsynMix.getGainIn1(), 1.0, EPSILON);
        assertEquals(jsynMix.getGainIn1(), 1.0, EPSILON);
        assertEquals(jsynMix.getGainIn2(), 2.0, EPSILON);
        assertEquals(jsynMix.getGainIn3(), 3.0, EPSILON);
        assertEquals(jsynMix.getGainIn4(), 4.0, EPSILON);
        assertEquals(jsynMix.getOut(), 5.0, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMemento_Null() {
        jsynMix.setMemento(null);
    }

    @Test
    public void testToString() {
        assertEquals(jsynMix.toString(), "Mix");
    }
}