package fr.istic.project.view.module;

import fr.istic.project.model.module.mix.Mix;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.Knob;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FxMix  extends FxModule<Mix> {

    @FXML
    private VBox knobs;

    @FXML
    private VBox knobs2;

    private Knob knob1;
    private Knob knob2;
    private Knob knob3;
    private Knob knob4;

    public FxMix(Mix module) {
        super(module);
        createView();
        createController();
    }

    private void createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/mix.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        knob1 = new Knob("in1", -100, Mix.MAX_ATTENUATION, 0.001);
        knob2 = new Knob("in2", -100, Mix.MAX_ATTENUATION, 0.001);
        knob3 = new Knob("in3", -100, Mix.MAX_ATTENUATION, 0.001);
        knob4 = new Knob("in4", -100, Mix.MAX_ATTENUATION, 0.001);

        knobs.getChildren().addAll(knob1, knob3);
        knobs2.getChildren().addAll(knob2, knob4);
    }

    private void createController() {
        // Listeners updating the model
        knob1.valueProperty().addListener((event) -> getModel().setGainIn1(knob1.getValue()));
        knob2.valueProperty().addListener((event) -> getModel().setGainIn2(knob2.getValue()));
        knob3.valueProperty().addListener((event) -> getModel().setGainIn3(knob3.getValue()));
        knob4.valueProperty().addListener((event) -> getModel().setGainIn4(knob4.getValue()));

        // Observers updating the view
        getModel().addObserver(PropertyType.IN1_CHANGED, (o, arg) -> knob1.setRotation(getModel().getGainIn1()));
        getModel().addObserver(PropertyType.IN2_CHANGED, (o, arg) -> knob2.setRotation(getModel().getGainIn2()));
        getModel().addObserver(PropertyType.IN3_CHANGED, (o, arg) -> knob3.setRotation(getModel().getGainIn3()));
        getModel().addObserver(PropertyType.IN4_CHANGED, (o, arg) -> knob4.setRotation(getModel().getGainIn4()));
    }
}
