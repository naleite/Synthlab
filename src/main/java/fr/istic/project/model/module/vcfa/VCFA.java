package fr.istic.project.model.module.vcfa;

import fr.istic.project.model.module.Module;

public interface VCFA extends Module {

    double MIN_FREQ = 0;
    double MAX_FREQ = 22000;
    double MIN_GAIN = 0;
    double MAX_GAIN = 2;

    /**
     * Returns the cutoff frequency.
     *
     * @return the cutoff frequency value
     */
    double getCutoffFrequency();

    /**
     * Returns the gain
     *
     * @return the gain value
     */
    double getGain();

    /**
     * Sets the cutoff frequency.
     *
     * @param f0 the new value
     * @post getCutoffFrequency() = f0
     */
    void setCutoffFrequency(double f0);

    /**
     * Sets the gain
     *
     * @param gain the new gain
     * @post getGain() == gain
     */
    void setGain(double gain);
}
