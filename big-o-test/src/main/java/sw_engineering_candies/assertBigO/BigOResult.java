package sw_engineering_candies.assertBigO;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table;

import java.util.Set;
import java.util.logging.Logger;


public class BigOResult {

    private final BigOAnalyser boa;

    private final Class<?> clazzUnderTest;

    BigOResult(Class<?> clazzUnderTest, BigOAnalyser boa) {
        Preconditions.checkNotNull(boa);
        Preconditions.checkNotNull(clazzUnderTest);

        this.clazzUnderTest = clazzUnderTest;
        this.boa = boa;
    }

    public BigOAnalyser getBigOAnalyser() {
        return boa;
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

    public void trace(Logger... logs) {
        Set<String> methodNames = this.boa.getAnalysedMethodNames();
        StringBuilder message = new StringBuilder();
        for (String method : methodNames) {
            message.append("BigOAnalyser for method '").append(method).append("'\n\n");
            final Table<Integer, String, Double> data = this.boa.getDataChecked(method);
            message.append(BigOReports.getPolynomialDegree(data)).append('\n');
            message.append(BigOReports.getBestFunctionsReport(data)).append('\n');
            message.append(BigOReports.getDataReport(data)).append('\n');
        }
        if (logs.length == 1) {
            logs[0].info(message.toString());
        }
    }

}
