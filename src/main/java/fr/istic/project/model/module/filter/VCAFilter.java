package fr.istic.project.model.module.filter;

import com.jsyn.unitgen.UnitBinaryOperator;
import com.softsynth.math.AudioMath;

public class VCAFilter extends UnitBinaryOperator {
    private double amplitudeCoef; //a0
    private double gain12 = AudioMath.decibelsToAmplitude(12);

    public VCAFilter(double amp) {
        super();
        this.amplitudeCoef = amp;
    }
    public void setAmplitudeCoef(double amplitudeCoef) {
        this.amplitudeCoef = amplitudeCoef;
    }

    public double getAmplitudeCoef(){ return this.amplitudeCoef;}

    @Override
    public void generate(int start, int limit) {

        if (start < 0) {
            throw new IllegalArgumentException();
        }
        if (limit < 0) {
            throw new IllegalArgumentException();
        }

        double[] inputsA = inputA.getValues();
        double[] inputsB = inputB.getValues();
        double[] outputs = output.getValues();

        //OLD
        for (int i = start; i < limit; i += 1) {
            double am = inputsA[i];
            double in = inputsB[i];
            outputs[i] = am * in *  Math.pow(10, -AudioMath.decibelsToAmplitude(amplitudeCoef));
        }

    }
}
