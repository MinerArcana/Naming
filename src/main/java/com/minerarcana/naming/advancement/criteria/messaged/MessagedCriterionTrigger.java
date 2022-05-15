package com.minerarcana.naming.advancement.criteria.messaged;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.api.capability.INamer;
import com.minerarcana.naming.capability.Namer;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.regex.Pattern;

public class MessagedCriterionTrigger extends SimpleCriterionTrigger<MessagedCriterionInstance> {
    public static final ResourceLocation ID = Naming.rl("messaged");

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected MessagedCriterionInstance createInstance(JsonObject pJson, EntityPredicate.Composite pEntityPredicate, DeserializationContext pConditionsParser) {
        MessageTarget messageTarget = MessageTarget.byName(GsonHelper.getAsString(pJson, "target"))
                .orElseThrow(() -> new JsonParseException(String.format(
                        "Invalid MessageTarget not in [%s]",
                        Arrays.toString(MessageTarget.values())
                )));
        if (pJson.has("pattern")) {
            return new MessagedCriterionInstance(pEntityPredicate, -1, Pattern.compile(GsonHelper.getAsString(pJson, "pattern")), messageTarget);
        } else {
            return new MessagedCriterionInstance(pEntityPredicate, GsonHelper.getAsInt(pJson, "phrases"), null, messageTarget);
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, String phrase, MessageTarget messageTarget) {
        INamer namer = player.getCapability(Namer.CAP)
                .orElse(new Namer());
        this.trigger(player, instance -> instance.matches(namer, phrase, messageTarget));
    }

    public MessagedCriterionInstance phrases(int i, MessageTarget messageTarget) {
        return new MessagedCriterionInstance(EntityPredicate.Composite.ANY, i, null, messageTarget);
    }
}
