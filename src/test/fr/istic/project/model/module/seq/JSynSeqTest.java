package fr.istic.project.model.module.seq;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.UnitGenerator;
import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.observable.PropertyType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JSynSeqTest {
    
    private static final double EPSILON = 1e-15;
    private Seq seq;
    
    @Before
    public void setUp() {
        seq = new JSynSeq();
        Synthesizer synthesizer = JSyn.createSynthesizer();
        for (UnitGenerator u : seq.getCircuit()) {
            synthesizer.add(u);
        }
        synthesizer.start();
        seq.setActivated(true);
        seq.resetStep();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValue_ForbiddenPropertyType() throws Exception {
        seq.getValue(PropertyType.BOARD_ACTIVATED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValue_ForbiddenPropertyType() throws Exception {
        seq.setValue(PropertyType.BOARD_ACTIVATED, 0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValue_OverUpperBound() throws Exception {
        seq.setValue(PropertyType.LEVEL1_CHANGED, Seq.MAX_VALUE + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValue_UnderLowerBound() throws Exception {
        seq.setValue(PropertyType.LEVEL1_CHANGED, Seq.MIN_VALUE - 1);
    }
    
    @Test
    public void testGetSetValue() throws Exception {
        for (PropertyType type : seq.getProperties().keySet()) {
            seq.setValue(type, 0.5);
            assertEquals(seq.getValue(type), 0.5, EPSILON);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValues_Null() throws Exception {
        seq.setValues(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValues_TooMuchValues() throws Exception {
        double[] values = new double[seq.getProperties().keySet().size() + 1];
        for (int i = 0; i < values.length; i++) {
            values[i] = 0.5;
        }
        seq.setValues(values);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetValues_NotEnoughValues() throws Exception {
        double[] values = new double[seq.getProperties().keySet().size() - 1];
        for (int i = 0; i < values.length; i++) {
            values[i] = 0.5;
        }
        seq.setValues(values);
    }

    @Test
    public void testGetSetValues() throws Exception {
        double[] values = new double[seq.getProperties().keySet().size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = 0.5;
        }
        seq.setValues(values);
        assertTrue(Arrays.equals(seq.getValues(), values));
    }

    @Test
    public void testGetMemento() throws Exception {
        Memento memento = seq.getMemento();
        for (PropertyType type : seq.getProperties().keySet()) {
            assertEquals((double) memento.getData().get(type.toString()), seq.getValue(type), EPSILON);
        }
    }

    @Test
    public void testSetMemento() throws Exception {
        Memento memento = seq.getMemento();
        memento.getData().put("x", 10.0);
        memento.getData().put("y", 10.0);
        for (PropertyType type : seq.getProperties().keySet()) {
            memento.getData().put(type.toString(), 0.5);
        }
        seq.setMemento(memento);

        assertEquals(seq.getPosition().getX(), 10.0, EPSILON);
        assertEquals(seq.getPosition().getY(), 10.0, EPSILON);
        for (PropertyType type : seq.getProperties().keySet()) {
            assertEquals(seq.getValue(type), 0.5, EPSILON);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMemento_Null() throws Exception {
        seq.setMemento(null);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(seq.toString(), "Sequencer");
    }
}
