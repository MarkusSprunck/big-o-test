package big_o_test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table;

import java.util.Set;
import java.util.logging.Logger;


public class BigOResult {

    private BigOAnalyser boa = null;

    private Class<?> clazzUnderTest = null;

    BigOResult(Class<?> clazzUnderTest, BigOAnalyser boa) {
        Preconditions.checkNotNull(boa);
        Preconditions.checkNotNull(clazzUnderTest);

        this.clazzUnderTest = clazzUnderTest;
        this.boa = boa;
    }

    public BigOAnalyser getBigOAnalyser() {
        return boa;
    }

    public Class<?> getClassUnderTest() {
        return clazzUnderTest;
    }

    public <P> BigOResult execute(BigOTestAction<P> action) {

        Preconditions.checkNotNull(action);
        Preconditions.checkNotNull(clazzUnderTest);

        @SuppressWarnings("unchecked") final P sut = (P) this.boa.createProxy(clazzUnderTest);

        // give JIT compiler the chance to optimize
        this.boa.deactivate();
        action.apply(sut);

        // here the measurement starts
        this.boa.activate();
        action.apply(sut);

        return this;
    }

    public BigOResult trace(Logger... logs) {

        Set<String> methodNames = this.boa.getAnalysedMethodNames();

        StringBuilder message = new StringBuilder();
        for (String method : methodNames) {
            message.append("BigOAnalyser for method '" + method + "'\n\n");
            final Table<Integer, String, Double> data = this.boa.getDataChecked(method);
            message.append(BigOReports.getPolynomialDegree(data)).append('\n');
            message.append(BigOReports.getBestFunctionsReport(data)).append('\n');
            message.append(BigOReports.getDataReport(data)).append('\n');
        }
        if (logs.length == 1) {
            logs[0].info(message.toString());
        }
        return this;
    }

}
