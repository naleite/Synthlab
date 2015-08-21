package fr.istic.project.model.module;

import com.jsyn.unitgen.UnitGenerator;
import fr.istic.project.model.memento.Caretaker;
import fr.istic.project.model.module.port.JSynPort;
import fr.istic.project.model.observable.ObservableModel;
import javafx.geometry.Point2D;

import java.util.List;
import java.util.Set;

/**
 * Defines the behavior of a Module.
 * @inv getPosition() != null
 * @inv getCircuit() != null
 * @inv getEntities() != null
 * @inv getInputLabel() != null
 * @inv getOutputLabel() != null
 * @inv getInputByLabel() != null
 * @inv getOutputByLabel() != null
 * @inv getAllConnectedInput() != null
 * @inv getAllConnectedOutput() != null
 */
public interface Module extends ObservableModel, Caretaker {

    public double MAX_VOLTAGE = 5.0;

    /**
     * Returns all input labels.
     *
     * @return the list of inputs label.
     */
    Set<String> getInputLabel();

    /**
     * Returns all output labels.
     *
     * @return the list of outputs label.
     */
    Set<String> getOutputLabel();

    /**
     * Returns all input labels.
     *
     * @return the list of inputs.
     */
    Set<JSynPort> getInputs();

    /**
     * Returns all output labels.
     *
     * @return the list of outputs.
     */
    Set<JSynPort> getOutputs();

    /**
     * Returns the port by label.
     *
     * @param label the input label.
     * @pre label != null
     * @pre getInputLabel().contains(label)
     * @return the input.
     */
    JSynPort getInputByLabel(String label);

    /**
     * Returns the port by label.
     *
     * @param label the output label.
     * @pre label != null
     * @pre getOutputLabel().contains(label)
     * @return the output.
     */
    JSynPort getOutputByLabel(String label);

    /**
     * Returns true if the input is available.
     *
     * @param label the input label.
     * @pre label != null
     * @pre getInputLabel().contains(label)
     * @return true if the input is available, false if not.
     */
    boolean isAvailableInput(String label);

    /**
     * Returns true if the output is available.
     *
     * @param label the output label
     * @pre label != null
     * @pre getOutputLabel().contains(label)
     * @return true if the output is available, false if not.
     */
    boolean isAvailableOutput(String label);

    /**
     * Returns the circuit.
     *
     * @return the module's circuit.
     */
    List<UnitGenerator> getCircuit();

    /**
     * Returns the activate status.
     *
     * @return true if the module is activated, false if not.
     */
    boolean isActivated();

    /**
     * Returns the position of the module.
     *
     * @return the module position.
     */
    Point2D getPosition();

    /**
     * Adds a input port.
     *
     * @param label the input identifier.
     * @pre label != null
     * @pre !getInputLabel().contains(label)
     * @post isAvailableInput(label)
     * @post getInputLabel().contains(label)
     */
    void addInput(String label);

    /**
     * Adds an output port.
     *
     * @param label the output identifier.
     * @pre label != null
     * @pre !getOutputLabel().contains(label)
     * @post isAvailableOutput(label)
     * @post getOutputLabel().contains(label)
     */
    void addOutput(String label);

    /**
     * Sets the activate status.
     *
     * @param activated the module state.
     * @post isActivated() == activated
     */
    void setActivated(boolean activated);

    /**
     * Adds Generator Entity.
     *
     * @param entity an entity to be added in the module.
     * @pre entity != null
     */
    void addEntity(UnitGenerator entity);

    /**
     * Adds Generator entities.
     *
     * @param entities a list of entities to be added in the module.
     * @pre entities.size != 0
     */
    void addEntities(UnitGenerator... entities);

    /**
     * Resets the module (remove wires, etc...)
     */
    void reset();

    /**
     * Returns the module's position.
     *
     * @param point the new position.
     * @pre point != null
     * @post getPosition().equals(point)
     */
    void setPosition(Point2D point);
}
