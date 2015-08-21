package fr.istic.project.model.memento;

/**
 * Defines the behavior of a generator of Memento.
 * @inv getMemento() != null
 */
public interface Caretaker {

    /**
     * Saves the current configuration.
     *
     * @return The current configuration.
     */
    Memento getMemento();

    /**
     * Restores an old configuration.
     *
     * @param m The memento containing the old configuration
     * @pre m != null
     * @throws IllegalAccessException if an illegal access exception occurs.
     */
    void setMemento(Memento m) throws IllegalAccessException;
}
