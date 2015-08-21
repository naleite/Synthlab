package fr.istic.project.model.module.port;

import com.jsyn.ports.ConnectableInput;
import com.jsyn.ports.ConnectableOutput;
import com.jsyn.unitgen.PassThrough;
import fr.istic.project.model.module.Module;
import fr.istic.project.model.module.out.JSynOut;
import fr.istic.project.model.module.vcoa.JSynVCOA;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSynPortTest {

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
    public void testConstructorInput_NullLabel() throws Exception {
        portTest = new JSynPort(module1, null, new PassThrough().input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorOutput_NullLabel() throws Exception {
        portTest = new JSynPort(module1, null, new PassThrough().output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullInput() throws Exception {
        ConnectableInput connectableInput = null;
        portTest = new JSynPort(module1, "in2", connectableInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullOutput() throws Exception {
        ConnectableOutput connectableOutput = null;
        portTest = new JSynPort(module1, "out2", connectableOutput);
    }

    @Test
    public void testGetModule() throws Exception {
        assertEquals(portIn.getModule(), module1);
        assertEquals(portOut.getModule(), module2);
    }

    @Test
    public void testIsConnected() throws Exception {
        portIn.connect(portOut);
        assertEquals(portIn.isConnected(), true);
        assertEquals(portOut.isConnected(), true);
    }

    @Test
    public void testCanConnect_NullPort() throws Exception {
        assertEquals(portIn.canConnect(portTest), false);
    }

    @Test
    public void testCanConnect_PortsFromSameModule() throws Exception {
        portIn.connect(portOut);
        module1.addOutput("out2");
        portTest = module1.getOutputByLabel("out2");
        assertEquals(portTest.canConnect(portIn), false);
    }

    @Test
    public void testCanConnect_TwoOutputs() throws Exception {
        module1.addOutput("out2");
        portTest = module1.getOutputByLabel("out2");
        assertEquals(portOut.canConnect(portTest), false);
    }

    @Test
    public void testCanConnect_ConnectiblePorts() throws Exception {
        assertEquals(portIn.canConnect(portOut), true);
        assertEquals(portOut.canConnect(portIn), true);
    }

    @Test
    public void testCanConnect_PortAlreadyConnected() throws Exception {
        portIn.connect(portOut);
        assertEquals(portIn.canConnect(portOut), false);
        assertEquals(portOut.canConnect(portIn), false);
    }

    @Test
    public void testGetConnected() throws Exception {
        portIn.connect(portOut);
        assertEquals(portOut.getConnected(), portIn);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetConnected_DisconnectedPort() {
        portIn.getConnected();
    }

    @Test
    public void testGetLabel() throws Exception {
        assertEquals(portIn.getLabel(), "in");
        assertEquals(portOut.getLabel(), "out");
    }

    @Test
    public void testGetPortType() throws Exception {
        assertEquals(portIn.getPortType(), JSynPort.PortType.INPUT);
        assertEquals(portOut.getPortType(), JSynPort.PortType.OUTPUT);
    }

    @Test
    public void testConnect_InputToOutput() throws Exception {
        portIn.connect(portOut);
        assertEquals(portIn.isConnected(), true);
        assertEquals(portOut.isConnected(), true);
    }

    @Test
    public void testConnect_OutputToInput() throws Exception {
        portOut.connect(portIn);
        assertEquals(portIn.isConnected(), true);
        assertEquals(portOut.isConnected(), true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnect_Self() {
        portIn.connect(portIn);
    }

    @Test(expected = IllegalStateException.class)
    public void testConnect_PortAlreadyConnected() {
        portIn.connect(portOut);
        portIn.connect(portOut);
    }

    @Test
    public void testDisconnect_Input() throws Exception {
        portIn.connect(portOut);
        assertEquals(portIn.isConnected(), true);
        assertEquals(portOut.isConnected(), true);
        portIn.disconnect();
        assertEquals(portIn.isConnected(), false);
        assertEquals(portOut.isConnected(), false);
    }

    @Test
    public void testDisconnect_Output() throws Exception {
        portIn.connect(portOut);
        assertEquals(portIn.isConnected(), true);
        assertEquals(portOut.isConnected(), true);
        portOut.disconnect();
        assertEquals(portIn.isConnected(), false);
        assertEquals(portOut.isConnected(), false);
    }

    @Test(expected = IllegalStateException.class)
    public void testDisconnect_PortAlreadyDisconnected() {
        portIn.disconnect();
    }

    @Test
    public void testEquals_NullPort() {
        assertEquals(portIn.equals(portTest), false);
    }

    @Test
    public void testEquals_WrongType() throws Exception {
        assertEquals(portIn.equals(""), false);
    }

    @Test
    public void testEquals_DifferentPortsFromDifferentModules() throws Exception {
        assertEquals(portIn.equals(portOut), false);
    }

    @Test
    public void testEquals_SamePorts() throws Exception {
        portTest = module1.getInputByLabel("in");
        assertEquals(portIn.equals(portTest), true);
    }

    @Test
    public void testEquals() throws Exception {
        module2.addInput("test");
        portTest = module2.getInputByLabel("test");
        assertEquals(portIn.equals(portTest), false);
        assertEquals(portOut.equals(portTest), false);
    }
}