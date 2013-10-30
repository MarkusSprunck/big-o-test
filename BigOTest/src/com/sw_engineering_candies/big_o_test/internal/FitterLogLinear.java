package com.sw_engineering_candies.big_o_test.internal;

import java.util.Map;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.CurveFitter;
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer;

public class FitterLogLinear extends FitterAbstractBase {

	/**
	 * Fit linear log function: Y = a0 *x * log ( a1 * x )
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
		return coefficients.get(0) * x * Math.log(coefficients.get(1) * x);
	}

	private void calculateCoefficients() {
		final LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer();
		final CurveFitter<ParametricUnivariateFunction> curveFitter = new CurveFitter<ParametricUnivariateFunction>(
				optimizer);

		for (int pointIndex = 1; pointIndex <= super.xValues.size(); pointIndex++) {
			final double x = super.xValues.get(pointIndex);
			final double y = super.yValues.get(pointIndex);
			curveFitter.addObservedPoint(x, y);
		}

		final ParametricUnivariateFunction f = new ParametricUnivariateFunction() {

			@Override
			public double value(double x, double... parameters) {
				final double a = parameters[0];
				final double b = parameters[1];
				return a * x * Math.log(b * x);
			}

			@Override
			public double[] gradient(double x, double... parameters) {

				final double a = parameters[0];
				final double b = parameters[1];
				final double[] gradients = new double[2];

				// derivative with respect to a
				gradients[0] = x * Math.log(b * x);

				// derivative with respect to b
				gradients[1] = a * x / b;

				return gradients;

			}
		};

		final double[] initialGuess = new double[] { 2.0, 0.5 };
		final double[] estimatedParameters = curveFitter.fit(f, initialGuess);

		super.coefficients.add(0, estimatedParameters[0]);
		super.coefficients.add(1, estimatedParameters[1]);
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		result.append(String.format("%+.2E", coefficients.get(0)));
		result.append(" * x * log( ");
		result.append(String.format("%+.2E", coefficients.get(1)));
		result.append(" * x )");
		return result.toString();
	}

}
