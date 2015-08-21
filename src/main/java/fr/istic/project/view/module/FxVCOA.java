package fr.istic.project.view.module;

import fr.istic.project.model.module.vcoa.VCOA;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.Knob;
import fr.istic.project.view.Switch;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FxVCOA extends FxModule<VCOA> {

    @FXML
    private HBox freqSwitch;

    @FXML
    private HBox knobs;

    private Knob octave;
    private Knob oscillatorType;
    private Knob fineTuning;
    private Switch refFreq;

    public FxVCOA(VCOA module) {
        super(module);
        createView();
        createController();
    }

    private void createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/vcoa.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        octave = new Knob("octave", VCOA.MIN_OCTAVE, VCOA.MAX_OCTAVE, 1, 0);
        fineTuning = new Knob("fineTuning", -1, 1, 0.001, 0);
        List<String> images = new ArrayList<>();
        images.add(getClass().getClassLoader().getResource("images/triangle-signal.png").toString());
        images.add(getClass().getClassLoader().getResource("images/square-signal.png").toString());
        images.add(getClass().getClassLoader().getResource("images/sawtooth-signal.png").toString());
        oscillatorType = new Knob("osc", 0, VCOA.OscillatorType.values().length - 1, 1, 1, images);
        refFreq = new Switch("LFO", "NFO");

        freqSwitch.getChildren().add(refFreq);
        knobs.getChildren().addAll(octave, fineTuning, oscillatorType);
    }

    private void createController() {
        // Listeners updating the model
        octave.valueProperty().addListener(event -> getModel().setOctave((int) octave.getValue()));
        fineTuning.valueProperty().addListener(event -> getModel().setFineTuning(fineTuning.getValue()));

        oscillatorType.valueProperty().addListener(event -> {
            VCOA.OscillatorType type;
            type = VCOA.OscillatorType.values()[(int) oscillatorType.getValue()];
            getModel().setOscillatorType(type);
        });

        refFreq.valueProperty().addListener(event -> {
            if (refFreq.isOn()) {
                getModel().setRefFrequency(VCOA.RefFrequencyType.LFO);
            } else {
                getModel().setRefFrequency(VCOA.RefFrequencyType.NFO);
            }
        });

        // Observers updating the view
        getModel().addObserver(PropertyType.OCTAVE_CHANGED, (o, arg) -> octave.setRotation(getModel().getOctave()));

        getModel().addObserver(PropertyType.FINE_TUNING_CHANGED, (o, arg) -> fineTuning.setRotation(getModel().getFineTuning()));

        getModel().addObserver(PropertyType.OSCILLATOR_TYPE_CHANGED, (o, arg) ->
                oscillatorType.setRotation(getModel().getOscillatorType().ordinal()));

        getModel().addObserver(PropertyType.REF_FREQUENCY_TYPE_CHANGED, (o, arg) -> {
            if (getModel().getRefFrequency() == VCOA.RefFrequencyType.LFO) {
                refFreq.switchOn();
            } else {
                refFreq.switchOff();
            }
        });
    }
}
