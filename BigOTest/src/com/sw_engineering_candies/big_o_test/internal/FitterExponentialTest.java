package com.sw_engineering_candies.big_o_test.internal;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class FitterExponentialTest {

	@Test
	public void getRSquareAdjusted_TenDataPoints_GetCorrectCoefficiantOfDetermination() {
		// ARRANGE
		final Table<Integer, String, Double> input = createTenPoints();
		final FitterExponential polynom = new FitterExponential();
		polynom.init(input.column("N1"), input.column("TIME"));

		// ACT
		final double result = polynom.getRSquareAdjusted();

		// ASSERT
		Assert.assertEquals(1.0, result, 0.000000000000001);
	}

	@Test
	public void init_ExponentalFunctionWithoutNoise_CorrectFunction() {
		// ARRANGE
		final Table<Integer, String, Double> input = createTenPoints();
		final FitterExponential exponentialFunction = new FitterExponential();

		// ACT
		exponentialFunction.init(input.column("N1"), input.column("TIME"));

		// ASSERT
		final String expected = "Exponential	1,0000  	y = 1.00E+02 * exp ( 5.00E-01 * x )";
		Assert.assertEquals(expected, exponentialFunction.toString());
	}

	private Table<Integer, String, Double> createTenPoints() {
		final Table<Integer, String, Double> input;
		input = TreeBasedTable.create();
		for (int i = 1; i <= 10; i++) {
			input.put(i, "N1", (double) i);
			input.put(i, "TIME", (100.0 * Math.exp(0.5 * i)));
		}
		return input;
	}

}
