package com.minerarcana.naming.advancement.criteria.naming;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.advancement.criteria.EntityChecker;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class NamingCriterionTrigger extends SimpleCriterionTrigger<NamingCriterionInstance> {
    public static final ResourceLocation ID = Naming.rl("naming");

    public void trigger(ServerPlayer player, Object named) {
        this.trigger(player, instance -> instance.matches(player.getLevel(), named));
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected NamingCriterionInstance createInstance(JsonObject pJson, EntityPredicate.Composite pPlayer, DeserializationContext pContext) {
        String type = GsonHelper.getAsString(pJson, "type");
        return switch (type) {
            case "entity" -> entityNaming(pJson, pPlayer);
            case "item" -> itemNaming(pJson, pPlayer);
            default -> throw new JsonParseException("Invalid Type: " + type);
        };
    }

    private static NamingCriterionInstance entityNaming(JsonObject jsonObject, EntityPredicate.Composite playerPredicate) {
        if (jsonObject.has("predicate")) {
            return new EntityNamingCriterionInstance(playerPredicate, EntityPredicate.fromJson(jsonObject.get("predicate")));
        } else if (jsonObject.has("checker")) {
            return new EntityNamingCriterionInstance(
                    playerPredicate,
                    EntityChecker.getByName(GsonHelper.getAsString(jsonObject, "checker"))
                            .mapRight(JsonParseException::new)
                            .orThrow()
            );
        } else {
            throw new JsonParseException("Missing 'predicate' or 'check'");
        }
    }

    private static NamingCriterionInstance itemNaming(JsonObject jsonObject, EntityPredicate.Composite playerPredicate) {
        return new ItemStackNamingCriterionInstance(playerPredicate, Ingredient.fromJson(jsonObject.get("item")));
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }

    public NamingCriterionInstance item(Ingredient ingredient) {
        return new ItemStackNamingCriterionInstance(EntityPredicate.Composite.ANY, ingredient);
    }

    public NamingCriterionInstance entityPredicate(EntityPredicate entityPredicate) {
        return new EntityNamingCriterionInstance(EntityPredicate.Composite.ANY, entityPredicate);
    }

    public NamingCriterionInstance checker(EntityChecker checker) {
        return new EntityNamingCriterionInstance(EntityPredicate.Composite.ANY, checker);
    }
}
