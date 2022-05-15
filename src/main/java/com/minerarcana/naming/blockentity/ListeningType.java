package com.minerarcana.naming.blockentity;

import com.minerarcana.naming.content.NamingText;
import net.minecraft.network.chat.Component;

public enum ListeningType implements IButtoned<ListeningType> {
    NONE(false, false, NamingText.NONE),
    LISTENING(true, false, NamingText.LISTENING),
    CONSUMING(true, true, NamingText.CONSUMING);

    private final boolean listening;
    private final boolean consuming;
    private final Component message;

    ListeningType(boolean listening, boolean consuming, Component title) {
        this.listening = listening;
        this.consuming = consuming;
        this.message = title;
    }

    public boolean isListening() {
        return listening;
    }

    public boolean isConsuming() {
        return consuming;
    }

    @Override
    public Component getMessage() {
        return message;
    }

    @Override
    public ListeningType cycle() {
        return switch (this) {
            case NONE -> LISTENING;
            case LISTENING -> CONSUMING;
            case CONSUMING -> NONE;
        };
    }
}
