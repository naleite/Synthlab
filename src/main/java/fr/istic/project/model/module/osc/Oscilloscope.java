package fr.istic.project.model.module.osc;

import com.jsyn.scope.swing.AudioScopeView;
import fr.istic.project.model.module.Module;

/**
 * Defines the behaviour of an Oscilloscope
 */
public interface Oscilloscope extends Module {

    public AudioScopeView getView();
    public boolean isScopeActivated();
}
