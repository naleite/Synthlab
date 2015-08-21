package fr.istic.project.model.module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.UnitGenerator;

import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.port.JSynPort;
import fr.istic.project.model.observable.AbstractObservableModel;
import fr.istic.project.model.observable.PropertyType;

import javafx.geometry.Point2D;

import java.util.*;

public abstract class AbstractModule extends AbstractObservableModel implements Module {

    /**
     * The module position.
     */
    private Point2D position;

    /**
     * Whether the module is activated or not.
     */
    private boolean activated;

    /**
     * The module circuit.
     */
    private final List<UnitGenerator> circuit;

    /**
     * List of inputs.
     */
    private final Map<String, JSynPort> inputs;

    /**
     * List of outputs.
     */
    private final Map<String, JSynPort> outputs;

    /**
     * Constructor.
     */
    protected AbstractModule() {
        position = new Point2D(0, 0);
        circuit = new ArrayList<>();
        inputs = new HashMap<>();
        outputs = new HashMap<>();
    }

    @Override
    public Set<String> getInputLabel() {
        return Collections.unmodifiableSet(inputs.keySet());
    }

    @Override
    public Set<String> getOutputLabel() {
        return Collections.unmodifiableSet(outputs.keySet());
    }

    @Override
    public Set<JSynPort> getInputs() {
        return Collections.unmodifiableSet(new HashSet<>(inputs.values()));
    }

    @Override
    public Set<JSynPort> getOutputs() {
        return Collections.unmodifiableSet(new HashSet<>(outputs.values()));
    }

    @Override
    public JSynPort getInputByLabel(String label) {
        if (label == null || !getInputLabel().contains(label)) {
            throw new IllegalArgumentException();
        }

        return inputs.get(label);
    }

    @Override
    public JSynPort getOutputByLabel(String label) {
        if (label == null || !getOutputLabel().contains(label)) {
            throw new IllegalArgumentException();
        }

        return outputs.get(label);
    }

    @Override
    public boolean isAvailableInput(String label) {
        if (label == null || !getInputLabel().contains(label)) {
            throw new IllegalArgumentException();
        }

        return !inputs.get(label).isConnected();
    }

    @Override
    public boolean isAvailableOutput(String label) {
        if (label == null || !getOutputLabel().contains(label)) {
            throw new IllegalArgumentException();
        }
        return !outputs.get(label).isConnected();
    }

    @Override
    public List<UnitGenerator> getCircuit() {
        return circuit;
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public void addInput(String label) {
        if (label == null || getInputLabel().contains(label)) {
            throw new IllegalArgumentException();
        }
        addInput(label, new UnitInputPort(label));
    }

    @Override
    public void addOutput(String label) {
        if (label == null || getOutputLabel().contains(label)) {
            throw new IllegalArgumentException();
        }
        addOutput(label, new UnitOutputPort(label));
    }

    protected void addOutput(String label, UnitOutputPort p) {
        if (label == null || getOutputLabel().contains(label) || p == null) {
            throw new IllegalArgumentException();
        }
        JSynPort port = new JSynPort(this, label, p);
        outputs.put(label, port);
    }

    protected void addInput(String label, UnitInputPort p) {
        if (label == null || getInputLabel().contains(label) || p == null) {
            throw new IllegalArgumentException();
        }
        JSynPort port = new JSynPort(this, label, p);
        inputs.put(label, port);
    }

    @Override
    public void setActivated(boolean activated) {
        Boolean old = isActivated();
        this.activated = activated;
        if (activated) {
            for (UnitGenerator unitGenerator : circuit) {
                unitGenerator.start();
            }
        } else {
            for (UnitGenerator unitGenerator : circuit) {
                unitGenerator.stop();
            }
        }
        firePropertyChange(PropertyType.BOARD_ACTIVATED, isActivated(), old);
    }

    @Override
    public void addEntity(UnitGenerator entity) {
        if (entity == null) {
            throw new IllegalArgumentException();
        }
        circuit.add(entity);
    }

    @Override
    public void addEntities(UnitGenerator... entities) {
        if (entities.length == 0) {
            throw new IllegalArgumentException();
        }

        for (UnitGenerator entity : entities) {
            addEntity(entity);
        }
    }

    @Override
    public void reset() {
        inputs.values().stream().filter(JSynPort::isConnected).forEach(JSynPort::disconnect);
        outputs.values().stream().filter(JSynPort::isConnected).forEach(JSynPort::disconnect);
    }

    @Override
    public void setPosition(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }
        this.position = new Point2D(Math.max(point.getX(), 0), Math.max(point.getY(), 0));
        firePropertyChange(PropertyType.MODULE_POSITION_CHANGED, point);
    }

    @Override
    public Memento getMemento() {
        Map<String, Object> data = new HashMap<>();

        data.put("x", getPosition().getX());
        data.put("y", getPosition().getY());

        return new Memento(data);
    }

    @Override
    public void setMemento(Memento m) {
        if (m == null) {
            throw new IllegalArgumentException();
        }
        Map<String, Object> data = m.getData();
        Point2D point = new Point2D((double) data.get("x"), (double) data.get("y"));
        setPosition(point);
    }
}
