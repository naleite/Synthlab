package fr.istic.project.model.module.seq;

import com.jsyn.unitgen.UnitGate;

public class SequenceGenerator extends UnitGate {

    private int step = 0;
    private double[] values;

    public SequenceGenerator(double[] values) {
        this.values = values;
    }

    public double getValue(int index) {
        return values[index];
    }

    public void setValue(int index, double value) {
        if (value < Seq.MIN_VALUE || value > Seq.MAX_VALUE) {
            throw new IllegalArgumentException("value must be between [" + Seq.MIN_VALUE + "; " + Seq.MAX_VALUE + "]");
        }
        values[index] = value;
    }

    @Override
    public void generate(int start, int limit) {
        double[] inputs = input.getValues();
        double[] outputs = output.getValues();

        for (int i = start; i < limit; i++) {
            if (input.checkGate(i)) {
                step = (step + 1) % values.length;
            }
            outputs[i] = inputs[i] * values[step];
        }
    }

    public void reset() {
        step = 0;
    }
}
