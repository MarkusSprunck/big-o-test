package com.sw_engineering_candies.big_o_test.internal;

import java.util.Map;

public class FitterLogarithmic extends FitterAbstractBase {

	/**
	 * Fit exponential function: Y = a0 + a1 * log ( x )
	 * 
	 * see http://mathworld.wolfram.com/LeastSquaresFittingLogarithmic.html
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
		return coefficients.get(0) + coefficients.get(1) * Math.log(x);
	}

	private void calculateCoefficients() {
		double sum1 = 0.0;
		double sum2 = 0.0;
		double sum3 = 0.0;
		double sum4 = 0.0;
		final int n = super.xValues.size();
		for (int pointIndex = 1; pointIndex <= n; pointIndex++) {
			final double x = super.xValues.get(pointIndex);
			final double y = super.yValues.get(pointIndex);
			sum1 += y * Math.log(x);
			sum2 += y;
			sum3 += Math.log(x);
			sum4 += Math.log(x) * Math.log(x);
		}
		final double b = (n * sum1 - sum2 * sum3) / (n * sum4 - sum3 * sum3);
		final double a = (sum2 - b * sum3) / (n);
		super.coefficients.add(0, a);
		super.coefficients.add(1, b);
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		result.append(String.format("%+.2E", coefficients.get(0)));
		result.append(" ");
		result.append(String.format("%+.2E", coefficients.get(1)));
		result.append(" * log (x)");
		return result.toString();
	}

}
