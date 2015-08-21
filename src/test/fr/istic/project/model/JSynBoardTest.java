package fr.istic.project.model;

import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.Module;
import fr.istic.project.model.module.out.JSynOut;
import fr.istic.project.model.module.out.Out;
import fr.istic.project.model.module.vca.JSynVCA;
import fr.istic.project.model.module.vca.VCA;
import fr.istic.project.view.module.Component;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class JSynBoardTest {

    private Map<Long, Module> modules;
    private Map<Module, Long> reverse;

    private JSynBoard jSynBoard;
    private Out out;
    private Module module;

    @Before
    public void setUp() throws Exception {
        jSynBoard = new JSynBoard();
        out = new JSynOut();
        jSynBoard.addModule(out.getClass(), 1);
    }

    /**
     * Test method getCurrentColor() and setCurrentColor(Color color)
     * @throws Exception
     */
    @Test
    public void testGetCurrentColor() throws Exception {
        assertEquals(jSynBoard.getCurrentColor(), Color.WHITE);
        jSynBoard.setCurrentColor(Color.BLUE);
        assertEquals(jSynBoard.getCurrentColor(), Color.BLUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSetCurrentColor() throws Exception {
        jSynBoard.setCurrentColor(null);
    }

    /**
     * Test method getModule(long id)
     * @throws Exception
     */
    @Test
    public void testGetModuleLong() throws Exception {
        assertEquals(jSynBoard.getModule(1).getClass(), JSynOut.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotGetModuleLong() throws Exception {
        jSynBoard.getModule(3);
    }

    /**
     * Test method getModule(Class<T> klass, long id)
     * @throws Exception
     */
    @Test
    public void testGetModuleKlassID() throws Exception {
        VCA vca = new JSynVCA();
        jSynBoard.addModule(vca.getClass());
        assertEquals(jSynBoard.getModule(vca.getClass(), 2).getClass(), JSynVCA.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetModuleKlassIDNotKlass() throws Exception {
        assertEquals(jSynBoard.getModule(null, 1).getClass(), JSynVCA.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetModuleKlassIDNotId() throws Exception {
        assertEquals(jSynBoard.getModule(out.getClass(), 5).getClass(), JSynVCA.class);
    }

    /**
     * Test method getModuleID(Module m)
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetModuleIDNull() throws Exception {
        jSynBoard.getModuleID(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetModuleIDNotModule() throws Exception {
        module = new JSynVCA();
        jSynBoard.getModuleID(module);
    }

    /**
     * Test method isActivated() and setActivated(boolean activate)
     * @throws Exception
     */
    @Test
    public void testIsActivated() throws Exception {
        assertEquals(jSynBoard.isActivated(), false);
        jSynBoard.setActivated(true);
        assertEquals(jSynBoard.isActivated(), true);
    }

    /**
     * Test method addModule(Class<? extends Module> klass)
     * @throws Exception
     */
    @org.junit.Test
    public void testAddModule() throws Exception {
        Collection<Component> components = new HashSet<>();
        Collections.addAll(components, Component.values());
        components.add(null);
        for (Component c : components) {
            Map<Long, Module> old = new HashMap<>(jSynBoard.getModules());
            long id = 0;
            try {
                if (c == null) {
                    jSynBoard.addModule(null);
                    fail();
                }
                id = jSynBoard.addModule(c.getKlass());
            } catch (IllegalArgumentException e) {
                if (c != null) {
                    fail();
                }
                continue;
            }
            assertEquals(old.keySet().size(), jSynBoard.getModules().keySet().size() - 1);
            assertEquals(old.values().size(), jSynBoard.getModules().values().size() - 1);
            assertEquals(jSynBoard.getModule(id).getClass(), c.getKlass());
        }
    }

    @Test
    public void testAddModule1() throws Exception {
        int size = jSynBoard.getModules().size();
        VCA vca = new JSynVCA();
        jSynBoard.addModule(vca.getClass());
        assertEquals(jSynBoard.getModules().size(), size + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddModuleNull() throws Exception {
        jSynBoard.addModule(null);
    }

    /**
     * Test method addModule(Class<? extends Module> klass, long id)
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddModuleKlassLongNull() throws Exception {
        jSynBoard.addModule(null, 1);
    }

    /**
     * Test method removeModule(long id)
     * @throws Exception
     */
    @Test
    public void testRemoveModuleID() throws Exception {
        long id = jSynBoard.addModule(JSynOut.class);
        Module m = jSynBoard.getModule(id);

        Map<Long, Module> old = new HashMap<>(jSynBoard.getModules());

        jSynBoard.removeModule(id);

        assertFalse(jSynBoard.getModules().containsValue(m));
        assertFalse(jSynBoard.getModules().containsKey(id));
        assertEquals(old.values().size(), jSynBoard.getModules().values().size() + 1);
        assertEquals(old.keySet().size(), jSynBoard.getModules().keySet().size() + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveModuleNullID(){
        jSynBoard.removeModule(4);
    }

    /**
     * Test method removeModule(Module m)
     * @throws Exception
     */
    @org.junit.Test
    public void testRemoveModule1() throws Exception {
        try {
            jSynBoard.removeModule(null);
            fail();
        } catch (IllegalArgumentException e) {
            //sounds good !
        }
        long id = jSynBoard.addModule(JSynOut.class);
        Module m = jSynBoard.getModule(id);
        Map<Long, Module> old = new HashMap<>(jSynBoard.getModules());

        jSynBoard.removeModule(m);

        assertFalse(jSynBoard.getModules().containsValue(m));
        assertFalse(jSynBoard.getModules().containsKey(id));
        assertEquals(old.values().size(), jSynBoard.getModules().values().size() + 1);
        assertEquals(old.keySet().size(), jSynBoard.getModules().keySet().size() + 1);
    }

    /**
     * Test method reset()
     * @throws Exception
     */
    @Test
    public void testReset() throws Exception {
        testAddModule();
        jSynBoard.reset();
        assertEquals(jSynBoard.getModules().values().size(), 0);
        assertEquals(jSynBoard.getModules().keySet().size(), 0);
    }

    @Test
    public void testSetMemento() throws Exception {
        testAddModule();
        Memento m = jSynBoard.getMemento();
        Memento n = new Memento(m.toJson());
        testReset();
        jSynBoard.setMemento(n);
        assertEquals(jSynBoard.getMemento().toJson().contains("\"class\":{\"1\":\"JSynOut\""), true);
        assertEquals(jSynBoard.getMemento().toJson().contains("{\"x\":0.0,\"y\":0.0,\"attenuation\":0.0}"), true);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMementoNull() throws Exception {
        jSynBoard.setMemento(null);
    }
}