/*
 * Copyright (C) 2013, Markus Sprunck <sprunck.markus@gmail.com>
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - The name of its contributor may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.sw_engineering_candies.big_o_test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.internal.Item;
import com.sw_engineering_candies.big_o_test.utils.Algorithms;

public class BigOAnalyserTest {

	final BigOAnalyser bom = new BigOAnalyser();

	@Test
	public void createProxy_RunCalled_CorrectResult() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
		final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
		final int[] n_input = { 11, 22, 33, 44 };
		final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };

		// ACT
		final double result = sut.run(m_input, true, n_input, k_input);

		// ASSERT
		Assert.assertEquals(117583.3969669342, result);
	}

	@Test
	public void getValue_OneCall_GetNamoTime() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
		final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
		final int[] n_input = { 11, 22, 33, 44 };
		final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
		sut.run(m_input, true, n_input, k_input);
		sut.run(m_input, true, n_input, k_input);

		// ACT
		final Item result = bom.getValue("run#8#4#5");

		// ASSERT
		assertThat(result.getTime(), greaterThan(0L));
	}

	@Test
	public void getValue_OneCall_GetNumberOfCallsOne() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
		final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
		final int[] n_input = { 11, 22, 33, 44 };
		final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
		sut.run(m_input, true, n_input, k_input);

		// ACT
		final Item result = bom.getValue("run#8#4#5");
		result.setCalls(1);

		// ASSERT
		Assert.assertEquals(1, result.getCalls());
	}

	@Test
	public void getValuel_OneCallWithWrongKey_RetrunsNull() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
		final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
		final int[] n_input = { 11, 22, 33, 44 };
		final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
		sut.run(m_input, true, n_input, k_input);

		// ACT
		final Item result = bom.getValue("ThisIsWrongKey#8#4#5");

		// ASSERT
		Assert.assertNull(result);
	}

	@Test
	public void getKeys_ThreeCallsWithDifferentValues_GetCorrectKeys() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);

		final List<Integer> m_input1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
		final int[] n_input1 = { 11, 22, 33, 44 };
		final float[] k_input1 = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
		sut.run(m_input1, true, n_input1, k_input1);
		sut.run(m_input1, true, n_input1, k_input1);

		final List<Integer> m_input2 = Arrays.asList(1, 4, 5, 6, 7, 10);
		final int[] n_input2 = { 11, 22, 33 };
		final float[] k_input2 = { 11.4f, 2.23f, 4.2f, 8.2f };
		sut.run(m_input2, true, n_input2, k_input2);

		final List<Integer> m_input3 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		final int[] n_input3 = { 11 };
		final float[] k_input3 = { 11.4f, 2.23f };
		sut.run(m_input3, true, n_input3, k_input3);

		// ACT
		final Set<String> actual = bom.getKeys();

		// ASSERT
		final Set<String> expectedKeys = new HashSet<String>();
		expectedKeys.add("run#8#4#5");
		expectedKeys.add("run#6#3#4");
		expectedKeys.add("run#10#1#2");
		Assert.assertEquals(expectedKeys, actual);
	}

	@Test
	public void estimatePolynomialDegree_MannyCallsOfRunLinear_GetDegreeOfPolynomIsThree() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
		for (int n = 1; n <= 128; n *= 2) {
			sut.runLinear((n));
			// replace measured results
			final Item result = bom.getValue("runLinear#" + n);
			result.setNanoTime(n * n * n * 134 + n * n * +n + 11);
			result.setCalls(1);
		}
		final Table<Integer, String, Double> report = bom.getResultTable("runLinear");

		// ACT
		final long result = Math.round(BigOAssert.estimatePolynomialDegree(report));

		// ASSERT
		Assert.assertEquals(3L, result);
	}

	@Test
	public void estimatePolynomialDegree_MannyCallsOfRunLinear_GetDegreeOfPolynomIsOne() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
		for (int n = 1; n <= 128; n *= 2) {
			sut.runLinear((n));
			// replace measured results
			final Item result = bom.getValue("runLinear#" + n);
			result.setNanoTime(n * 134 + 11);
			result.setCalls(1);
		}
		final Table<Integer, String, Double> report = bom.getResultTable("runLinear");

		// ACT
		final long result = Math.round(BigOAssert.estimatePolynomialDegree(report));

		// ASSERT
		Assert.assertEquals(1L, result);
	}

	@Test
	public void estimatePolynomialDegree_MannyCallsOfrunQuadratic_GetDegreeOfPolynomIsTwo() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
		for (int n = 1; n <= 128; n *= 2) {
			sut.runQuadratic((n));
			// replace measured results
			final Item result = bom.getValue("runQuadratic#" + n);
			result.setNanoTime(n * n * 123 + n + 1);
			result.setCalls(1);
		}
		final Table<Integer, String, Double> report = bom.getResultTable("runQuadratic");

		// ACT
		final long result = Math.round(BigOAssert.estimatePolynomialDegree(report));

		// ASSERT
		Assert.assertEquals(2L, result);
	}

	@Test
	public void estimatePolynomialDegree_MannyCallsOfrunConst_GetDegreeOfPolynomIsZero() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
		for (int n = 1; n <= 128; n *= 2) {
			sut.runQuadratic((n));
			// replace measured results
			final Item result = bom.getValue("runQuadratic#" + n);
			result.setNanoTime(100);
		}
		final Table<Integer, String, Double> report = bom.getResultTable("runQuadratic");

		// ACT
		final long result = Math.round(BigOAssert.estimatePolynomialDegree(report));

		// ASSERT
		Assert.assertEquals(0L, result);
	}

	@Test
	public void getResultTable_TwoCalls_GetTableWithAllValues() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);

		final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
		final int[] n_input = { 11, 22, 33, 44 };
		final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
		sut.run(m_input, true, n_input, k_input);
		final Item result = bom.getValue("run#8#4#5");
		result.setNanoTime(12345);
		result.setCalls(1);

		final List<Integer> m_input2 = Arrays.asList(1, 2, 3, 4, 5, 6);
		final int[] n_input2 = { 11, 22, 33, 44, 55, 66, 77, 88 };
		final float[] k_input2 = {};
		sut.run(m_input2, true, n_input2, k_input2);
		final Item result2 = bom.getValue("run#6#8#0");
		result2.setNanoTime(23456);
		result2.setCalls(1);

		// ACT
		final String actual = bom.getResultTable("run").toString();

		// ASSERT
		final String expected = "{1={N1=8.0, N2=4.0, N3=5.0, TIME=12345.0}, 2={N1=6.0, N2=8.0, N3=0.0, TIME=23456.0}}";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void getResultTable_MixedCallsOfTwoFunctions_GetTableforDifferentMethodNames() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
		final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
		final int[] n_input = { 11, 22, 33, 44 };
		final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
		sut.run(m_input, true, n_input, k_input);
		final Item result = bom.getValue("run#8#4#5");
		result.setNanoTime(12345);
		result.setCalls(1);

		final List<Integer> m_input2 = Arrays.asList(1, 2, 3, 4, 5, 6);
		final int[] n_input2 = { 11, 22, 33, 44, 55, 66, 77, 88 };
		final float[] k_input2 = {};
		sut.run(m_input2, true, n_input2, k_input2);
		final Item result2 = bom.getValue("run#6#8#0");
		result2.setNanoTime(23456);
		result2.setCalls(1);

		sut.runLinear(1000);
		final Item result3 = bom.getValue("runLinear#1000");
		result3.setNanoTime(2000);
		result3.setCalls(1);

		sut.runLinear(100);
		final Item result4 = bom.getValue("runLinear#100");
		result4.setNanoTime(1000);
		result4.setCalls(1);

		// ACT
		final Table<Integer, String, Double> resultTableRun = bom.getResultTable("run");
		final Table<Integer, String, Double> resultTableLinear = bom.getResultTable("runLinear");

		// ASSERT
		final StringBuilder expected = new StringBuilder();
		expected.append("N1; N2; N3; TIME\n");
		expected.append("8.0; 4.0; 5.0; 12345.0\n");
		expected.append("6.0; 8.0; 0.0; 23456.0\n");
		final String actualRun = BigOAnalyser.createDataReport(resultTableRun);
		Assert.assertEquals(expected.toString(), actualRun);

		final StringBuilder expected2 = new StringBuilder();
		expected2.append("N1; TIME\n");
		expected2.append("100.0; 1000.0\n");
		expected2.append("1000.0; 2000.0\n");
		final String actualRunLinear = BigOAnalyser.createDataReport(resultTableLinear);
		Assert.assertEquals(expected2.toString(), actualRunLinear);
	}

	@Test
	public void createCSV_MoreCallsOfOneFunction_GetCorrectReport() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);

		final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
		final int[] n_input = { 11, 22, 33, 44 };
		final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
		sut.run(m_input, true, n_input, k_input);
		final Item result = bom.getValue("run#8#4#5");
		result.setNanoTime(12345);
		result.setCalls(1);

		final List<Integer> m_input2 = Arrays.asList(1, 2, 3, 4, 5, 6);
		final int[] n_input2 = { 11, 22, 33, 44, 55, 66, 77, 88 };
		final float[] k_input2 = {};
		sut.run(m_input2, true, n_input2, k_input2);
		final Item result2 = bom.getValue("run#6#8#0");
		result2.setNanoTime(23456);
		result2.setCalls(1);
		final Table<Integer, String, Double> resultTable = bom.getResultTable("run");

		// ACT
		final String actual = BigOAnalyser.createDataReport(resultTable);

		// ASSERT
		final StringBuilder expected = new StringBuilder();
		expected.append("N1; N2; N3; TIME\n");
		expected.append("8.0; 4.0; 5.0; 12345.0\n");
		expected.append("6.0; 8.0; 0.0; 23456.0\n");
		Assert.assertEquals(expected.toString(), actual);
	}

	@Test
	public void createCSV_OneCallOfRunLinear_GetReport() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
		sut.runLinear(10);
		final Item result = bom.getValue("runLinear#10");
		result.setNanoTime(123);
		result.setCalls(1);
		final Table<Integer, String, Double> resultTable = bom.getResultTable("runLinear");

		// ACT
		final String actual = BigOAnalyser.createDataReport(resultTable);

		// ASSERT
		Assert.assertEquals("N1; TIME\n10.0; 123.0\n", actual);
	}

	@Test
	public void getValueOfLastCall_FourCallsOfrunLinear_GetReport() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);

		sut.runLinear(10);
		final Item result = bom.getValue("runLinear#10");
		result.setNanoTime(123);
		result.setCalls(1);

		sut.runLinear(100);
		final Item result2 = bom.getValue("runLinear#100");
		result2.setNanoTime(345);
		result2.setCalls(1);

		sut.runLinear(100);
		final Item result4 = bom.getValue("runLinear#100");
		result4.setNanoTime(1000);
		result4.setCalls(2);

		sut.runLinear(1000);
		final Item result3 = bom.getValue("runLinear#1000");
		result3.setNanoTime(567);
		result3.setCalls(1);

		final Table<Integer, String, Double> resultTable = bom.getResultTable("runLinear");

		// ACT
		final String actual = BigOAnalyser.createDataReport(resultTable);

		// ASSERT
		final String expected = "N1; TIME\n100.0; 500.0\n10.0; 123.0\n1000.0; 567.0\n";
		Assert.assertEquals(expected.toString(), actual);
	}

	@Test
	public void getResultTable_FourMixedCalls_GetCorrectValues() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);

		sut.runLinear(10);
		final Item result = bom.getValue("runLinear#10");
		result.setNanoTime(123);
		result.setCalls(1);

		sut.runLinear(100);
		final Item result2 = bom.getValue("runLinear#100");
		result2.setNanoTime(345);
		result2.setCalls(1);

		sut.runLinear(1000);
		final Item result3 = bom.getValue("runLinear#1000");
		result3.setNanoTime(2000);
		result3.setCalls(1);

		sut.runLinear(100);
		final Item result4 = bom.getValue("runLinear#100");
		result4.setNanoTime(1000);
		result4.setCalls(2);

		// ACT
		final Table<Integer, String, Double> resultTable = bom.getResultTable("runLinear");

		// ASSERT
		final String actual = BigOAnalyser.createDataReport(resultTable);
		final String expected = "N1; TIME\n100.0; 500.0\n10.0; 123.0\n1000.0; 2000.0\n";
		Assert.assertEquals(expected.toString(), actual);
	}

	@Test
	public void createCSV_OneCall_GetCorrectReport() {
		// ARRANGE
		final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);

		final List<Integer> m_input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 10);
		final int[] n_input = { 11, 22, 33, 44 };
		final float[] k_input = { 11.4f, 2.1f, 2.23f, 4.2f, 8.2f };
		sut.run(m_input, true, n_input, k_input);
		final Item result = bom.getValue("run#8#4#5");
		result.setNanoTime(12345);
		result.setCalls(1);
		final Table<Integer, String, Double> resultTable = bom.getResultTable("run");

		// ACT
		final String actual = BigOAnalyser.createDataReport(resultTable);

		// ASSERT
		final String expected = "N1; N2; N3; TIME\n8.0; 4.0; 5.0; 12345.0\n";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void setNanoTime_SetNewValue_OldSumIsOverwritten() {
		// ARRANG
		final Item bigOProbe = new Item();
		bigOProbe.addTime(123);
		bigOProbe.addTime(234);

		// ACT
		bigOProbe.setNanoTime(456);

		// ASSERT
		Assert.assertEquals(456, bigOProbe.getTime());
	}

	@Test
	public void addNanoTime_TwoValues_SumIsCorrect() {
		// ARRANG
		final Item bigOProbe = new Item();

		// ACT
		bigOProbe.addTime(123);
		bigOProbe.addTime(234);

		// ASSERT
		Assert.assertEquals(357, bigOProbe.getTime());
	}

	@Test
	public void addNanoTime_OneValue_SumIsCorrect() {
		// ARRANG
		final Item bigOProbe = new Item();

		// ACT
		bigOProbe.addTime(123);

		// ASSERT
		Assert.assertEquals(123, bigOProbe.getTime());
	}

	@Test
	public void addNanoTime_OneValue_CallsAsExpectedTwo() {
		// ARRANG
		final Item bigOProbe = new Item();

		// ACT
		bigOProbe.addTime(123);
		bigOProbe.addTime(234);

		// ASSERT
		Assert.assertEquals(2, bigOProbe.getCalls());
	}

	@Test
	public void addNanoTime_OneValue_CallsAsExpectedOne() {
		// ARRANG
		final Item bigOProbe = new Item();

		// ACT
		bigOProbe.addTime(123);

		// ASSERT
		Assert.assertEquals(1, bigOProbe.getCalls());
	}

}
