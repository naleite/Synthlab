package fr.istic.project.model.module.player;

import fr.istic.project.model.module.Module;

import java.io.IOException;

/**
 * Defines the behavior of Player.
 */

public interface Player extends Module {

    /**
     * Get the current file path.
     * @return the current file path.
     */
    String getFilePath();

    /**
     * Return true if it's playing.
     * @return true if it's playing.
     */
    boolean isPlaying();

    /**
     * Set the current file path.
     * @pre path != null
     * @pre !isPlaying()
     * @post getFilePath() != null
     * @param path the file path.
     */
    void setFilePath(String path) throws IOException;

    /**
     * Start the looped playing.
     * @pre !isRecording()
     * @pre getFilePaht() != null
     */
    void startPlaying() throws IOException;


    /**
     * Stop the playing.
     * @pre isRecording()
     */
    void stopPlaying() throws IOException;
}