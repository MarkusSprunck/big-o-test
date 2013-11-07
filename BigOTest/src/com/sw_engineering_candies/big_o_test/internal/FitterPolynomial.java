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

import java.util.Map;

/**
 * The polynomial fitter has no performance optimizations to better understand and maintain the
 * implementation. A degree polynomial has (degree+1) coefficients.
 * 
 */
public class FitterPolynomial extends FitterAbstractBase {

	/**
	 * Set all the input data and execute fit
	 */
	public boolean init(Map<Integer, Double> xValues_in, Map<Integer, Double> yValues_in, int degree) {
		if (xValues_in.size() <= degree)
			return false;
		super.xValues = xValues_in;
		super.yValues = yValues_in;
		super.k = degree + 1;

		calculateCoefficients(degree);
		calculateCoefficientOfDeterminationOriginalData();
		return true;
	}

	/**
	 * Calculates the fitted polynomial for point x
	 */
	@Override
	public double getY(final double x) {
		double result = 0.0;
		for (int p = 0; p < coefficients.size(); p++) {
			result += coefficients.get(p) * Math.pow(x, p);
		}
		return result;
	}

	/**
	 * Calculates the Polynomial Regression
	 * 
	 * see http://www.arachnoid.com/sage/polynomial.html
	 * 
	 * @param degree
	 *           Is the degree of the polynomial
	 */
	private void calculateCoefficients(int degree) {
		final int numberOfPoints = super.xValues.size();
		final int equations = degree + 1;
		final Matrix A = new Matrix(equations, equations);
		final Matrix b = new Matrix(equations, 1);
		for (int pointIndex = 1; pointIndex <= numberOfPoints; pointIndex++) {
			final double x = super.xValues.get(pointIndex);
			final double y = super.yValues.get(pointIndex);
			for (int row = 0; row < equations; row++) {
				for (int col = 0; col < equations; col++) {
					A.setValue(row, col, A.getValue(row, col) + Math.pow(x, row + col));
				}
				b.setValue(row, 0, b.getValue(row, 0) + Math.pow(x, row) * y);
			}
		}
		final Matrix result = A.solve(b);

		coefficients.clear();
		for (int i = 0; i <= degree; i++) {
			coefficients.add(i, result.getValue(i, 0));
		}
	}

	/**
	 * Creates the equation of the fitted polynomial
	 */
	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		final int maxIndex = coefficients.size() - 1;
		for (int index = maxIndex; index >= 0; index--) {
			result.append(String.format("%.2E", coefficients.get(index)));
			if (index > 0) {
				result.append(" * x^").append(index).append(" + ");
			}
		}
		return String.format("Polynomial\t%.4f  \ty = ", getRSquareAdjusted()) + result.toString();
	}

}