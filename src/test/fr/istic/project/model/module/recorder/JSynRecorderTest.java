package fr.istic.project.model.module.recorder;

import fr.istic.project.model.memento.Memento;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JSynRecorderTest {

    private JSynRecorder recorder;

    @Before
    public void setUp() throws IOException {
        recorder = new JSynRecorder();
        recorder.setFilePath("sortie.mp3");
    }

    @Test
    public void testGetFilePath() throws Exception {
        assertEquals("sortie.mp3", recorder.getFilePath());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("Recorder", recorder.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFilePathNull() throws Exception {
        recorder.setFilePath(null);
    }

    @Test
    public void testSetFilePath() throws Exception {
        recorder.setFilePath("test.mp3");
    }

    @Test(expected = IllegalStateException.class)
    public void testSetFilePathNotRecording() throws Exception {
        recorder.stopRecording();
        assertFalse(recorder.isRecording());
        recorder.setFilePath("test.mp3");
        recorder.startRecording();
        assertTrue(recorder.isRecording());
        recorder.setFilePath("test1");
    }

    @Test(expected = IllegalStateException.class)
    public void testSetFilePathRecording() throws Exception {
        recorder.startRecording();
        assertTrue(recorder.isRecording());
        recorder.setFilePath("test.mp3");
    }

    @Test(expected = IllegalStateException.class)
    public void testStartRecordingYet() throws Exception {
        recorder.startRecording();
        recorder.startRecording();
        assertTrue(recorder.isRecording());
    }

    @Test
    public void testStartRecording() throws Exception {
        recorder.startRecording();
        assertTrue(recorder.isRecording());
    }

    @Test
    public void testStopRecording() throws Exception {
        recorder.startRecording();
        recorder.stopRecording();
        assertFalse(recorder.isRecording());
    }

    @Test
    public void testSetMemento() throws Exception {
        Memento m1 = recorder.getMemento();
        m1.getData().put("filepath", recorder.getFilePath());

        recorder.setMemento(m1);

        Memento m2 = recorder.getMemento();
        m2.getData().put("filepath", null);

        recorder.setMemento(m1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMementoNull() throws Exception {
        recorder.setMemento(null);
    }
}