package fr.istic.project.model.module.osc;

import com.jsyn.scope.AudioScope;
import com.jsyn.scope.swing.AudioScopeView;
import com.jsyn.unitgen.PassThrough;

import fr.istic.project.model.JSynBoard;
import fr.istic.project.model.module.AbstractModule;
import fr.istic.project.model.module.port.JSynPort;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.module.Component;

import java.util.Observer;

public class JSynOscilloscope extends AbstractModule implements Oscilloscope {
    private PassThrough passThrough;
    private AudioScope scope;
    private boolean isScopeActivated;

    /**
     * Constructor.
     *
     * @throws IllegalAccessException if an illegal access exception occurs.
     * @throws InstantiationException if an instantiation exception occurs.
     */
    public JSynOscilloscope() throws IllegalAccessException, InstantiationException {
        makeCircuit();

        scope = new AudioScope(JSynBoard.engine.getSynthesizer());
        scope.addProbe(passThrough.output);
        scope.setTriggerMode(AudioScope.TriggerMode.NORMAL);

        JSynPort input = getInputByLabel("input");

        Observer obs = (o, arg) -> {
            Boolean old = isScopeActivated();
            isScopeActivated = input.isConnected() && isActivated();
            firePropertyChange(PropertyType.SCOPE_ACTIVATED, isScopeActivated(), old);
        };

        addObserver(PropertyType.BOARD_ACTIVATED, obs);
        input.addObserver(PropertyType.PORT_CONNECTED, obs);

        obs = (o, arg) -> {
            if (isScopeActivated()) {
                scope.start();
            } else {
                scope.stop();
            }
        };
        addObserver(PropertyType.SCOPE_ACTIVATED, obs);
    }

    private void makeCircuit() {
        passThrough = new PassThrough();

        addEntity(passThrough);

        addInput("input", passThrough.input);
        addOutput("output", passThrough.output);
    }

    @Override
    public AudioScopeView getView() {
        return scope.getView();
    }

    @Override
    public boolean isScopeActivated() {
        return isScopeActivated;
    }

    @Override
    public String toString() {
        return Component.OSC.toString();
    }
}
