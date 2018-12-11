package com.fintellix.test.automation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fintellix.test.automation.utils.TestProperties;

import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;

public class FintellixTestAutomation {

	private static Logger logger = LoggerFactory.getLogger(FintellixTestAutomation.class);

	@Parameter(variableArity = true, description = "Test data file(s) ex: file1.xlsx file2.xlsx ..")
	List<String> testDataFilesList = new ArrayList<String>();
	static boolean enableParallel;
	static int numberOfParallelTests;
	static int testng_verbocity = 1;
	static boolean enableATUReports = false;

	public static void main(String[] args) {

		FintellixTestAutomation ft = new FintellixTestAutomation();
		new JCommander(ft, args);
		

		// Parse test properties file
		enableParallel = Boolean.parseBoolean(TestProperties.getProperty("test.enableParallelExecution"));
		numberOfParallelTests = 1;
		if (enableParallel) {
			try {
				numberOfParallelTests = Integer.parseInt(TestProperties.getProperty("test.numberOfparallelTests"));
			} catch (Exception e) {
				logger.error("test.numberOfparallelTests property not set", e);
				numberOfParallelTests = 5;
			}

		}
		try{
			testng_verbocity = Integer.parseInt(TestProperties.getProperty("test.log.verbose.level"));
		}catch(Exception e){
			logger.error("Failed to parse test.log.verbose.level from testing.properties",e);
			testng_verbocity = 5;
		}
		if(!enableParallel){
			try{
				enableATUReports = Boolean.parseBoolean(TestProperties.getProperty("test.atu.enableATUReporting"));
			}catch(Exception e){
				logger.error("Failed to parse test.atu.enableATUReporting",e);
			}
			System.setProperty("atu.reporter.config", "conf/atu.properties");
		}
		
		ft.initiateTests();
	}

	private void initiateTests() {

		if (testDataFilesList.size() == 0) {
			String excelFileName = TestProperties.getProperty("test.datafile");
			testDataFilesList.add(excelFileName);
			logger.info("Using test data file specified in testing.properties:test.datafile = " + excelFileName);
		}

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		for (String testDataFile : testDataFilesList) {
			suites.add(createTestSuite(testDataFile));

		}
		TestNG testng = new TestNG();
		testng.setDefaultSuiteName("Fintellix Default Test Suite");
		testng.setSuiteThreadPoolSize(20);
		testng.setXmlSuites(suites);
		testng.setVerbose(testng_verbocity);
		testng.setConfigFailurePolicy("continue");
		// testng.setThreadCount(50);
		if(enableATUReports){
			List <Class> listenerList = new ArrayList <Class>();
			listenerList.add(ATUReportsListener.class);
			listenerList.add(ConfigurationListener.class);
			listenerList.add(MethodListener.class);
			testng.setListenerClasses(listenerList);
		}
		
		logger.info("03: running testNg Tests");
		testng.run();
	}

	private XmlSuite createTestSuite(String testDataFile) {
		XmlSuite suite = new XmlSuite();
		suite.setName(FilenameUtils.getBaseName(testDataFile));
		if (enableParallel) {
			suite.setParallel(XmlSuite.PARALLEL_INSTANCES);
		} else {
			suite.setParallel(XmlSuite.PARALLEL_NONE);
		}
		suite.setThreadCount(numberOfParallelTests);

		List<XmlClass> classes = new ArrayList<XmlClass>();
		XmlClass clazz = new XmlClass(TestFactory.class);
		classes.add(clazz);

		XmlTest test = new XmlTest(suite);
		test.setName("Fintellix-Test");
		test.setXmlClasses(classes);
		test.addParameter("testDataFile", testDataFile);
		logger.info("Created Test suite for "+ testDataFile);
		return suite;
	}
}
