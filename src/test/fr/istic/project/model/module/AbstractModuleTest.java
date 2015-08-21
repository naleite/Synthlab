package fr.istic.project.model.module;

import com.jsyn.unitgen.UnitGenerator;
import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.out.JSynOut;
import fr.istic.project.model.module.out.Out;
import fr.istic.project.model.module.port.JSynPort;
import fr.istic.project.model.module.vcoa.JSynVCOA;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractModuleTest {

    private JSynPort portIn;
    private JSynPort portOut;
    private JSynPort portTest;
    private Module module1;
    private Module module2;
    @Before
    public void setUp() throws Exception {
        module1 = new JSynVCOA();
        module2 = new JSynOut();
        module1.addInput("in");
        module2.addOutput("out");
        portIn = module1.getInputByLabel("in");
        portOut = module2.getOutputByLabel("out");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInputByLabelNull() throws Exception {
        module1.getInputByLabel(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInputByLabelNotContains() throws Exception {
        module1.getInputByLabel("in2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOutputByLabelNull() throws Exception {
        module1.getOutputByLabel(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOutputByLabelNotContains() throws Exception {
        module1.getOutputByLabel("out8");
    }

    /**
     * Test method isAvailableInput(String label)
     * @throws Exception
     */
    @Test
    public void testIsAvailableInput() throws Exception {
        assertEquals(module1.isAvailableInput("in"), true);
        portIn.connect(portOut);
        assertEquals(module1.isAvailableInput("in"), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAvailableInputNull() throws Exception {
        module1.isAvailableInput(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAvailableInputNotContains() throws Exception {
        module1.isAvailableInput("in4");
    }

    /**
     * Test method isAvailableOutput(String label)
     * @throws Exception
     */
    @Test
    public void testIsAvailableOutput() throws Exception {
        assertEquals(module2.isAvailableOutput("out"), true);
        portIn.connect(portOut);
        assertEquals(module2.isAvailableOutput("out"), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAvailableOutputNull() throws Exception {
        module2.isAvailableOutput(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAvailableOutputNotContains() throws Exception {
        module2.isAvailableOutput("out4");
    }

    /**
     * Test method addInput(String label)
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddInputNull() throws Exception {
        module1.addInput(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInputLabelContains() throws Exception {
        module1.addInput("in");
    }

    /**
     * Test metod addOutput(String label)
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddOutputNull() throws Exception {
        module2.addOutput(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOutputLabelContains() throws Exception {
        module1.addOutput("out");
    }

    /**
     * Test method addEntity(UnitGenerator entity)
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddEntity() throws Exception {
        module1.addEntity(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEntities() throws Exception {
        UnitGenerator[] entities = new UnitGenerator[0];
        module1.addEntities(entities);
    }

    /**
     * Test method reset()
     * @throws Exception
     */
    @Test
    public void testReset() throws Exception {
        portIn.connect(portOut);
        assertEquals(portIn.isConnected(), true);
        assertEquals(portOut.isConnected(), true);
        module1.reset();
        assertEquals(portIn.isConnected(), false);
        assertEquals(portOut.isConnected(), false);
    }

    /**
     * Test method setPosition(Point2D point)
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetPositionNull() throws Exception {
        module1.setPosition(null);
    }

    /**
     * Test method setMemento(Memento m)
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMementoNull() throws Exception {
        module1.setMemento(null);
    }
}