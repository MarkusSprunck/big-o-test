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

import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.internal.FitterPolynomial;

public class BigOAssert {

	public static void assertPolynomialDegree(BigOAnalyser bom, String method, double expected,
			double range) {

		final Table<Integer, String, Double> resultTable = getDataTable(bom, method);

		final double estimate = estimatePolynomialDegree(resultTable);
		if (estimate < expected - range || estimate > expected + range)
			throw new BigOAssertException(
					"BigOAssertException assertPolynomialDegree failed:\n\tPolynomial degree expected = "
							+ expected + "\n\tPolynomial degree actual   = " + estimate);

	}

	public static void assertConstant(BigOAnalyser bom, String method) {

		final Table<Integer, String, Double> resultTable = getDataTable(bom, method);

		// A Constant function should have a polynomial degree of 0.0
		assertPolynomialDegree(bom, method, 0.0, 0.1);

		final String details = BigOAnalyser.createBestFitReport(resultTable, true).trim();
		if (!details.startsWith("Polynomial"))
			throw new BigOAssertException("BigOAssertException assertConstant failed:\n" + details);
	}

	public static void assertLinear(BigOAnalyser bom, String method) throws BigOAssertException {

		final Table<Integer, String, Double> resultTable = getDataTable(bom, method);

		// A Linear function should have a polynomial degree of 1.0
		assertPolynomialDegree(bom, method, 1.0, 0.2);

		final String details = BigOAnalyser.createBestFitReport(resultTable, true).trim();
		if (!details.startsWith("Polynomial"))
			throw new BigOAssertException("BigOAssertException assertLinear failed:\n" + details);
	}

	public static void assertLogLinear(BigOAnalyser bom, String method) {

		// Get measured data
		final Table<Integer, String, Double> resultTable = getDataTable(bom, method);

		// A LogLinear function should have a polynomial degree of 1.1
		assertPolynomialDegree(bom, method, 1.1, 0.2);

		final String details = BigOAnalyser.createBestFitReport(resultTable, true).trim();
		if (!details.startsWith("LogLinear"))
			throw new BigOAssertException("BigOAssertException assertLogLinear failed:\n" + details);
	}

	public static void assertQuadratic(BigOAnalyser bom, String method) {

		final Table<Integer, String, Double> resultTable = getDataTable(bom, method);

		// A LogLinear function should have a polynomial degree of 2.0
		assertPolynomialDegree(bom, method, 2.0, 0.1);

		final String details = BigOAnalyser.createBestFitReport(resultTable, true).trim();
		if (!details.startsWith("Polynomial"))
			throw new BigOAssertException("BigOAssertException assertQuadratic failed:\n" + details);

	}

	public static Double estimatePolynomialDegree(Table<Integer, String, Double> report) {
		// Calculate logarithms of both axis
		final Map<Integer, Double> xValues = new TreeMap<Integer, Double>();
		final Map<Integer, Double> yValues = new TreeMap<Integer, Double>();
		for (int index = 1; index <= report.column("N1").size(); index++) {
			xValues.put(index, Math.log10(report.column("N1").get(index)));
			yValues.put(index, Math.log10(report.column("TIME").get(index)));
		}
		// Fit polynomial of first degree (a0 + a1 * x)
		final FitterPolynomial polynom = new FitterPolynomial();
		polynom.init(xValues, yValues, 1);
		// The coefficient a1 is the estimation of the polynomial degree
		return polynom.getCoefficient(1);
	}

	private static Table<Integer, String, Double> getDataTable(BigOAnalyser bom, String method) {
		final Table<Integer, String, Double> resultTable = bom.getResultTable(method);
		Preconditions.checkArgument(resultTable.column("TIME").size() >= 4,
				"minimum 5 data points needed for reliable analysis");
		return resultTable;
	}
}
