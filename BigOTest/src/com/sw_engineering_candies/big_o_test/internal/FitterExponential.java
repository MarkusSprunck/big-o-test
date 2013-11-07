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

public class FitterExponential extends FitterAbstractBase {

	/**
	 * Fit exponential function: Y = a0 * exp ( a1 * x )
	 * 
	 * see http://mathworld.wolfram.com/LeastSquaresFittingExponential.html
	 */
	public boolean init(Map<Integer, Double> xValues_in, Map<Integer, Double> yValues_in) {

		if (xValues_in.size() <= 3)
			return false;

		super.xValues = xValues_in;
		super.yValues = yValues_in;
		super.k = 2;

		calculateCoefficients();
		calculateCoefficientOfDeterminationLogarithmicData();
		return true;
	}

	/**
	 * Calculates the fitted polynomial for point x
	 */
	@Override
	public double getY(final double x) {
		return coefficients.get(0) * Math.exp(coefficients.get(1) * x);
	}

	private void calculateCoefficients() {
		final Matrix A = new Matrix(2, 2);
		final Matrix b = new Matrix(2, 1);
		for (int pointIndex = 1; pointIndex <= super.xValues.size(); pointIndex++) {
			final double x = super.xValues.get(pointIndex);
			final double y = super.yValues.get(pointIndex);
			A.setValue(0, 0, A.getValue(0, 0) + y);
			A.setValue(0, 1, A.getValue(0, 1) + x * y);
			A.setValue(1, 0, A.getValue(1, 0) + x * y);
			A.setValue(1, 1, A.getValue(1, 1) + x * x * y);
			b.setValue(0, 0, b.getValue(0, 0) + y * Math.log(y));
			b.setValue(1, 0, b.getValue(1, 0) + x * y * Math.log(y));
		}
		final Matrix result = A.solve(b);
		super.coefficients.add(0, Math.exp(result.getValue(0, 0)));
		super.coefficients.add(1, result.getValue(1, 0));
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		result.append(String.format("%+.4f", coefficients.get(0)));
		result.append(" * exp ( ");
		result.append(String.format("%+.4f", coefficients.get(1)));
		result.append(" * x )");
		return result.toString();
	}

}
