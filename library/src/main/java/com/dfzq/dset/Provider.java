package com.dfzq.dset;

import androidx.annotation.NonNull;

public interface Provider<T> {
    @NonNull
    T get();
}
