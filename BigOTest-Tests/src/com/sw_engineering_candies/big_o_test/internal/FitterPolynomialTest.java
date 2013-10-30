package com.sw_engineering_candies.big_o_test.internal;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class FitterPolynomialTest {

	@Test
	public void init_SevenDataPointsAndDegreeSeven_InitIsNotSuccessfull() {
		// ARRANGE
		final Table<Integer, String, Double> input = createSevenPoints();
		final FitterPolynomial polynom = new FitterPolynomial();

		// ACT
		final boolean result = polynom.init(input.column("N1"), input.column("TIME"), 7);

		// ASSERT
		Assert.assertFalse(result);
	}

	@Test
	public void init_SevenDataPointsAndDegreeSix_InitIsSuccessfull() {
		// ARRANGE
		final Table<Integer, String, Double> input = createSevenPoints();
		final FitterPolynomial polynom = new FitterPolynomial();

		// ACT
		final boolean result = polynom.init(input.column("N1"), input.column("TIME"), 6);

		// ASSERT
		Assert.assertTrue(result);
	}

	@Test
	public void getCoefficient_PolynomialRegressionDataSecondDegree_CorrectCoefficient() {
		// ARRANGE
		final Table<Integer, String, Double> input = createSevenPoints();
		final FitterPolynomial polynom = new FitterPolynomial();
		polynom.init(input.column("N1"), input.column("TIME"), 2);

		// ACT
		final double result = polynom.getCoefficient(1);

		// ASSERT
		Assert.assertEquals(0.9956756058992482, result, 1E-12);
	}

	@Test
	public void fit_PolynomialRegressionDataSecondDegree_CorrectPolynom() {
		// ARRANGE
		final Table<Integer, String, Double> input = createSevenPoints();
		final FitterPolynomial polynom = new FitterPolynomial();

		// ACT
		polynom.init(input.column("N1"), input.column("TIME"), 2);

		// ASSERT
		final String expected = "-8.60E-02x^2 +9.96E-01x^1 +1.66E+00";
		Assert.assertEquals(expected, polynom.toString());
	}

	@Test
	public void getCoefficientOfDetermination_SevenDataPoints_GetCorrectCoefficiantOfDetermination() {
		// ARRANGE
		final Table<Integer, String, Double> input = createSevenPoints();
		final FitterPolynomial polynom = new FitterPolynomial();
		polynom.init(input.column("N1"), input.column("TIME"), 2);

		// ACT
		final double result = polynom.getCoefficientOfDetermination();

		// ASSERT
		Assert.assertEquals(0.4854187308473215, result, 0.000000000000001);
	}

	private Table<Integer, String, Double> createSevenPoints() {
		final Table<Integer, String, Double> input;
		input = TreeBasedTable.create();
		input.put(1, "N1", -1.0);
		input.put(1, "TIME", -1.0);
		input.put(2, "N1", 0.0);
		input.put(2, "TIME", 3.0);
		input.put(3, "N1", 2.0);
		input.put(3, "TIME", 5.0);
		input.put(4, "N1", 3.0);
		input.put(4, "TIME", 4.0);
		input.put(5, "N1", 5.0);
		input.put(5, "TIME", 2.0);
		input.put(6, "N1", 7.0);
		input.put(6, "TIME", 5.0);
		input.put(7, "N1", 9.0);
		input.put(7, "TIME", 4.0);
		return input;
	}

}
