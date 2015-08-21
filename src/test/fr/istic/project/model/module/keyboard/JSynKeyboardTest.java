package fr.istic.project.model.module.keyboard;

import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JSynKeyboardTest {

    private static final double EPSILON = 1e-15;
    private Keyboard keyboard;
    
    @Before
    public void setUp() {
        keyboard = new JSynKeyboard();
        // Increment octave
        keyboard.handleKeyPressed(KeyCode.X);
    }
    
    @Test
    public void testHandleKeyPressed_IncrementOctave() throws Exception {
        double oldOctave = keyboard.getOctave();
        double oldSemitone = keyboard.getSemitone();
        // Increment octave
        keyboard.handleKeyPressed(KeyCode.X);
        
        assertFalse(keyboard.isNoteOn());
        assertEquals(keyboard.getOctave(), oldOctave + 1, EPSILON);
        assertEquals(keyboard.getSemitone(), oldSemitone, EPSILON);
    }

    @Test
    public void testHandleKeyPressed_DecrementOctave() throws Exception {
        double oldOctave = keyboard.getOctave();
        double oldSemitone = keyboard.getSemitone();
        // Decrement octave
        keyboard.handleKeyPressed(KeyCode.W);
        
        assertFalse(keyboard.isNoteOn());
        assertEquals(keyboard.getOctave(), oldOctave - 1, EPSILON);
        assertEquals(keyboard.getSemitone(), oldSemitone, EPSILON);
    }

    @Test
    public void testHandleKeyPressed_SemitoneChanged() throws Exception {
        assertEquals(keyboard.getSemitone(), 0.0, EPSILON);
        keyboard.handleKeyPressed(KeyCode.S);
        assertTrue(keyboard.isNoteOn());
        assertEquals(keyboard.getSemitone(), 2.0, EPSILON);
    }

    @Test
    public void testHandleKeyPressed_UnrecognizedKeyCode() throws Exception {
        keyboard.handleKeyPressed(KeyCode.A);
        // TODO: IllgalArgumentException ?
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleKeyPressed_Null() throws Exception {
        keyboard.handleKeyPressed(null);
    }

    @Test
    public void testHandleKeyReleased_ActualKey() throws Exception {
        keyboard.handleKeyPressed(KeyCode.S);
        assertTrue(keyboard.isNoteOn());
        keyboard.handleKeyReleased(KeyCode.S);
        assertFalse(keyboard.isNoteOn());
    }

    @Test
    public void testHandleKeyReleased_PreviousKey() throws Exception {
        keyboard.handleKeyPressed(KeyCode.Q);
        keyboard.handleKeyPressed(KeyCode.S);
        assertTrue(keyboard.isNoteOn());
        keyboard.handleKeyReleased(KeyCode.Q);
        assertTrue(keyboard.isNoteOn());
    }

    @Test
    public void testHandleKeyReleased_UnrecognizedKeyCode() throws Exception {
        keyboard.handleKeyReleased(KeyCode.A);
        // TODO: IllgalArgumentException ?
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testHandleKeyReleased_Null() throws Exception {
        keyboard.handleKeyReleased(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMemento_Null() throws Exception {
        keyboard.setMemento(null);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(keyboard.toString(), "Keyboard");
    }
}
