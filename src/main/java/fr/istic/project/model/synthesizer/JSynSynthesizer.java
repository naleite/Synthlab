package fr.istic.project.model.synthesizer;

import com.jsyn.JSyn;
import com.jsyn.unitgen.UnitGenerator;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class JSynSynthesizer implements Synthesizer<com.jsyn.Synthesizer> {

    private final com.jsyn.Synthesizer synthesizer;
    private boolean started;
    private Collection<UnitGenerator> components;

    public JSynSynthesizer() {
        synthesizer = JSyn.createSynthesizer();
        components = new LinkedList<>();
    }

    @Override
    public com.jsyn.Synthesizer getSynthesizer() {
        return synthesizer;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public Collection<UnitGenerator> getComponents() {
        return Collections.unmodifiableCollection(components);
    }

    @Override
    public void add(UnitGenerator component) {
        if (component == null) {
            throw new IllegalArgumentException();
        }
        components.add(component);
        synthesizer.add(component);
    }

    @Override
    public void remove(UnitGenerator component) {
        if (component == null) {
            throw new IllegalArgumentException();
        }
        if (!getComponents().contains(component)) {
            throw new IllegalArgumentException();
        }
        components.remove(component);
        synthesizer.remove(component);
    }

    @Override
    public void addAll(Collection<UnitGenerator> components) {
        if (components == null) {
            throw new IllegalArgumentException();
        }
        for (UnitGenerator u : components) {
            add(u);
        }
    }

    @Override
    public void removeAll(Collection<UnitGenerator> components) {
        if (components == null) {
            throw new IllegalArgumentException();
        }
        if (!getComponents().containsAll(components)) {
            throw new IllegalArgumentException();
        }
        for (UnitGenerator u : components) {
            remove(u);
        }
    }

    @Override
    public void start() {
        if (isStarted()) {
            throw new IllegalStateException();
        }
        started = true;
        synthesizer.start();
    }

    @Override
    public void stop() {
        if (!isStarted()) {
            throw new IllegalStateException();
        }
        started = false;
        synthesizer.stop();
    }
}
