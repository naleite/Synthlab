package fr.istic.project.model.module.port;

import com.jsyn.ports.ConnectableInput;
import com.jsyn.ports.ConnectableOutput;
import fr.istic.project.model.module.Module;
import fr.istic.project.model.observable.AbstractObservableModel;
import fr.istic.project.model.observable.PropertyType;

public class JSynPort extends AbstractObservableModel {

    private final String label;
    private final Module module;
    private JSynPort connected;

    private ConnectableInput input;
    private ConnectableOutput output;

    public enum PortType {
        INPUT,
        OUTPUT
    }

    public JSynPort(Module module, String label, ConnectableInput port) {
        if (label == null || port == null) {
            throw new IllegalArgumentException();
        }
        this.label = label;
        this.input = port;
        this.module = module;
    }

    public JSynPort(Module module, String label, ConnectableOutput port) {
        if (label == null || port == null) {
            throw new IllegalArgumentException();
        }
        this.label = label;
        this.output = port;
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public boolean isConnected() {
        return connected != null;
    }

    public boolean canConnect(JSynPort p) {
        return p != null
                && !isConnected()
                && !p.isConnected()
                && !p.getModule().equals(getModule())
                && !p.getPortType().equals(getPortType());
    }

    public JSynPort getConnected() {
        if (!isConnected()) {
            throw new IllegalStateException();
        }
        return connected;
    }

    public String getLabel() {
        return label;
    }

    public PortType getPortType() {
        if (input != null) {
            return PortType.INPUT;
        } else {
            return PortType.OUTPUT;
        }
    }

    public void connect(JSynPort p) {
        if (isConnected()) {
            throw new IllegalStateException();
        }
        if (!canConnect(p)) {
            throw new IllegalArgumentException();
        }

        if (getPortType().equals(PortType.INPUT)) {
            input.connect(p.output);
        } else {
            output.connect(p.input);
        }

        connected = p;
        p.connected = this;

        p.firePropertyChange(PropertyType.PORT_CONNECTED, true);
        firePropertyChange(PropertyType.PORT_CONNECTED, true);
    }

    public void disconnect() {
        if (!isConnected()) {
            throw new IllegalStateException();
        }
        if (getPortType().equals(PortType.INPUT)) {
            input.disconnect(connected.output);
        } else {
            output.disconnect(connected.input);
        }
        JSynPort old = connected;
        connected.connected = null;
        connected = null;

        old.firePropertyChange(PropertyType.PORT_CONNECTED, false);
        firePropertyChange(PropertyType.PORT_CONNECTED, false);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !JSynPort.class.isInstance(o)) {
            return false;
        }
        JSynPort p = (JSynPort) o;
        return p.module.equals(module) && p.input.equals(input)
                && p.label.equals(label);
    }
}
