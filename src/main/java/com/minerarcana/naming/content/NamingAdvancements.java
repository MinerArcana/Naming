package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.advancement.criteria.naming.EntityChecker;
import com.minerarcana.naming.advancement.criteria.naming.NamingCriterionInstance;
import com.minerarcana.naming.advancement.criteria.naming.NamingCriterionTrigger;
import com.mojang.datafixers.util.Function3;
import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.command.FunctionObject;
import net.minecraft.entity.EntityType;
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
                .build(Naming.rl("magic_of_names"));
        provider.accept(magicOfNames);

        Advancement firstInitiation = Advancement.Builder.advancement()
                .addCriterion("first", NamingCriteriaTriggers.SIGNING.create(Items.WRITTEN_BOOK))
                .display(displayInfo.apply(
                        Items.WRITTEN_BOOK,
                        "The First Degree",
                        "Sign a written book"
                ))
                .parent(magicOfNames)
                .rewards(rewardAbility("naming"))
                .build(Naming.rl("first_initiation"));
        provider.accept(firstInitiation);

        Advancement secondInitiation = Advancement.Builder.advancement()
                .display(displayInfo.apply(
                        Items.ENCHANTED_BOOK,
                        "The Second Degree",
                        "Name a villager, a hostile mob, and a tamed creature"
                ))
                .addCriterion("villager", NamingCriteriaTriggers.NAMING.entityPredicate(EntityPredicate.Builder.entity()
                        .of(EntityType.VILLAGER)
                        .build()
                ))
                .addCriterion("hostile", NamingCriteriaTriggers.NAMING.checker(EntityChecker.HOSTILE))
                .addCriterion("tamed", NamingCriteriaTriggers.NAMING.checker(EntityChecker.TAMED))
                .parent(firstInitiation)
                .rewards(rewardAbility("listening_stone"))
                .build(Naming.rl("second_initiation"));
        provider.accept(secondInitiation);

    }

    private static AdvancementRewards rewardAbility(String name) {
        return new AdvancementRewards(
                0,
                new ResourceLocation[0],
                new ResourceLocation[0],
                new FunctionObject.CacheableFunction(
                        Naming.rl(name)
                )
        );
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
