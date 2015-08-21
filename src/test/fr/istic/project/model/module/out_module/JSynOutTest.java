package fr.istic.project.model.module.out_module;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.UnitGenerator;
import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.out.JSynOut;
import fr.istic.project.model.module.out.Out;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSynOutTest {
    
    private static final double EPSILON = 1e-15;
    private Out jsynOut;

    @Before
    public void setUp() {
        jsynOut = new JSynOut();
        Synthesizer synthesizer = JSyn.createSynthesizer();
        for (UnitGenerator u : jsynOut.getCircuit()) {
            synthesizer.add(u);
        }
        synthesizer.start();
        jsynOut.setActivated(true);
    }

    @Test
    public void testGetFilter() throws Exception {
        jsynOut.getFilter();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAttenuation_OverUpperBound() throws Exception {
        jsynOut.setAttenuation(Out.MAX_ATTENUATION + 1);
    }

    @Test
    public void testAttenuation_Accessor() throws Exception {
        jsynOut.setAttenuation(10.0);
        assertEquals(jsynOut.getAttenuation(), 10.0, EPSILON);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(jsynOut.toString(), "Out");
    }

    @Test
    public void testGetMemento() throws Exception {
        Memento memento = jsynOut.getMemento();
        assertEquals((double) memento.getData().get("attenuation"), jsynOut.getAttenuation(), EPSILON);
    }

    @Test
    public void testSetMemento() throws Exception {
        Memento memento = jsynOut.getMemento();
        memento.getData().put("x", 10.0);
        memento.getData().put("y", 10.0);
        memento.getData().put("attenuation", 10.0);
        jsynOut.setMemento(memento);
        
        assertEquals(jsynOut.getPosition().getX(), 10.0, EPSILON);
        assertEquals(jsynOut.getPosition().getY(), 10.0, EPSILON);
        assertEquals(jsynOut.getAttenuation(), 10.0, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMemento_Null() throws Exception {
        jsynOut.setMemento(null);
    }
}
