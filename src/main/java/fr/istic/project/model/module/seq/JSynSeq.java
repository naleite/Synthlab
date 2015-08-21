package fr.istic.project.model.module.seq;

import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.module.Component;

import java.util.HashMap;
import java.util.Map;

public class JSynSeq extends AbstractModule implements Seq {

    private static final Map<PropertyType, Integer> PROPERTIES = new HashMap<>();
    static {
        PROPERTIES.put(PropertyType.LEVEL1_CHANGED, 0);
        PROPERTIES.put(PropertyType.LEVEL2_CHANGED, 1);
        PROPERTIES.put(PropertyType.LEVEL3_CHANGED, 2);
        PROPERTIES.put(PropertyType.LEVEL4_CHANGED, 3);
        PROPERTIES.put(PropertyType.LEVEL5_CHANGED, 4);
        PROPERTIES.put(PropertyType.LEVEL6_CHANGED, 5);
        PROPERTIES.put(PropertyType.LEVEL7_CHANGED, 6);
        PROPERTIES.put(PropertyType.LEVEL8_CHANGED, 7);
    }

    private static final int STEP_SIZE = 8;

    /**
     * List containing all the controls values.
     */
    private double[] values;

    /**
     * The sequence generator.
     */
    private SequenceGenerator seqGen;

    /**
     * Constructor.
     */
    public JSynSeq() {
        values = new double[STEP_SIZE];
        seqGen = new SequenceGenerator(values);

        makeCircuit();

        // Observers
        for (PropertyType p : PROPERTIES.keySet()) {
            addObserver(p, (o, arg) -> seqGen.setValue(PROPERTIES.get(p), values[PROPERTIES.get(p)]));
        }
    }

    private void makeCircuit() {
        addInput("gate", seqGen.input);
        addOutput("out", seqGen.output);
        addEntity(seqGen);
    }

    @Override
    public double getValue(PropertyType index) {
        if (!PROPERTIES.containsKey(index)) {
            System.out.println(index);
            throw new IllegalArgumentException();
        }
        return values[PROPERTIES.get(index)];
    }

    @Override
    public void setValue(PropertyType index, double value) {
        if (!PROPERTIES.containsKey(index)) {
            throw new IllegalArgumentException();
        }
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Value must be between [" + MIN_VALUE + "; " + MAX_VALUE + "]");
        }
        double old = getValue(index);
        int i = PROPERTIES.get(index);
        values[i] = value;
        firePropertyChange(index, getValue(index), old);
    }

    @Override
    public void resetStep() {
        seqGen.reset();
    }

    @Override
    public String toString() {
        return Component.SEQ.toString();
    }

    @Override
    public Memento getMemento() {
        Memento m = super.getMemento();
        for (PropertyType  p : PROPERTIES.keySet()) {
            m.getData().put(p.toString(), getValue(p));
        }
        return m;
    }

    @Override
    public void setMemento(Memento m) {
        if (m == null) {
            throw new IllegalArgumentException();
        }
        super.setMemento(m);
        for (PropertyType  p : PROPERTIES.keySet()) {
            setValue(p, (Double) m.getData().get(p.toString()));
        }
    }

    @Override
    public double[] getValues() {
        return values;
    }

    @Override
    public void setValues(double[] values) {
        if (values == null || values.length != PROPERTIES.keySet().size()) {
            throw new IllegalArgumentException();
        }
        this.values = values.clone();
    }

    @Override
    public Map<PropertyType, Integer> getProperties() {
        return PROPERTIES;
    }
}
