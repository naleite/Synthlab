package fr.istic.project.model.module.recorder;

import fr.istic.project.model.module.Module;

import java.io.IOException;

/**
 * Defines the behavior of a Output Module.
 * @inv getFilePath() != null
 */
public interface Recorder extends Module {

    String DEFAULT_FILEPATH = "output.wav";

    /**
     * Get the current file path.
     * @return the current file path.
     */
    String getFilePath();

    /**
     * Return true if it's recording.
     * @return true if it's recording.
     */
    boolean isRecording();

    /**
     * Set the current file path.
     * @pre path != null
     * @pre !isRecording()
     * @post getFilePath() != null
     * @param path the file path.
     */
    void setFilePath(String path) throws IOException;

    /**
     * Start the current recording. If getFilePath() already exists, it will be
     * erased.
     * @pre !isRecording()
     */
    void startRecording() throws IOException;

    /**
     * Stop the current recording.
     * @pre isRecording()
     */
    void stopRecording() throws IOException;
}
