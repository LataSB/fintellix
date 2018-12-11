package com.fintellix.test.automation.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestProperties {
	private static Properties tprop;
	private static Logger logger = LoggerFactory.getLogger(TestProperties.class);
	private static TestProperties testProperties = new TestProperties();
	public static boolean caputureScreenOnFailure;
	public static boolean caputureScreenOnSuccess;
	
	public TestProperties(){
		loadProperties();
	}
	
	private static void loadProperties(){
		if (TestProperties.tprop == null){
			logger.info("Reading data from testing.properties");
			Properties prop = new Properties();
			InputStream in = null;
			//in = new FileInputStream("testing.properties");
			in = TestProperties.class.getClassLoader().getResourceAsStream("testing.properties");
			try {
				prop.load(in);
				in.close();
			} catch (IOException e) {
				System.err.println("Unable to load testing.properties");
				logger.error("Unable to load testing.properties",e);
			}
			TestProperties.tprop = prop;
			caputureScreenOnFailure = Boolean.parseBoolean(getProperty("test.caputureScreenOnFailure"));
			caputureScreenOnSuccess = Boolean.parseBoolean(getProperty("test.caputureScreenOnSuccess"));
			
		}
		
	}
	
	public static String getProperty(String key){
		return tprop.getProperty(key);
	}
}
