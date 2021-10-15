package com.minerarcana.naming.advancement.criteria.heard;

import com.google.gson.JsonObject;
import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.loot.ConditionArraySerializer;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class HeardCriterionInstance extends CriterionInstance {
    private final int phrases;
    private final Pattern pattern;

    public HeardCriterionInstance(EntityPredicate.AndPredicate player, int phrases, Pattern pattern) {
        super(HeardCriterionTrigger.ID, player);
        this.phrases = phrases;
        this.pattern = pattern;
    }

    public boolean matches(INamer namer, String heard) {
        if (pattern != null) {
            return pattern.matcher(heard).find();
        } else {
            return namer.getHeardMessages().size() >= phrases;
        }
    }

    @Override
    @Nonnull
    public JsonObject serializeToJson(@Nonnull ConditionArraySerializer pConditions) {
        JsonObject jsonObject = super.serializeToJson(pConditions);
        if (pattern != null) {
            jsonObject.addProperty("pattern", pattern.pattern());
        } else {
            jsonObject.addProperty("phrases", phrases);
        }
        return jsonObject;
    }
}
