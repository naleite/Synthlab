package fr.istic.project.model.module.vcfb;

import fr.istic.project.model.module.Module;

public interface VCFB extends Module {

    double MIN_FREQ = 0;
    double MAX_FREQ = 22000;

    /**
     * Returns the cutoff frequency.
     *
     * @return the cutoff frequency value
     */
    double getCutoffFrequency();

    /**
     * Sets the cutoff frequency
     *
     * @param f0 the new value
     * @post getCutoffFrequency() = f0
     */
    void setCutoffFrequency(double f0);
}
