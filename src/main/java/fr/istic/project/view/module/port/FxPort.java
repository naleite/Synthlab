package fr.istic.project.view.module.port;

import fr.istic.project.model.module.port.JSynPort;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class FxPort extends VBox {

    private final JSynPort model;

    @FXML
    private Button port;

    @FXML
    private Label label;

	public FxPort(JSynPort model) {
        if (model == null) {
            throw new IllegalArgumentException();
        }
		this.model = model;

        final FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getClassLoader().getResource("fxml/port.fxml");
        loader.setLocation(url);
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException ignore) {
            //nothing
        }

        if (model.getPortType().equals(JSynPort.PortType.INPUT)) {
            port.getStyleClass().add("input");
        } else {
            port.getStyleClass().add("output");
        }

        label.setLabelFor(port);
        label.setText(model.getLabel());
    }

	public JSynPort getModel() {
		return model;
	}

    public void setOnAction(EventHandler<ActionEvent> value) {
        port.setOnAction(value);
    }

    public void setOnResize(ChangeListener<Number> cl) {
        if (cl == null) {
            throw new IllegalArgumentException();
        }
        heightProperty().addListener(cl);
        widthProperty().addListener(cl);
    }

    public void setOnMove(ChangeListener<Number> cl) {
        if (cl == null) {
            throw new IllegalArgumentException();
        }

        port.layoutXProperty().addListener(cl);
        port.layoutYProperty().addListener(cl);
        layoutXProperty().addListener(cl);
        layoutYProperty().addListener(cl);
    }

    public double getX() {
        double result = 0;
        List<Node> parents = new LinkedList<>();
        parents.add(port.getParent());

        for (int i = 0; i < parents.size(); i += 1) {
            Node p = parents.get(i);
            if (p != null) {
                result += p.getLayoutX();
                parents.add(p.getParent());
            }
        }
        return result + port.getLayoutX() + port.getWidth() / 2;
    }

    public double getY() {
        double windowY = 0;
        double containerY = 0;
        double result = 0;
        List<Node> parents = new LinkedList<>();
        parents.add(port.getParent());

        for (int i = 0; i < parents.size(); i += 1) {
            Node p = parents.get(i);
            if (p != null) {
                containerY = windowY;
                windowY = p.getLayoutY();
                result += p.getLayoutY();
                parents.add(p.getParent());
            }
        }

        return result + (windowY - containerY) + port.getLayoutY() + port.getHeight() / 2;
    }

    public void select() {
        getStyleClass().add("portSelected");
    }

    public void deselect() {
        getStyleClass().remove("portSelected");
    }
}
