package com.fintellix.test.automation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectRepository {

	private static final String OBJECT_REPO_PATH = "PageObjectRepository";
	private static HashMap<String, Properties> pageObjectsMap = new HashMap<String, Properties>();
	private static Logger logger = LoggerFactory.getLogger(ObjectRepository.class);

	private static String getRawFieldDef(String pageName, String fieldName) throws Exception {
		Properties pageProperties = (Properties) ObjectRepository.pageObjectsMap.get(pageName);
		if (null == pageProperties) {
			pageProperties = loadPageProperties(pageName);
		}
		String fieldDefinition = pageProperties.getProperty(fieldName);
		if (null == fieldDefinition) {
			throw new Exception("Definition for field " + fieldName + " not located in page " + pageName);
		}
		return fieldDefinition;
	}

	public static By locateElement(String pageName, String fieldName) throws Exception {
		String fieldDefinition = getRawFieldDef(pageName, fieldName);
		String splitDef[] = fieldDefinition.split(":", 2);
		if (splitDef.length != 2) {
			throw new Exception("Incorrect field definition for field " + fieldName);
		}
		String searchBy = splitDef[0].toLowerCase();
		String searchString = splitDef[1];
		return findElementByDef(searchBy, searchString);

	}

	public static By locateElement(String pageName, String fieldName, String parameter) throws Exception {
		String fieldDefinition = getRawFieldDef(pageName, fieldName);
		String splitDef[] = fieldDefinition.split(":", 2);
		if (splitDef.length != 2) {
			throw new Exception("Incorrect field definition for field " + fieldName);
		}
		String searchBy = splitDef[0].toLowerCase();
		String searchString = splitDef[1];
		searchString = String.format(searchString, parameter);
		logger.debug("Formatted searchString :" + searchString);
		return findElementByDef(searchBy, searchString);
		
	}

	private static By findElementByDef(String searchBy, String searchString) {
		By by;
		logger.debug("Element def: by=" + searchBy + " searchString=" + searchString);
		switch (searchBy) {
		case "id":
			by = By.id(searchString);
			break;
		case "name":
			by = By.name(searchString);
			break;
		case "xpath":
			by = By.xpath(searchString);
			break;
		case "css":
			by = By.cssSelector(searchString);
			break;
		case "linktext":
			by = By.linkText(searchString);
			break;
		case "partialLinkText":
			by = By.partialLinkText(searchString);
			break;
		default:
			by = null;
			break;
		}

		return by;
	}

	private static Properties loadPageProperties(String pageName) throws Exception {
		String propertyFileName = OBJECT_REPO_PATH + "/" + pageName + ".properties";
		logger.debug("Loading file " + propertyFileName);
		InputStream in = ObjectRepository.class.getClassLoader().getResourceAsStream(propertyFileName);
		Properties prop = new Properties();
		prop.load(in);
		in.close();
		ObjectRepository.pageObjectsMap.put(pageName, prop);
		return prop;

	}

}
