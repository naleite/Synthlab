package fr.istic.project.model.module.filter;

import com.jsyn.unitgen.UnitFilter;

/**
 * Defines the behavior of a FrequencyModulationFilter.
 */
public class FrequencyModulationFilter extends UnitFilter {

    private double refFrequency;
    private double k;

    /**
     * Returns the reference frequency.
     *
     * @return the refFrequency
     */
    public double getRefFrequency() {
        return refFrequency;
    }

    /**
     * Returns the k value.
     *
     * @return the k value.
     */
    public double getK() {
        return k;
    }

    /**
     * Sets the refFrequency.
     *
     * @param refFrequency the reference frequency.
     * @post getValue() == refFrequency
     */
    public void setRefFrequency(double refFrequency) {
        if (refFrequency < 0) {
            throw new IllegalArgumentException();
        }
        this.refFrequency = refFrequency;
    }

    /**
     * Sets the k value.
     *
     * @param k the new value.
     * @post getK() == k
     */
    public void setK(double k) {
        this.k = k;
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
        double[] inputsVFm = input.getValues();
        double[] outputs = output.getValues();

        for (int i = start; i < limit; i++) {
            outputs[i] = refFrequency * Math.pow(2, k + inputsVFm[i]);
        }
    }
}
