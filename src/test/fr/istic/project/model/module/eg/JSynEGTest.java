package fr.istic.project.model.module.eg;

import com.jsyn.unitgen.EnvelopeDAHDSR;
import fr.istic.project.model.memento.Memento;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSynEGTest {

    private static final double EPSILON = 1e-15;
    private EG eg;
    private EnvelopeDAHDSR env;

    @Before
    public void setUp() throws Exception {
        eg = new JSynEG();
        env = ((JSynEG) eg).getEnvelopeDAHDSR();
    }

    @Test
    public void testInit() throws Exception {
        assertEquals(eg.getAttack(), 0.0, EPSILON);
        assertEquals(eg.getDecay(), 0.0, EPSILON);
        assertEquals(eg.getSustain(), 0.0, EPSILON);
        assertEquals(eg.getRelease(), 0.0, EPSILON);

        assertEquals(env.attack.get(), 0.0, EPSILON);
        assertEquals(env.decay.get(), 0.0, EPSILON);
        assertEquals(env.sustain.get(), 0.0, EPSILON);
        assertEquals(env.release.get(), 0.0, EPSILON);
    }

    @Test
    public void testDecayChanged() throws Exception {
        eg.setDecay(1.0);
        assertEquals(eg.getDecay(), 1.0, EPSILON);
        assertEquals(env.decay.get(), 1.0, EPSILON);
    }

    @Test
    public void testAttackChanged() throws Exception {
        eg.setAttack(1.0);
        assertEquals(eg.getAttack(), 1.0, EPSILON);
    }

    @Test
    public void testSustainChanged() throws Exception {
        eg.setSustain(1.0);
        assertEquals(eg.getSustain(), 1.0, EPSILON);
    }

    @Test
    public void testReleaseChanged() throws Exception {
        eg.setRelease(1.0);
        assertEquals(eg.getRelease(), 1.0, EPSILON);
        assertEquals(env.release.get(), 1.0, EPSILON);
    }

    @Test
    public void testGetMemento() throws Exception {
        Memento memento = eg.getMemento();
        assertEquals((double) memento.getData().get("attack"), eg.getAttack(), EPSILON);
        assertEquals((double) memento.getData().get("decay"), eg.getDecay(), EPSILON);
        assertEquals((double) memento.getData().get("sustain"), eg.getSustain(), EPSILON);
        assertEquals((double) memento.getData().get("release"), eg.getRelease(), EPSILON);
    }

    @Test
    public void testSetMemento() throws Exception {
        Memento memento = eg.getMemento();
        memento.getData().put("x", 10.0);
        memento.getData().put("y", 10.0);
        memento.getData().put("attack", 1.0);
        memento.getData().put("decay", 1.0);
        memento.getData().put("sustain", 1.0);
        memento.getData().put("release", 1.0);
        eg.setMemento(memento);
        
        assertEquals(eg.getPosition().getX(), 10.0, EPSILON);
        assertEquals(eg.getPosition().getY(), 10.0, EPSILON);
        assertEquals(eg.getAttack(), 1.0, EPSILON);
        assertEquals(eg.getDecay(), 1.0, EPSILON);
        assertEquals(eg.getSustain(), 1.0, EPSILON);
        assertEquals(eg.getRelease(), 1.0, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMemento_Null() throws Exception {
        eg.setMemento(null);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(eg.toString(), "EG");
    }

    @After
    public void verifyHoldAndDelay() throws Exception {
        //verify that we do not modify hold and delay value
        assertEquals(env.delay.get(), 0.0, EPSILON);//always equals to 0
        assertEquals(env.hold.get(), 0.0, EPSILON);//always equals to 0
    }
}