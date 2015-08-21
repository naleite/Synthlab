package fr.istic.project.model.module.vcfb;

import com.jsyn.unitgen.FilterHighPass;
import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.model.module.filter.FrequencyModulationFilter;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.module.Component;

import java.util.Map;
import java.util.Observer;

public class JSynVCFB extends AbstractModule implements VCFB {

    private FilterHighPass filterHighPass;
    private FrequencyModulationFilter frequencyModulationFilter;
    private double f0;

    public JSynVCFB() {
        createCircuit();

        Observer obs = (o, arg) -> frequencyModulationFilter.setRefFrequency(getCutoffFrequency());
        addObserver(PropertyType.FILTER_VALUE_CHANGED, obs);
    }

    private void createCircuit() {
        filterHighPass = new FilterHighPass();
        frequencyModulationFilter = new FrequencyModulationFilter();

        addInput("in", filterHighPass.input);
        addInput("fm", frequencyModulationFilter.input);
        addOutput("out", filterHighPass.output);
        frequencyModulationFilter.output.connect(filterHighPass.frequency);

        addEntities(filterHighPass, frequencyModulationFilter);
    }

    @Override
    public Memento getMemento() {
        Memento m = super.getMemento();
        m.getData().put("cutoffFreq", getCutoffFrequency());

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
    }

    @Override
    public double getCutoffFrequency() {
        return f0;
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
    public String toString() {
        return Component.VCFB.toString();
    }
}
