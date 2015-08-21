package fr.istic.project.view.module;

import fr.istic.project.model.module.eg.EG;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.Knob;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FxEG extends FxModule<EG> {

    @FXML
    private VBox knobs;

    private Knob attack;
    private Knob decay;
    private Knob sustain;
    private Knob release;

    public FxEG(EG model) {
        super(model);
        createView();
        createController();
    }

    private void createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/eg.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        attack = new Knob("attack", 0, 2, 0.1);
        decay = new Knob("decay", 0, 5, 0.1);
        sustain = new Knob("sustain", 0, 1, 0.1);
        release = new Knob("release", 0, 8, 0.1);

        knobs.getChildren().addAll(attack, decay, sustain, release);
    }

    private void createController() {
        // Listeners updating the model
        attack.valueProperty().addListener((event) -> getModel().setAttack(attack.getValue()));
        decay.valueProperty().addListener((event) -> getModel().setDecay(decay.getValue()));
        sustain.valueProperty().addListener((event) -> getModel().setSustain(sustain.getValue()));
        release.valueProperty().addListener((event) -> getModel().setRelease(release.getValue()));

        // Observers updating the view
        getModel().addObserver(PropertyType.ATTACK_CHANGED, (o, arg) -> attack.setRotation(getModel().getAttack()));
        getModel().addObserver(PropertyType.DECAY_CHANGED, (o, arg) -> decay.setRotation(getModel().getDecay()));
        getModel().addObserver(PropertyType.SUSTAIN_CHANGED, (o, arg) -> sustain.setRotation(getModel().getSustain()));
        getModel().addObserver(PropertyType.RELEASE_CHANGED, (o, arg) -> release.setRotation(getModel().getRelease()));
    }
}
