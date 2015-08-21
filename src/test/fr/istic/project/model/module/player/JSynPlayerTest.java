package fr.istic.project.model.module.player;

import fr.istic.project.model.memento.Memento;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSynPlayerTest {

    private Player player;
    private String curDir = System.getProperty("user.dir")+"/src/test/fr/istic/project/model/module/player/soundtest.wav";

    @Before
    public void setUp() throws Exception {
        player = new JSynPlayer();
    }

    @Test
    public void testGetAndSetFilePath() throws Exception {
        player.setFilePath("test/sound.wav");
        assertEquals(player.getFilePath(), "test/sound.wav");
    }

    @Test
    public void testGetMemento() throws Exception {
        Memento memento = player.getMemento();
        assertEquals(memento.getData().get("filepath"), player.getFilePath());
    }

    @Test
    public void testIsPlaying() throws Exception {
        assertEquals(player.isPlaying(), false);
        player.setFilePath(curDir);
        player.startPlaying();
        assertEquals(player.isPlaying(), true);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(player.toString(), "Player");
    }

    @Test(expected = IllegalStateException.class)
    public void testStartPlayingPathNull() throws Exception {
        player.startPlaying();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartPlayingIsPlaying() throws Exception {
        player.setFilePath(curDir);
        player.startPlaying();
        assertEquals(player.isPlaying(), true);
        player.startPlaying();
    }

    @Test
    public void testStopPlaying() throws Exception {
        assertEquals(player.isPlaying(), false);
        player.setFilePath(curDir);
        player.startPlaying();
        assertEquals(player.isPlaying(), true);
        player.stopPlaying();
        assertEquals(player.isPlaying(), false);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopPlayingNotStart() throws Exception{
        player.stopPlaying();
    }

    @Test
    public void testSetMemento() throws Exception {
        Memento memento = player.getMemento();
        player.setFilePath("test/sound.wav");
        memento.getData().put("filepath", player.getFilePath());
        player.setMemento(memento);
        Assert.assertEquals(player.getFilePath(), "test/sound.wav");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMementoNull() throws Exception {
        player.setMemento(null);
    }
}