## BigOTest.jar 
### is a Library to Empirically Estimate Big-O Time Efficiency and Check Results of Analysis in JUnit Tests

This library supports the need to check time efficiency of algorithms during development. 

#### How does it look like?

The goal is to support unit-test in the following form:

	public class HeapSortTest {
	
	   @Test
	   public void assertLogLinear_RunHeapSort_DetectLogLinear() {
	
	      // ARRANGE
	      final BigOAnalyser boa = new BigOAnalyser();
	      final HeapSort sut = (HeapSort) boa.createProxy(HeapSort.class);

	      // ACT
	      for (int x = (16 * 1024); x >= 1024; x /= 2) {
	         sut.sort(createSortInput(x));
	      }
	      traceReport(boa, "sort");
	
	      // ASSERT
	      BigOAssert.assertLogLinearOrPowerLaw(boa, "sort");
	
	   }
	
	   private static List<Long> createSortInput(int size) {
	      final List<Long> result = new ArrayList<Long>(size);
	      for (int i = 0; i < size; i++) {
	         result.add(Math.round(Long.MAX_VALUE * Math.random()));
	      }
	      return result;
	   }
	
	   private static void traceReport(final BigOAnalyser boa, String method) {
	      System.out.println("--- HeapSortTest -----------------------");
	      System.out.println();
	      final Table<Integer, String, Double> data = boa.getDataChecked(method);
	      System.out.println(BigOReports.getPolynomialDegree(data));
	      System.out.println(BigOReports.getBestFunctionsReport(data));
	      System.out.println(BigOReports.getDataReport(data));
	   }
	
	}
	
	
With the system under test (sut):

	public class HeapSort {
	
	   public Long[] sort(@BigOParameter List<Long> unsorted) {
			
			...		
			
		   }
		   
		   ....
		   
	   }

	
The expected output looks like:


	--- HeapSortTest -----------------------
	
	ESTIMATED-POLYNOMIAL-DEGREE
	1.1311
	
	TYPE      	R^2 (adjusted)	FUNCTION
	LogLinear	0.9984  		y = 1.43E+02 * x * log( 1.12E+00 * x )
	Logarithmic	0.7853  		y = -5.44E+07 + 7.54E+06 * log ( x )
	Exponential	0.7359  		y = 2.60E+06 * exp ( 1.37E-04 * x )
	
	
	N1		TIME
	1024	961666
	2048	2402231
	4096	4799169
	8192	10723234
	16384	22943745


#### Read More
http://www.sw-engineering-candies.com/blog-1/bigotest-jar-a-library-to-empirically-estimate-big-o-time-efficiency-and-check-results-of-analysis-in-junit-tests

