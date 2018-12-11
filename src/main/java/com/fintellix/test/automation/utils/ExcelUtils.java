package com.fintellix.test.automation.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.fintellix.test.automation.TestStep;
import com.google.common.base.Strings;

public class ExcelUtils {
	
	private static Workbook wb;
	private static Sheet sheet;
	private static Row row;
	private static Cell cell;
	private static Cell cellType;
	private static FileInputStream fis;
	private static FileOutputStream fos;
	
	static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
	WebDriverFactory wdf = new WebDriverFactory();
	//String downloadPath = wdf.downloadFilepath;
	//static String downloadPath =WebDriverFactory.getDownloadPath();
	DataFormatter formatter = new DataFormatter();
	
	
public ArrayList<String> getExcelRowData(String fileName, String rowNum, String sheetName) throws EncryptedDocumentException, InvalidFormatException, IOException {
	ArrayList<String> rowData = new ArrayList<String>();
	
	logger.info("in getExcelRowData method");
	if((fileName.indexOf("\\") >=0) || (fileName.indexOf("/") >=0))  {
		logger.info("full file path has been provided");
		
	}else{
		logger.info("full file path is not provided so considering default automation downloads folder");
		fileName =WebDriverFactory.getDownloadPath() +fileName;
	}
	
	fis = new FileInputStream(fileName);
	wb = WorkbookFactory.create(fis);
	if(Strings.isNullOrEmpty(sheetName)) {
		sheet = wb.getSheetAt(0);
	}else {
		sheet = wb.getSheet(sheetName);
	}
	row=sheet.getRow(Integer.parseInt(rowNum));
	for(int i=0; i<row.getLastCellNum(); i++) {
		cell=row.getCell(i);	
		String cellData = formatter.formatCellValue(cell);
		rowData.add(cellData);
		
	}//end of for loop
	
	return rowData;
	
}//end of method
	
public void setExcelRowData(String fileName, String rowData, String sheetName, String rowNum,String startCellNum) throws EncryptedDocumentException, InvalidFormatException, IOException {
	logger.info("in setExcelRowData method ");
	int intRowNum=0;
	if(!Strings.isNullOrEmpty(rowNum)) {
		 intRowNum = Integer.parseInt(rowNum);
	}
		
	if((fileName.indexOf("\\") >=0) || (fileName.indexOf("/") >=0))  {
		logger.info("full file path has been provided");
		
	}else{
		logger.info("full file path is not provided so considering default automation downloads folder");
		fileName =WebDriverFactory.getDownloadPath() +fileName;
	}
	System.out.println("file is = " +fileName);
	fis = new FileInputStream(fileName);
	
	wb= WorkbookFactory.create(fis);
	
	if(Strings.isNullOrEmpty(sheetName)) {
		sheet = wb.getSheetAt(0);
	}else {
		sheet = wb.getSheet(sheetName);
	}
	
	if(Strings.isNullOrEmpty(rowNum)) {			
		intRowNum = sheet.getLastRowNum();
	}
	
	row = sheet.getRow(intRowNum);
	row = sheet.createRow(intRowNum+1);
	System.out.println("new row created is "+intRowNum + 1);
	
	int totacellDtatoAdd = rowData.split("\\|").length;
	String[] cellData = rowData.split("\\|");
	
	//We are considering index number here which starts from 0
	if(!Strings.isNullOrEmpty(startCellNum))
	//if(!(startCellNum==null && startCellNum==""))
	{
		System.out.println("Naye wala me ja raha hu");
		for(int i=Integer.parseInt(startCellNum); i<totacellDtatoAdd; i++) {
			System.out.println("writing data to cells = "+i+ "and data is = "+cellData[i]);
			cell = row.createCell(i);
			cell.setCellValue(cellData[i]);
		}
	}
	else {
		System.out.println("purane wala me ja raha hu");
		for(int i=1; i<=totacellDtatoAdd; i++) {
			System.out.println("writing data to cells = "+i+ "and data is = "+cellData[i-1]);
			cell = row.createCell(i);
			cell.setCellValue(cellData[i-1]);
		}
	}
	
		
	
	fis.close();
	
	fos = new FileOutputStream(fileName);
	wb.write(fos);
	fos.close();
	wb.close();
	
}//end of method

@SuppressWarnings("deprecation")
public String getExcelCellData(String fileName, String rowNum, String colNum, String sheetName) throws EncryptedDocumentException, InvalidFormatException, IOException {
	
	int rowNumber = Integer.parseInt(rowNum);
	int colNumber = Integer.parseInt(colNum);
	
	logger.info("in getExcelCellData function : reading data from excell colummn");
	if((fileName.indexOf("\\") >=0) || (fileName.indexOf("/") >=0))  {
		logger.info("full file path has been provided");
		
	}else{
		logger.info("full file path is not provided so considering default automation downloads folder");
		fileName =WebDriverFactory.getDownloadPath() +fileName;
	}
		
	fis= new FileInputStream(fileName);
	wb = WorkbookFactory.create(fis);
	if(!Strings.isNullOrEmpty(sheetName)) {
		sheet = wb.getSheet(sheetName);
	}else {
		logger.info("sheetName is not specified so reading from first sheet");
		sheet=wb.getSheetAt(0);
	}
	System.out.println("sheet is = "+sheet);
	row = sheet.getRow(rowNumber);
	
	//String column = formatter.formatCellValue(row.getCell(colNumber));
	//row.getCell(colNumber).setCellType(Cell.CELL_TYPE_STRING);
	cell = row.getCell(colNumber);
	String cellData =formatter.formatCellValue(cell);
	return cellData;
		
}//end of method

public void setExcelCellData(String fileName, String rowNum, String colNum, String data, String sheetName) throws EncryptedDocumentException, InvalidFormatException, IOException {
	
	int rowNumber = Integer.parseInt(rowNum);
	int colNumber = Integer.parseInt(colNum);
	
	logger.info("in setCellData function : setting data to excell colummn");
	if((fileName.indexOf("\\") >=0) || (fileName.indexOf("/") >=0))  {
		logger.info("full file path has been provided");
		
	}else{
		logger.info("full file path is not provided so considering default automation downloads folder");
		fileName =WebDriverFactory.getDownloadPath() +fileName;
	}
	
	fis = new FileInputStream(fileName);
	
	wb= WorkbookFactory.create(fis);
	
	if(Strings.isNullOrEmpty(sheetName)) {
		sheet = wb.getSheetAt(0);
	}else {
		sheet = wb.getSheet(sheetName);
	}
	
	row = sheet.getRow(rowNumber);
	CellType cellType1;
	try {
		cell=row.getCell(colNumber);
		cellType1  = cellType.getCellTypeEnum();
		cellType.setCellType(cellType1);
		System.out.println("In try block of excellset data cell already present");
	}catch(Exception e) {
		System.out.println("In catch block  setExcelCelldata");
		System.out.println("column to create is: "+colNumber);
		cell = row.createCell(colNumber);
	}//end of try ct
	
	System.out.println("celldata to set: "+data);
	//cellType = cellType1; 
	
	cell.setCellValue(data);
	fis.close();
	fos = new FileOutputStream(fileName);
	wb.write(fos);
	fos.close();
	wb.close();
	
}//end of method


public ArrayList<ArrayList<Object>> readExcelFile(String fileName) throws EncryptedDocumentException, InvalidFormatException, IOException {
	ArrayList<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();

	int maxColCount = 0;
	
	logger.info("in readExcelFile function : reading excel data");
	if((fileName.indexOf("\\") >=0) || (fileName.indexOf("/") >=0))  {
		logger.info("full file path has been provided");
		
	}else{
		logger.info("full file path is not provided so considering default automation downloads folder");
		fileName =WebDriverFactory.getDownloadPath() +fileName;
	}
	
	try {
		fis = new FileInputStream(fileName);
		wb= WorkbookFactory.create(fis);
		
		sheet = wb.getSheetAt(0);
		
		Iterator<Row> rowIterator = sheet.iterator();
		
		while(rowIterator.hasNext()) {
			row = rowIterator.next();
			if(row.getRowNum() == 0) {
				maxColCount = row.getLastCellNum();
				continue;
			}
			
			if(this.isRowEmpty(row)) {
				break;
			}
			
		ArrayList<Object> eachRows = new ArrayList<Object>();
		
		//iterate through all the rows for each column
		for(int cn=0; cn<maxColCount; cn++) {
			cell = row.getCell(cn,Row.CREATE_NULL_AS_BLANK);
			switch(cell.getCellType()) {
				case Cell.CELL_TYPE_NUMERIC:
					if(DateUtil.isCellDateFormatted(cell)) {
						eachRows.add(new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue()));
					}else {
						eachRows.add(cell.getNumericCellValue());
					}
					break;
				case Cell.CELL_TYPE_STRING:
					eachRows.add(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BLANK:
					eachRows.add(null);
					break;
				default:
					eachRows.add(cell.getStringCellValue());
				
			}
		}
		
		list.add(eachRows);		
		}
		
		fis.close();
		wb.close();
	}catch(Exception e) {
		e.printStackTrace();
	}
	return list;
	
}//end of method
	
	
	@SuppressWarnings("deprecation")
	public static boolean isRowEmpty(Row row) {
		if (null == row) return true;
		
	    for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
	        Cell cell = row.getCell(c);
	        if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
	            return false;
	    }
	    return true;
	}

	public static void logTestSuccess(){
		
	}
	
	public static void logTestFailure(){
		
	}
	
	
	
	private static void createResultFile(){
		String fileName = "TestResults_"+ new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date()) + ".xslx";
		Workbook wb = new XSSFWorkbook();
	}
	
	
	public static File readExcelWriteToTextFile	(String fileName) throws EncryptedDocumentException, InvalidFormatException, IOException {
		//ArrayList<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();
		 File list = new File("writedatasamp.txt");  
         Writer writer = new BufferedWriter(new FileWriter(list));
		
		
		int maxColCount = 0;
		
		logger.info("in readExcelFile function : reading excel data");
		if((fileName.indexOf("\\") >=0) || (fileName.indexOf("/") >=0))  {
			logger.info("full file path has been provided");
			
		}else{
			logger.info("full file path is not provided so considering default automation downloads folder");
			fileName =WebDriverFactory.getDownloadPath() +fileName;
		}
		
		try {
			fis = new FileInputStream(fileName);
			wb= WorkbookFactory.create(fis);
			
			sheet = wb.getSheetAt(0);
			
			Iterator<Row> rowIterator = sheet.iterator();
			
			while(rowIterator.hasNext()) {
				row = rowIterator.next();
				if(row.getRowNum() == 0) {
					maxColCount = row.getLastCellNum();
					continue;
				}
				
				if(isRowEmpty(row)) {
					break;
				}
				
			ArrayList<Object> eachRows = new ArrayList<Object>();
			
			//iterate through all the rows for each column
			for(int cn=0; cn<maxColCount; cn++) {
				cell = row.getCell(cn,Row.CREATE_NULL_AS_BLANK);
				switch(cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						if(DateUtil.isCellDateFormatted(cell)) {
							eachRows.add(new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue()));
						}else {
							eachRows.add(cell.getNumericCellValue());
						}
						break;
					case Cell.CELL_TYPE_STRING:
						eachRows.add(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_BLANK:
						eachRows.add(null);
						break;
					default:
						eachRows.add(cell.getStringCellValue());
					
				}
			}
			
			//list.add(eachRows);
			writer.write(");"+"\n");
			}
			
			fis.close();
			wb.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
		
	}//end of method
	
	
	
}
