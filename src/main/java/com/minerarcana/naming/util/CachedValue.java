package com.minerarcana.naming.util;

import java.util.function.Supplier;

public class CachedValue<T> {
    private final Supplier<T> supplier;

    private T value;

    public CachedValue(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (value == null) {
            value = supplier.get();
        }
        return value;
    }

    public void invalidate() {
        this.value = null;
    }
}
