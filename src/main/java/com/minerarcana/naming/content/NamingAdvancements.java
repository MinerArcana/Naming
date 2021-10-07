package com.minerarcana.naming.content;

import com.mojang.datafixers.util.Function3;
import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class NamingAdvancements {
    public static void generateAdvancements(RegistrateAdvancementProvider provider) {
        Function3<IItemProvider, String, String, DisplayInfo> displayInfo = displayInfoFunction(provider);

        Advancement magicOfNames = Advancement.Builder.advancement()
                .addCriterion("root", InventoryChangeTrigger.Instance.hasItems(Items.BOOK))
                .display(
                        new ItemStack(Items.BOOK),
                        provider.title("naming", "magic_of_names", "Magic of Names"),
                        provider.desc("naming", "magic_of_names", "The Magic of Names: The Namer's Art"),
                        new ResourceLocation("textures/gui/advancements/backgrounds/adventure.png"),
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .save(provider, "magic_of_names");

        Advancement firstInitiation = Advancement.Builder.advancement()
                .addCriterion("first", NamingCriteriaTriggers.SIGNING.create(Items.WRITTEN_BOOK))
                .display(displayInfo.apply(
                        Items.WRITTEN_BOOK,
                        "The First Degree",
                        "Sign a Written Book"
                ))
                .parent(magicOfNames)
                .save(provider, "first_initiation");

    }

    private static Function3<IItemProvider, String, String, DisplayInfo> displayInfoFunction(RegistrateAdvancementProvider provider) {
        return (item, title, desc) -> {
            String name = title.toLowerCase(Locale.ROOT)
                    .replace(" ", "_");
            return new DisplayInfo(
                    new ItemStack(item),
                    provider.title("naming", name, title),
                    provider.desc("naming", name, desc),
                    null,
                    FrameType.TASK,
                    true,
                    true,
                    false
            );
        };
    }
}
