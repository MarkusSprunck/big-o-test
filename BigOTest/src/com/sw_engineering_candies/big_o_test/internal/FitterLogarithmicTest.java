package com.sw_engineering_candies.big_o_test.internal;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class FitterLogarithmicTest {

	@Test
	public void getRSquareAdjusted_HunderedDataPoints_GetCorrectCoefficiantOfDetermination() {
		// ARRANGE
		final Table<Integer, String, Double> input = createTenPoints();
		final FitterLogarithmic fitter = new FitterLogarithmic();
		fitter.init(input.column("N1"), input.column("TIME"));

		// ACT
		final double result = fitter.getRSquareAdjusted();

		// ASSERT
		Assert.assertEquals(1.0, result, 0.000000000000001);
	}

	@Test
	public void init_LogarithmicFunctionWithoutNoise_CorrectFunction() {
		// ARRANGE
		final Table<Integer, String, Double> input = createTenPoints();
		final FitterLogarithmic fitter = new FitterLogarithmic();

		// ACT
		fitter.init(input.column("N1"), input.column("TIME"));

		// ASSERT
		final String expected = "Logarithmic	1,0000  	y = 1.00E+02 + 1.05E+01 * log ( x )";
		Assert.assertEquals(expected, fitter.toString());
	}

	private Table<Integer, String, Double> createTenPoints() {
		final Table<Integer, String, Double> input;
		input = TreeBasedTable.create();
		for (int i = 1; i <= 100; i++) {
			input.put(i, "N1", (double) i);
			input.put(i, "TIME", (100.0 + 10.5 * Math.log(i)));
		}
		return input;
	}

}
