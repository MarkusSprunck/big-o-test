## BigOTest.jar 
### is a Library to Empirically Estimate Big-O Time Efficiency and Check Results of Analysis in Unit Tests

This library supports the need to check time efficiency of algorithms during development 
and refactoring. 

#### How does it look like?

The goal is to support unit-test in the following form:

    public class WrapperTest {
    
    	public Long[] simpleWrapperFirst(@BigOParameter List<Long> input) {
    		final SortedSet<Long> sorted = new TreeSet<Long>();
    		sorted.addAll(input);
    		Long[] result = new Long[sorted.size()];
    		return sorted.toArray(result);
    	}
    	
    	public static List<Long> createSortInput(int size) {
			final List<Long> result = new ArrayList<Long>(size);
			for (int i = 0; i < size; i++) {
				result.add(Math.round(Long.MAX_VALUE * Math.random()));
			}
			return result;
		}
    
    	@Test
    	public void simpleWrapper() {
    
    		// ARRANGE
    		final BigOAnalyser bom = new BigOAnalyser();
    		final WrapperTest sut = (WrapperTest) bom.createProxy(WrapperTest.class);
    		
    		// ACT
    		for (int x = 4 * 65536; x >= 512; x /= 2) {
    			sut.simpleWrapperFirst(createSortInput(x));
    		}
    		
    		// ASSERT
    		BigOAssert.assertPowerLaw(bom, "simpleWrapperFirst");
    	}
	}

#### First Steps

The best starting point are the unit test in the sub-project BigOTest-Demo. This work is a 
prove of concept and in an early stage. 
