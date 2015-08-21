package fr.istic.project.model;

import fr.istic.project.model.memento.Caretaker;
import fr.istic.project.model.module.Module;
import fr.istic.project.model.observable.ObservableModel;

import javafx.scene.paint.Color;

import java.util.Map;

/**
 * Defines the behavior of a Board, the main Model of our Application.
 *
 * @inv getModules() != null
 * @inv getModules() != null
 * @inv getModules().containsValue(getModules())
 * @inv getCurrentColor() != null
 */
public interface Board extends ObservableModel, Caretaker {

    /**
     * Returns all modules.
     *
     * @return a map of all modules.
     */
    Map<Long, Module> getModules();

    /**
     * Returns the currently selected color.
     *
     * @return the color
     */
    Color getCurrentColor();

    /**
     * Returns the module by id.
     *
     * @param id the module identifier.
     * @pre getModules().containsKey(id)
     * @return the module.
     */
    Module getModule(long id);

    /**
     * Returns the module by id.
     *
     * @param klass the module class.
     * @param id the module identifier.
     * @param <T> the module class type.
     * @pre getModules().containsKey(id)
     * @return the module.
     */
    <T extends Module> T getModule(Class<T> klass, long id);

    /**
     * Returns a module's id.
     *
     * @param m the module
     * @pre getModules().containsValue(m)
     * @return the module id.
     */
    Long getModuleID(Module m);

    /**
     * Returns the activated status.
     *
     * @return the board's status.
     */
    boolean isActivated();

    /**
     * Sets the activate status.
     *
     * @param activate the new board's status.
     * @post isActivated() = activate
     */
    void setActivated(boolean activate);

    /**
     * Sets the current color.
     *
     * @param color the new color value.
     * @pre color != null
     * @post getCurrentColor().equals(color);
     */
    void setCurrentColor(Color color);

    /**
     * Creates a new module with the class klass. Return the id of the new Module.
     *
     * @param klass the module's class.
     * @pre klass != null
     * @post getModules().values().size() == (old getModules().values().size()) + 1
     * @post getModules().keySet().size() == (old getModules().keySet().size()) + 1
     * @post klass.isInstance(getModules(klass, result))
     * @return the module identifier.
     * @throws IllegalAccessException if an illegal access exception occurs.
     * @throws InstantiationException if an instantiation exception occurs.
     */
    Long addModule(Class<? extends Module> klass) throws IllegalAccessException, InstantiationException;

    /**
     * Deletes the module of id id.
     *
     * @param id the module identifier.
     * @pre getModules().containsKey(id)
     * @post getModules().values().size() == (old getModules().values().size()) - 1
     * @post getModules().keySet().size() == (old getModules().keySet().size()) - 1
     * @post !getModules().containsValue((old getModule(id)))
     * @post !getModules().containsKey(id)
     */
    void removeModule(long id);

    /**
     * Deletes the module m.
     *
     * @param m the module.
     * @pre m != null
     * @pre getModules().containsValue(m)
     * @post getModules().values().size() == (old getModules().values().size()) - 1
     * @post getModules().keySet().size() == (old getModules().keySet().size()) - 1
     * @post !getModules().containsKey((old getModuleID(m)))
     * @post !getModules().containsValue(m)
     */
    void removeModule(Module m);

    /**
     * Restores the board's modules to his initial state.
     * @post getModules().values().size() == 0
     * @post getModules().keySet().size() == 0
     */
    void reset();
}
