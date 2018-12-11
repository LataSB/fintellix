package com.fintellix.test.automation.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.security.auth.login.Configuration;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fintellix.test.automation.TestCase;
import com.fintellix.test.automation.TestCaseList;
import com.fintellix.test.automation.TestConfig;
import com.fintellix.test.automation.TestStep;

public class ExcelParser {
	public static final String TESTCASE_ID_COL = "Testcase Id";
	public static final String TESTCASE_NAME_COL = "Testcase Name";
	public static final String TESTCASE_DESC_COL = "Testcase Desc";
	public static final String TESTCASE_COMMAND_COL = "Test Action";
	public static final String TESTCASE_PAGENAME_COL = "Page";
	public static final String TESTCASE_FIELDNAME_COL = "Field";
	public static final String TESTCASE_PARAM1_COL = "Input Parameter 1";
	public static final String TESTCASE_PARAM2_COL = "Input Parameter 2";
	public static final String TESTCASE_PARAM3_COL = "Input Parameter 3";
	public static final String TESTCASE_PARAM4_COL = "Input Parameter 4";
	public static final String TESTCASE_PARAM5_COL = "Input Parameter 5";
	public static final String TESTCASE_PARAM6_COL = "Input Parameter 6";
	public static final String TESTCASE_PARAM7_COL = "Input Parameter 7";
	public static final String TESTCASE_PARAM8_COL = "Input Parameter 8";
	public static final String TESTCASE_PARAM9_COL = "Input Parameter 9";
	public static final String TESTCASE_PARAM10_COL = "Input Parameter 10";
	public static final String TESTCASE_PARAM11_COL = "Input Parameter 11";
	public static final String TESTCASE_PARAM12_COL = "Input Parameter 12";
	public static final String TESTCASE_CONFIGURATION_SHEET = "Test Configuration";
	public static final String CUSTOM_COMMANDS_SHEET = "Custom Commands";
	public static final String SETUP_PRETEST = "SETUP-PRETEST";
	public static final String SETUP_POSTTEST = "SETUP-POSTTEST";
	public static final String ExcludedGroupID = "groupExcluded"; //thisGroupShouldBe excluded test step to testcase

	private HashMap<String, TestCaseList> allTestCaseLists;

	private Logger logger = LoggerFactory.getLogger(ExcelParser.class);
	private String excelFileName;
	private TestConfig testConfig;
	private HashMap<String, ArrayList<TestStep>> customCommands;

	private String runGroup;

	public String getExcelFileName() {
		return this.excelFileName;
	}

	public void setExcelFileName(String excelFileName) throws Exception {
		this.excelFileName = excelFileName;
		parseExcelFile();
	}

	public ExcelParser() {
		this.excelFileName = null;
	}

	public ExcelParser(String excelFileName) throws Exception {
		this.excelFileName = excelFileName;
		parseExcelFile();
	}

	public void setAllTestCaseLists(HashMap<String, TestCaseList> allTestCaseLists) {
		this.allTestCaseLists = allTestCaseLists;
	}

	public HashMap<String, TestCaseList> getAllTestCaseLists() {
		return this.allTestCaseLists;

	}

	public TestConfig getTestConfig() {
		return testConfig;
	}

	public void setTestConfig(TestConfig testConfig) {
		this.testConfig = testConfig;
	}

	public HashMap<String, ArrayList<TestStep>> getCustomCommands() {
		return customCommands;
	}

	public void setCustomCommands(HashMap<String, ArrayList<TestStep>> customCommands) {
		this.customCommands = customCommands;
	}

	private void parseExcelFile() throws Exception {
		logger.debug("Parsing Excel document " + excelFileName);
		this.allTestCaseLists = new HashMap<String, TestCaseList>();
		File file = new File(this.excelFileName);
		Workbook wb = WorkbookFactory.create(file,null,true);
		// Check if test configuration exists!!
		parseTestConfig(wb.getSheet(TESTCASE_CONFIGURATION_SHEET));

		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			Sheet sheet = wb.getSheetAt(i);
			String sheetName = sheet.getSheetName();
			logger.debug("Parsing Sheet " + sheet.getSheetName());
			if (sheetName.equalsIgnoreCase(TESTCASE_CONFIGURATION_SHEET)) {
				// parseTestConfig(sheet);
				continue;
			} else if (sheetName.equalsIgnoreCase(CUSTOM_COMMANDS_SHEET)) {
				parseCustomCommands(sheet);
			} else {
				HashMap<String, Object> sheetConfig = this.testConfig.getTestListConfig(sheetName);
				if (null != sheetConfig && ((String) sheetConfig.get("enabled")).equalsIgnoreCase("Y")) {
					if(null != sheetConfig.get("runGroups")) {
						runGroup = (String) sheetConfig.get("runGroups");
						TestCaseList tcList = parseExcelSheet(sheet);
						tcList.setTestListType((String) sheetConfig.get("testType"));
						allTestCaseLists.put(sheetName, tcList);
					}
				}
			}
		}
		wb.close();
	}

	public TestCaseList parseExcelSheet(Sheet sheet) throws Exception {
		logger.debug("Start parseExcelTestList method. Sheet Name :" + sheet.getSheetName());

		int lastRowNumber = sheet.getLastRowNum();
		logger.debug("Sheet has " + lastRowNumber + " rows");

		TestCaseList tcList = new TestCaseList();
		ArrayList<TestCase> testCases = new ArrayList<TestCase>();
		tcList.setTestcaseListName(sheet.getSheetName());

		TestCase tc = new TestCase();
		ArrayList<TestStep> testStepList = new ArrayList<TestStep>();

		int testCasePriority = 1;
		TestStep ts;
		Row row;
		String testCaseName;
		String testCaseId;
		String prevTestCaseId = null;

		// row 0 is header row. ignore same
		for (int index = 1; index <= lastRowNumber; index++) {
			row = sheet.getRow(index);
			ts = parseExcelRow(row, sheet);
			// ignore if there are blank rows			
			if (null == ts && index <lastRowNumber) 			
				continue;
		
			if(null == ts && index == lastRowNumber) {
				testCaseId = ExcludedGroupID;
			}else {
				testCaseId = ts.getTestCaseId();
			}			
			if (testCaseId != null && !testCaseId.equalsIgnoreCase("") && prevTestCaseId != null) {
				if (!testCaseId.equalsIgnoreCase(prevTestCaseId)
						|| (index == lastRowNumber && testStepList.size() > 0)) {
					// start a new test case
					if (prevTestCaseId.equalsIgnoreCase(SETUP_PRETEST)) {
						tcList.setPreTestSetup(testStepList);
						logger.debug("Pre Test Steps list added");
					} else if (prevTestCaseId.equalsIgnoreCase(SETUP_POSTTEST)) {
						tcList.setPostTestSetup(testStepList);
						logger.debug("Post Test Steps list added");
					} else {
						logger.info("in else loop of excelsheet parser");
							
							if (index == lastRowNumber && testCaseId != ExcludedGroupID) {
								testStepList.add(ts);
							}
							tc.setTestSteps(testStepList);
							tc.setPriority(testCasePriority++);
							testCases.add(tc);
							logger.debug("Test Case Added :" + tc);
							if(testCaseId != ExcludedGroupID)
								tc = new TestCase();
							
					}
					if(testCaseId != ExcludedGroupID)
						testStepList = new ArrayList<TestStep>();
						prevTestCaseId = testCaseId;
				}
			} else if (null == prevTestCaseId) {
				prevTestCaseId = testCaseId;
			}

			if (null != testCaseId && !testCaseId.equalsIgnoreCase("") && !testCaseId.equalsIgnoreCase(ExcludedGroupID)) {
				tc.setTestCaseId(testCaseId);
				testCaseName = ts.getTestCaseName();
				if (null != testCaseName)
					tc.setTestCaseName(testCaseName);
			}

			if(testCaseId != null && testCaseId != ExcludedGroupID)
				testStepList.add(ts);

		}

		tcList.setTestCases(testCases);
		return tcList;

	}

	public TestStep parseExcelRow(Row row, Sheet sheet) throws Exception {
		if (ExcelUtils.isRowEmpty(row))
			return null;
		int firstCellNum = row.getFirstCellNum();
		int lastCellNum = row.getLastCellNum();
		Boolean stepBelongstoExecutableGroup = false;
		logger.debug("Parsing row " + row.getRowNum() + " start =" + firstCellNum + " end=" + lastCellNum);
		TestStep ts = new TestStep();
		DataFormatter formatter = new DataFormatter();
		for (int index = firstCellNum; index <= lastCellNum; index++) {

			// Ignore Testcase Desc column. It may contain merged cells as well.
			if (index == 2)
				continue;
			
			int rowindex = index;
			//ignoring test group column (for CUSTOM_COMMAND Sheet we haven't included rowgroup column)
			if(sheet.getSheetName().equalsIgnoreCase(CUSTOM_COMMANDS_SHEET) && index >= 3)
				rowindex = index + 1;
			Cell cell = row.getCell(index);
			if (index == 3) {
				String groupcelltext = formatter.formatCellValue(cell);
				if(sheet.getSheetName().equalsIgnoreCase(CUSTOM_COMMANDS_SHEET)) {
					stepBelongstoExecutableGroup = true;
				}else {
					HashMap<String, Object> sheetConfig1 = testConfig.getTestListConfig(sheet.getSheetName());
					String runGrpsIncluded = (String) sheetConfig1.get("runGroups");
					if(runGrpsIncluded == null || runGrpsIncluded == "")
						throw new Exception("no groups have been included for sheet: if you want to run all tests privide value 'all' in testconfig sheet  "+sheet.getSheetName());
						
					if(runGrpsIncluded.equalsIgnoreCase("all")) {
						stepBelongstoExecutableGroup = true;
					}else if(formatter.formatCellValue(row.getCell(firstCellNum)).equalsIgnoreCase(SETUP_PRETEST) || formatter.formatCellValue(row.getCell(firstCellNum)).equalsIgnoreCase(SETUP_POSTTEST)) {
						logger.info("presetup and postsetup steps will be included by default don't depend on groups");
						stepBelongstoExecutableGroup = true;
					}else if(groupcelltext.equalsIgnoreCase("") || groupcelltext == null && (!formatter.formatCellValue(row.getCell(firstCellNum)).equalsIgnoreCase(SETUP_PRETEST) || !formatter.formatCellValue(row.getCell(firstCellNum)).equalsIgnoreCase(SETUP_POSTTEST) )) {
						//rows other than presetup and postsetup with value null will be ignore
						stepBelongstoExecutableGroup = false;
					}else {
						String runGrps[] = runGrpsIncluded.split(",");
						for(int m=0; m<runGrps.length; m++) {	
							if(groupcelltext.equalsIgnoreCase(runGrps[m])) {
								stepBelongstoExecutableGroup = true;
								break;
							}
						}
					}
				}
			
			}
					
			if (null != cell) {
				String cellText = formatter.formatCellValue(cell);	
				
				//if (rowindex > 4 || (rowindex >3 &&  sheet.getSheetName().equalsIgnoreCase(CUSTOM_COMMANDS_SHEET))) {
				if (index > 4 ) {
					// variable substitution from property file is enabled
					// for all fields after column 4 (i.e page name, field name etc.)
					if (cellText.startsWith("${") && cellText.endsWith("}")) {
						String propertyName = cellText.substring(2, cellText.length() - 1);
						String propertyValue = TestProperties.getProperty(propertyName);
						if (null != propertyValue) {
							logger.debug("Substitution : " + cellText + " = " + propertyValue);
							cellText = propertyValue;
						} else {
							logger.debug("Skip substition for " + cellText);
						}
					} else if (cellText.contains("${")) {
						String jenkinsBuildNumber = System.getenv("BUILD_NUMBER");
						String propertyName = cellText.substring(cellText.indexOf("{") + 1, cellText.indexOf("}"));
						if (propertyName.equalsIgnoreCase("buildnumber")) {
							if (jenkinsBuildNumber != null) {
								cellText = cellText.replaceAll("\\$\\{" + propertyName + "\\}", jenkinsBuildNumber);
								logger.debug("Replaced cell text : " + cellText);
							} else {
								String propertyValue = TestProperties.getProperty(propertyName);
								if (null != propertyValue) {
									cellText = cellText.replaceAll("\\$\\{" + propertyName + "\\}", propertyValue);
									logger.debug("Replaced cell text : " + cellText);
								} else {
									logger.debug("Skip substition for " + cellText);
								}
							}
						} else {
							String propertyValue = TestProperties.getProperty(propertyName);
							if (null != propertyValue) {
								cellText = cellText.replaceAll("\\$\\{" + propertyName + "\\}", propertyValue);
								logger.debug("Replaced cell text : " + cellText);
							} else {
								logger.debug("Skip substition for " + cellText);
							}
						}
					}
				}
				
				
				switch (rowindex) {
				case 0:
					ts.setTestCaseId(cellText);
					break;
				case 1:
					ts.setTestCaseName(cellText);
					break;
				case 3:
					ts.setTestCaseGroup(cellText);
					break;
				case 4:
					ts.setCommand(cellText);
					break;									
				case 5:
					ts.setPageName(cellText);
					break;
				case 6:
					ts.setFieldName(cellText);
					break;
				case 7:
					ts.setParameter1(cellText);
					break;
				case 8:
					ts.setParameter2(cellText);
					break;
				case 9:
					ts.setParameter3(cellText);
					break;
				case 10:
					ts.setParameter4(cellText);
					break;
				case 11:
					ts.setParameter5(cellText);
					break;
				case 12:
					ts.setParameter6(cellText);
					break;
				case 13:
					ts.setParameter7(cellText);
					break;
				case 14:
					ts.setParameter8(cellText);
					break;
				case 15:
					ts.setParameter9(cellText);
					break;
				case 16:
					ts.setParameter10(cellText);
					break;
				case 17:
					ts.setParameter11(cellText);
					break;
				case 18:
					ts.setParameter12(cellText);
					break;
				}
			}

		}
		
		if(!stepBelongstoExecutableGroup) {
			return null;
		}else {
			return ts;
		}
		
		

	}

	private void parseTestConfig(Sheet sheet) throws Exception {
		if (null == sheet) {
			throw new Exception(TESTCASE_CONFIGURATION_SHEET + " not found!!");
		}
		logger.debug("Parse Test configuration");
		int firstRowNumber = 1;
		int lastRowNumber = sheet.getLastRowNum();
		logger.debug("Sheet has " + lastRowNumber + " rows");

		this.testConfig = new TestConfig();

		// row 0 has table heading. Ignore!!!
		for (int index = firstRowNumber; index <= lastRowNumber; index++) {
			Row row = sheet.getRow(index);
			if (ExcelUtils.isRowEmpty(row))
				continue;
			HashMap<String, Object> sheetConfig = new HashMap<String, Object>();
			String sheetName = null;
			for (int colIndex = row.getFirstCellNum(); colIndex <= row.getLastCellNum(); colIndex++) {
				Cell cell = row.getCell(colIndex);
				try {
					switch (colIndex) {
					case 0:
						break;
					case 1:
						sheetName = cell.getStringCellValue();
						sheetConfig.put("sheetName", sheetName);
						break;
					case 2:
						sheetConfig.put("enabled", cell.getStringCellValue().toLowerCase());
						break;
					case 3:
						sheetConfig.put("runlevel", (int) (cell.getNumericCellValue()));
						break;
					case 4:
						sheetConfig.put("executionOrder", cell.getNumericCellValue());
						break;
					case 5:
						sheetConfig.put("testType", cell.getStringCellValue());
						break;
					case 6:	
						sheetConfig.put("runGroups", cell.getStringCellValue());
					default:
						break;
					}
				} catch (Exception e) {
					logger.error("Error while parsing test Configuration.class Row :" + (index + 1) + " col="
							+ (colIndex + 1), e);
				}

			}
			this.testConfig.setTestListConfig(sheetName, sheetConfig);
			logger.debug("Test Config name Name: " + sheetName + " Config:" + sheetConfig.toString());
		}

	}

	private void parseCustomCommands(Sheet sheet) throws Exception {
		logger.debug("Start parseCustomCommands method.");

		int lastRowNumber = sheet.getLastRowNum();
		logger.debug("Sheet has " + lastRowNumber + " rows");

		this.customCommands = new HashMap<String, ArrayList<TestStep>>();
		ArrayList<TestStep> commandSteps = new ArrayList<TestStep>();

		TestStep ts;
		Row row;
		String commandId;
		String prevCommandId = null;

		// row 0 is header row. ignore same

		for (int index = 1; index <= lastRowNumber; index++) {
			row = sheet.getRow(index);
			ts = parseExcelRow(row, sheet);
			// ignore if there are blank rows
			
			if (null == ts) 
				continue;
			
			commandId = ts.getTestCaseId();
			if (commandId != null && prevCommandId != null) {
				if (!commandId.equalsIgnoreCase(prevCommandId) || (index == lastRowNumber && commandSteps.size() > 0)) {
					// start a new test case
					if (index == lastRowNumber) {
						commandSteps.add(ts);
					}
					customCommands.put(prevCommandId, commandSteps);
					logger.debug("Recorded custom command :" + prevCommandId);
					commandSteps = new ArrayList<TestStep>();
					prevCommandId = commandId;
				}
			} else if (null == prevCommandId) {
				prevCommandId = commandId;
			}

			commandSteps.add(ts);

		}

	}

}
