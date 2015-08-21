package fr.istic.project.model.module.keyboard;

import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.model.module.filter.ControlVoltageFilter;
import fr.istic.project.model.module.filter.KeyBoardGateFilter;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.module.Component;

import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

public class JSynKeyboard extends AbstractModule implements Keyboard {

    private static final Map<KeyCode, Double> SEMITONES = new HashMap<>();
    static {
        SEMITONES.put(KeyCode.Q, 0.0);
        SEMITONES.put(KeyCode.Z, 1.0);
        SEMITONES.put(KeyCode.S, 2.0);
        SEMITONES.put(KeyCode.E, 3.0);
        SEMITONES.put(KeyCode.D, 4.0);
        SEMITONES.put(KeyCode.F, 5.0);
        SEMITONES.put(KeyCode.T, 6.0);
        SEMITONES.put(KeyCode.G, 7.0);
        SEMITONES.put(KeyCode.Y, 8.0);
        SEMITONES.put(KeyCode.H, 9.0);
        SEMITONES.put(KeyCode.U, 10.0);
        SEMITONES.put(KeyCode.J, 11.0);
        SEMITONES.put(KeyCode.K, 12.0);
    }

    private double octave;
    private double semitone;
    private boolean noteOn;
    private ControlVoltageFilter cvFilter;
    private KeyBoardGateFilter gateFilter;

    public JSynKeyboard() {
        octave = 3;
        semitone = 0;
        noteOn = false;
        cvFilter = new ControlVoltageFilter(octave, semitone);
        gateFilter = new KeyBoardGateFilter(noteOn);

        makeCircuit();

        // Observers
        addObserver(PropertyType.KB_OCTAVE_CHANGED, (o, arg) -> cvFilter.setOctave(octave));
        addObserver(PropertyType.KB_SEMITONE_CHANGED, (o, arg) -> cvFilter.setSemitone(semitone));
        addObserver(PropertyType.KB_NOTE_ON, (o, arg) -> gateFilter.setNoteOn(noteOn));
    }

    private void makeCircuit() {
        addOutput("cv", cvFilter.output);
        addOutput("gate", gateFilter.output);
        addEntities(cvFilter, gateFilter);
    }

    @Override
    public void handleKeyPressed(KeyCode key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (key.equals(KeyCode.W)) {
            decrementOctave();
        } else if (key.equals(KeyCode.X)) {
            incrementOctave();
        } else {
            setSemitone(key);
        }
    }

    @Override
    public void handleKeyReleased(KeyCode key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (SEMITONES.containsKey(key) && SEMITONES.get(key) == semitone) {
            setNoteOn(false);
        }
    }

    @Override
    public double getSemitone() {
        return semitone;
    }

    @Override
    public void setSemitone(KeyCode keyPressed) {
        if (!SEMITONES.containsKey(keyPressed)) {
            //throw new IllegalArgumentException();
            System.out.println("Unrecognized Key");
            return;
        }
        setNoteOn(true);
        double old = semitone;
        semitone = SEMITONES.get(keyPressed);
        firePropertyChange(PropertyType.KB_SEMITONE_CHANGED, getSemitone(), old);
    }

    @Override
    public double getOctave() {
        return octave;
    }

    @Override
    public void incrementOctave() {
        if (octave < MAX_OCTAVE) {
            double old = octave;
            octave++;
            firePropertyChange(PropertyType.KB_OCTAVE_CHANGED, getOctave(), old);
        }
    }

    @Override
    public void decrementOctave() {
        if (MIN_OCTAVE < octave) {
            double old = octave;
            octave--;
            firePropertyChange(PropertyType.KB_OCTAVE_CHANGED, getOctave(), old);
        }
    }

    @Override
    public boolean isNoteOn() {
        return noteOn;
    }

    @Override
    public void setNoteOn(boolean noteOn) {
        boolean old = this.noteOn;
        this.noteOn = noteOn;
        firePropertyChange(PropertyType.KB_NOTE_ON, isNoteOn(), old);
    }

    @Override
    public String toString() {
        return Component.KEYBOARD.toString();
    }
}
