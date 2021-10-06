package com.minerarcana.naming.advancement.criteria;

import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.util.ResourceLocation;

public class SigningCriterionInstance extends CriterionInstance {
    private final String title;

    public SigningCriterionInstance(ResourceLocation id, String title, EntityPredicate.AndPredicate predicate) {
        super(id, predicate);
        this.title = title;
    }

    public boolean matches(String bookTitle) {
        return title == null || title.equalsIgnoreCase(bookTitle);
    }
}
