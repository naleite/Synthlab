package fr.istic.project.model.module.keyboard;

import fr.istic.project.model.module.Module;

import javafx.scene.input.KeyCode;

public interface Keyboard extends Module {

    double MIN_OCTAVE = 0;
    double MAX_OCTAVE = 7;

    /**
     * Number of semitones between two octaves.
     */
    double NB_SEMITONES = 12;

    /**
     * C (Do) note when the octave is 0.
     */
    double INITIAL_FREQ = 32.70;

    /**
     * Gets the current semitone.
     *
     * @return the current semitone.
     */
    double getSemitone();

    /**
     * Sets the current semitone according to the pressed key.
     *
     * @param keyPressed the pressed key.
     */
    void setSemitone(KeyCode keyPressed);

    /**
     * Gets the current octave.
     *
     * @return the current octave.
     */
    double getOctave();

    /**
     * Increments the current octave.
     *
     * @pre octave &lt; MAX_OCTAVE
     * @post octave &lt;= MAX_OCTAVE
     */
    void incrementOctave();

    /**
     * Decrements the current octave.
     *
     * @pre MIN_OCTAVE &lt; octave
     * @post MIN_OCTAVE &lt;= octave
     */
    void decrementOctave();

    /**
     * Gets the boolean defining whether or not a note is played.
     *
     * @return true if a note is played.
     */
    boolean isNoteOn();

    /**
     * Sets the boolean defining whether or not a note is played.
     *
     * @param noteOn defines whether or not a note is played.
     */
    void setNoteOn(boolean noteOn);

    /**
     * Handles key pressed events.
     *
     * @param key the key code of the pressed key.
     */
    void handleKeyPressed(KeyCode key);

    /**
     * Handles key released events.
     *
     * @param key the key code of the released key.
     * @post isNoteOn() == false
     */
    void handleKeyReleased(KeyCode key);
}
