package com.sw_engineering_candies.big_o_test;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation marks the parameter to be investigated
 */
@Target({ PARAMETER })
@Retention(RUNTIME)
public @interface BigOParameter {
}
