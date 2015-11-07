package com.sw_engineering_candies.big_o_test;

@FunctionalInterface
public interface BigOTestAction<T> {

   void apply(T t);

}