package com.minerarcana.naming.advancement.criteria.naming;

import com.google.gson.JsonObject;
import com.minerarcana.naming.advancement.criteria.EntityChecker;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.Locale;

public class EntityNamingCriterionInstance extends NamingCriterionInstance {
    private final EntityPredicate named;
    private final EntityChecker checker;

    public EntityNamingCriterionInstance(EntityPredicate.Composite playerPredicate, EntityPredicate named) {
        super(playerPredicate);
        this.named = named;
        this.checker = null;
    }

    public EntityNamingCriterionInstance(EntityPredicate.Composite playerPredicate, EntityChecker checker) {
        super(playerPredicate);
        this.named = null;
        this.checker = checker;
    }


    public boolean matches(ServerLevel world, Object object) {
        if (object instanceof Entity) {
            if (named != null) {
                return named.matches(world, Vec3.ZERO, (Entity) object);
            } else if (checker != null) {
                return checker.matches((Entity) object);
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public JsonObject serializeToJson(@Nonnull SerializationContext pConditions) {
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
