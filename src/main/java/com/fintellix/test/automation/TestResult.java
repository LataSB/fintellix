package com.fintellix.test.automation;

public class TestResult {
	private String testCaseListName;
	private String testCaseName;
	private String testCaseId;
	private int testResult;
	private String screenShotPath;
	
	public String getTestCaseListName() {
		return testCaseListName;
	}
	public void setTestCaseListName(String testCaseListName) {
		this.testCaseListName = testCaseListName;
	}
	public String getTestCaseName() {
		return testCaseName;
	}
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}
	public String getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}
	public int getTestResult() {
		return testResult;
	}
	public void setTestResult(int testResult) {
		this.testResult = testResult;
	}
	public String getScreenShotPath() {
		return screenShotPath;
	}
	public void setScreenShotPath(String screenShotPath) {
		this.screenShotPath = screenShotPath;
	}

}
