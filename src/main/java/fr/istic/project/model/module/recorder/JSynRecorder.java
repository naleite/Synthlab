package fr.istic.project.model.module.recorder;

import com.jsyn.unitgen.PassThrough;
import com.jsyn.util.WaveRecorder;

import fr.istic.project.model.JSynBoard;
import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.module.Component;

import java.io.File;
import java.io.IOException;

public class JSynRecorder extends AbstractModule implements Recorder {

    private WaveRecorder recorder;
    private String filepath;
    private PassThrough passThrough;

    public JSynRecorder() throws IOException {
        makeCircuit();
        setFilePath(DEFAULT_FILEPATH);
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
    public boolean isRecording() {
        return recorder != null;
    }

    @Override
    public String toString() {
        return Component.RECORDER.toString();
    }

    @Override
    public void setFilePath(String path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException();
        }
        if (isRecording()) {
            throw new IllegalStateException();
        }
        filepath = path;
        firePropertyChange(PropertyType.FILEPATH, getFilePath());
    }

    @Override
    public void startRecording() throws IOException {
        if (isRecording()) {
            throw new IllegalStateException();
        }
        recorder = new WaveRecorder(JSynBoard.engine.getSynthesizer(),  new File(filepath));
        passThrough.output.connect(recorder.getInput());
        recorder.start();
        firePropertyChange(PropertyType.RECORDING, isRecording());

    }

    @Override
    public void stopRecording() throws IOException {
        if (!isRecording()) {
            throw new IllegalStateException();
        }
        recorder.stop();
        recorder.close();
        passThrough.output.disconnectAll();
        recorder = null;
        firePropertyChange(PropertyType.RECORDING, isRecording());
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
        passThrough = new PassThrough();
        addEntity(passThrough);
        addInput("input", passThrough.input);
    }
}
