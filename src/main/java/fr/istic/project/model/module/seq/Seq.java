package fr.istic.project.model.module.seq;

import fr.istic.project.model.module.Module;
import fr.istic.project.model.observable.PropertyType;

import java.util.Map;

public interface Seq extends Module {

    double MIN_VALUE = -1;
    double MAX_VALUE = 1;

    /**
     * Gets the properties map.
     *
     * @return the properties map.
     */
    Map<PropertyType, Integer> getProperties();

    /**
     * Gets the list of values.
     *
     * @return the list of values.
     */
    double[] getValues();

    /**
     * Sets the list of values.
     *
     * @param values the new list of values.
     */
    void setValues(double[] values);

    /**
     * Gets the corresponding value at the index.
     *
     * @param index the value index.
     * @return the value at the index.
     */
    double getValue(PropertyType index);

    /**
     * Sets the value at the corresponding index.
     *
     * @param index the value index.
     * @param value the new value.
     */
    void setValue(PropertyType index, double value);

    /**
     * Resets the step.
     */
    void resetStep();
}
