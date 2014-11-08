package com.piotrglazar.webs;

import rx.Observable;

import java.util.List;

public final class TestUtilities {

    private TestUtilities() {
        // Utility class
    }

    public static <T> List<T> toListToBlocking(Observable<T> observable) {
        return observable.toList().toBlocking().first();
    }
}
