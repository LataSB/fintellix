package com.fintellix.test.automation.utils;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import ru.stqa.selenium.factory.WebDriverPool;

public class WebDriverFactory {
	
	public static final String FIREFOX="firefox";
	public static final String CHROME="chrome";
	public static final String INTERNET_EXPLORER="internet explorer";
	public static final String PHANTOM_JS="phantomjs";
	
	public static String downloadFilepath = TestProperties.getProperty("test.downloadDir");
	
	private static String hubUrl;
	private static String appUrl;
	private static String browser;
	private static int timeoutInSeconds;
	private static String ieDriverPath;
	private static String chromeDriverPath;
	private static String firefoxDriverPath;
	private static String phantomjsPath;
	private static Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);
	
	public static String downloadBaseDir;
	public static String setDownloadpath;
	static File file;
	
	public static String systemOSName;
	
	static {
		hubUrl = TestProperties.getProperty("selenium.hubUrl");
		appUrl = TestProperties.getProperty("application.url");
		browser = TestProperties.getProperty("test.browser");
		timeoutInSeconds = Integer.parseInt(TestProperties.getProperty("test.timeoutInSeconds"));
		logger.info("Fetching syetem OS name");
		systemOSName = System.getProperty("os.name").toLowerCase();
		if(systemOSName.startsWith("windows")) {
			System.setProperty("webdriver.ie.driver","drivers/IEDriverServer.exe");
			System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
			System.setProperty("webdriver.gecko.driver","drivers/geckodriver.exe");
			System.setProperty("phantomjs.binary.path","drivers/phantomjs.exe");
		}
		else {
			System.setProperty("webdriver.ie.driver","drivers/IEDriverServer");
			System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
			System.setProperty("webdriver.gecko.driver","drivers/geckodriver");
			System.setProperty("phantomjs.binary.path","drivers/phantomjs");
		}
	}
	
	public static WebDriver getWebDriver() throws Exception{
		WebDriver driver;
		
		
		
		
		DesiredCapabilities capability = new DesiredCapabilities();
		//capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);//....Added
		
		//Initializing on 6th aug --next one line only
		capability.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
		
		//Handling unexpected popups
		capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
		
		/*//Adding for security scan
		
		Proxy proxy = new Proxy();
		String PROXY = "http://172.50.3.4:8080";
		proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY).setSocksProxy(PROXY);
		
		capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		capability.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
		capability.setCapability("proxy", proxy);
        //System.setProperty("webdriver.chrome.driver","/home/vishnu/Desktop/chrome_driver/chromedriver");
        //ChromeDriver driver = new ChromeDriver(capabilities);

		
		//scan code ends
*/		
		URL hub = null;
		if(hubUrl != null && !hubUrl.trim().isEmpty()){
			hub = new URL(hubUrl);
		}
		if(browser.trim().equalsIgnoreCase(FIREFOX) ||
				browser.trim().equalsIgnoreCase(CHROME) ||
				browser.trim().equalsIgnoreCase(INTERNET_EXPLORER) ||
				browser.trim().equalsIgnoreCase(PHANTOM_JS) ){
			capability.setBrowserName(browser.trim());
			if(browser.trim().equalsIgnoreCase(CHROME)){
				
				ChromeOptions options = new ChromeOptions();
				
				options.setExperimentalOption("excludeSwitches", Arrays.asList("disable-popup-blocking"));
				options.addArguments("--no-sandbox");
				
				//below two lines added to address cookies issue-on 6th aug
				
				 HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				// System.out.println("download path = "+downloadFilepath);
				 setDownloadpath = getDownloadDir();
				 System.out.println("download path = "+setDownloadpath);
				
				 chromePrefs.put("profile.default_content_settings.popups", 0);
				 chromePrefs.put("download.prompt_for_download", "false");
				 chromePrefs.put("download.default_directory", setDownloadpath);
				 
			       HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
			       options.setExperimentalOption("prefs", chromePrefs);
			       
				//Map<String, Object> prefs = new HashMap<String, Object>();
				//.put("profile.default_content_settings_values.popups", 1);
				//options.setExperimentalOption("prefs", prefs);
				capability.setCapability(ChromeOptions.CAPABILITY, options);
				//added below two lines on 6th aug
				capability.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
				capability.setCapability("applicationCacheEnabled", false);
			}
			
			if(browser.trim().equalsIgnoreCase(FIREFOX)) {
				FirefoxProfile customProfile = new FirefoxProfile();
				customProfile.setPreference("dom.disable_beforeunload", true);
				capability.setCapability(ChromeOptions.CAPABILITY, customProfile);
				//FirefoxDriver driver = new FirefoxDriver(customProfile);
			}
		}else{
			capability.setBrowserName(INTERNET_EXPLORER);
		}
		driver = WebDriverPool.DEFAULT.getDriver(hub,capability);	
		driver.manage().timeouts().implicitlyWait(timeoutInSeconds, TimeUnit.SECONDS);
		logger.debug("Giving driver instance"+driver);
		return driver;
	}
	
	public static void releaseDriver(WebDriver driver){
		logger.debug("Dismiss driver"+driver);
		WebDriverPool.DEFAULT.dismissDriver(driver);
	}

	public static void releaseAll() {
		logger.debug("clear web driver pool");
		WebDriverPool.DEFAULT.dismissAll();
		
	}
	
	public static String getDownloadDir() throws IOException {
		
		downloadBaseDir = System.getProperty("user.dir");
		file = new File(downloadBaseDir);
		downloadBaseDir = file.getCanonicalPath() +"/downloads/";
		 File directory = new File(String.valueOf(downloadBaseDir));

		    if (!directory.exists()) {
		        directory.mkdir();
		        System.out.println("Main download directory created");
		    }else {
		    	System.out.println("Main download directory already present.. so.. not creating");
		    }
		    
	    Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
	  //  System.out.println("system time" +timeStamp);
	    String currentTimeStamp = timeStamp.toString();
	    currentTimeStamp = currentTimeStamp.replace("-", "");
	    currentTimeStamp = currentTimeStamp.replace(":", "");
	    currentTimeStamp = currentTimeStamp.replace(" ", "");
	    currentTimeStamp = currentTimeStamp.replace(".", "");
		  
	    //File handling code for windows and linux
	    
		//String   SubDownloadpath = file.getCanonicalPath() +"\\downloads\\subDownLoad"+currentTimeStamp+"\\";
		//for windows
	    	String   SubDownloadpath = file.getCanonicalPath() +"\\downloads\\subDownLoad\\";
	    //for linux
		//String   SubDownloadpath = file.getCanonicalPath() +"/downloads/subDownLoad/";
			 File subDirectory = new File(String.valueOf(SubDownloadpath));

			    if (!subDirectory.exists()) {
			    	subDirectory.mkdir();
			        System.out.println("Sub download directory created");
			    }else {
			    	System.out.println("Sub download directory already present.. so.. not creating");
			    }	    
		
		return SubDownloadpath;	
	}//end of methods
	
	public static String getDownloadPath() {
		return setDownloadpath;
		
		
	}
	
	public void checkFileExistInDownloadedDirectory(String filename) throws InterruptedException, IOException {
		File folder = new File(WebDriverFactory.getDownloadDir());
		 Thread.sleep(5000);
	    File[] listOfFiles = folder.listFiles();
	    System.out.println("total files = "+listOfFiles.length);
	    
	    
	    String[] fileExt = null;
	    int extIndex = 0;
	    
	    if(listOfFiles.length ==0) {
	   	 Thread.sleep(25000);
	   	 logger.info("seems file is not downloaded yet so waiting 20 more seconds for file to download");
	    }
	    
	    boolean check = new File(WebDriverFactory.getDownloadDir()+filename).exists();
		if(check==true) {
			logger.info("File named "+filename+" downloaded successfully");
			System.out.println("File Exist>>>>>>>>>>>>>>>>>");
			assertTrue(check, "Report template named-- "+filename+" --downloaded successfully");
		}
		 else {
	   	// logger.debug("please specify a new Name to rename the existing file");
			 System.out.println("error!!!!!!");
	    }
	}
	
	public void checkTabExistInExcel(String filename) throws EncryptedDocumentException, InvalidFormatException, IOException {
		 
		Workbook workbook = WorkbookFactory.create(new File(WebDriverFactory.getDownloadDir()+filename));
		 System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
		 
		 for(Sheet sheet: workbook) {
			 
			 if(sheet.getSheetName().equalsIgnoreCase("Validation Status Summary")) {
				 System.out.println("Summary tab is available");
			 }
			 else if (sheet.getSheetName().equalsIgnoreCase("Validation Status Details")) {
				 System.out.println("Details tab is available");
			}
	            //System.out.println("=> " + sheet.getSheetName());
	        }

	}
	
}
