package com.dfzq.dset;

import android.support.annotation.NonNull;

public interface Provider<T> {
    @NonNull
    T get();
}
