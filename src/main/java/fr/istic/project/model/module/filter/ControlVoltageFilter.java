package fr.istic.project.model.module.filter;

import com.jsyn.unitgen.UnitFilter;

import fr.istic.project.model.module.keyboard.Keyboard;

public class ControlVoltageFilter extends UnitFilter {

    private double octave;
    private double semitone;

    /**
     * Default constructor.
     */
    public ControlVoltageFilter() {
        octave = 0;
        semitone = 0;
    }

    /**
     * Constructor setting the current octave and the current semitone.
     *
     * @param octave initial octave value.
     * @param semitone initial semitone value.
     */
    public ControlVoltageFilter(double octave, double semitone) {
        this.octave = octave;
        this.semitone = semitone;
    }

    /**
     * Gets the current octave.
     *
     * @return the current octave.
     */
    public double getOctave() {
        return this.octave;
    }

    /**
     * Sets the current octave.
     *
     * @param octave new octave value.
     */
    public void setOctave(double octave) {
        this.octave = octave;
    }

    /**
     * Gets the current semitone.
     *
     * @return the current semitone.
     */
    public double getSemitone() {
        return this.semitone;
    }

    /**
     * Sets the current semitone.
     *
     * @param semitone new semitone value.
     */
    public void setSemitone(double semitone) {
        this.semitone = semitone;
    }

    /**
     * Computes the frequency according to the current octave.
     *
     * @return the frequency according to the current octave.
     */
    private double f0() {
        return Keyboard.INITIAL_FREQ * Math.pow(2, octave);
    }

    /**
     * Converts a frequency into Volts/Octave (Standard 1 Volt per Octave).
     *
     * @param freq the frequency to convert.
     * @return the Volts/Octave value.
     */
    private double hzToVolts(double freq) {
        return Math.log(freq / Keyboard.INITIAL_FREQ) / Math.log(2.0);
    }

    /**
     * Transforms the data which entered in the filter.
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

        // Get signal arrays from ports.
        double[] outputs = output.getValues();

        for (int i = start; i < limit; i += 1) {
            // Compute the output frequency.
            // f0 * 2 ^ (semitone / 12)
            double outFreq = f0() * Math.pow(2, semitone / Keyboard.NB_SEMITONES);
            // TODO: Understand this or change it ?
            // Because we multiply by 2 in FrequencyModulationFilter ?
            outputs[i] = hzToVolts(outFreq) / 2.0;
        }
    }
}
