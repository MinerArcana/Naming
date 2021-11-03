package com.minerarcana.naming.advancement.criteria.messaged;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
import java.util.Arrays;
import java.util.regex.Pattern;

public class MessagedCriterionTrigger extends AbstractCriterionTrigger<MessagedCriterionInstance> {
    public static final ResourceLocation ID = Naming.rl("messaged");

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected MessagedCriterionInstance createInstance(JsonObject pJson, EntityPredicate.AndPredicate pEntityPredicate, ConditionArrayParser pConditionsParser) {
        MessageTarget messageTarget = MessageTarget.byName(JSONUtils.getAsString(pJson, "target"))
                .orElseThrow(() -> new JsonParseException(String.format(
                        "Invalid MessageTarget not in [%s]",
                        Arrays.toString(MessageTarget.values())
                )));
        if (pJson.has("pattern")) {
            return new MessagedCriterionInstance(pEntityPredicate, -1, Pattern.compile(JSONUtils.getAsString(pJson, "pattern")), messageTarget);
        } else {
            return new MessagedCriterionInstance(pEntityPredicate, JSONUtils.getAsInt(pJson, "phrases"), null, messageTarget);
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, String phrase, MessageTarget messageTarget) {
        INamer namer = player.getCapability(Namer.CAP)
                .orElse(new Namer());
        this.trigger(player, instance -> instance.matches(namer, phrase, messageTarget));
    }

    public MessagedCriterionInstance phrases(int i, MessageTarget messageTarget) {
        return new MessagedCriterionInstance(EntityPredicate.AndPredicate.ANY, i, null, messageTarget);
    }
}
