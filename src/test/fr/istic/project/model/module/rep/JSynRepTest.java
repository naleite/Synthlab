package fr.istic.project.model.module.rep;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSynRepTest {

    private JSynRep jSynRep;

    @Before
    public void setUp() throws Exception {
        jSynRep = new JSynRep();
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(jSynRep.toString(), "Replicator");
    }
}