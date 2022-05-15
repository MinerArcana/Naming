package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class NamingItemTags {
    public static final TagKey<Item> CAN_ECHO = ItemTags.create(Naming.rl("can_echo"));

    public static void generate(RegistrateTagsProvider<Item> tagsProvider) {
        tagsProvider.tag(CAN_ECHO)
                .add(Items.PAPER);
    }
}
