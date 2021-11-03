package com.minerarcana.naming.advancement.criteria.messaged;

import com.minerarcana.naming.api.capability.INamer;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public enum MessageTarget {
    SPEAK_TO(INamer::getHeardHistory),
    HEARD_BY(INamer::getSpokenHistory);

    private final Function<INamer, Collection<String>> getHistory;

    MessageTarget(Function<INamer, Collection<String>> getHistory) {
        this.getHistory = getHistory;
    }

    public boolean check(INamer namer, int phrases) {
        return this.getHistory.apply(namer).size() >= phrases;
    }

    public static Optional<MessageTarget> byName(String name) {
        for (MessageTarget messageTarget : values()) {
            if (messageTarget.name().equalsIgnoreCase(name)) {
                return Optional.of(messageTarget);
            }
        }
        return Optional.empty();
    }
}
