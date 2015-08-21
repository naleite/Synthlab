package fr.istic.project.model.module.eg;

import com.jsyn.unitgen.EnvelopeDAHDSR;

import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.model.module.filter.AmplitudeModulationFilter;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.module.Component;

public class JSynEG extends AbstractModule implements EG {

    private EnvelopeDAHDSR envelopeDAHDSR;
    private AmplitudeModulationFilter ampliFilter;

    public JSynEG() {
        envelopeDAHDSR = new EnvelopeDAHDSR();
        ampliFilter = new AmplitudeModulationFilter(5);

        // DO NOT TOUCH
        envelopeDAHDSR.delay.set(0); // Time in seconds for first stage of the envelope, before the attack.
        envelopeDAHDSR.hold.set(0); // Time in seconds for the plateau between the attack and decay stages.

        // PARAM
        envelopeDAHDSR.attack.set(0); // Time in seconds for the rising stage of the envelope to go from 0.0 to 1.0.
        envelopeDAHDSR.decay.set(0); // Time in seconds for the falling stage to go from 0 dB to -90 dB.
        envelopeDAHDSR.sustain.set(0); // Level for the sustain stage.
        envelopeDAHDSR.release.set(0); // Time in seconds to go from 0 dB to -90 dB.

        createCircuit();
    }

    private void createCircuit() {
        addInput("gate", envelopeDAHDSR.input);
        envelopeDAHDSR.output.connect(ampliFilter.input);
        addOutput("out", ampliFilter.output);
        addEntity(envelopeDAHDSR);
        addEntity(ampliFilter);
    }

    // Request

    @Override
    public double getAttack() {
        return envelopeDAHDSR.attack.get();
    }

    @Override
    public double getSustain() {
        return envelopeDAHDSR.sustain.get();
    }

    @Override
    public double getRelease() {
        return envelopeDAHDSR.release.get();
    }

    @Override
    public double getDecay() {
        return envelopeDAHDSR.decay.get();
    }

    public EnvelopeDAHDSR getEnvelopeDAHDSR() {
        return envelopeDAHDSR;
    }

    // Command

    @Override
    public void setAttack(double attack) {
        double old = getAttack();

        envelopeDAHDSR.attack.set(attack);
        firePropertyChange(PropertyType.ATTACK_CHANGED, getAttack(), old);
    }

    @Override
    public void setSustain(double sustain) {
        double old = getSustain();
        envelopeDAHDSR.sustain.set(sustain);
        firePropertyChange(PropertyType.SUSTAIN_CHANGED, getSustain(), old);
    }

    @Override
    public void setRelease(double release) {
        double old = getRelease();
        envelopeDAHDSR.release.set(release);
        firePropertyChange(PropertyType.RELEASE_CHANGED, getRelease(), old);
    }

    @Override
    public void setDecay(double decay) {
        double old = getDecay();
        envelopeDAHDSR.decay.set(decay);
        firePropertyChange(PropertyType.DECAY_CHANGED, getDecay(), old);
    }

    @Override
    public Memento getMemento() {
        Memento m = super.getMemento();
        m.getData().put("attack", getAttack());
        m.getData().put("decay", getDecay());
        m.getData().put("sustain", getSustain());
        m.getData().put("release", getRelease());

        return m;
    }

    @Override
    public void setMemento(Memento m) {
        if (m == null)  {
            throw new IllegalArgumentException();
        }
        super.setMemento(m);
        setAttack((double) m.getData().get("attack"));
        setDecay((double) m.getData().get("decay"));
        setSustain((double) m.getData().get("sustain"));
        setRelease((double) m.getData().get("release"));
    }

    @Override
    public String toString() {
        return Component.EG.toString();
    }
}
