package com.minerarcana.naming.advancement.criteria.naming;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;

public abstract class NamingCriterionInstance extends AbstractCriterionTriggerInstance {
    public NamingCriterionInstance(EntityPredicate.Composite predicate) {
        super(NamingCriterionTrigger.ID, predicate);
    }

    @Override
    @Nonnull
    public JsonObject serializeToJson(@Nonnull SerializationContext pConditions) {
        JsonObject jsonobject = super.serializeToJson(pConditions);
        jsonobject.addProperty("type", this.getType());
        return jsonobject;
    }

    public abstract String getType();

    public abstract boolean matches(ServerLevel world, Object object);
}
