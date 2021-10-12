package com.minerarcana.naming.advancement.criteria.naming;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Locale;

public class EntityNamingCriterionInstance extends NamingCriterionInstance {
    private final EntityPredicate named;
    private final EntityChecker checker;

    public EntityNamingCriterionInstance(EntityPredicate.AndPredicate playerPredicate, EntityPredicate named) {
        super(playerPredicate);
        this.named = named;
        this.checker = null;
    }

    public EntityNamingCriterionInstance(EntityPredicate.AndPredicate playerPredicate, EntityChecker checker) {
        super(playerPredicate);
        this.named = null;
        this.checker = checker;
    }


    public boolean matches(ServerWorld world, Object object) {
        if (object instanceof Entity) {
            if (named != null) {
                return named.matches(world, Vector3d.ZERO, (Entity) object);
            } else if (checker != null) {
                return checker.matches((Entity) object);
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public JsonObject serializeToJson(@Nonnull ConditionArraySerializer pConditions) {
        JsonObject jsonObject = super.serializeToJson(pConditions);
        if (named != null) {
            jsonObject.add("predicate", named.serializeToJson());
        } else if (checker != null) {
            jsonObject.addProperty("checker", checker.name().toLowerCase(Locale.ROOT));
        }
        return jsonObject;
    }

    @Override
    public String getType() {
        return "entity";
    }
}
