package fr.istic.project.model;

import com.google.gson.internal.LinkedTreeMap;

import fr.istic.project.model.memento.Memento;
import fr.istic.project.model.module.Module;
import fr.istic.project.model.module.port.JSynPort;
import fr.istic.project.model.observable.AbstractObservableModel;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.model.synthesizer.Synthesizer;
import fr.istic.project.model.synthesizer.JSynSynthesizer;
import fr.istic.project.view.module.Component;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JSynBoard extends AbstractObservableModel implements Board {

    public final static Synthesizer<com.jsyn.Synthesizer> engine = new JSynSynthesizer();

    private Color currentColor;
    private final Map<Long, Module> modules;
    private final Map<Module, Long> reverse;
    private final Map<JSynPort, Color> colors;
    private boolean isActivated;
    private long last;

    public JSynBoard() throws InstantiationException, IllegalAccessException {
        colors = new HashMap<>();
        currentColor = Color.WHITE;
        modules = new ConcurrentHashMap<>();
        reverse = new ConcurrentHashMap<>();
        last = 0;

        addObserver(PropertyType.BOARD_ACTIVATED, (o, arg) -> {
            for (Module m : modules.values()) {
                m.setActivated(isActivated());
            }
            if (isActivated() != engine.isStarted()) {
                if (isActivated()) {
                    engine.start();
                } else {
                    engine.stop();
                }
            }

        });

        addObserver(PropertyType.MODULE_LIST_CHANGED, (o, arg) -> {
            for (Module m : modules.values()) {
                m.setActivated(isActivated());
            }
        });
    }

    @Override
    public Map<Long, Module> getModules() {
        return Collections.unmodifiableMap(modules);
    }

    @Override
    public Color getCurrentColor() {
        return currentColor;
    }

    @Override
    public Module getModule(long id) {
        if (!getModules().containsKey(id)) {
            throw new IllegalArgumentException();
        }

        return modules.get(id);
    }

    @Override
    public <T extends Module> T getModule(Class<T> klass, long id) {
        if (klass == null) {
            throw new IllegalArgumentException();
        }
        if (!getModules().containsKey(id)) {
            throw new IllegalArgumentException();
        }

        return (T) modules.get(id);
    }

    @Override
    public Long getModuleID(Module m) {
        if (m == null) {
            throw new IllegalArgumentException();
        }
        if (!getModules().containsValue(m)) {
            throw new IllegalArgumentException();
        }

        return reverse.get(m);
    }

    @Override
    public Memento getMemento() {
        Map<String, Object> data = new HashMap<>();
        Map<Long, Memento> mementos = new HashMap<>();
        Map<Long, String> klass = new HashMap<>();
        List<String> inputs = new LinkedList<>();

        for (long id : modules.keySet()) {
            Module m = modules.get(id);
            mementos.put(id, m.getMemento());
            klass.put(id, m.getClass().getSimpleName());
            for (JSynPort i : m.getInputs()) {
                if (!i.isConnected()) {
                    continue;
                }
                String str =
                    id
                    + ";" + i.getLabel()
                    + ";"
                    + reverse.get(i.getConnected().getModule())
                    + ";"
                    + i.getConnected().getLabel()
                    + ";"
                    + colors.get(i)
                    ;
                inputs.add(str);
            }
        }
        data.put("mementos", mementos);
        data.put("class", klass);
        data.put("inputs", inputs);

        return new Memento(data);
    }

    @Override
    public boolean isActivated() {
        return isActivated;
    }

    @Override
    public void setActivated(boolean activate) {
        boolean old = isActivated();
        isActivated = activate;
        firePropertyChange(PropertyType.BOARD_ACTIVATED, isActivated(), old);
    }

    @Override
    public void setCurrentColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException();
        }
        Color old = getCurrentColor();
        currentColor = color;
        firePropertyChange(PropertyType.CURRENT_COLOR_CHANGED, getCurrentColor(), old);
    }

    @Override
    public Long addModule(Class<? extends Module> klass) throws IllegalAccessException, InstantiationException {
        if (klass == null) {
            throw new IllegalArgumentException();
        }
        last += 1;
        addModule(klass, last);

        return last;
    }

    @Override
    public void removeModule(long id) {
        if (!getModules().containsKey(id)) {
            throw new IllegalArgumentException();
        }
        Module m = modules.get(id);
        m.setActivated(false);
        for (JSynPort input : m.getInputs()) {
            if (input.isConnected()) {
                input.disconnect();
            }
        }
        for (JSynPort output : m.getOutputs()) {
            if (output.isConnected()) {
                output.disconnect();
            }
        }

        engine.removeAll(m.getCircuit());
        modules.remove(id);
        reverse.remove(m);
        firePropertyChange(PropertyType.MODULE_LIST_CHANGED, null);
    }

    @Override
    public void removeModule(Module m) {
        removeModule(getModuleID(m));
    }

    @Override
    public void reset() {
        last = 0;
        modules.keySet().forEach(this::removeModule);
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers();
        for (Module m : modules.values()) {
            m.notifyObservers();
        }
    }

    @Override
    public void setMemento(Memento m) {
        if (m == null) {
            throw new IllegalArgumentException();
        }
        reset();
        Color init = getCurrentColor();
        Map<String, Object> data = m.getData();
        LinkedTreeMap<String, Memento> mementos = (LinkedTreeMap<String, Memento>) data.get("mementos");
        Map<String, String> klass = (Map<String, String>) data.get("class");

        for (String stringID : klass.keySet()) {
            Long id = Long.parseLong(stringID);
            try {
                addModule(Component.getBySimpleName(klass.get(stringID)).getKlass(), id);
                String str = (String.valueOf(mementos.get(stringID)));
                Memento memento = new Memento(str);
                getModule(id).setMemento(memento);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        List<String> inputs = (List<String>) data.get("inputs");
        for (String i : inputs) {
            String id1 = i.split(";")[0];
            String id2 = i.split(";")[2];

            String input = i.split(";")[1];
            String output = i.split(";")[3];

            setCurrentColor(Color.valueOf(i.split(";")[4]));
            modules.get(Long.valueOf(id1)).getInputByLabel(input)
                .connect(modules.get(Long.valueOf(id2)).getOutputByLabel(output));
        }
        setCurrentColor(init);
    }

    protected void addModule(Class<? extends Module> klass, long id) throws IllegalAccessException, InstantiationException {
        if (klass == null) {
            throw new IllegalArgumentException();
        }
        last = id;
        Module m = klass.newInstance();
        modules.put(id, m);
        reverse.put(m, id);

        engine.addAll(m.getCircuit());
        for (JSynPort p : m.getInputs()) {
            p.addObserver(PropertyType.PORT_CONNECTED, (o, arg) -> {
                if (p.isConnected()) {
                    colors.put(p, getCurrentColor());
                } else {
                    colors.remove(p);
                }
            });
        }
        firePropertyChange(PropertyType.MODULE_LIST_CHANGED, null);
    }
}
