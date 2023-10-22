package com.sw_engineering_candies;

import big_o_test.BigOAnalyser;
import big_o_test.BigOReports;
import com.google.common.collect.Table;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "DemoController", description = "Executes the test measurements")
public class DemoController {

    private static List<Long> createSortInput(int size) {
        final List<Long> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(Math.round(Long.MAX_VALUE * Math.random()));
        }
        return result;
    }

    @GetMapping(value = "/heap-sort", produces = MediaType.TEXT_PLAIN_VALUE)
    String runDemo() {
        StringBuilder sb = new StringBuilder();

        final BigOAnalyser boa = new BigOAnalyser();
        final HeapSort sut = (HeapSort) boa.createProxy(HeapSort.class);

        sb.append("EXECUTIONS\n");
        int step = 1;
        for (int x = (128 * 1024); x >= 256; x /= 2, step++) {
            List<Long> sortInput = createSortInput(x);
            Long start = System.currentTimeMillis();
            sut.sort(sortInput);
            sb.append(step + "\t" + sortInput.size() + "\t" + (System.currentTimeMillis() - start) + "ms\n");
        }
        sb.append("\n");

        final Table<Integer, String, Double> data = boa.getDataChecked("sort");
        sb.append(BigOReports.getPolynomialDegree(data));
        sb.append("\n");
        sb.append(BigOReports.getBestFunctionsReport(data));

        return sb.toString();
    }


    @GetMapping(value = "/bubble-sort", produces = MediaType.TEXT_PLAIN_VALUE)
    String runDemo2() {
        StringBuilder sb = new StringBuilder();

        final BigOAnalyser boa = new BigOAnalyser();
        final BubbleSort sut = (BubbleSort) boa.createProxy(BubbleSort.class);

        sb.append("EXECUTIONS\n");
        int step = 1;
        for (int x = (4 * 1024); x >= 256; x /= 2, step++) {
            List<Long> sortInput = createSortInput(x);
            Long start = System.currentTimeMillis();
            sut.sort(sortInput);
            sb.append(step + "\t" + sortInput.size() + "\t" + (System.currentTimeMillis() - start) + "ms\n");
        }
        sb.append("\n");

        final Table<Integer, String, Double> data = boa.getDataChecked("sort");
        sb.append(BigOReports.getPolynomialDegree(data));
        sb.append("\n");
        sb.append(BigOReports.getBestFunctionsReport(data));

        return sb.toString();
    }

}
