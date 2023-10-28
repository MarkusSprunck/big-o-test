import big_o_test.BigOAnalyser;
import big_o_test.BigOReports;
import com.google.common.collect.Table;
import com.sw_engineering_candies.HeapSort;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static big_o_test.BigOAssert.assertLogLinearOrPowerLaw;


@Log
public class HeapSortTest {

    private static List<Long> createSortInput(int size) {
        final List<Long> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(Math.round(Long.MAX_VALUE * Math.random()));
        }
        return result;
    }

    private static void traceReport(final BigOAnalyser boa, String method) {
        final Table<Integer, String, Double> data = boa.getDataChecked(method);
        System.out.println("--- HeapSortTest -----------------------");
        System.out.println(BigOReports.getPolynomialDegree(data));
        System.out.println(BigOReports.getBestFunctionsReport(data));
        System.out.println(BigOReports.getDataReport(data));
    }

    @Test
    public void assertLogLinear_RunHeapSort_DetectLogLinear() {
        // given
        final BigOAnalyser boa = new BigOAnalyser();
        final HeapSort sut = (HeapSort) boa.createProxy(HeapSort.class);

        // when
        for (int x = (128 * 1024); x >= 256; x /= 2) {
            List<Long> sortInput = createSortInput(x);
            log.info("Sort input size " + sortInput.size());
            sut.sort(sortInput);
        }
        log.info("Print trace report");
        traceReport(boa, "sort");

        // then
        log.info("Assert LogLinearOrPowerLaw");
        assertLogLinearOrPowerLaw(boa, "sort");
    }

}