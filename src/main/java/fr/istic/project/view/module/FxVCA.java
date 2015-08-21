package fr.istic.project.view.module;

import fr.istic.project.model.module.vca.VCA;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.Knob;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FxVCA extends FxModule<VCA> {

    @FXML
    private VBox knobs;

    private Knob amplifier;

    public FxVCA(VCA module) {
        super(module);
        createView();
        createController();
    }

    private void createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/vca.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        amplifier = new Knob("amplification", 0, 10, 1);

        knobs.getChildren().addAll(amplifier);
    }

    private void createController() {
        // Listeners updating the model
        amplifier.valueProperty().addListener(e -> getModel().setAmplification((int) amplifier.getValue()));

        // Observers updating the view
        getModel().addObserver(PropertyType.AMPLIFICATION_CHANGED, (o, arg) -> amplifier.setRotation(getModel().getAmplification()));
    }

    @Override
    public String toString() {
        return Component.VCA.toString();
    }
}
