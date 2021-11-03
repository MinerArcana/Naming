package com.minerarcana.naming.content;

import com.minerarcana.naming.advancement.criteria.messaged.MessagedCriterionTrigger;
import com.minerarcana.naming.advancement.criteria.naming.NamingCriterionTrigger;
import com.minerarcana.naming.advancement.criteria.signing.SigningCriterionTrigger;
import com.minerarcana.naming.advancement.criteria.spell.SpellCriterionTrigger;
import com.minerarcana.naming.advancement.criteria.targetedspell.TargetedSpellCriterionTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class NamingCriteriaTriggers {
    public static final SigningCriterionTrigger SIGNING = CriteriaTriggers.register(new SigningCriterionTrigger());
    public static final NamingCriterionTrigger NAMING = CriteriaTriggers.register(new NamingCriterionTrigger());
    public static final MessagedCriterionTrigger MESSAGED = CriteriaTriggers.register(new MessagedCriterionTrigger());
    public static final SpellCriterionTrigger SPELL = CriteriaTriggers.register(new SpellCriterionTrigger());
    public static final TargetedSpellCriterionTrigger TARGETED_SPELL = CriteriaTriggers.register(new TargetedSpellCriterionTrigger());

    public static void setup() {

    }
}
