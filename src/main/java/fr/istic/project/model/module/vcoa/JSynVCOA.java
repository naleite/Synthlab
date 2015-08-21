package fr.istic.project.model.module.vcoa;

import com.jsyn.unitgen.PassThrough;
import com.jsyn.unitgen.UnitOscillator;
import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.model.module.filter.AmplitudeModulationFilter;
import fr.istic.project.model.module.filter.FrequencyModulationFilter;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.module.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

public class JSynVCOA extends AbstractModule implements VCOA {

    protected final Map<OscillatorType, UnitOscillator> oscillators;
    private AmplitudeModulationFilter amplitudeModulationFilter;
    private FrequencyModulationFilter frequencyModulationFilter;
    private PassThrough passThrough;

    private OscillatorType currentOscillator;
    private RefFrequencyType refFrequency;
    private double currentFrequency;
    private int octave;
    private double fineTuning;

	public JSynVCOA() throws IllegalAccessException, InstantiationException {
        currentOscillator = OscillatorType.SQUARE;
        refFrequency = RefFrequencyType.NFO;
        oscillators = new HashMap<>();
        makeCircuit();

        Observer obs = (o, arg) -> frequencyModulationFilter.setRefFrequency(getRefFrequency().getValue());
        addObserver(PropertyType.REF_FREQUENCY_TYPE_CHANGED, obs);

        Observer obs1 = (o, arg) -> frequencyModulationFilter.setK(getOctave() + getFineTuning());
        addObserver(PropertyType.OCTAVE_CHANGED, obs1);
        addObserver(PropertyType.FINE_TUNING_CHANGED, obs1);
    }

    private void makeCircuit() throws IllegalAccessException, InstantiationException {

        /*
            (in1) --> (AMF) --->(FMF) ---> (OSC) ---> (FLP) ---> (out1)
         */

        // Because the voltage's range should be between -10V / 10V
        amplitudeModulationFilter = new AmplitudeModulationFilter(2);
        frequencyModulationFilter = new FrequencyModulationFilter();
        passThrough = new PassThrough();

        // Construct all kind of oscillator
        for (OscillatorType o: OscillatorType.values()) {
            UnitOscillator ug = o.getJSynClass().newInstance();
            ug.frequency.connect(frequencyModulationFilter.output);
            ug.amplitude.set(MAX_VOLTAGE);
            oscillators.put(o, ug);
            addEntity(ug);
        }

        // connections
        amplitudeModulationFilter.output.connect(frequencyModulationFilter.input);
        oscillators.get(currentOscillator).output.connect(passThrough.input);

        addInput("fm", amplitudeModulationFilter.input);
        addOutput("out", passThrough.output);

        // add
        addEntities(amplitudeModulationFilter, frequencyModulationFilter, passThrough);
    }

    @Override
    public int getOctave() {
        return octave;
    }

    @Override
    public double getFineTuning() {
        return fineTuning;
    }

    @Override
    public OscillatorType getOscillatorType() {
        return currentOscillator;
    }

    @Override
    public RefFrequencyType getRefFrequency() {
        return refFrequency;
    }

    @Override
    public double getCurrentFrequency() {
        return currentFrequency;
    }

    @Override
    public Memento getMemento() {
        Memento m = super.getMemento();
        m.getData().put("refFrequency", getRefFrequency());
        m.getData().put("octave", getOctave());
        m.getData().put("fineTuning", getFineTuning());
        m.getData().put("oscillatorType", getOscillatorType());
        return m;
    }

    @Override
    public void setOctave(int octave) {
        if (octave < MIN_OCTAVE || octave > MAX_OCTAVE) {
            throw new IllegalArgumentException();
        }
        int old = getOctave();
        this.octave = octave;
        firePropertyChange(PropertyType.OCTAVE_CHANGED, getOctave(), old);
    }

    @Override
    public void setFineTuning(double fineTuning) {
        if (fineTuning < -1 || fineTuning > 1) {
            throw new IllegalArgumentException();
        }
        double old = getFineTuning();
        this.fineTuning = fineTuning;
        firePropertyChange(PropertyType.FINE_TUNING_CHANGED, getFineTuning(), old);
    }

    @Override
    public void setOscillatorType(OscillatorType o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
        OscillatorType old = getOscillatorType();
        oscillators.get(currentOscillator).output.disconnect(passThrough.input);
        currentOscillator = o;
        oscillators.get(currentOscillator).output.connect(passThrough.input);
        firePropertyChange(PropertyType.OSCILLATOR_TYPE_CHANGED, getOscillatorType(), old);
    }

    @Override
    public void setRefFrequency(RefFrequencyType refFrequency) {
        RefFrequencyType old = getRefFrequency();
        this.refFrequency = refFrequency;
        firePropertyChange(PropertyType.REF_FREQUENCY_TYPE_CHANGED, getCurrentFrequency(), old);
    }

    @Override
    public void setMemento(Memento m) {
        if (m == null) {
            throw new IllegalArgumentException();
        }
        super.setMemento(m);
        Map<String, Object> data = m.getData();
        setRefFrequency(RefFrequencyType.refFrequencyTypeByName((String) data.get("refFrequency")));
        setOctave(((Double) data.get("octave")).intValue());
        setFineTuning((Double) data.get("fineTuning"));
        setOscillatorType(OscillatorType.oscillatorTypeByName((String) data.get("oscillatorType")));
    }

    @Override
    public String toString() {
        return Component.VCOA.toString();
    }
}
