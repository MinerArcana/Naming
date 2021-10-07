package com.minerarcana.naming.api.capability;

import java.util.Collection;

public interface INamer {
    Collection<String> getAbilities();

    boolean grantAbility(String name);

    boolean hasAbility(String name);
}
