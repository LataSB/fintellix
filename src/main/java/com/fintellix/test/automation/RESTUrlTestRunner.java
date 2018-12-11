package com.fintellix.test.automation;

import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import com.fintellix.test.automation.utils.BrowserCommands;
import com.fintellix.test.automation.utils.CustomPriorityInterceptor;
import com.fintellix.test.automation.utils.RESTUrlCommands;
import com.fintellix.test.automation.utils.TestProperties;
import com.fintellix.test.automation.utils.WebDriverFactory;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import atu.testng.selenium.reports.CaptureScreen.ScreenshotOf;

@Listeners({ CustomPriorityInterceptor.class })
public class RESTUrlTestRunner implements ITest {
	private static final String SCREEN_CAPUTURE_DIR = "ScreenShots";
	protected TestCase testCase;
	protected StringBuffer verificationErrors = new StringBuffer();
	protected String hubUrl, browser, appUrl, userid, password;
	private String testName;
	public int priority;
	private ArrayList<TestStep> preTestSteps;
	private ArrayList<TestStep> postTestSteps;
	private HashMap<String, ArrayList<TestStep>> customCommands;
	private RESTUrlCommands commands;
	private boolean enableATUDetailLog;
	private boolean enableStepLevelScreenShot;

	Logger logger = LoggerFactory.getLogger(RESTUrlTestRunner.class);

	public RESTUrlTestRunner(TestCase testCase) {
		this.testCase = testCase;
		this.testName = this.testCase.getTestCaseId() + "_"	+ this.testCase.getTestCaseName();
		this.priority = this.testCase.getPriority();
		this.commands = new RESTUrlCommands();
		try {
			enableATUDetailLog = Boolean.parseBoolean(TestProperties.getProperty("test.atu.enableStepLevelReporting"));
			enableStepLevelScreenShot = Boolean
					.parseBoolean(TestProperties.getProperty("test.atu.enableStepLevelScreenShot"));
		} catch (Exception e) {
			enableATUDetailLog = false;
		}
	}

	public RESTUrlTestRunner(TestCase testCase, ArrayList<TestStep> preTestSteps, ArrayList<TestStep> postTestSteps,
			HashMap<String, ArrayList<TestStep>> customCommands) {
		this(testCase);
		this.preTestSteps = preTestSteps;
		this.postTestSteps = postTestSteps;
		this.customCommands = customCommands;
		this.commands.setCustomCommands(customCommands);
	}

	public String getTestName() {
		return this.testName;
	}

	@BeforeMethod(alwaysRun = true)
	public void setUpTests() throws Exception {
		logger.debug("Start method: setUpBeforeClass ");
		try {
			if (null != preTestSteps) {
				logger.debug("Execution pre-test steps");
				for (TestStep preStep : preTestSteps) {
					logger.debug("Executing " + preStep);
					commands.executeCommand(preStep);
				}
			}
		} catch (Exception e) {
			if (enableATUDetailLog) {
				ATUReports.add("Pre Test Set up", LogAs.FAILED,
						(enableStepLevelScreenShot ? new CaptureScreen(ScreenshotOf.BROWSER_PAGE) : null));
			}

		}
	}

	@Test
	public void executeTest() throws Exception {
		ArrayList<Object[]> timeLog = new ArrayList<Object[]>();
		long start, finish;
		ArrayList<TestStep> testSteps = this.testCase.getTestSteps();
		logger.info("Executing test " + this.testName);
		try {
			for (TestStep testStep : testSteps) {
				logger.debug("Executing " + testStep);
				start = System.currentTimeMillis();
				commands.executeCommand(testStep);
				finish = System.currentTimeMillis();
				timeLog.add(new Object[] { testStep.getCommand(), start, (finish - start) });
			}
		} finally {
			ITestResult testResult = Reporter.getCurrentTestResult();
			testResult.setAttribute("timeLog", timeLog);
			testResult.setAttribute("testCaseListName",this.testCase.getTestCaseListName());
			testResult.setAttribute("testCaseId",this.testCase.getTestCaseId());
		}

	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult iResult) throws Exception {
		try {
			TestResult testResult = new TestResult();
			testResult.setTestCaseId(testCase.getTestCaseId());
			testResult.setTestCaseName(testCase.getTestCaseName());
			testResult.setTestCaseListName(testCase.getTestCaseListName());
			testResult.setTestResult(iResult.getStatus());
			TestResultList.addTestResult(testResult);
			
			try {
				if (null != postTestSteps) {
					logger.debug("Execution post-test steps");
					for (TestStep postStep : postTestSteps) {
						logger.debug("Executing " + postStep);
						commands.executeCommand(postStep);
					}
				}
			} catch (Exception e) {
				logger.error("Failure in post steps execution. " + testCase.getTestCaseId(), e);
			}
		} catch (Exception e) {
			logger.error("Error in post execution method", e);
		} finally {
			// WebDriverFactory.releaseDriver(driver);
			logger.debug("Skip dismiss driver");
		}

	}

}
