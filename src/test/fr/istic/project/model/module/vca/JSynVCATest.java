package fr.istic.project.model.module.vca;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.UnitGenerator;
import fr.istic.project.model.memento.Memento;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSynVCATest {

    private JSynVCA vca;
    private double coef;
    private static final double DELTA = 1e-15;
    private Synthesizer synthesizer;
    
    @Before
    public void setUp() throws Exception {
        vca=new JSynVCA();
        this.coef=2.0;
        synthesizer= JSyn.createSynthesizer();
        for (UnitGenerator u : vca.getCircuit()) {
            synthesizer.add(u);
        }
        synthesizer.start();
        vca.setActivated(true);
    }

    @After
    public void tearDown(){
        vca.setActivated(false);
        synthesizer.stop();
    }

    @Test
    public void testGetAndSetAmplification() throws Exception {

        Assert.assertEquals(0,vca.getAmplification(),DELTA);
        vca.setAmplification(this.coef);
        Assert.assertEquals(this.coef,vca.getAmplification(),DELTA);
        vca.setAmplification(5.34);
        Assert.assertEquals(5.34,vca.getAmplification(),DELTA);

        vca.setAmplification(10.0);
        Assert.assertEquals(10.0,vca.getAmplification(),DELTA);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetTooSmall() {
        vca.setAmplification(-0.001);
        vca.setAmplification(-0.1);
        vca.setAmplification(-10);
    }


    @Test(expected = IllegalArgumentException.class)
    public void TestSetTooBig() {
        vca.setAmplification(10.001);
        vca.setAmplification(10.01);
        vca.setAmplification(100.001);

    }

    @Test
    public void testToString() throws Exception {
        Assert.assertEquals(vca.toString(), "VCA");
    }

    @Test
    public void testSetMemento() throws Exception {
        Memento memento = vca.getMemento();
        memento.getData().put("amplification", 1.0);
        vca.setMemento(memento);
        Assert.assertEquals(vca.getAmplification(), 1.0, 1e-15);
    }

    @Test
    public void testGetMemento() throws Exception {
        Memento memento = vca.getMemento();
        assertEquals(memento.getData().get("amplification"), vca.getAmplification());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMementoNull() {
        vca.setMemento(null);

    }
}