package com.fintellix.test.automation.utils;


import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.fintellix.test.automation.TestStep;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class RESTUrlCommands {
 	public static final String QUERY_RESULT_EQUAL = "QUERY_RESULT_EQUAL";
	public static final String SET_API ="SET_API";
	public static final String SET_INT = "SET_INT";
	public static final String SET_STRING = "SET_STRING";
	public static final String POST_REQUEST="POST_REQUEST";
	public static final String TEST_STRING = "TEST_STRING";
	public static final String SET_DOUBLE = "SET_DOUBLE";
	public static final String TEST_ASSURED = "TEST_ASSURED";
	public static final String SET_BASE_URL = "SET_BASE_URL";
	public static final String TEST_INT = "TEST_INT";
	public static final String TEST_DOUBLE = "TEST_DOUBLE";
	public static final String TEST_ARRAY_ELEMENTS = "TEST_ARRAY_ELEMENTS";
	
	Logger logger = LoggerFactory.getLogger(RESTUrlCommands.class);

	private HashMap<String, ArrayList<TestStep>> customCommands;
	Response res=null;
	

	JSONObject json = new JSONObject();
	private String baseUrl;
	private String setApi;
	public RESTUrlCommands() {
		super();
		
	}

	public RESTUrlCommands(HashMap<String, ArrayList<TestStep>> customCommands) {
		super();
		this.customCommands = customCommands;
	}

	/*
	 * public static final String OPEN_URL = "OPEN_URL"; public static final
	 * String OPEN_URL = "OPEN_URL"; public static final String OPEN_URL =
	 * "OPEN_URL"; public static final String OPEN_URL = "OPEN_URL"; public
	 * static final String OPEN_URL = "OPEN_URL"; public static final String
	 * OPEN_URL = "OPEN_URL";
	 */

	public HashMap<String, ArrayList<TestStep>> getCustomCommands() {
		return customCommands;
	}

	public void setCustomCommands(HashMap<String, ArrayList<TestStep>> customCommands) {
		this.customCommands = customCommands;
	}

	public void executeCommand( TestStep testStep) throws Exception {
		logger.debug("Executing "+testStep);
		
		String command = testStep.getCommand().toUpperCase();
		String pageName = testStep.getPageName();
		String fieldName = testStep.getFieldName();
		String parameter1 = testStep.getParameter1();
		String parameter2 = testStep.getParameter2();
		String parameter3 = testStep.getParameter3();
		String parameter4 = testStep.getParameter4();
		String key=testStep.getFieldName();
		String fieldText;
		
		
		String connName, query, query_result;
		int waitTime;
		
		
		
		
		switch (command) {
		
		case SET_BASE_URL: this.baseUrl = parameter1; break;
		case SET_API : this.setApi = baseUrl+parameter1;break;
		case SET_INT: this.json.put(key, Integer.parseInt(parameter1)); break;
		case SET_DOUBLE: this.json.put(key, Integer.parseInt(parameter1));break;
		case SET_STRING: this.json.put(key, parameter1);break;
		case POST_REQUEST:
			String jsonPayload = this.json.toString();
			this.res = RestAssured.given().contentType(ContentType.JSON).body(jsonPayload).post(setApi); 
			System.out.println(res.asString());
			break;
		case TEST_STRING: res.then().body(key,equalTo(parameter1)); break;
		case TEST_INT : res.then().body(key, equalTo(Integer.parseInt(parameter1))); break;
		case TEST_DOUBLE: res.then().body(key, equalTo(Double.parseDouble(parameter1)));break;
		case TEST_ARRAY_ELEMENTS : String [] tempArry = parameter1.split(",");
			res.then().body(key, hasItems(tempArry));
			break;
		 
			/*
		String menuUrl;
			String functionalityName = parameter1.toUpperCase();
			query = "SELECT URL FROM VYASAFUNCTIONALITYMASTER ";
			query += " WHERE UPPER(FUNCTIONALITYNAME) = '" + functionalityName + "'";
			query += " OR UPPER(FUNCTIONALITYDISPLAYNAME) = '" + functionalityName + "'";
			query += "AND ROWNUM < 2";
			query_result = DBUtils.getSingleRow(query, connName);
			
			if (null != query_result) {
				menuUrl = query_result;
				logger.debug("GOTO_MENU : menuUrl  =" + menuUrl);
				String fullUrl = appBaseUrl + menuUrl;
				driver.get(fullUrl);
			} else {
				throw new Exception("Functionality/Menu not found: " + parameter1);
			}
	
			break;
			
			*/		

//		case RESTUrlCommands.WAIT_UNTIL_VISIBLE:
//
//			try {
//				waitTime = Integer.parseInt(parameter1);
//				logger.debug("Wait time for element is " + waitTime);
//			} catch (Exception e) {
//				logger.error("Illegal wait period for WAIT_UNTIL_VISIBLE command", e);
//				waitTime = 10;
//			}
//			wait = new WebDriverWait(driver, waitTime);
//			wait.until(ExpectedConditions.visibilityOf(element));
//			logger.debug("Exiting wait event");
//			break;
//		case RESTUrlCommands.WAIT_UNTIL_CLICKABLE:
//			try {
//				waitTime = Integer.parseInt(parameter1);
//				logger.debug("Wait time for element is " + waitTime);
//			} catch (Exception e) {
//				logger.error("Illegal wait period for WAIT_UNTIL_VISIBLE command", e);
//				waitTime = 10;
//			}
//			wait = new WebDriverWait(driver, waitTime);
//			wait.until(ExpectedConditions.elementToBeClickable(element));
//			logger.debug("Exiting wait event");
//			break;
//		case RESTUrlCommands.CLICK_ALERT_OK:
//			Alert alert = driver.switchTo().alert();
//			alert.accept();
//			break;
//		case RESTUrlCommands.KEY_DOWN:
//			element.sendKeys(Keys.DOWN);
//			break;
//		case RESTUrlCommands.SELECT_DROPDOWN_EXT_IF_NOT_NULL:
//			if(null == parameter1 || parameter1.equalsIgnoreCase("")){
//				break;
//			}
//		case RESTUrlCommands.SELECT_DROPDOWN_EXT:
//			element.sendKeys(Keys.DOWN);
//			fieldLocator = ObjectRepository.locateElement("Common", "Drop_down_list_item", parameter1);
//			WebElement element2 = driver.findElement(fieldLocator);
//			wait = new WebDriverWait(driver, 10);
//			wait.until(ExpectedConditions.elementToBeClickable(element2));
//			element2.click();
//			break;
			
		default:
			if (null != this.customCommands) {
				ArrayList<TestStep> customCommandSteps = (ArrayList<TestStep>) this.customCommands.get(command);
				if (null != customCommandSteps) {
					// Check in parameter 1 is defined for custom command. If
					// yes, set value in a HashMap.
					HashMap<String, String> customCommandArgs = new HashMap<String, String>();
					if (null != parameter1 && !parameter1.equalsIgnoreCase("")) {
						String[] args = parameter1.split("\\|");
						for (int index = 0; index < args.length; index++) {
							customCommandArgs.put("$" + (index + 1), args[index]);
							logger.debug("Custom argument:" + "$" + (index + 1) + " value:" + args[index]);
						}
					}

					for (TestStep customStep : customCommandSteps) {
						this.executeCommand(setCustomCommandParams(customStep, customCommandArgs));
					}
				} else {
					throw new Exception("Custom Command Definition not found for " + command);
				}
			} else {
				throw new Exception("Custom Command Definition not found for " + command);
			}
		}

	}

	private TestStep setCustomCommandParams(TestStep customStep, HashMap<String, String> commandArgs) {
		TestStep retStep = new TestStep(customStep);
		String parameter1 = customStep.getParameter1();
		String parameter2 = customStep.getParameter2();
		String parameter3 = customStep.getParameter3();
		String parameter4 = customStep.getParameter4();
		if (null != parameter1 && null != commandArgs.get(parameter1)) {
			logger.debug("Retrived from args" + (String) commandArgs.get(parameter1));
			retStep.setParameter1((String) commandArgs.get(parameter1));
		}
		if (null != parameter2 && null != commandArgs.get(parameter2)) {
			retStep.setParameter2((String) commandArgs.get(parameter2));
		}
		if (null != parameter3 && null != commandArgs.get(parameter3)) {
			retStep.setParameter3((String) commandArgs.get(parameter3));
		}
		if (null != parameter4 && null != commandArgs.get(parameter4)) {
			retStep.setParameter4((String) commandArgs.get(parameter4));
		}
		logger.debug("Updated custom command" + customStep);
		return retStep;
	}

}
