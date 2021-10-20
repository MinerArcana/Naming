package com.minerarcana.naming.worlddata;

import java.lang.ref.WeakReference;
import java.util.function.Function;

public class WeakFunction<T, R> implements Function<T, R> {
    private final WeakReference<Function<T, R>> weakReference;
    private final R defaultResult;

    public WeakFunction(Function<T, R> weakReference, R defaultResult) {
        this.weakReference = new WeakReference<>(weakReference);
        this.defaultResult = defaultResult;
    }

    @Override
    public R apply(T t) {
        Function<T, R> function = weakReference.get();
        return function != null ? function.apply(t) : defaultResult;
    }

    public boolean isValid() {
        return weakReference.get() != null;
    }
}
