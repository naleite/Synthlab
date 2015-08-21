package fr.istic.project.view.module;

import fr.istic.project.model.module.vcfa.VCFA;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.Knob;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FxVCFA extends FxModule<VCFA> {

    @FXML
    private VBox knobs;

    private Knob cutoffFrequency;
    private Knob gain;

    public FxVCFA(VCFA model) {
        super(model);
        createView();
        createController();
    }

    private void createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/vcfa.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        cutoffFrequency = new Knob("frequency", VCFA.MIN_FREQ, VCFA.MAX_FREQ, 100);
        gain = new Knob("gain", VCFA.MIN_GAIN, VCFA.MAX_GAIN, 0.01);

        knobs.getChildren().addAll(cutoffFrequency, gain);
    }

    private void createController() {
        // Listeners updating the model
        cutoffFrequency.valueProperty().addListener(e -> getModel().setCutoffFrequency(cutoffFrequency.getValue()));
        gain.valueProperty().addListener(e -> getModel().setGain(gain.getValue()));

        // Observers updating the view
        getModel().addObserver(PropertyType.FILTER_VALUE_CHANGED, (o, arg) -> cutoffFrequency.setRotation(getModel().getCutoffFrequency()));
        getModel().addObserver(PropertyType.GAIN_VALUE_CHANGED, (o,arg) -> gain.setRotation(getModel().getGain()));
    }

    @Override
    public String toString() {
        return Component.VCFA.toString();
    }
}
