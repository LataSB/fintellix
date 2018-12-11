package com.fintellix.test.automation;

import java.util.HashMap;

public class TestConfig {
	
	private HashMap<String, HashMap<String, Object>> testConfig;
	
	public TestConfig(){
		this.testConfig = new HashMap <String, HashMap<String, Object> >();
	}
	
	public HashMap<String, HashMap<String, Object>> getTestConfig() {
		return testConfig;
	}

	public void setTestConfig(HashMap<String, HashMap<String, Object>> testConfig) {
		this.testConfig = testConfig;
	}
	
	public void setTestListConfig(String sheetName, HashMap<String, Object> sheetConfig){
		this.testConfig.put(sheetName, sheetConfig);
	}
	
	public HashMap<String, Object> getTestListConfig(String sheetName){
		return this.testConfig.get(sheetName);
	}
	

}
