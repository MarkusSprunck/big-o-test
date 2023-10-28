/*
 * Copyright (C) 2013-2023, Markus Sprunck <sprunck.markus@gmail.com>
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

package big_o_test;

import big_o_test.interfaces.BigOParameter;
import big_o_test.math.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.google.common.collect.TreeBasedTable;
import javassist.util.proxy.MethodHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This class measures the Big-O time efficiency with a proxy of the system under test.
 */
public class BigOAnalyser {

    /**
     * Measurement interval in nanoseconds
     */
    private static final long MINIMAL_MEASUREMENT_INTERVAL = 100 * 1000 * 1000L;

    /**
     * Stores all measured results in <b>Item</b> objects. The <b>keys</b> of the hash map follow the
     * convention: <i>[method name]#[first size]#[second size]...#[last size]</i>
     */
    private final Map<String, BigODataPoint> values = new HashMap<String, BigODataPoint>(1000);

    /**
     * This flag is used to deactivate measurement during execution. This is needed, because the
     * first results are usually not representative.
     */
    private boolean active = true;

    /**
     * Creates a BigOResult object for a certain class. This is needed to use a Lambda expression
     * like:
     *
     * <pre>
     * <code>
     * List<List<Long>> values = LongStream.range(6, 14)
     *       .mapToInt(i -> 1 << i)
     *       .mapToObj(x -> BubbleSortTest.createSortInput(x))
     *       .collect(Collectors.toList());
     *
     * BigOResult actual = BigOAnalyser.classUnderTest(BubbleSort.class)
     *       .execute((BubbleSort o) -> values.stream().forEach(value -> o.sort(value)));
     *
     * BigOAssert.assertQuadratic(actual.getBigOAnalyser(), "sort");
     * </code>
     * </pre>
     */
    public static <P> BigOResult classUnderTest(Class<?> clazzUnderTest) {
        BigOAnalyser analyser = new BigOAnalyser();
        BigOResult bigOAnalyserHandler = new BigOResult(clazzUnderTest, analyser);

        return bigOAnalyserHandler;
    }

    public static Double estimatePolynomialDegree(Table<Integer, String, Double> data) {
        // calculate logarithms of both axis
        final Map<Integer, Double> xValues = new TreeMap<Integer, Double>();
        final Map<Integer, Double> yValues = new TreeMap<Integer, Double>();
        for (int index = 1; index <= data.column("N1").size(); index++) {
            xValues.put(index, Math.log10(data.column("N1").get(index)));
            yValues.put(index, Math.log10(data.column("TIME").get(index)));
        }

        // fit polynomial of first degree (a0 + a1 * x)
        final FitterPolynomial polynom = new FitterPolynomial();
        polynom.init(xValues, yValues, 1);

        // coefficient of the linear term a1 it what we need
        final double result = polynom.getCoefficient(1);

        // check the quality of the fit in cases the function is not constant
        if (result > 0.8) {
            final double coefficientOfDetermination = polynom.getRSquareAdjusted();
            Preconditions.checkState(coefficientOfDetermination > 0.8, "R^2=" + coefficientOfDetermination);
        }
        return result;
    }

    /**
     * Helper function to find the best fitting function. The approach is based on the estimation of
     * the polynomial degree of the measured data.
     */
    protected static BigOFittingResults calculateBestFittingFunctions(final Table<Integer, String, Double> input) {

        final BigOFittingResults result = new BigOFittingResults();

        final double degree = estimatePolynomialDegree(input);

        if (isProbablyPolynomial(degree)) {
            // fit polynomial Function
            final FitterPolynomial fitterPolynomial = new FitterPolynomial();
            fitterPolynomial.init(input.column("N1"), input.column("TIME"), (int) Math.round(degree));
            result.put(fitterPolynomial.getRSquareAdjusted(), fitterPolynomial.toString());
        } else {
            if (isProbablyLogLinear(degree)) {
                // LogLinear Function
                final FitterLogLinear fitterLogLinear = new FitterLogLinear();
                fitterLogLinear.init(input.column("N1"), input.column("TIME"));
                result.put(fitterLogLinear.getRSquareAdjusted(), fitterLogLinear.toString());
            } else {
                // PowerLaw Function
                final FitterPowerLaw fitterPowerLaw = new FitterPowerLaw();
                fitterPowerLaw.init(input.column("N1"), input.column("TIME"));
                result.put(fitterPowerLaw.getRSquareAdjusted(), fitterPowerLaw.toString());
            }
        }

        // ensure that it is not a constant function, because of problems in some fit functions
        if (degree > 0.1) {
            // Exponential Function
            final FitterExponential fitterExponential = new FitterExponential();
            fitterExponential.init(input.column("N1"), input.column("TIME"));
            result.put(fitterExponential.getRSquareAdjusted(), fitterExponential.toString());

            // Logarithmic Function
            final FitterLogarithmic fitterLogarithmic = new FitterLogarithmic();
            fitterLogarithmic.init(input.column("N1"), input.column("TIME"));
            result.put(fitterLogarithmic.getRSquareAdjusted(), fitterLogarithmic.toString());
        }
        return result;
    }

    private static boolean isProbablyPolynomial(double degree) {
        final double rest = degree - Math.floor(degree);
        Range<Double> invalidRange = Range.closed(0.1, 0.9);
        return !invalidRange.contains(rest);
    }

    private static boolean isProbablyLogLinear(double degree) {
        Range<Double> validRange = Range.closed(1.05, 1.15);
        return validRange.contains(degree);
    }

    /**
     * Deactivate measurement
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Activate measurement
     */
    public void activate() {
        active = true;
    }

    /**
     * Creates a class proxy that makes all the time measurements and stores the results in a
     * hash-map for later analysis. The annotation @BigOParameter marks the parameter to be
     * investigated.
     */
    public Object createProxy(Class<?> type) {
        Object proxy = null;
        try {
            final javassist.util.proxy.ProxyFactory pf = new javassist.util.proxy.ProxyFactory();
            pf.setSuperclass(type);
            final MethodHandler methodHandler = createMethodHandler();
            proxy = pf.create(new Class<?>[0], new Object[0], methodHandler);
        } catch (final NoSuchMethodException
                       | InstantiationException
                       | IllegalArgumentException
                       | IllegalAccessException
                       | InvocationTargetException e) {

            Preconditions.checkState(false, "ERROR in create proxy -> " + e.getMessage());
        }
        return proxy;
    }

    /**
     * Get measured data for one method.
     */
    public Table<Integer, String, Double> getData(String method) {
        final TreeBasedTable<Integer, String, Double> result = TreeBasedTable.create();
        int rowIndex = 0;
        for (final String key : getKeys()) {
            final String[] splitKey = key.split("#");
            if (splitKey[0].equals(method)) {
                rowIndex++;
                for (int i = 1; i < splitKey.length; i++) {
                    final double cell = Long.parseLong(splitKey[i]);
                    result.put(rowIndex, "N" + i, cell);
                }
                final BigODataPoint lastCall = getValue(key);
                final double cell = (double) lastCall.getTime() / (double) lastCall.getCalls();
                result.put(rowIndex, "TIME", cell);
            }
        }
        Preconditions.checkState(!result.isEmpty(), "No data for method name '" + method + "'");

        return Tables.unmodifiableTable(result);
    }

    /**
     * Get measured data for one method - with some internal checks.
     */
    public Table<Integer, String, Double> getDataChecked(String method) {
        // check preconditions
        Preconditions.checkNotNull(method);
        Preconditions.checkArgument(!method.isEmpty());

        // fetch data table
        final Table<Integer, String, Double> result = getData(method);

        // check size of data point table
        final boolean isNumberOfDataPointsSufficient = result.column("TIME").size() >= 4;
        final String message = "minimum 4 data points are needed for a reliable analysis";
        Preconditions.checkState(isNumberOfDataPointsSufficient, message);

        return Tables.unmodifiableTable(result);
    }

    /**
     * This method should return true, in the case you have measured data. This will be not the case
     * if not you may forget to: use the annotation @BigOParameter in your method to be tested, the
     * data type is not yet implemented (see appendParameterInformation), the measurement was
     * deactivated, the method was static and/or you used the wrong method name.
     */
    public boolean isAnalysed(String method) {
        for (final String key : getKeys()) {
            final String[] splitKey = key.split("#");
            if (splitKey[0].equals(method)) {
                return true;
            }
        }
        return false;
    }

    public Set<String> getAnalysedMethodNames() {
        Set<String> names = new TreeSet<String>();
        for (final String key : getKeys()) {
            final String[] splitKey = key.split("#");
            names.add(splitKey[0]);
        }
        return names;
    }

    private MethodHandler createMethodHandler() {

        final MethodHandler handler = new MethodHandler() {

            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) {
                final String Key = getCurrentKey(thisMethod, args);
                long endTime = 0;
                long calls = 0;
                Object result = null;
                final long startTime = System.nanoTime();
                try {
                    do {
                        result = proceed.invoke(self, args);
                        calls++;
                        endTime = System.nanoTime();
                    } while ((endTime - startTime) < MINIMAL_MEASUREMENT_INTERVAL);
                } catch (final IllegalArgumentException | IllegalAccessException e) {
                    Preconditions.checkState(false, "ERROR in invoke -> " + e.getMessage());
                } catch (final InvocationTargetException e) {
                    Preconditions.checkState(false, "ERROR in invoke -> " + e.getCause());
                }
                if (active) {
                    storeTimeMeasurement(Key, endTime - startTime, calls);
                }
                return result;
            }

            private void storeTimeMeasurement(String currentKey, long deltaTime, long calls) {
                if (values.containsKey(currentKey)) {
                    final BigODataPoint bigOProbe = values.get(currentKey);
                    bigOProbe.addTime(deltaTime);
                    bigOProbe.setCalls(calls);
                } else {
                    final BigODataPoint bigOProbe = new BigODataPoint();
                    bigOProbe.addTime(deltaTime);
                    bigOProbe.setCalls(calls);
                    values.put(currentKey, bigOProbe);
                }
            }

            private String getCurrentKey(Method method, Object[] args) {
                final StringBuilder key = new StringBuilder(method.getName());
                final Type[] types = method.getGenericParameterTypes();
                int index = 0;
                for (final Annotation[] annotations : method.getParameterAnnotations()) {
                    for (final Annotation annotation : annotations) {
                        if (annotation instanceof BigOParameter) {
                            appendParameterInformation(key, types[index], args[index]);
                        }
                    }
                    index++;
                }
                return key.toString();
            }

            /**
             * Java VM Type Signatures (see
             * <a href="http://docs.oracle.com/javase/1.5.0/docs/guide/jni/spec/types.html#wp276">...</a>)
             */
            @SuppressWarnings("rawtypes")
            private void appendParameterInformation(final StringBuilder result, final Type parameterType,
                                                    Object parameterArgument) {
                if (parameterType.toString().equals("class [I")) {
                    result.append('#').append(((int[]) parameterArgument).length);
                } else if (parameterType.toString().equals("class [F")) {
                    result.append('#').append(((float[]) parameterArgument).length);
                } else if (parameterType.toString().equals("class [Z")) {
                    result.append('#').append(((boolean[]) parameterArgument).length);
                } else if (parameterType.toString().equals("class [S")) {
                    result.append('#').append(((short[]) parameterArgument).length);
                } else if (parameterType.toString().equals("class [C")) {
                    result.append('#').append(((char[]) parameterArgument).length);
                } else if (parameterType.toString().equals("class [J")) {
                    result.append('#').append(((long[]) parameterArgument).length);
                } else if (parameterType.toString().equals("class [D")) {
                    result.append('#').append(((double[]) parameterArgument).length);
                } else if (parameterType.toString().equals("class [B")) {
                    result.append('#').append(((byte[]) parameterArgument).length);
                } else if (parameterType.toString().equals("int[]")) {
                    result.append('#').append(((int[]) parameterArgument).length);
                } else if (parameterType.toString().equals("long[]")) {
                    result.append('#').append(((long[]) parameterArgument).length);
                } else if (parameterType.toString().equals("double[]")) {
                    result.append('#').append(((double[]) parameterArgument).length);
                } else if (parameterType.toString().equals("byte[]")) {
                    result.append('#').append(((byte[]) parameterArgument).length);
                } else if (parameterType.toString().equals("float[]")) {
                    result.append('#').append(((float[]) parameterArgument).length);
                } else if (parameterType.toString().equals("int")) {
                    result.append('#').append(parameterArgument);
                } else if (parameterType.toString().equals("class java.lang.String")) {
                    result.append('#').append(((String) parameterArgument).length());
                } else if (parameterType.toString().equals("long")) {
                    result.append('#').append(parameterArgument);
                } else if (parameterType.toString().startsWith("java.util.List")) {
                    result.append('#').append(((List) parameterArgument).size());
                } else if (parameterType.toString().startsWith("java.util.Set")) {
                    result.append('#').append(((Set) parameterArgument).size());
                } else if (parameterType.toString().startsWith("java.util.Map")) {
                    result.append('#').append(((Map) parameterArgument).values().size());
                } else {
                    final StringBuilder message = new StringBuilder(100);
                    message.append("Not supported data type '");
                    message.append(parameterType);
                    message.append("' for method ");
                    message.append((result.toString().split("#"))[0]);
                    Preconditions.checkState(false, message);
                }
            }

        };
        return handler;
    }

    public BigODataPoint getValue(String key) {
        return values.get(key);
    }

    public Set<String> getKeys() {
        return values.keySet();
    }

}
