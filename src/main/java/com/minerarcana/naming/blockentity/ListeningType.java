package com.minerarcana.naming.blockentity;

public enum ListeningType {
    NONE(false, false),
    LISTENING(true, false),
    CONSUMING(true, true);

    private final boolean listening;
    private final boolean consuming;

    ListeningType(boolean listening, boolean consuming) {
        this.listening = listening;
        this.consuming = consuming;
    }

    public boolean isListening() {
        return listening;
    }

    public boolean isConsuming() {
        return consuming;
    }
}
