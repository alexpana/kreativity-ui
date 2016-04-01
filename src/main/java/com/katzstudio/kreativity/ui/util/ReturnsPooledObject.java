package com.katzstudio.kreativity.ui.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the method's result is a pooled object which must be released
 * by calling {@code Pools.release(...)}
 */
@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.METHOD)
public @interface ReturnsPooledObject {
}
