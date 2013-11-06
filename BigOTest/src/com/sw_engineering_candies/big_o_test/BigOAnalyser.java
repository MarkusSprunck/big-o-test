/*
 * Copyright (C) 2013, Markus Sprunck <sprunck.markus@gmail.com>
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - The name of its contributor may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.sw_engineering_candies.big_o_test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.util.proxy.MethodHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.sw_engineering_candies.big_o_test.internal.Item;

public class BigOAnalyser {

	/**
	 * Measurement interval in nanoseconds
	 */
	private static final long MEASUREMENT_INTERVAL = 50 * 1000 * 1000L;

	/**
	 * Stores all measured results in <b>Item</b> objects. The <b>keys</b> of the hash map follow the
	 * convention: <i>[method name]#[first size]#[second size]...#[last size]</i>
	 */
	private final Map<String, Item> values = new HashMap<String, Item>(1000);

	/**
	 * Used to deactivate measurement during analysis. This is needed, because the first results are
	 * usually not representative.
	 */
	private boolean active = true;

	/**
	 * Creates a class proxy that makes all the time measurements and stores the results in a
	 * hash-map for later analysis. The annotation @BigOParameter marks the parameter to be
	 * investigated.
	 */
	public Object createProxy(Class<?> type) {
		Object proxy = null;
		try {
			final javassist.util.proxy.ProxyFactory pf = new javassist.util.proxy.ProxyFactory();
			pf.setSuperclass(type);
			final MethodHandler methodHandler = createMethodHandler();
			proxy = pf.create(new Class<?>[0], new Object[0], methodHandler);
		} catch (final NoSuchMethodException e) {
			System.err.println("ERROR #2  in createproxy -> " + e.getMessage());
		} catch (final InstantiationException e) {
			System.err.println("ERROR #3  in createproxy -> " + e.getMessage());
		} catch (final IllegalArgumentException e) {
			System.err.println("ERROR #1 in createproxy -> " + e.getMessage());
		} catch (final IllegalAccessException e) {
			System.err.println("ERROR #4  in createproxy -> " + e.getMessage());
		} catch (final InvocationTargetException e) {
			System.err.println("ERROR #5  in createproxy -> " + e.getMessage());
		}
		return proxy;
	}

	private MethodHandler createMethodHandler() {

		final MethodHandler handler = new MethodHandler() {

			@Override
			public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) {
				final String Key = getCurrentKey(thisMethod, args);
				long endTime = 0;
				long calls = 0;
				Object result = null;
				final long startTime = System.nanoTime();
				try {
					do {
						result = proceed.invoke(self, args);
						calls++;
						endTime = System.nanoTime();
					} while ((endTime - startTime) < MEASUREMENT_INTERVAL);
				} catch (final IllegalArgumentException e) {
					System.err.println("ERROR #6 in MethodHandler -> " + e.getMessage());
				} catch (final IllegalAccessException e) {
					System.err.println("ERROR #6  in MethodHandler -> " + e.getMessage());
				} catch (final InvocationTargetException e) {
					System.err.println("ERROR #7  in MethodHandler -> " + e.getMessage());
				}
				if (active) {
					storeTimeMeasurement(Key, endTime - startTime, calls);
				}
				return result;
			}

			private void storeTimeMeasurement(String currentKey, long deltaTime, long calls) {
				if (values.containsKey(currentKey)) {
					final Item bigOProbe = values.get(currentKey);
					bigOProbe.addTime(deltaTime);
					bigOProbe.setCalls(calls);
				} else {
					final Item bigOProbe = new Item();
					bigOProbe.addTime(deltaTime);
					bigOProbe.setCalls(calls);
					values.put(currentKey, bigOProbe);
				}
			}

			private String getCurrentKey(Method method, Object[] args) {
				final StringBuilder key = new StringBuilder(method.getName());
				final Type[] types = method.getGenericParameterTypes();
				int index = 0;
				for (final Annotation[] annotations : method.getParameterAnnotations()) {
					for (final Annotation annotation : annotations) {
						if (annotation instanceof BigOParameter) {
							appendParameterInformation(key, types[index], args[index]);
						}
					}
					index++;
				}
				return key.toString();
			}

			@SuppressWarnings("rawtypes")
			private void appendParameterInformation(final StringBuilder result, final Type parameterType,
					Object parameterArgument) {
				if (parameterType.toString().equals("int[]")) {
					result.append("#").append(((int[]) parameterArgument).length);
				} else if (parameterType.toString().equals("long[]")) {
					result.append("#").append(((long[]) parameterArgument).length);
				} else if (parameterType.toString().equals("double[]")) {
					result.append("#").append(((double[]) parameterArgument).length);
				} else if (parameterType.toString().equals("byte[]")) {
					result.append("#").append(((byte[]) parameterArgument).length);
				} else if (parameterType.toString().equals("float[]")) {
					result.append("#").append(((float[]) parameterArgument).length);
				} else if (parameterType.toString().equals("int")) {
					result.append("#").append(parameterArgument);
				} else if (parameterType.toString().equals("class java.lang.String")) {
					result.append("#").append(((String) parameterArgument).length());
				} else if (parameterType.toString().equals("long")) {
					result.append("#").append(parameterArgument);
				} else if (parameterType.toString().startsWith("java.util.List")) {
					result.append("#").append(((List) parameterArgument).size());
				} else if (parameterType.toString().startsWith("java.util.Set")) {
					result.append("#").append(((Set) parameterArgument).size());
				} else if (parameterType.toString().startsWith("java.util.Map")) {
					result.append("#").append(((Map) parameterArgument).values().size());
				} else {
					final StringBuilder message = new StringBuilder();
					message.append("Not supported data type '");
					message.append(parameterType);
					message.append("' for BigOAnalysis in method ");
					message.append((result.toString().split("#"))[0]);
					Preconditions.checkState(false, message);
				}
			}

		};
		return handler;
	}

	public Table<Integer, String, Double> getResultTable(String methodName) {
		final TreeBasedTable<Integer, String, Double> result = TreeBasedTable.create();
		int rowIndex = 0;
		for (final String key : this.getKeys()) {
			final String[] splitedKey = key.split("#");
			if (splitedKey[0].equalsIgnoreCase(methodName)) {
				rowIndex++;
				for (int i = 1; i < splitedKey.length; i++) {
					final double cell = Long.parseLong(splitedKey[i]);
					result.put(rowIndex, "N" + i, cell);
				}
				final Item lastCall = this.getValue(key);
				final double cell = lastCall.getTime() / lastCall.getCalls();
				result.put(rowIndex, "TIME", cell);
			}
		}
		Preconditions.checkState(!result.isEmpty(), "No data for method name '" + methodName + "'");
		return result;
	}

	public void deactivate() {
		active = false;
	}

	public void activate() {
		active = true;
	}

	protected Item getValue(String key) {
		return values.get(key);
	}

	protected Set<String> getKeys() {
		return values.keySet();
	}

}
