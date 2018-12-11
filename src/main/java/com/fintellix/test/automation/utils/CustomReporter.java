package com.fintellix.test.automation.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import com.fintellix.test.automation.TestResult;
import com.fintellix.test.automation.TestResultList;

public class CustomReporter implements IReporter {
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		Workbook wb = new XSSFWorkbook();
		Sheet summarySheet = wb.createSheet("Run Summary");
		//adding code
		
		  CellStyle passstyle = wb.createCellStyle();
		  passstyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		  passstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		  
		  CellStyle failstyle = wb.createCellStyle();
		  failstyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		  failstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  
		    //code end
		
		int rowNum = 0;
		Row row = summarySheet.createRow(rowNum++);
		String[] reportHeadings = { "Srl. No", "File Name", "Sheet Name", "Testcase Id", "Testcase Name",
				"Test Result" };
		int cellNo = 0;
		for (String cellText : reportHeadings) {
			Cell cell = row.createCell(cellNo++);
			cell.setCellValue(cellText);
		}
		// ArrayList<TestResult> testResultList =
		// TestResultList.getTestResultList();

		Sheet detailsSheet = wb.createSheet("Run Details (excluding skipped)");
		int rowNum1 = 0;
		Row row1 = detailsSheet.createRow(rowNum1++);
		String[] reportHeadings2 = { "Srl. No", "File Name", "Sheet Name", "Testcase Name", "Command", "Time Taken",
				"Start TIme" };
		int cellNo1 = 0;
		for (String cellText : reportHeadings2) {
			Cell cell = row1.createCell(cellNo1++);
			cell.setCellValue(cellText);
		}
		// Iterating over each suite included in the test
		for (ISuite suite : suites) {
			// Following code gets the suite name
			String suiteName = suite.getName();
			// Getting the results for the said suite
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for (ISuiteResult sr : suiteResults.values()) {
				ITestContext tc = sr.getTestContext();
				IResultMap passCases = tc.getPassedTests();
				IResultMap failCases = tc.getFailedTests();
				IResultMap skipCases = tc.getSkippedTests();
				IResultMap[] allResults = { failCases, skipCases, passCases };
				for (IResultMap rm : allResults) {
					Set<ITestResult> results = rm.getAllResults();
					for (ITestResult res : results) {
						
						String testCaseName = res.getTestName();
						
						//String testCaseName = (String) res.getAttribute("testCaseName");
						
						String testCaseId = (String) res.getAttribute("testCaseId");
						ArrayList<Object[]> timeLog = (ArrayList<Object[]>) res.getAttribute("timeLog");
						String testCaseListName = (String) res.getAttribute("testCaseListName");
						if (null != timeLog) {
							for (Object[] testStep : timeLog) {
								row = detailsSheet.createRow(rowNum1++);
								cellNo1 = 0;
								Cell cell = row.createCell(cellNo1++);
								cell.setCellValue(rowNum1 - 1);
								cell = row.createCell(cellNo1++);
								cell.setCellValue(suiteName);
								cell = row.createCell(cellNo1++);
								cell.setCellValue(testCaseListName);
								cell = row.createCell(cellNo1++);
								cell.setCellValue(testCaseName);
								cell = row.createCell(cellNo1++);
								cell.setCellValue((String) testStep[0]);
								cell = row.createCell(cellNo1++);
								long timeTaken = (long) testStep[2];
								long seconds = TimeUnit.MILLISECONDS.toSeconds(timeTaken);
								long mills = timeTaken % 1000;
								cell.setCellValue(Long.toString(seconds) + " s " + mills + " ms");
								cell = row.createCell(cellNo1++);
								long startTime = (long) testStep[1];
								cell.setCellValue(new Date(startTime));
							}
						} else {
							row = detailsSheet.createRow(rowNum1++);
							cellNo1 = 0;
							Cell cell = row.createCell(cellNo1++);
							cell.setCellValue(rowNum1 - 1);
							cell = row.createCell(cellNo++);
							cell.setCellValue(suiteName);
							cell = row.createCell(cellNo1++);
							cell.setCellValue(testCaseName);
							cell = row.createCell(cellNo1++);
							cell = row.createCell(cellNo1++);
							cell = row.createCell(cellNo1++);
						}

						row = summarySheet.createRow(rowNum++);
						cellNo = 0;
						Cell cell = row.createCell(cellNo++);
						cell.setCellValue(rowNum - 1);

						cell = row.createCell(cellNo++);
						cell.setCellValue(suiteName);

						cell = row.createCell(cellNo++);
						cell.setCellValue(testCaseListName);

						cell = row.createCell(cellNo++);
						cell.setCellValue(testCaseId);

						cell = row.createCell(cellNo++);
						cell.setCellValue(testCaseName);
						

						cell = row.createCell(cellNo++);
						
						
						switch (res.getStatus()) {
						case ITestResult.SUCCESS:
							cell.setCellValue("Pass");
							cell.setCellStyle(passstyle);
							break;
						case ITestResult.FAILURE:
							cell.setCellValue("Fail");
							cell.setCellStyle(failstyle);
							break;
						case ITestResult.SKIP:
							cell.setCellValue("Skip");
							break;
						default:
							cell.setCellValue("Unknown");
							break;
						}

					}
				}

			}
		}

		String fileName = "TestResults_" + new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date()) + ".xlsx";
		try {
			FileOutputStream out = new FileOutputStream(new File(fileName));
			wb.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
