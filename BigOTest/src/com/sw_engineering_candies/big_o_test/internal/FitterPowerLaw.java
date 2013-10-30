package com.sw_engineering_candies.big_o_test.internal;

import java.util.Map;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.CurveFitter;
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer;

public class FitterPowerLaw extends FitterAbstractBase {

	/**
	 * Fit exponential function: Y = a0 * x ^ a1
	 * 
	 * see http://mathworld.wolfram.com/LeastSquaresFittingPowerLaw.html
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
		return coefficients.get(0) * Math.pow(x, coefficients.get(1));
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
			sum1 += Math.log(y) * Math.log(x);
			sum2 += Math.log(x);
			sum3 += Math.log(y);
			sum4 += Math.log(x) * Math.log(x);
		}
		final double b = (n * sum1 - sum2 * sum3) / (n * sum4 - sum2 * sum2);
		final double a = Math.exp((sum3 - b * sum2) / n);
		super.coefficients.add(0, a);
		super.coefficients.add(1, b);

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
				return a * Math.pow(x, b);
			}

			@Override
			public double[] gradient(double x, double... parameters) {

				final double b = parameters[1];
				final double[] gradients = new double[2];

				// derivative with respect to a
				gradients[0] = 1;

				// derivative with respect to b
				gradients[1] = Math.pow(x, b) * Math.log(x);

				return gradients;

			}
		};

		final double[] initialGuess = new double[] { coefficients.get(0), coefficients.get(1) };
		final double[] estimatedParameters = curveFitter.fit(f, initialGuess);

		super.coefficients.add(0, estimatedParameters[0]);
		super.coefficients.add(1, estimatedParameters[1]);
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		result.append(String.format("%+.2E", coefficients.get(0)));
		result.append(" * x ^ ");
		result.append(String.format("%+.2E", coefficients.get(1)));
		return result.toString();
	}

}
