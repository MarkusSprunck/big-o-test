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

		calculateCoefficients();
		calculateCoefficientOfDetermination();
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
		result.append(String.format("%+.2E", coefficients.get(0)));
		result.append(" * exp ( ");
		result.append(String.format("%+.2E", coefficients.get(1)));
		result.append(" * x)");
		return result.toString();
	}

}
