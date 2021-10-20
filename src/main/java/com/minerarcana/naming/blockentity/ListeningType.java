package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.content.NamingText;
import net.minecraft.util.text.ITextComponent;

public enum ListeningType {
    NONE(false, false, NamingText.NONE),
    LISTENING(true, false, NamingText.LISTENING),
    CONSUMING(true, true, NamingText.CONSUMING);

    private final boolean listening;
    private final boolean consuming;
    private final ITextComponent message;

    ListeningType(boolean listening, boolean consuming, ITextComponent message) {
        this.listening = listening;
        this.consuming = consuming;
        this.message = message;
    }

    public boolean isListening() {
        return listening;
    }

    public boolean isConsuming() {
        return consuming;
    }

    public ITextComponent getMessage() {
        return message;
    }

    public ListeningType cycle() {
        switch (this) {
            case NONE:
                return LISTENING;
            case LISTENING:
                return CONSUMING;
            case CONSUMING:
                return NONE;
        }
        return NONE;
    }
}
