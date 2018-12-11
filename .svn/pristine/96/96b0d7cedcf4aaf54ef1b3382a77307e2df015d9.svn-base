package com.fintellix.test.automation;

import java.util.ArrayList;

public class TestCase {
	private String testCaseName;
	private String testCaseId;
	private int priority;
	private String testCaseListName;
	
	public TestCase(){
		this.priority = 999;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}

	private ArrayList <TestStep> testSteps;
	
	public String getTestCaseName() {
		return testCaseName;
	}
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}
	
	public ArrayList<TestStep> getTestSteps() {
		return testSteps;
	}
	public void setTestSteps(ArrayList<TestStep> testSteps) {
		this.testSteps = testSteps;
	}
	
	public String toString(){
		StringBuilder retValue = new StringBuilder();
		retValue.append(testCaseName);
		for(TestStep temp: testSteps){
			retValue.append(temp).append("\n");
		}
		return retValue.toString();
	}
	public String getTestCaseListName() {
		return testCaseListName;
	}
	public void setTestCaseListName(String testCaseListName) {
		this.testCaseListName = testCaseListName;
	}
}
