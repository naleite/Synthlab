package fr.istic.project.model.module.filter;

import com.jsyn.unitgen.UnitFilter;

public class KeyBoardGateFilter extends UnitFilter {

    private boolean noteOn;
    private double tension;

    /**
     * Constructor.
     *
     * @param noteOn defines whether or not a note is played.
     */
    public KeyBoardGateFilter(boolean noteOn) {
        this.noteOn = noteOn;
        this.tension = 0;
    }

    /**
     * Gets the boolean defining whether or not a note is played.
     *
     * @return the boolean defining whether or not a note is played.
     */
    public boolean isNoteOn() {
        return noteOn;
    }

    /**
     * Sets the boolean defining whether or not a note is played.
     *
     * @param noteOn defines whether or not a note is played.
     */
    public void setNoteOn(boolean noteOn) {
        this.noteOn = noteOn;
        this.tension = isNoteOn() ? 5 : -5;
    }

    /**
     * Gets the current tension (-5V or +5V).
     * Its value is 0 at initialization.
     *
     * @return the current tension.
     */
    public double getTension() {
        return tension;
    }

    /**
     * Transforms the data which entered in the filter (frequency computed with the current octave and then
     * the computation of the earlier result with the semitone).
     *
     * @param start TODO
     * @param limit TODO
     * @pre start &gt;= 0
     * @pre limit &gt;= 0
     */
    @Override
    public void generate(int start, int limit) {
        if (start < 0) {
            throw new IllegalArgumentException();
        }
        if (limit < 0) {
            throw new IllegalArgumentException();
        }

        double[] outputs = output.getValues();

        for (int i = start; i < limit; i += 1) {
            outputs[i] = tension;
        }
    }
}
