package com.sw_engineering_candies.big_o_test.internal;

import java.util.ArrayList;
import java.util.Map;

public abstract class FitterAbstractBase {

	/**
	 * Coefficient of Determination for the fitted polynomial and the underlying
	 * data points
	 */
	private double coefficientOfDetermination = 0.0;

	/**
	 * Coefficients of the fitted Polynomial starting with a0=coefficients[0],
	 * a1=coefficients[1], ...
	 */
	protected final ArrayList<Double> coefficients = new ArrayList<Double>();

	/**
	 * All x Values of underlying data points
	 */
	protected Map<Integer, Double> xValues = null;
	/**
	 * All y Values of underlying data points
	 */
	protected Map<Integer, Double> yValues = null;

	/**
	 * Calculates the fitted polynomial for point x
	 */
	abstract public double getY(final double x);

	/**
	 * Get a single coefficient of the fitted polynomial
	 */
	public double getCoefficient(int index) {
		return coefficients.get(index);
	}

	/**
	 * Get coefficient of determination for the fitted polynomial and the
	 * underlying data points
	 */
	public double getCoefficientOfDetermination() {
		return coefficientOfDetermination;
	}

	/**
	 * Calculates the Coefficient of Determination
	 * 
	 * see http://en.wikipedia.org/wiki/Coefficient_of_determination
	 */
	protected void calculateCoefficientOfDetermination() {

		// Calculate the mean value of y
		double ySum = 0.0;
		for (int index = 1; index <= this.xValues.size(); index++) {
			final double y = this.yValues.get(index);
			ySum += y;
		}
		final double yMean = ySum / this.yValues.size();

		// Coefficient of determination
		double SS_tot = 0.0;
		double SS_res = 0.0;
		for (int index = 1; index <= this.xValues.size(); index++) {
			final double x = this.xValues.get(index);
			final double y = this.yValues.get(index);
			SS_tot += Math.pow(y - yMean, 2);
			SS_res += Math.pow(y - this.getY(x), 2);
		}
		this.coefficientOfDetermination = 1.0 - SS_res / SS_tot;
	}

}