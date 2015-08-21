package fr.istic.project.model.module.vca;

import fr.istic.project.model.module.Module;

/**
 * Defines the behavior of a VCA Module.
 * @inv out ==  SIGNAL * Math.pow(10, getAmplification() + AM)
 * @inv MIN_AMPLIFICATION &lt;= getAmplification() &lt;= MAX_AMPLIFICATION
 */
public interface VCA extends Module {

    double MIN_AMPLIFICATION = 0;
    double MAX_AMPLIFICATION = 10;

    /**
     * Returns the amplification value.
     *
     * @return the amplification value.
     */
    double getAmplification();

    /**
     * Set the amplification value.
     *
     * @param amplification the new value.
     * @pre MIN_AMPLIFICATION &lt;= amplification &lt;= MAX_AMPLIFICATION
     * @post getAmplification() == amplification
     */
    void setAmplification(double amplification);
}
