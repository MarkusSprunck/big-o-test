Big-O-Test
==========

Library to Empirically Estimate Big-O Time Efficiency and Check Results of Analysis in Unit Tests
-------------------------------------------------------------------------------------------------

This library supports the need to check time efficiency of algorithms during development 
and refactoring. 

The goal is to support unit-test in the following form:

    @Test
    public void assertLinear_ThreeDataPoints_Ok() {
    
        // ARRANGE
        final BigOAnalyser bom = new BigOAnalyser();
    
        // ACT
        final Algorithms sut = (Algorithms) bom.createProxy(Algorithms.class);
        sut.runLinear(10000);
        sut.runLinear(3000);
        sut.runLinear(1000);
        sut.runLinear(300);
    
        // ASSERT
        BigOAssert.assertLinear(bom, "runLinear");
    }

This work is a prove of concept and in an early stage. The best starting point 
are the unit test in the sub-project BigOTest-Tests.
