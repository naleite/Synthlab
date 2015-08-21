package fr.istic.project.view.module;

import fr.istic.project.model.module.Module;
import fr.istic.project.model.module.port.JSynPort;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.module.port.FxPort;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.HashSet;
import java.util.Set;

public abstract class FxModule<T extends Module> extends BorderPane {

    private final T model;
    private Set<FxPort> ports;
    private Label name;

    public FxModule(T model) {
        if (model == null) {
            throw new IllegalArgumentException();
        }
        this.model = model;
        createName();
        createPorts();
        createController();
    }

    public T getModel() {
        return model;
    }

    public Set<FxPort> getPorts() {
        return ports;
    }

    private void createName() {
        name = new Label(model.toString());
        name.setFont(new Font(20));
        BorderPane top = new BorderPane(name);

        top.getStyleClass().add("module-header");
        setTop(top);
    }

    private void createPorts() {
        ports = new HashSet<>();
        HBox pane = new HBox();

        pane.getStyleClass().add("module-footer");
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(10);

        setBottom(pane);

        for (JSynPort port : model.getInputs()) {
            FxPort j = new FxPort(port);
            ports.add(j);
            pane.getChildren().add(j);
        }

        for (JSynPort port : model.getOutputs()) {
            FxPort j = new FxPort(port);
            ports.add(j);
            pane.getChildren().add(j);
        }
    }

    private void createController() {
        model.addObserver(PropertyType.MODULE_POSITION_CHANGED, (o, arg) -> relocate(model.getPosition().getX(), model.getPosition().getY()));
        setOnMouseClicked(event -> toFront());
        setOnMouseDragged(event -> toFront());
    }
}
