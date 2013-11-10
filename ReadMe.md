## BigOTest.jar 
### is a Library to Empirically Estimate Big-O Time Efficiency and Check Results of Analysis in JUnit Tests

This library supports the need to check time efficiency of algorithms during development. 

#### How does it look like?

The goal is to support unit-test in the following form:

	public class WrapperTest {
	
	   public void sortWrapper(@BigOParameter List<Long> values) {
	      Collections.sort(values);
	   }
	
	   @Test
	   public void sortWrapper_RunJavaCollectionsSort_DetectPolynomialDegree() {
	
	      // ARRANGE
	      final BigOAnalyser boa = new BigOAnalyser();
	      final WrapperTest sut = (WrapperTest) boa.createProxy(WrapperTest.class);
	      boa.deactivate();                              // measurement is deactivated
	      sut.sortWrapper(createSortInput(32 * 1024));   // give JIT compiler the chance to optimize
	      boa.activate();                                // measurement is active
	
	      // ACT
	      for (int x = (32 * 1024); x >= 64; x /= 2) {
	         sut.sortWrapper(createSortInput(x));
	      }
	
	      // ASSERT
	      BigOAssert.assertPolynomialDegree(boa, "sortWrapper", 1.25, 0.5);
	   }
	
	   private static List<Long> createSortInput(int size) {
	      final List<Long> result = new ArrayList<Long>(size);
	      for (int i = 0; i < size; i++) {
	         result.add(Math.round(Long.MAX_VALUE * Math.random()));
	      }
	      return result;
	   }
	
	}


#### First Steps

The best starting point are the unit test in the sub-project BigOTest-Demo. 

#### Status 

This work is a prove of concept and in an early stage. 
