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
		return createBestFitReport(input).concat(createDataReport(input));
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

	public static String createBestFit(final Table<Integer, String, Double> input) {
		// try to find best fits
		final Map<Double, String> result = findBestFittingFunctions(input);

		// order the function by the R^2 value of the fit
		final SortedSet<Double> keys = new TreeSet<Double>(Collections.reverseOrder());
		keys.addAll(result.keySet());

		// return best fit
		return result.get(keys.first());
	}

	public static String createBestFitReport(final Table<Integer, String, Double> input) {
		// try to find best fits
		final Map<Double, String> resultMap = findBestFittingFunctions(input);

		// order the function by the R^2 value of the fit
		final SortedSet<Double> keys = new TreeSet<Double>(Collections.reverseOrder());
		keys.addAll(resultMap.keySet());
		final StringBuilder result = new StringBuilder();
		result.append("TYPE      \tR^2 (adjusted)\tFUNCTION\n");
		for (final Double key : keys) {
			result.append(resultMap.get(key)).append('\n');
		}
		result.append("\n");

		// return all fits
		return result.toString();
	}

	private static Map<Double, String> findBestFittingFunctions(final Table<Integer, String, Double> input) {

		// first Polynomial Function
		final FitterPolynomial fitterPolymomial = new FitterPolynomial();
		final double degree = BigOAssert.estimatePolynomialDegree(input);
		fitterPolymomial.init(input.column("N1"), input.column("TIME"), (int) Math.round(degree));
		final Map<Double, String> result = new TreeMap<Double, String>();
		result.put(fitterPolymomial.getRSquareAdjusted(), fitterPolymomial.toString());

		// ensure that it is not a constant function, because of problems in some fit functions
		if (degree > 0.1) {
			// second Exponential Function
			final FitterExponential fitterExponential = new FitterExponential();
			fitterExponential.init(input.column("N1"), input.column("TIME"));
			result.put(fitterExponential.getRSquareAdjusted(), fitterExponential.toString());

			// third Logarithmic Function
			final FitterLogarithmic fitterLogarithmic = new FitterLogarithmic();
			fitterLogarithmic.init(input.column("N1"), input.column("TIME"));
			result.put(fitterLogarithmic.getRSquareAdjusted(), fitterLogarithmic.toString());

			// it is likely not a Quadratic Function
			if (degree < 1.95 || degree > 2.05) {
				// fourth PowerLaw Function
				final FitterPowerLaw fitterPowerLaw = new FitterPowerLaw();
				fitterPowerLaw.init(input.column("N1"), input.column("TIME"));
				result.put(fitterPowerLaw.getRSquareAdjusted(), fitterPowerLaw.toString());
			}

			// it is likely a LogLinear
			if (degree > 1.05 && degree < 1.25) {
				// sixth LogLinear Function
				final FitterLogLinear fitterLogLinear = new FitterLogLinear();
				fitterLogLinear.init(input.column("N1"), input.column("TIME"));
				result.put(fitterLogLinear.getRSquareAdjusted(), fitterLogLinear.toString());
			}
		}
		return result;
	}

}
