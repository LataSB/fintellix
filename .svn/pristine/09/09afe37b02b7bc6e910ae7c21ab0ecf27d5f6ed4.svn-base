package com.fintellix.test.automation.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.fintellix.test.automation.TestStep;

public class Command {
	public static final String OPEN_URL = "OPEN_URL";
	public static final String INPUT_TEXT = "INPUT_TEXT";
	public static final String CLICK = "CLICK";
	public static final String TEXT_EQUAL = "TEXT_EQUAL";
	public static final String CLICK_WITH_PARAM = "CLICK_WITH_PARAM";
	public static final String QUERY_RESULT_EQUAL="QUERY_RESULT_EQUAL";
	public static final String GOTO_MENU = "GOTO_MENU";
	Logger logger = LoggerFactory.getLogger(Command.class);

	private HashMap<String, ArrayList<TestStep>> customCommands;

	public Command() {
		super();
	}

	public Command(HashMap<String, ArrayList<TestStep>> customCommands) {
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

	public void executeCommand(WebDriver driver, TestStep testStep) throws Exception {

		String command = testStep.getCommand().toUpperCase();
		String pageName = testStep.getPageName();
		String fieldName = testStep.getFieldName();
		String parameter1 = testStep.getParameter1();
		String parameter2 = testStep.getParameter2();
		String parameter3 = testStep.getParameter3();
		String parameter4 = testStep.getParameter4();
		String parameter5 = testStep.getParameter5();
		String parameter6 = testStep.getParameter6();
		String parameter7 = testStep.getParameter7();
		String parameter8 = testStep.getParameter8();
		String parameter9 = testStep.getParameter9();
		String parameter10 = testStep.getParameter10();
		String parameter11 = testStep.getParameter11();
		String parameter12 = testStep.getParameter12();
		String fieldText;
		By fieldLocator;
		WebElement element = null;

		if (null != fieldName && !fieldName.equalsIgnoreCase("")) {
			if(command.equalsIgnoreCase(CLICK_WITH_PARAM)){
				fieldLocator = ObjectRepository.locateElement(pageName, fieldName,parameter1);
			}else{
				fieldLocator = ObjectRepository.locateElement(pageName, fieldName);
				
			}
			element = driver.findElement(fieldLocator);
			
		}
		
		String connName, query, query_result;
		switch (command) {
		case Command.OPEN_URL:
			driver.get(parameter1);
			break;
		case Command.INPUT_TEXT:
			element.sendKeys(parameter1);
			break;
		case Command.CLICK_WITH_PARAM:
		case Command.CLICK:
			element.click();
			break;
		case Command.TEXT_EQUAL:
			fieldText = element.getText();
			Assert.assertEquals(fieldText, parameter1);
			break;
		case Command.QUERY_RESULT_EQUAL:
			connName = parameter1;
			query = parameter2;
			String expectedResult = parameter3;
			if(null == expectedResult || expectedResult.equalsIgnoreCase("")){
				throw new Exception("Expected result not specified.");
			}
			query_result = DBUtils.getSingleRow(query, connName);
			Assert.assertEquals(query_result, expectedResult);
			break;
		case Command.GOTO_MENU:
			connName = "app";
			String appBaseUrl = TestProperties.getProperty("application.url");
			if(!appBaseUrl.endsWith("/")){
				appBaseUrl = appBaseUrl + "/";
			}
			logger.debug("GOTO_MENU : base url =" + appBaseUrl);
			String menuUrl ;
			String functionalityName = parameter1.toUpperCase();
			query = "SELECT URL FROM VYASAFUNCTIONALITYMASTER ";
					query+= " WHERE UPPER(FUNCTIONALITYNAME) = '" + functionalityName + "'";
					query+= " OR UPPER(FUNCTIONALITYDISPLAYNAME) = '" + functionalityName + "'";
					query+= "AND ROWNUM < 2";
			query_result = DBUtils.getSingleRow(query, connName);
			if(null != query_result){
				menuUrl = query_result;
				logger.debug("GOTO_MENU : menuUrl  =" + menuUrl);
				String fullUrl = appBaseUrl + menuUrl;
				driver.get(fullUrl);
			}else{
				throw new Exception("Functionality/Menu not found: "+ parameter1);
			}
			break;
			
		default:
			if (null != this.customCommands) {
				ArrayList<TestStep> customCommandSteps = (ArrayList<TestStep>) this.customCommands.get(command);
				if (null != customCommandSteps) {
					for (TestStep customStep : customCommandSteps) {
						this.executeCommand(driver, customStep);
					}
				} else {
					throw new Exception("Custom Command Definition not found for " + command);
				}
			} else {
				throw new Exception("Custom Command Definition not found for " + command);
			}
		}

	}
	
	
}
