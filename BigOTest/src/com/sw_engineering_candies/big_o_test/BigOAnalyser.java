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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javassist.util.proxy.MethodHandler;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.sw_engineering_candies.big_o_test.internal.FitterExponential;
import com.sw_engineering_candies.big_o_test.internal.FitterLogLinear;
import com.sw_engineering_candies.big_o_test.internal.FitterLogarithmic;
import com.sw_engineering_candies.big_o_test.internal.FitterPolynomial;
import com.sw_engineering_candies.big_o_test.internal.FitterPowerLaw;
import com.sw_engineering_candies.big_o_test.internal.Item;

public class BigOAnalyser {

	/**
	 * Stores all measured results in probe objects. The keys convention:
	 * "<method name>#<first size>#<second size>...#<last size>"
	 */
	private final Map<String, Item> values = new HashMap<String, Item>(1000);

	/**
	 * Used to deactivate measurement during analysis. This is needed, because
	 * the first results are usually not representative.
	 */
	private boolean activeMeasurement = true;

	/**
	 * Creates a class proxy that makes all the time measurements and stores the
	 * results in a hash-map for later analysis. The annotation @BigOParameter
	 * marks the parameter to be investigated.
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
				final long startTime = System.nanoTime();
				long endTime = 0;
				int calls = 0;

				// Execute method about 50ms
				Object result = null;
				try {
					do {
						result = proceed.invoke(self, args);
						calls++;
						endTime = System.nanoTime();
					} while ((endTime - startTime) < 50000000L);
				} catch (final IllegalArgumentException e) {
					System.err.println("ERROR #6 in MethodHandler -> " + e.getMessage());
				} catch (final IllegalAccessException e) {
					System.err.println("ERROR #6  in MethodHandler -> " + e.getMessage());
				} catch (final InvocationTargetException e) {
					System.err.println("ERROR #7  in MethodHandler -> " + e.getMessage());
				}
				if (activeMeasurement) {
					storeTimeMeasurement(Key, endTime - startTime, calls);
				}
				return result;
			}

			private void storeTimeMeasurement(String currentKey, long deltaTime, int calls) {
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

			@SuppressWarnings("rawtypes")
			private String getCurrentKey(Method method, Object[] args) {
				final StringBuilder result = new StringBuilder(method.getName());
				final Type[] types = method.getGenericParameterTypes();
				int index = 0;
				for (final Annotation[] annotations : method.getParameterAnnotations()) {
					for (final Annotation annotation : annotations) {
						if (annotation instanceof BigOParameter) {
							final Type type = types[index];
							if (type.toString().equals("int[]")) {
								result.append("#").append(((int[]) args[index]).length);
							} else if (type.toString().equals("float[]")) {
								result.append("#").append(((float[]) args[index]).length);
							} else if (type.toString().equals("int")) {
								result.append("#").append(args[index]);
							} else if (type.toString().startsWith("java.util.List")) {
								result.append("#").append(((List) args[index]).size());
							} else {
								System.err.println("BigOMeter -> missing '" + type + "'");
							}
						}
					}
					index++;
				}
				return result.toString();
			}

		};
		return handler;
	}

	public void deactivateMeasurement() {
		activeMeasurement = false;
	}

	public void activateMeasurement() {
		activeMeasurement = true;
	}

	public Item getValue(String key) {
		return values.get(key);
	}

	public Set<String> getKeys() {
		return values.keySet();
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
		return result;
	}

	public static String createDataReport(Table<Integer, String, Double> input) {
		final StringBuilder result = new StringBuilder();
		// header of the table
		final Set<String> cols = input.columnKeySet();
		for (int i = 1; i < cols.size(); i++) {
			result.append("N" + i + "\t");
		}
		result.append("TIME\n");
		// values of the table
		final Set<Integer> rows = input.rowKeySet();
		for (int row = 1; row <= rows.size(); row++) {
			for (int col = 1; col < cols.size(); col++) {
				result.append(String.format("%.0f", input.get(row, "N" + col)) + "\t");
			}
			result.append(String.format("%.0f", input.get(row, "TIME"))).append('\n');
		}
		return result.toString();
	}

	public static String createBestFitReport(final Table<Integer, String, Double> input,
			boolean sumary) {

		final StringBuilder result = new StringBuilder();
		final double degree = BigOAssert.estimatePolynomialDegree(input);

		final Map<Double, String> resultMap = new TreeMap<Double, String>();

		Double coefficientOfDetermination;
		String message;

		final FitterPolynomial fitterPolynomialLin = new FitterPolynomial();
		fitterPolynomialLin.init(input.column("N1"), input.column("TIME"), (int) Math.round(degree));
		coefficientOfDetermination = fitterPolynomialLin.getCoefficientOfDetermination();
		message = String.format("Polynomial\t%.4f  \ty=", coefficientOfDetermination)
				+ fitterPolynomialLin.toString() + "\n";
		resultMap.put(coefficientOfDetermination, message);

		if (degree > 0.5) {
			if (degree < 1.3 && degree > 1.05) {
				final FitterLogLinear fitterLinearLog = new FitterLogLinear();
				fitterLinearLog.init(input.column("N1"), input.column("TIME"));
				coefficientOfDetermination = fitterLinearLog.getCoefficientOfDetermination();
				message = String.format("LogLinear\t%.4f  \ty=", coefficientOfDetermination)
						+ fitterLinearLog.toString() + "\n";
				resultMap.put(coefficientOfDetermination, message);
			}

			final FitterExponential fitterExponential = new FitterExponential();
			fitterExponential.init(input.column("N1"), input.column("TIME"));
			coefficientOfDetermination = fitterExponential.getCoefficientOfDetermination();
			message = String.format("Exponential\t%.4f  \ty=", coefficientOfDetermination)
					+ fitterExponential.toString() + "\n";
			resultMap.put(coefficientOfDetermination, message);

			final FitterLogarithmic fitterLogarithmic = new FitterLogarithmic();
			fitterLogarithmic.init(input.column("N1"), input.column("TIME"));
			coefficientOfDetermination = fitterLogarithmic.getCoefficientOfDetermination();
			message = String.format("Logarithmic\t%.4f  \ty=", coefficientOfDetermination)
					+ fitterLogarithmic.toString() + "\n";
			resultMap.put(coefficientOfDetermination, message);

			if (degree > 1.1) {
				final FitterPowerLaw fitterPowerLaw = new FitterPowerLaw();
				fitterPowerLaw.init(input.column("N1"), input.column("TIME"));
				coefficientOfDetermination = fitterPowerLaw.getCoefficientOfDetermination();
				message = String.format("PowerLaw\t%.4f  \ty=", coefficientOfDetermination)
						+ fitterPowerLaw.toString() + "\n";
				resultMap.put(coefficientOfDetermination, message);
			}
		}

		// just print the three best fitting functions
		result.append("TYPE      \tR^2     \tFUNCTION\n");
		final SortedSet<Double> keys = new TreeSet<Double>(Collections.reverseOrder());
		keys.addAll(resultMap.keySet());
		for (final Double key : keys) {
			result.append(resultMap.get(key));
		}
		result.append("\n");

		if (sumary)
			return resultMap.get(keys.first());
		else
			return result.toString();
	}

}
