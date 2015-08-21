package fr.istic.project.model.memento;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MementoTest {

    private Memento memento;
    private Map<String, Object> data;

    @Before
    public void setUp() throws Exception {
        memento = new Memento();
    }

    /**
     * Test method Memento(Map<String, Object> data)
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMementoMapNull() throws Exception{
        data = null;
        memento = new Memento(data);
    }

    /**
     * Test method Memento(String json)
     * @throws Exception
     */
    @Test
    public void testMementoString() throws Exception{
        memento = new Memento("{test : 5}");
        assertEquals(memento.getData().toString(), "{test=5.0}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMementoStringNull()throws Exception{
        String m = null;
        memento = new Memento(m);
    }

    @Test
    public void testGetDataAndSetData() throws Exception {
        assertEquals(memento.getData().size(), 0);
        data = memento.getData();


    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDataNull() throws Exception {
        memento.setData(null);
    }

    @Test
    public void testToAndFromJson() throws Exception {
        memento.fromJson("{test : 10 }");
        assertEquals(memento.toJson(), "{\"test\":10.0}" );

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsonNull() throws Exception {
        memento.fromJson(null);
    }
}