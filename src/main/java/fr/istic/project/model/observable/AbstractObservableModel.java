package fr.istic.project.model.observable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class AbstractObservableModel extends Observable implements ObservableModel {

    private final Map<PropertyType, Set<Observer>> obs;

    protected AbstractObservableModel() {
        obs = new HashMap<>();
    }

    @Override
    public Set<Observer> getObservers(PropertyType property) {
        if (property == null) {
            throw new IllegalArgumentException();
        }
        Set<Observer> result = obs.get(property);
        if (result == null) {
            result = Collections.emptySet();
        }
        return result;
    }

    @Override
    public void addObserver(PropertyType property, Observer o) {
        if (property == null ||  o == null) {
            throw new IllegalArgumentException();
        }
        if (getObservers(property).contains(o)) {
            throw new IllegalArgumentException();
        }
        Set<Observer> set = obs.get(property);
        if (set == null) {
            set = new HashSet<>();
            obs.put(property, set);
        }
        set.add(o);
        o.update(this, null);
    }

    @Override
    public void removeObserver(PropertyType property, Observer o) {
        if (property == null ||  o == null) {
            throw new IllegalArgumentException();
        }
        if (!getObservers(property).contains(o)) {
            throw new IllegalArgumentException();
        }
        Set<Observer> set = obs.get(property);
        set.remove(o);
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
        for (Set<Observer> set : obs.values()) {
            for (Observer o : set) {
                o.update(this, null);
            }
        }
    }

    protected void firePropertyChange(PropertyType property, Object value) {
        if (property == null) {
            throw new IllegalArgumentException();
        }

        Set<Observer> set = obs.get(property);
        if (set == null) {
            set = new HashSet<>();
            obs.put(property, set);
        }
        Set<Observer> clone = new HashSet<>(set);
        for (Observer o : clone) {
            o.update(this, value);
        }
    }

    protected void firePropertyChange(PropertyType property,
        Object newValue, Object oldValue) {
        if (property == null) {
            throw new IllegalArgumentException();
        }
        if ((newValue != null && !newValue.equals(oldValue))
            || (newValue == null && oldValue != null)) {
            firePropertyChange(property, newValue);
        }
    }
}
