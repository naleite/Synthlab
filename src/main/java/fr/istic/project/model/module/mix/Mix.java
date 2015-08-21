package fr.istic.project.model.module.mix;

import fr.istic.project.model.module.Module;

/**
 * Defines the behavior of Mix.
 * @inv getIn*() &lt;= MAX_ATTENUATION
 */
public interface Mix extends Module {

    double MAX_ATTENUATION = 12;

    /**
     * Returns the gain of the first input of the Mix module.
     *
     * @return a double value representing the gain.
     */
    double getGainIn1();

    /**
     * Returns the gain of the second input of the Mix module.
     *
     * @return a double value representing the gain.
     */
    double getGainIn2();

    /**
     * Returns the gain of the third input of the Mix module.
     *
     * @return a double value representing the gain.
     */
    double getGainIn3();

    /**
     * Returns the gain of the fourth input of the Mix module.
     *
     * @return a double value representing the gain.
     */
    double getGainIn4();

    /**
     * Returns the output value of the Mix Module that adds the input's frequencys and return it.
     *
     * @return a double value representing the out.
     */
    double getOut();

    /**
     * Sets a double value into the first Mix module input.
     *
     * @param value the value to insert into the input.
     * @pre value &lt;= MAX_ATTENUATION
     * @post getGainIn1() == value
     */
    void setGainIn1(double value);

    /**
     * Sets a double value into the second input of the Mix module.
     *
     * @param value the value to insert into the input.
     * @pre value &lt;= MAX_ATTENUATION
     * @post getGainIn2() == value
     */
    void setGainIn2(double value);

    /**
     * Sets a double value into the third Mix module input.
     *
     * @param value the value to insert into the input.
     * @pre value &lt;= MAX_ATTENUATION
     * @post getGainIn3() == value
     */
    void setGainIn3(double value);

    /**
     * Sets a double value into the fourth Mix module input.
     *
     * @param value the value to insert into the input.
     * @pre value &lt;= MAX_ATTENUATION
     * @post getGainIn4() == value
     */
    void setGainIn4(double value);

    /**
     * Sets a double value into the Mix module output.
     *
     * @param value the value of out.
     * @post getOut() == value
     */
    void setOut(double value);
}
