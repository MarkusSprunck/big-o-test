package com.sw_engineering_candies.big_o_test.internal;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class FitterPowerLawTest {

	@Test
	public void getCoefficientOfDetermination_HunderedDataPoints_GetCorrectCoefficiantOfDetermination() {
		// ARRANGE
		final Table<Integer, String, Double> input = createTenPoints();
		final FitterPowerLaw fitter = new FitterPowerLaw();
		fitter.init(input.column("N1"), input.column("TIME"));

		// ACT
		final double result = fitter.getRSquareAdjusted();

		// ASSERT
		Assert.assertEquals(1.0, result, 0.000000000000001);
	}

	@Test
	public void init_PowerLawWithoutNoise_CorrectFunction() {
		// ARRANGE
		final Table<Integer, String, Double> input = createTenPoints();
		final FitterPowerLaw fitter = new FitterPowerLaw();

		// ACT
		fitter.init(input.column("N1"), input.column("TIME"));

		// ASSERT
		final String expected = "PowerLaw	1,0000  	y = 1.00E+01 * x^1.10E+00";
		Assert.assertEquals(expected, fitter.toString());
	}

	private Table<Integer, String, Double> createTenPoints() {
		final Table<Integer, String, Double> input;
		input = TreeBasedTable.create();
		for (int i = 1; i <= 100; i++) {
			input.put(i, "N1", (double) i);
			input.put(i, "TIME", (10.0 * Math.pow(i, 1.1)));
		}
		return input;
	}

}
