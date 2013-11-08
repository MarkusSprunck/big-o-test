import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOReports;

public class TestBase {

	public static List<Long> createSortInput(int size) {
		final List<Long> result = new ArrayList<Long>(size);
		for (int i = 0; i < size; i++) {
			result.add(Math.round(Long.MAX_VALUE * Math.random()));
		}
		return result;
	}

	public static void traceReport(final BigOAnalyser bom, String method, String title) {
		System.out.println("----------------------------------------");
		System.out.println(title);
		Table<Integer, String, Double> resultTable = bom.getResultTable(method);
		System.out.println(BigOReports.caclulateBestFunctionsTable(resultTable));
		System.out.println(BigOReports.createDataReport(resultTable));
	}

}