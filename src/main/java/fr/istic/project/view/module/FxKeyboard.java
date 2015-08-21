package fr.istic.project.view.module;

import fr.istic.project.model.module.keyboard.Keyboard;
import fr.istic.project.model.observable.PropertyType;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;

public class FxKeyboard extends FxModule<Keyboard> {

    @FXML
    private Label octaveLabel;

    public FxKeyboard(Keyboard module) {
        super(module);
        createView();
        createController();
    }

    private void createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/keyboard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        octaveLabel.setText(String.valueOf((int) getModel().getOctave()));
    }

    private void createController() {
        // We need to focus the module to be able to handle key events.
        setOnMouseClicked(event -> requestFocus());

        // TODO: Handle multiple unreleased keys.
        // Key events handler.
        setOnKeyPressed(event -> {
            if (!event.isShortcutDown()) {
                getModel().handleKeyPressed(event.getCode());
            }
        });

        setOnKeyReleased(event -> {
            if (!event.isShortcutDown()) {
                getModel().handleKeyReleased(event.getCode());
            }
        });

        // Observers updating the view.
        getModel().addObserver(PropertyType.KB_OCTAVE_CHANGED,
                (o, arg) -> octaveLabel.setText(String.valueOf((int) getModel().getOctave())));
    }
}
