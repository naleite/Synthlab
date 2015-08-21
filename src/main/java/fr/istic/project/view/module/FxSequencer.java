package fr.istic.project.view.module;

import fr.istic.project.model.module.seq.Seq;
import fr.istic.project.model.observable.PropertyType;
import fr.istic.project.view.Knob;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

public class FxSequencer extends FxModule<Seq> {

    private final int KNOBS_PER_LINE = 4;

    @FXML
    private VBox knobs;

    @FXML
    private Button resetBtn;

    private SortedMap<PropertyType, Knob> knobList;

    /**
     * Constructor.
     *
     * @param model the sequencer model.
     */
    public FxSequencer(Seq model) {
        super(model);
        knobList = new TreeMap<>();

        createView();
        createController();
    }

    private void createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/seq.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        knobList.put(PropertyType.LEVEL1_CHANGED, new Knob("Level1", -1, 1, 0.01));
        knobList.put(PropertyType.LEVEL2_CHANGED, new Knob("Level2", -1, 1, 0.01));
        knobList.put(PropertyType.LEVEL3_CHANGED, new Knob("Level3", -1, 1, 0.01));
        knobList.put(PropertyType.LEVEL4_CHANGED, new Knob("Level4", -1, 1, 0.01));
        knobList.put(PropertyType.LEVEL5_CHANGED, new Knob("Level5", -1, 1, 0.01));
        knobList.put(PropertyType.LEVEL6_CHANGED, new Knob("Level6", -1, 1, 0.01));
        knobList.put(PropertyType.LEVEL7_CHANGED, new Knob("Level7", -1, 1, 0.01));
        knobList.put(PropertyType.LEVEL8_CHANGED, new Knob("Level8", -1, 1, 0.01));

        final int numberOfLine = knobList.size() / KNOBS_PER_LINE;

        List<HBox> lines = new ArrayList<>();

        for (int i = 0; i < numberOfLine; i++) {
            int knobIndex = 0;
            HBox line = new HBox();
            line.setSpacing(15);

            for (Knob knob : knobList.values()) {
                // Add KNOBS_PER_LINE knobs to each line
                if (knobIndex / KNOBS_PER_LINE == i) {
                    line.getChildren().add(knob);
                }
                knobIndex++;
            }
            lines.add(line);
        }

        knobs.getChildren().addAll(lines);
    }

    private void createController() {
        for (PropertyType type : knobList.keySet()) {
            Knob knob = knobList.get(type);

            knob.valueProperty().addListener(event -> getModel().setValue(type, knob.getValue()));
            getModel().addObserver(type, (o, arg) -> knob.setRotation(getModel().getValue(type)));
            resetBtn.setOnAction(event -> getModel().resetStep());
        }
    }
}
