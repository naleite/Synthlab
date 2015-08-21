package fr.istic.project.model.module.vcfa;

import com.jsyn.unitgen.FilterLowPass;

import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.model.module.filter.FrequencyModulationFilter;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.module.Component;

import java.util.Map;

public class JSynVCFA extends AbstractModule implements VCFA {

    private double f0;
    private double gain;
    private FilterLowPass filterLowPass;
    private FrequencyModulationFilter frequencyModulationFilter;

    public JSynVCFA() {
        makeCircuit();
        setGain(1);
        setCutoffFrequency(11000);
        addObserver(PropertyType.GAIN_VALUE_CHANGED, (o, args) -> filterLowPass.Q.set(getGain()));
        addObserver(PropertyType.FILTER_VALUE_CHANGED, (o, arg) -> frequencyModulationFilter.setRefFrequency(getCutoffFrequency()));
    }

    private void makeCircuit() {
        filterLowPass = new FilterLowPass();
        frequencyModulationFilter = new FrequencyModulationFilter();

        addInput("in", filterLowPass.input);
        addInput("fm", frequencyModulationFilter.input);
        addOutput("out", filterLowPass.output);
        frequencyModulationFilter.output.connect(filterLowPass.frequency);

        addEntities(filterLowPass, frequencyModulationFilter);
    }

    @Override
    public Memento getMemento() {
        Memento m = super.getMemento();
        m.getData().put("cutoffFreq", getCutoffFrequency());
        m.getData().put("gain", getGain());
        return m;
    }

    @Override
    public void setMemento(Memento m) {
        if (m == null) {
            throw new IllegalArgumentException();
        }
        super.setMemento(m);
        Map<String, Object> data = m.getData();
        setCutoffFrequency((Double) data.get("cutoffFreq"));
        setGain((Double) data.get("gain"));
    }

    @Override
    public double getCutoffFrequency() {
        return f0;
    }

    @Override
    public double getGain() {
        return gain;
    }

    @Override
    public void setCutoffFrequency(double f0) {
        if (f0 < MIN_FREQ || f0 > MAX_FREQ) {
            throw new IllegalArgumentException();
        }
        double old = getCutoffFrequency();
        this.f0 = f0;
        firePropertyChange(PropertyType.FILTER_VALUE_CHANGED, getCutoffFrequency(), old);
    }

    @Override
    public void setGain(double gain) {
        if (gain < MIN_GAIN || gain > MAX_GAIN) {
            throw new IllegalArgumentException();
        }
        double old = getGain();
        this.gain = gain;
        firePropertyChange(PropertyType.GAIN_VALUE_CHANGED, getCutoffFrequency(), old);
    }

    @Override
    public String toString() {
        return Component.VCFA.toString();
    }
}
