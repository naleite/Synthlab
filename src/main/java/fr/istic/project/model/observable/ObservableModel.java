package fr.istic.project.model.observable;

import java.util.Observer;
import java.util.Set;

/**
 * An interface to overcome the lack of specification on this in the API.
 * @inv getObservers(p) != nulls
 */
public interface ObservableModel {

    /**
     * The number of observers registered with this model.
     *
     * @param property the property type.
     * @pre property != null
     * @return the observer list for the property
     */
    Set<Observer> getObservers(PropertyType property);

    /**
     * Registers an observer on this model if it is not already there.
     *
     * @param property the property type.
     * @param o the observer
     * @pre o != null
     * @pre property != null
     * @pre !getObservers(property).contains(o)
     * @post getObservers(property).contains(o)
     */
    void addObserver(PropertyType property, Observer o);

    /**
     * Registers an observer on this model if it is not already there.
     *
     * @param property the property type.
     * @param o the observer
     * @pre o != null
     * @pre property != null
     * @pre getObservers(property).contains(o)
     * @post !getObservers(property).contains(o)
     */
    void removeObserver(PropertyType property, Observer o);

    /**
     * Notifies all observers.
     */
    void notifyObservers();
}
