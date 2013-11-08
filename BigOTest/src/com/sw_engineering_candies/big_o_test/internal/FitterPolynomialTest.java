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
		Assert.assertEquals(2.0000000000002167, result, 1E-12);
	}

	@Test
	public void init_PolynomialRegressionDataSecondDegree_CorrectPolynom() {
		// ARRANGE
		final Table<Integer, String, Double> input = createSevenPoints();
		final FitterPolynomial polynom = new FitterPolynomial();

		// ACT
		polynom.init(input.column("N1"), input.column("TIME"), 2);

		// ASSERT
		final String expected = "Polynomial	1,0000  	y = 3.00E+00 * x^2 + 2.00E+00 * x^1 + 1.00E+00";
		Assert.assertEquals(expected, polynom.toString());
	}

	@Test
	public void getRSquareAdjusted_SevenDataPoints_GetCorrectCoefficiantOfDetermination() {
		// ARRANGE
		final Table<Integer, String, Double> input = createSevenPoints();
		final FitterPolynomial polynom = new FitterPolynomial();
		polynom.init(input.column("N1"), input.column("TIME"), 2);

		// ACT
		final double result = polynom.getRSquareAdjusted();

		// ASSERT
		Assert.assertEquals(1.0, result, 0.000000000000001);
	}

	private Table<Integer, String, Double> createSevenPoints() {
		final Table<Integer, String, Double> input;
		input = TreeBasedTable.create();
		input.put(2, "N1", 2.0);
		input.put(2, "TIME", 17.0);
		input.put(3, "N1", 3.0);
		input.put(3, "TIME", 34.0);
		input.put(4, "N1", 4.0);
		input.put(4, "TIME", 57.0);
		input.put(5, "N1", 5.0);
		input.put(5, "TIME", 86.0);
		input.put(6, "N1", 6.0);
		input.put(6, "TIME", 121.0);
		input.put(7, "N1", 7.0);
		input.put(7, "TIME", 162.0);
		input.put(1, "N1", 1.0);
		input.put(1, "TIME", 6.0);
		return input;
	}

}
