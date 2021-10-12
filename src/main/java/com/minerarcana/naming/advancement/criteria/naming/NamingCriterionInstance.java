package com.minerarcana.naming.advancement.criteria.naming;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public abstract class NamingCriterionInstance extends CriterionInstance {
    public NamingCriterionInstance(EntityPredicate.AndPredicate predicate) {
        super(NamingCriterionTrigger.ID, predicate);
    }

    @Override
    @Nonnull
    public JsonObject serializeToJson(@Nonnull ConditionArraySerializer pConditions) {
        JsonObject jsonobject = super.serializeToJson(pConditions);
        jsonobject.addProperty("type", this.getType());
        return jsonobject;
    }

    public abstract String getType();

    public abstract boolean matches(ServerWorld world, Object object);
}
