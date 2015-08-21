package fr.istic.project.model.module.vcoa;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.UnitGenerator;
import fr.istic.project.model.memento.Memento;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSynVCOATest {

    private JSynVCOA vcoa;
    Synthesizer synthesizer;

    public JSynVCOATest() throws IllegalAccessException, InstantiationException {
    }

    @Before
    public void setUp() throws Exception {
        vcoa = new JSynVCOA();
        synthesizer = JSyn.createSynthesizer();
        for (UnitGenerator u : vcoa.getCircuit()) {
            synthesizer.add(u);
        }
        synthesizer.start();
        vcoa.setActivated(true);
    }

    @Test
    public void basicOsc() {
        assertEquals(VCOA.OscillatorType.SQUARE, vcoa.getOscillatorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void oscNull() {
        vcoa.setOscillatorType(null);
    }

    @Test
    public void oscSquare() {
        vcoa.setOscillatorType(VCOA.OscillatorType.SQUARE);
        assertEquals(VCOA.OscillatorType.SQUARE, vcoa.getOscillatorType());
    }

    @Test
    public void oscSawtooth() {
        vcoa.setOscillatorType(VCOA.OscillatorType.SAWTOOTH);
        assertEquals(VCOA.OscillatorType.SAWTOOTH, vcoa.getOscillatorType());
    }

    @Test
    public void oscTriangle() {
        vcoa.setOscillatorType(VCOA.OscillatorType.TRIANGLE);
        assertEquals(VCOA.OscillatorType.TRIANGLE, vcoa.getOscillatorType());
    }

    @Test
    public void refFrequencyNFO() {
        assertEquals(VCOA.RefFrequencyType.NFO, vcoa.getRefFrequency());
    }

    @Test
    public void refFrequencyNFO2() {
        vcoa.setRefFrequency(VCOA.RefFrequencyType.NFO);
        assertEquals(VCOA.RefFrequencyType.NFO, vcoa.getRefFrequency());
    }

    @Test
    public void refFrequencyLFO2() {
        vcoa.setRefFrequency(VCOA.RefFrequencyType.LFO);
        assertEquals(VCOA.RefFrequencyType.LFO, vcoa.getRefFrequency());
    }

    @Test
    public void setOctaveNotNull() {
        vcoa.setOctave(2);
        assertEquals(2, vcoa.getOctave());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setOctaveOutOfBoundsLower() {
        vcoa.setOctave(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setOctaveOutOfBoundsUpper() {
        vcoa.setOctave(168);
    }

    @Test
    public void setFineTuningNotNull() {
        vcoa.setFineTuning(0.05);
        assertEquals(0.05, vcoa.getFineTuning(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setFineTuningOutOfBoundsLower() {
        vcoa.setFineTuning(-5.05);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setFineTuningOutOfBoundsUpper() {
        vcoa.setFineTuning(7.42);
    }

    @Test
    public void toStringTest() {
        String res = vcoa.toString();
        assertTrue(res.equalsIgnoreCase("VCOA"));
    }

    @Test
    public void refFrequenceType() {
        VCOA.RefFrequencyType rType = vcoa.getRefFrequency();
        assertEquals(VCOA.RefFrequencyType.NFO.getValue(), rType.getValue(), 0.1);

        rType = VCOA.RefFrequencyType.refFrequencyTypeByName("LFO");
        assertEquals(VCOA.RefFrequencyType.LFO.getValue(), rType.getValue(), 0.1);

        rType = VCOA.RefFrequencyType.refFrequencyTypeByName("NFO");
        assertEquals(VCOA.RefFrequencyType.NFO.getValue(), rType.getValue(), 0.1);

        rType = VCOA.RefFrequencyType.refFrequencyTypeByName("TEST");
        assertNull(rType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refFrequenceTypeNull() {
        VCOA.RefFrequencyType type = VCOA.RefFrequencyType.refFrequencyTypeByName(null);
    }

    @Test
    public void OscillatorType() {
        VCOA.OscillatorType oType = vcoa.getOscillatorType();
        assertEquals(VCOA.OscillatorType.SQUARE.name(), oType.name());

        oType = VCOA.OscillatorType.oscillatorTypeByName(VCOA.OscillatorType.TRIANGLE.name());
        assertEquals(VCOA.OscillatorType.TRIANGLE.name(), oType.name());

        oType = VCOA.OscillatorType.oscillatorTypeByName(VCOA.OscillatorType.SAWTOOTH.name());
        assertEquals(VCOA.OscillatorType.SAWTOOTH.name(), oType.name());

        oType = VCOA.OscillatorType.oscillatorTypeByName(VCOA.OscillatorType.SQUARE.name());
        assertEquals(VCOA.OscillatorType.SQUARE.name(), oType.name());

        oType = VCOA.OscillatorType.oscillatorTypeByName("TEST");
        assertNull(oType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void OscillatorTypeNull() {
        VCOA.OscillatorType oType = VCOA.OscillatorType.oscillatorTypeByName(null);
    }

    @Test
    public void getMemento() {
        Memento m = vcoa.getMemento();
        System.out.println(m.toJson());
        assertEquals(m.getData().get("x"), vcoa.getPosition().getX());
        assertEquals(m.getData().get("y"), vcoa.getPosition().getY());
        assertEquals(m.getData().get("fineTuning"), vcoa.getFineTuning());
        assertEquals(m.getData().get("oscillatorType"), vcoa.getOscillatorType());
        assertEquals(m.getData().get("refFrequency"), vcoa.getRefFrequency());
        assertEquals(m.getData().get("octave"), vcoa.getOctave());
    }

    @Test
    public void setMemento() {
        Memento m = vcoa.getMemento();
        m.getData().put("x", 17.2);
        m.getData().put("y", 53.5);
        m.getData().put("fineTuning", 0.7);
        m.getData().put("oscillatorType", "TRIANGLE");
        m.getData().put("refFrequency", "LFO");
        m.getData().put("octave", 2.0);

        System.out.println(m.toJson());

        vcoa.setMemento(m);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMementoNull() {
        vcoa.setMemento(null);

    }
}

