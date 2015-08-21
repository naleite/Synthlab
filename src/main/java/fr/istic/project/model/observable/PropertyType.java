package fr.istic.project.model.observable;

public enum PropertyType {

    // Board
    BOARD_ACTIVATED,

    // Color picker
    CURRENT_COLOR_CHANGED,

    // Ports
    PORT_CONNECTED,

    // Wire
    WIRE_POSITION_CHANGED,

    // Modules
    MODULE_LIST_CHANGED,
    MODULE_POSITION_CHANGED,

    // Out
    ATTENUATION_CHANGED,

    // VCA
    AMPLIFICATION_CHANGED,

    // Oscilloscope
    SCOPE_ACTIVATED,
    DATA_CHANGED,

    // VCFA, VCFB
    FILTER_VALUE_CHANGED,
    GAIN_VALUE_CHANGED,

    // VCOA
    OCTAVE_CHANGED,
    FINE_TUNING_CHANGED,
    OSCILLATOR_TYPE_CHANGED,
    REF_FREQUENCY_TYPE_CHANGED,

    // Recorder
    RECORDING,
    FILEPATH,

    // EG
    ATTACK_CHANGED,
    DECAY_CHANGED,
    SUSTAIN_CHANGED,
    RELEASE_CHANGED,

    // Mix
    IN1_CHANGED,
    IN2_CHANGED,
    IN3_CHANGED,
    IN4_CHANGED,
    AMF_CHANGED,
    OUT_CHANGED,

    // Sequencer
    LEVEL1_CHANGED,
    LEVEL2_CHANGED,
    LEVEL3_CHANGED,
    LEVEL4_CHANGED,
    LEVEL5_CHANGED,
    LEVEL6_CHANGED,
    LEVEL7_CHANGED,
    LEVEL8_CHANGED,

    // Player
    PLAYING,

    // Keyboard
    KB_OCTAVE_CHANGED,
    KB_SEMITONE_CHANGED,
    KB_NOTE_ON
}