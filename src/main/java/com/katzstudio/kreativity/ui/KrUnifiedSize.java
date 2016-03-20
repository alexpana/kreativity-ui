package com.katzstudio.kreativity.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Describes how a component should be sized relative to its parent
 */
@RequiredArgsConstructor
public class KrUnifiedSize {

    @Getter private final float absolute;

    @Getter private final float relative;

    public static KrUnifiedSize absolute(float value) {
        return new KrUnifiedSize(value, 0);
    }

    public static KrUnifiedSize relative(float value) {
        return new KrUnifiedSize(0, value);
    }

    public KrUnifiedSize addAbsolute(float absolute) {
        return new KrUnifiedSize(this.absolute + absolute, relative);
    }

    public KrUnifiedSize addRelative(float relative) {
        return new KrUnifiedSize(this.absolute, this.relative + relative);
    }
}
