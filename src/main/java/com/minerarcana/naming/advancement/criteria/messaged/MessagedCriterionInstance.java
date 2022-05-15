package com.minerarcana.naming.advancement.criteria.messaged;

import com.google.gson.JsonObject;
import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.regex.Pattern;

public class MessagedCriterionInstance extends AbstractCriterionTriggerInstance {
    private final int phrases;
    private final Pattern pattern;
    private final MessageTarget messageTarget;

    public MessagedCriterionInstance(EntityPredicate.Composite player, int phrases, Pattern pattern, MessageTarget messageTarget) {
        super(MessagedCriterionTrigger.ID, player);
        this.phrases = phrases;
        this.pattern = pattern;
        this.messageTarget = messageTarget;
    }

    public boolean matches(INamer namer, String heard, MessageTarget messageTarget) {
        if (this.messageTarget != messageTarget) {
            return false;
        } else if (pattern != null) {
            return pattern.matcher(heard).find();
        } else {
            return this.messageTarget.check(namer, phrases);
        }
    }

    @Override
    @Nonnull
    public JsonObject serializeToJson(@Nonnull SerializationContext pConditions) {
        JsonObject jsonObject = super.serializeToJson(pConditions);
        jsonObject.addProperty("target", messageTarget.name().toLowerCase(Locale.ROOT));
        if (pattern != null) {
            jsonObject.addProperty("pattern", pattern.pattern());
        } else {
            jsonObject.addProperty("phrases", phrases);
        }
        return jsonObject;
    }
}
