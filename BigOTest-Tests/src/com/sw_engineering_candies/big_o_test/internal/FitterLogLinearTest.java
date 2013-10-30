package com.sw_engineering_candies.big_o_test.internal;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class FitterLogLinearTest {

	@Test
	public void getCoefficientOfDetermination_HunderedDataPoints_GetCorrectCoefficiantOfDetermination() {
		// ARRANGE
		final Table<Integer, String, Double> input = createTestFunction();
		final FitterLogLinear fitter = new FitterLogLinear();
		fitter.init(input.column("N1"), input.column("TIME"));

		// ACT
		final double result = fitter.getCoefficientOfDetermination();

		// ASSERT
		Assert.assertEquals(1.0, result, 0.000000000000001);
	}

	@Test
	public void init_PowerLawWithoutNoise_CorrectFunction() {
		// ARRANGE
		final Table<Integer, String, Double> input = createTestFunction();
		final FitterLogLinear fitter = new FitterLogLinear();

		// ACT
		fitter.init(input.column("N1"), input.column("TIME"));

		// ASSERT
		final String expected = "+5.00E+00 * x * log( +3.00E+00 * x )";
		Assert.assertEquals(expected, fitter.toString());
	}

	private Table<Integer, String, Double> createTestFunction() {
		final Table<Integer, String, Double> input;
		input = TreeBasedTable.create();
		int index = 1;
		for (int i = 1; i <= 100; i *= 2) {
			final double x = i;
			input.put(index, "N1", x);
			input.put(index, "TIME", (5 * x * Math.log(3.0 * x)));
			index++;
		}
		return input;
	}

}
