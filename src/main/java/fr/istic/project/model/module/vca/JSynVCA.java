package fr.istic.project.model.module.vca;

import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.model.module.filter.VCAFilter;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.module.Component;

public class JSynVCA extends AbstractModule implements VCA {

    /**
     * Represent the output by using a multiplying unit
     * OUT = IN * 10 ^ -(k + AM)
     *     = IN * 10 ^ (-k) * 10 ^ (-AM)
     */
    private double amplification; //a0
    public VCAFilter vcafilter;

    public JSynVCA() throws IllegalAccessException, InstantiationException {
        makeCircuit();
        amplification = 0;

        addObserver(PropertyType.AMPLIFICATION_CHANGED, (o, arg) -> vcafilter.setAmplitudeCoef(getAmplification()));
    }

    /**
     * IN -> FILTERIN --> \
     *                     -> X --> OUT
     * AM -> FILTERAM --> /
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void makeCircuit() throws IllegalAccessException, InstantiationException {
        vcafilter = new VCAFilter(amplification);
        addInput("In", vcafilter.inputB);
        addInput("Am", vcafilter.inputA);
        addOutput("out", vcafilter.output);
        addEntities(vcafilter);
    }

    public double getAmplification() {
        return amplification;
    }

    public void setAmplification(double amplification) {
        if (amplification < MIN_AMPLIFICATION || amplification > MAX_AMPLIFICATION) {
            throw new IllegalArgumentException();
        }
        double old = this.amplification;
        this.amplification = amplification;
        firePropertyChange(PropertyType.AMPLIFICATION_CHANGED, getAmplification(), old);

    }

    @Override
    public String toString() {
        return Component.VCA.toString();
    }

    @Override
    public Memento getMemento() {
        Memento m = super.getMemento();
        m.getData().put("amplification", getAmplification());
        return m;
    }

    @Override
    public void setMemento(Memento m) {
        if (m == null) {
            throw new IllegalArgumentException();
        }
        super.setMemento(m);
        setAmplification((Double) m.getData().get("amplification"));
    }
}
