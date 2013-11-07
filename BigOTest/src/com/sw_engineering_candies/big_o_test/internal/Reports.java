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

package com.sw_engineering_candies.big_o_test.internal;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.BigOAssert;

public class Reports {

	public static String createFullReport(Table<Integer, String, Double> input) {
		final StringBuilder result = new StringBuilder();
		result.append(createBestFitReport(input, false));
		result.append(createDataReport(input));
		return result.toString();
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
		final SortedSet<Double> rows = new TreeSet<Double>();
		rows.addAll(input.column("N1").values());
		for (final Double value : rows) {
			Integer row = 0;
			for (final Integer index : input.column("N1").keySet()) {
				if (value.equals(input.get(index, "N1"))) {
					row = index;
					break;
				}
			}
			for (int col = 1; col < cols.size(); col++) {
				result.append(String.format("%.0f", input.get(row, "N" + col)) + "\t");
			}
			result.append(String.format("%.0f", input.get(row, "TIME"))).append('\n');
		}
		return result.toString();
	}

	public static String createBestFitReport(final Table<Integer, String, Double> input, boolean sumary) {

		final StringBuilder result = new StringBuilder();
		final double degree = BigOAssert.estimatePolynomialDegree(input);

		final Map<Double, String> resultMap = new TreeMap<Double, String>();

		Double coefficientOfDetermination;
		String message;

		final FitterPolynomial fitterPolynomialLin = new FitterPolynomial();
		fitterPolynomialLin.init(input.column("N1"), input.column("TIME"), (int) Math.round(degree));
		coefficientOfDetermination = fitterPolynomialLin.getCoefficientOfDetermination();
		message = String.format("Polynomial\t%.4f  \ty = ", coefficientOfDetermination) + fitterPolynomialLin.toString()
				+ "\n";
		if (coefficientOfDetermination > 0.0 || degree < 0.5) {
			resultMap.put(coefficientOfDetermination, message);
		}

		if (degree > 0.5) {
			if (degree < 1.3 && degree > 1.05) {
				final FitterLogLinear fitterLinearLog = new FitterLogLinear();
				fitterLinearLog.init(input.column("N1"), input.column("TIME"));
				coefficientOfDetermination = fitterLinearLog.getCoefficientOfDetermination();
				message = String.format("LogLinear\t%.4f  \ty = ", coefficientOfDetermination) + fitterLinearLog.toString()
						+ "\n";
				if (coefficientOfDetermination > 0.0) {
					resultMap.put(coefficientOfDetermination, message);
				}
			}

			final FitterExponential fitterExponential = new FitterExponential();
			fitterExponential.init(input.column("N1"), input.column("TIME"));
			coefficientOfDetermination = fitterExponential.getCoefficientOfDetermination();
			message = String.format("Exponential\t%.4f  \ty = ", coefficientOfDetermination)
					+ fitterExponential.toString() + "\n";
			if (coefficientOfDetermination > 0.0) {
				resultMap.put(coefficientOfDetermination, message);
			}

			final FitterLogarithmic fitterLogarithmic = new FitterLogarithmic();
			fitterLogarithmic.init(input.column("N1"), input.column("TIME"));
			coefficientOfDetermination = fitterLogarithmic.getCoefficientOfDetermination();
			message = String.format("Logarithmic\t%.4f  \ty = ", coefficientOfDetermination)
					+ fitterLogarithmic.toString() + "\n";
			if (coefficientOfDetermination > 0.0) {
				resultMap.put(coefficientOfDetermination, message);
			}

			if (degree > 1.1) {
				final FitterPowerLaw fitterPowerLaw = new FitterPowerLaw();
				fitterPowerLaw.init(input.column("N1"), input.column("TIME"));
				coefficientOfDetermination = fitterPowerLaw.getCoefficientOfDetermination();
				message = String.format("PowerLaw\t%.4f  \ty = ", coefficientOfDetermination) + fitterPowerLaw.toString()
						+ "\n";
				if (coefficientOfDetermination > 0.0) {
					resultMap.put(coefficientOfDetermination, message);
				}
			}
		}

		// just print the three best fitting functions
		result.append("TYPE      \tR^2 (adjusted)\tFUNCTION\n");
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
