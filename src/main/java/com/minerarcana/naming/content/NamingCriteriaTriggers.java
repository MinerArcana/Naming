package com.minerarcana.naming.content;

import com.minerarcana.naming.advancement.criteria.heard.HeardCriterionTrigger;
import com.minerarcana.naming.advancement.criteria.naming.NamingCriterionTrigger;
import com.minerarcana.naming.advancement.criteria.signing.SigningCriterionTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class NamingCriteriaTriggers {
    public static final SigningCriterionTrigger SIGNING = CriteriaTriggers.register(new SigningCriterionTrigger());
    public static final NamingCriterionTrigger NAMING = CriteriaTriggers.register(new NamingCriterionTrigger());
    public static final HeardCriterionTrigger HEARD = CriteriaTriggers.register(new HeardCriterionTrigger());

    public static void setup() {

    }
}
