package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.blockentity.ListeningType;
import com.minerarcana.naming.blockentity.SpeakingTarget;
import com.minerarcana.naming.container.ListeningContainer;
import com.minerarcana.naming.container.SpeakingContainer;
import com.minerarcana.naming.screen.MessageScreen;
import com.minerarcana.naming.screen.SpeakingStoneScreen;
import com.tterrag.registrate.util.entry.MenuEntry;
public class NamingContainers {

    public static final MenuEntry<ListeningContainer> LISTENING = Naming.getRegistrate()
            .object("listening_stone")
            .<ListeningContainer, MessageScreen<ListeningContainer, ListeningType>>menu(
                    ListeningContainer::new,
                    () -> MessageScreen::new
            )
            .register();

    public static final MenuEntry<SpeakingContainer> SPEAKING = Naming.getRegistrate()
            .object("speaking_stone")
            .<SpeakingContainer, MessageScreen<SpeakingContainer, SpeakingTarget>>menu(
                    SpeakingContainer::new,
                    () -> SpeakingStoneScreen::new
            )
            .register();

    public static void setup() {

    }
}
