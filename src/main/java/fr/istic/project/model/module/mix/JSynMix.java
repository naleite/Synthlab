package fr.istic.project.model.module.mix;

import com.jsyn.unitgen.Add;

import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.model.module.filter.AmplitudeModulationFilter;
import fr.istic.project.model.module.filter.FilterAttenuation;
import fr.istic.project.model.module.port.JSynPort;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.module.Component;

import java.util.HashSet;
import java.util.Observer;
import java.util.Set;

public class JSynMix extends AbstractModule implements Mix {

    /**
     * Represents the input's value (four inputs)
     */
    private double gain1, gain2, gain3, gain4;

    /**
     * FilterAttenuation objects that allow to get the gain values (input). Used to permit the frequency's computation.
     */
    private FilterAttenuation filter1, filter2, filter3, filter4;

    /**
     * Add objects used into the circuit to add the frequency of the connected inputs.
     */
    private Add addF1F2, addF3F4, addRes;

    /**
     * Attribute used for the module output.
     */
    private double out;

    /**
     * AmplitudeModulationFilter object used to modulate the output voltage between -5V to +5V.
     */
    private AmplitudeModulationFilter ampMF;

    /**
     * Set that keeps the inputs that are connected (used into the setAmpMF function to modulate the output's voltage).
     */
    private Set<JSynPort> setInputsConnected;

    /**
     * Constructor. It calls the function that creates the Mix module circuit and instantiate every observers needed.
     * to update the inputs and output values to the GUI.
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public JSynMix() throws IllegalAccessException, InstantiationException {
        createCircuit();

        /*
        *  In1 ---> Gain Adjustment ----
        *                               |
        *  In2 ---> Gain Adjustment ----|
        *                               |----> out (Sum of gained In1, In2, In3 and In4)
        *  In3 ---> Gain Adjustment ----|
        *                               |
        *  In4 ---> Gain Adjustment ----
        */

        // Set containing the inputs connected into the module initialization
        setInputsConnected = new HashSet<>();

        // Observer creation for input value following
        Observer obs1 = (o, arg) -> filter1.setAttenuationDB(getGainIn1());
        Observer obs2 = (o, arg) -> filter2.setAttenuationDB(getGainIn2());
        Observer obs3 = (o, arg) -> filter3.setAttenuationDB(getGainIn3());
        Observer obs4 = (o, arg) -> filter4.setAttenuationDB(getGainIn4());

        // Observers added to the module
        addObserver(PropertyType.IN1_CHANGED, obs1);
        addObserver(PropertyType.IN2_CHANGED, obs2);
        addObserver(PropertyType.IN3_CHANGED, obs3);
        addObserver(PropertyType.IN4_CHANGED, obs4);

        for (JSynPort input: getInputs()) {
            input.addObserver(PropertyType.PORT_CONNECTED, (o, arg) -> {
                if (input.isConnected()) {
                    setInputsConnected.add(input);
                } else {
                    setInputsConnected.remove(input);
                }
                ampMF.setCoeff(1.0 / Math.max(1, setInputsConnected.size()));
            });
        }
    }

    /**
     * This function create the circuit of the module. It instantiates the several filters (on the inputs) and make the
     * links between the filters and the Add objects (3 Add objects : one for input 1-2, one for 3-4 and the last one
     * to merge the two last results). This function make the links between the circuit objects and the GUI.
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void createCircuit() throws IllegalAccessException, InstantiationException {
        // AmplitudeFilter instantiation
        ampMF = new AmplitudeModulationFilter(0);

        // Filter Creation for gain application on values from inputs
        filter1 = new FilterAttenuation(gain1);
        filter2 = new FilterAttenuation(gain2);
        filter3 = new FilterAttenuation(gain3);
        filter4 = new FilterAttenuation(gain4);

        // Making the sum of the inputs 1 and 2
        addF1F2 = new Add();
        addF1F2.inputA.connect(filter1.getOutput());
        addF1F2.inputB.connect(filter2.getOutput());

        // Making the sum of the inputs 3 and 4
        addF3F4 = new Add();
        addF3F4.inputA.connect(filter3.getOutput());
        addF3F4.inputB.connect(filter4.getOutput());

        // Making the sum of the last sums (1 and 2 AND 3 and 4)
        addRes = new Add();
        addRes.inputA.connect(addF1F2.output);
        addRes.inputB.connect(addF3F4.output);

        // Link between addRes output ans ampMF input
        addRes.output.connect(ampMF.getInput());

        // Link between the view and the model's inputs
        addInput("in1", filter1.input);
        addInput("in2", filter2.input);
        addInput("in3", filter3.input);
        addInput("in4", filter4.input);
        addOutput("out", ampMF.output);

        // Add all the entities we need to the circuit
        addEntities(filter1, filter2, filter3, filter4, addF1F2, addF3F4, addRes, ampMF);
    }

    @Override
    public double getGainIn1() {
        setAmpMF(this.ampMF);
        return this.gain1;
    }

    @Override
    public double getGainIn2() {
        setAmpMF(this.ampMF);
        return this.gain2;
    }

    @Override
    public double getGainIn3() {
        setAmpMF(this.ampMF);
        return this.gain3;
    }

    @Override
    public double getGainIn4() {
        setAmpMF(this.ampMF);
        return this.gain4;
    }

    @Override
    public double getOut() {
        return this.out;
    }

    @Override
    public void setGainIn1(double value) {
        if (value > MAX_ATTENUATION) {
            throw new IllegalArgumentException();
        }
        double old = getGainIn1();
        this.gain1 = value;
        firePropertyChange(PropertyType.IN1_CHANGED, getGainIn1(), old);
    }

    @Override
    public void setGainIn2(double value) {
        if (value > MAX_ATTENUATION) {
            throw new IllegalArgumentException();
        }
        double old = getGainIn2();
        this.gain2 = value;
        firePropertyChange(PropertyType.IN2_CHANGED, getGainIn2(), old);
    }

    @Override
    public void setGainIn3(double value) {
        if (value > MAX_ATTENUATION) {
            throw new IllegalArgumentException();
        }
        double old = getGainIn3();
        this.gain3 = value;
        firePropertyChange(PropertyType.IN3_CHANGED, getGainIn3(), old);
    }

    @Override
    public void setGainIn4(double value) {
        if (value > MAX_ATTENUATION) {
            throw new IllegalArgumentException();
        }
        double old = getGainIn4();
        this.gain4 = value;
        firePropertyChange(PropertyType.IN4_CHANGED, getGainIn4(), old);
    }

    @Override
    public void setOut(double value) {
        double old = getOut();
        this.out = value;
        firePropertyChange(PropertyType.OUT_CHANGED, getOut(), old);
    }

    /**
     * Function that sets an AmplitudeModulationFilter object (in parameter) into the attributes ampMF used to make
     * the output voltage between -5V to +5V.
     *
     * @param ampMF the AmplitudeModulationFilter that is used to set into the ampMF attribute
     */
    public void setAmpMF(AmplitudeModulationFilter ampMF) {
        this.ampMF = ampMF;

        ampMF.setCoeff(1.0 / Math.max(1, setInputsConnected.size()));
        firePropertyChange(PropertyType.AMF_CHANGED, ampMF);
    }

    /**
     * Function that allow saved the actual state of the module (The four inputs and the output) into a Memento object
     * and return it.
     *
     * @return memento object containing the four inputs of the module and the output
     */
    @Override
    public Memento getMemento() {
        Memento m = super.getMemento();

        m.getData().put("gainIn1", getGainIn1());
        m.getData().put("gainIn2", getGainIn2());
        m.getData().put("gainIn3", getGainIn3());
        m.getData().put("gainIn4", getGainIn4());
        m.getData().put("out", getOut());

        return m;
    }

    /**
     * Function that allows to load an old module configuration thanks to a Memento object. Used when the user want to
     * load a synthetizer's configuration saved earlier.
     *
     * @param m The memento containing the old configuration
     */
    @Override
    public void setMemento(Memento m) {
        if (m == null) {
            throw new IllegalArgumentException();
        }
        super.setMemento(m);
        setGainIn1((Double) m.getData().get("gainIn1"));
        setGainIn2((Double) m.getData().get("gainIn2"));
        setGainIn3((Double) m.getData().get("gainIn3"));
        setGainIn4((Double) m.getData().get("gainIn4"));
        setOut((Double) m.getData().get("out"));
    }

    /**
     * Function that returns the id of the Mix Module (MIX)
     *
     * @return the module's name.
     */
    @Override
    public String toString() {
        return Component.MIX.toString();
    }
}
