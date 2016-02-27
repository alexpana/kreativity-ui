package com.katzstudio.kreativity.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A pair contains two values: first and second. The pair structure is immutable.
 */
@RequiredArgsConstructor
public class KrPair<F, S> {
    @Getter private final F first;
    @Getter private final S second;
}
