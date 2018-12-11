package com.fintellix.test.automation;

import java.util.ArrayList;

public class TestCaseList {
	public static final String BROWSER="BROWSER";
	public static final String REST_API="REST_API";
	
	private String testcaseListName;
	private int executionOrder;
	private ArrayList <TestCase> testCases;
	private ArrayList <TestStep> preTestSetup;
	private ArrayList <TestStep> postTestSetup;
	private String testListType;
	
	public int getExecutionOrder() {
		return executionOrder;
	}
	public void setExecutionOrder(int executionOrder) {
		this.executionOrder = executionOrder;
	}
	public String getTestcaseListName() {
		return testcaseListName;
	}
	public void setTestcaseListName(String testcaseListName) {
		this.testcaseListName = testcaseListName;
	}
	public ArrayList<TestCase> getTestCases() {
		return testCases;
	}
	public void setTestCases(ArrayList<TestCase> testCases) {
		this.testCases = testCases;
	}
	
	public ArrayList<TestStep> getPreTestSetup() {
		return preTestSetup;
	}
	public void setPreTestSetup(ArrayList<TestStep> preTestSetup) {
		this.preTestSetup = preTestSetup;
	}
	public ArrayList<TestStep> getPostTestSetup() {
		return postTestSetup;
	}
	public void setPostTestSetup(ArrayList<TestStep> postTestSetup) {
		this.postTestSetup = postTestSetup;
	}
	public String getTestListType() {
		return testListType;
	}
	public void setTestListType(String testListType) {
		this.testListType = testListType;
	}
	

}
