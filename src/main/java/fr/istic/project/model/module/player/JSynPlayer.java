package fr.istic.project.model.module.player;

import com.jsyn.data.FloatSample;
import com.jsyn.unitgen.FixedRateStereoReader;
import com.jsyn.unitgen.SequentialDataReader;
import com.jsyn.util.SampleLoader;

import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.module.Component;

import java.io.File;
import java.io.IOException;

public class JSynPlayer extends AbstractModule implements Player {

    private String filepath;
    private SequentialDataReader samplePlayer;
    private boolean isPlaying;

    public JSynPlayer() {
        makeCircuit();
    }

    @Override
    public String getFilePath() {
        return filepath;
    }

    @Override
    public Memento getMemento() {
        Memento m =  super.getMemento();
        m.getData().put("filepath", getFilePath());
        return m;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public String toString() {
        return Component.PLAYER.toString();
    }

    @Override
    public void setFilePath(String path) throws IOException {
        filepath = path;
        firePropertyChange(PropertyType.FILEPATH, getFilePath());
    }

    @Override
    public void startPlaying() throws IOException {
        if (getFilePath() == null) {
            throw new IllegalStateException();
        }
        if (isPlaying()) {
            throw new IllegalStateException();
        }
        startPlaying(true);
    }

    @Override
    public void stopPlaying() throws IOException {
        if (!isPlaying()) {
            throw new IllegalStateException();
        }
        isPlaying = false;
        samplePlayer.dataQueue.clear();
        firePropertyChange(PropertyType.PLAYING, isPlaying());
    }

    @Override
    public void setMemento(Memento m) {
        if (m == null) {
            throw new IllegalArgumentException();
        }
        super.setMemento(m);
        try {
            setFilePath((String) m.getData().get("filepath"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeCircuit() {
        samplePlayer = new FixedRateStereoReader();
        addEntity(samplePlayer);
        addOutput("output", samplePlayer.output);
    }

    // TODO: pouvoir rendre cette méthode publique. Cela est impossible
    // car actuellement, on ne peut pas savoir si le samplePlayer a fini
    // de lire le son.
    private void startPlaying(boolean loop) throws IOException {
        File sampleFile = new File(getFilePath());
        SampleLoader.setJavaSoundPreferred(true);
        FloatSample sound = SampleLoader.loadFloatSample(sampleFile);
        if (!loop) {
            samplePlayer.dataQueue.queue(sound, 0, sound.getNumFrames());
        } else {
            samplePlayer.dataQueue.queueLoop(sound, 0, sound.getNumFrames());
        }
        isPlaying = true;
        firePropertyChange(PropertyType.PLAYING, isPlaying());
    }
}
