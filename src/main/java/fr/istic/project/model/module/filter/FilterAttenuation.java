package fr.istic.project.model.module.filter;

import com.jsyn.unitgen.UnitFilter;
import com.softsynth.math.AudioMath;

/**
 * Defines the behavior of a FilterAttenuation.
 */
public class FilterAttenuation  extends UnitFilter {

    private double attenuation;

    public FilterAttenuation(double db) {
        super();
        setAttenuationDB(db);
    }

    /**
     * Returns the attenuation in amplitude.
     *
     * @return the filter's attenuation
     */
    public double getAttenuation() {
        return attenuation;
    }

    /**
     * Sets the attenuation.
     *
     * @param amplitude the new value (numeric).
     * @post getAttenuation() == amplitude
     */
    public void setAttenuation(double amplitude) {
        attenuation = amplitude;
    }

    /**
     * Sets the attenuation by giving a value in decibel.
     *
     * @param db the new value in decibel (dB).
     * @post getAttenuation() == AudioMath.decibelsToAmplitude(db);
     */
    public void setAttenuationDB(double db) {
        attenuation =  AudioMath.decibelsToAmplitude(db);
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
        double[] inputs = input.getValues();
        double[] outputs = output.getValues();

        for (int i = start; i < limit; i += 1) {
            double amplitude = inputs[i]; //amplitude
            outputs[i] = amplitude * attenuation;
        }
    }
}
