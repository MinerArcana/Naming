package com.minerarcana.naming.advancement.criteria.heard;

import com.google.gson.JsonObject;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.api.capability.INamer;
import com.minerarcana.naming.capability.Namer;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.regex.Pattern;

public class HeardCriterionTrigger extends AbstractCriterionTrigger<HeardCriterionInstance> {
    public static final ResourceLocation ID = Naming.rl("heard");

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected HeardCriterionInstance createInstance(JsonObject pJson, EntityPredicate.AndPredicate pEntityPredicate, ConditionArrayParser pConditionsParser) {
        if (pJson.has("pattern")) {
            return new HeardCriterionInstance(pEntityPredicate, -1, Pattern.compile(JSONUtils.getAsString(pJson, "pattern")));
        } else {
            return new HeardCriterionInstance(pEntityPredicate, JSONUtils.getAsInt(pJson, "phrases"), null);
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, String heard) {
        INamer namer = player.getCapability(Namer.CAP)
                .orElse(new Namer());
        this.trigger(player, instance -> instance.matches(namer, heard));
    }

    public HeardCriterionInstance phrases(int i) {
        return new HeardCriterionInstance(EntityPredicate.AndPredicate.ANY, i, null);
    }
}
