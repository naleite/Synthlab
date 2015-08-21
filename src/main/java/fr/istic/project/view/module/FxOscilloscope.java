package fr.istic.project.view.module;

import fr.istic.project.model.module.osc.Oscilloscope;
import fr.istic.project.model.observable.PropertyType;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class FxOscilloscope extends FxModule<Oscilloscope> {
    @FXML
    private SwingNode swingNode;

    public FxOscilloscope(Oscilloscope model) {
        super(model);

        createView();
        createController();
    }

    private void createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/osc.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void createController() {
        swingNode.setContent(getModel().getView());
        getModel().addObserver(PropertyType.SCOPE_ACTIVATED, (o, arg) -> {
            if (getModel().isScopeActivated()) {
                swingNode.setVisible(true);
            } else {
                swingNode.setVisible(false);
            }
        });
    }
}
