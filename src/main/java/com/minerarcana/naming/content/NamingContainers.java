package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.container.ListeningStoneContainer;
import com.minerarcana.naming.screen.ListeningStoneScreen;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.inventory.container.ContainerType;

public class NamingContainers {
    
    public static final RegistryEntry<ContainerType<ListeningStoneContainer>> LISTENING_STONE = Naming.getRegistrate()
            .container(ListeningStoneContainer::new, () -> ListeningStoneScreen::new)
            .register();
    
    public static void setup() {
        
    }
}
