package fr.istic.project.model.synthesizer;

import com.jsyn.unitgen.UnitGenerator;

import java.util.Collection;

/**
 * Defines the behavior of a Synthesizer.
 * The param T defines the type of the adaptee.
 * @param <T>
 * @inv
 *  getComponents() != null
 */
public interface Synthesizer<T> {

    /**
     * Gets the synthesizer adaptee.
     *
     * @return the synthesizer adaptee.
     */
    T getSynthesizer();

    /**
     * Checks if the synthesizer is activated or not.
     *
     * @return true if the synthesizer is activated, false if not.
     */
    boolean isStarted();

    /**
     * Returns a view of all components.
     * @return all components.
     */
    Collection<UnitGenerator> getComponents();

    /**
     * Adds a components to the synthesizer.
     *
     * @pre component != null
     * @post getComponents().contains(component)
     * @param component the components
     */
    void add(UnitGenerator component);

    /**
     * Removes a collection of components from the synthesizer.
     *
     * @pre component != null
     * @pre getComponents().contains(component)
     * @post !getComponents().contains(component)
     * @param component the components
     */
    void remove(UnitGenerator component);

    /**
     * Adds a collection of components to the synthesizer.
     *
     * @pre components != null
     * @post getComponents().containsAll(components)
     * @param components the collection of components
     */
    void addAll(Collection<UnitGenerator> components);

    /**
     * Removes a collection of components from the synthesizer.
     *
     * @pre components != null
     * @pre getComponents().containsAll(components)
     * @post !getComponents().containsAll(components)
     * @param components the collection of components
     */
    void removeAll(Collection<UnitGenerator> components);

    /**
     * Starts the synthesizer.
     *
     * @pre !isStarted()
     * @post isStarted()
     */
    void start();

    /**
     * Stops the synthesizer.
     *
     * @pre isStarted()
     * @post !isStarted()
     */
    void stop();
}
