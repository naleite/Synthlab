package fr.istic.project.model.module.filter;

import com.jsyn.unitgen.UnitFilter;

/**
 * Defines the behavior of a AmplitudeModulationFilter.
 */
public class AmplitudeModulationFilter extends UnitFilter {

    private double coeff;

    /**
     * Constructor.
     * @param coeff the filter coefficient.
     */
    public AmplitudeModulationFilter(double coeff) {
        this.coeff = coeff;
    }

    /**
     * Returns the filter's coefficient value.
     *
     * @return the filter coefficient.
     */
    public double getCoeff() {
        return coeff;
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
        double[] inputs = input.getValues();
        double[] outputs = output.getValues();

        for (int i = start; i < limit; i += 1) {
            double x = inputs[i];
            outputs[i] = x * coeff;
        }
    }

    /**
     * Sets the filter's coefficient.
     *
     * @param coeff The new coefficient.
     * @post getCoeff() == coeff
     */
    public void setCoeff(double coeff) {
        this.coeff = coeff;
    }
}
