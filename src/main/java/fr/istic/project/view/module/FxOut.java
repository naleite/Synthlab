package fr.istic.project.view.module;

import fr.istic.project.model.module.out.Out;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.Knob;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FxOut extends FxModule<Out> {

    @FXML
    private Label attenuation;

    @FXML
    private Button muteBtn;

    @FXML
    private VBox knobs;

    private Knob attenuator;

    @FXML
    private void onMuteBtnClick() {
        getModel().setActivated(!getModel().isActivated());
    }

    public FxOut(Out module) {
        super(module);
        createView();
        createController();
    }

    private void createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/out.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        attenuator = new Knob("Attenuator", -100, Out.MAX_ATTENUATION, 0.001, 0);

        knobs.getChildren().add(attenuator);
    }

    private void createController() {
        // Listeners updating the model
        attenuator.valueProperty().addListener(event -> getModel().setAttenuation(attenuator.getValue()));

        // Observers updating the view
        getModel().addObserver(PropertyType.BOARD_ACTIVATED, (o, arg) -> {
            muteBtn.getStyleClass().removeAll("mute-off", "mute-on");
            if (getModel().isActivated()) {
                muteBtn.getStyleClass().add("mute-off");
            } else {
                muteBtn.getStyleClass().add("mute-on");
            }
        });

        getModel().addObserver(PropertyType.ATTENUATION_CHANGED, (o, arg) -> {
            attenuator.setRotation(getModel().getAttenuation());
            attenuation.setText(String.valueOf((int) getModel().getAttenuation()));
        });
    }
}
