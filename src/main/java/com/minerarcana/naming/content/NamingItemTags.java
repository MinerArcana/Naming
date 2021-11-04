package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

public class NamingItemTags {
    public static final Tags.IOptionalNamedTag<Item> CAN_ECHO = ItemTags.createOptional(Naming.rl("can_echo"));

    public static void generate(RegistrateTagsProvider<Item> tagsProvider) {
        tagsProvider.tag(CAN_ECHO)
                .add(Items.PAPER);
    }
}
