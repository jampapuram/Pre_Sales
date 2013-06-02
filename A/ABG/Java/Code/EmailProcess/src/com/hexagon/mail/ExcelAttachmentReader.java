package com.hexagon.mail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DateUtil;

public class ExcelAttachmentReader {

	public ExcelAttachmentReader() {
	}

	@SuppressWarnings("unchecked")
	public void displayFromExcel(String xlsPath) {
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(xlsPath);
		} catch (FileNotFoundException e) {
			System.out.println("File not found in the specified path.");
			e.printStackTrace();
		}

		POIFSFileSystem fileSystem = null;

		try {
			fileSystem = new POIFSFileSystem(inputStream);

			HSSFWorkbook workBook = new HSSFWorkbook(fileSystem);
			HSSFSheet sheet = workBook.getSheetAt(0);
			Iterator rows = sheet.rowIterator();

			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();

				// display row number in the console.
				System.out.println("Row No.: " + row.getRowNum());

				// once get a row its time to iterate through cells.
				Iterator cells = row.cellIterator();

				while (cells.hasNext()) {
					HSSFCell cell = (HSSFCell) cells.next();

					// System.out.println ("Cell No.: " + cell.getCellNum ());

					/*
					 * Now we will get the cell type and display the values
					 * accordingly.
					 */
					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC: {
						if (DateUtil.isCellDateFormatted(cell)) {
							System.out.println(" Date is : "
									+ cell.getDateCellValue());

						} else {
							System.out.println(cell.getNumericCellValue());
						}

						break;
					}

					case HSSFCell.CELL_TYPE_STRING: {

						// cell type string.
						HSSFRichTextString richTextString = cell
								.getRichStringCellValue();

						System.out.println("String value: "
								+ richTextString.getString());

						break;
					}

					default: {

						// types other than String and Numeric.
						System.out.println("Type not supported.");

						break;
					}
					}
				}

				HSSFRichTextString DATE_OF_BIRTH = null;
				String CLIENT_CODE = "";
				String CLIENT_NAME = "";
				String APPLICATION_ID = "";
				int AMOUNT = 0;
				String GENDER = "";
				String GROUP_NAME = "";
				String TAX_STATUS = "";
				String BUSINESS_UNIT = "";
				HSSFRichTextString DATE_OF_ACTIVATION = null;
				String CONTACT_PERSON_NAME = "";
				String ADDRESS1 = "";
				String ADDRESS2 = "";
				String ADDRESS3 = "";
				String CITY = "";
				String STATE = "";
				String ZIP = "";
				String FAX = "";
				String TELEPHONE = "";
				String MOBILE_NO = "";
				String EMAIL = "";
				String INTRODUCED_BY = "";
				String TDS_APPLICABILITY = "";
				String PAN_NO = "";
				String BRANCH = "";
				String RM_NAME = "";
				String INTEREST_RATE = "";
				String TYPE_OF_REQUEST = "";
				HSSFRichTextString CR_DT = null;
				String CR_BY = "";
				String ACTIVE = "";
				String dob = "";
				String doa = "";
				String created_date = "";
				try {

					if (row.getCell(0) != null) {
						CLIENT_CODE = row.getCell(0).getStringCellValue();
						System.out
								.println("CLIENT_CODE Value : " + CLIENT_CODE);
					} else {
						CLIENT_CODE = "";
					}

					if (row.getCell(1) != null) {
						CLIENT_NAME = row.getCell(1).getStringCellValue();
						System.out
								.println("CLIENT_NAME Value : " + CLIENT_NAME);
					} else {
						CLIENT_NAME = "";
					}
					if (row.getCell(2) != null) {
						System.out.println("APPLICATION_ID Value in IF: ");
						APPLICATION_ID = row.getCell(2).getStringCellValue();
						System.out.println("APPLICATION_ID Value : "
								+ APPLICATION_ID);
					} else {
						System.out.println("APPLICATION_ID Value in else: ");
						APPLICATION_ID = "";
					}
					if (row.getCell(3) != null) {
						AMOUNT = (int) row.getCell(3).getNumericCellValue();
						System.out.println("AMOUNT Value : " + AMOUNT);
					} else {
						AMOUNT = 0;
					}

					if (row.getCell(4).getRichStringCellValue() != null) {
						DATE_OF_BIRTH = row.getCell(4).getRichStringCellValue();
						System.out.println("DATEOFBIRTH Value : "
								+ DATE_OF_BIRTH);
					} else {

						DATE_OF_BIRTH = null;
					}
					if (row.getCell(5) != null) {
						GENDER = row.getCell(5).getStringCellValue();
						System.out.println("GENDER Value : " + GENDER);
					} else {
						GENDER = "";
					}
					if (row.getCell(6) != null) {
						GROUP_NAME = row.getCell(6).getStringCellValue();
						System.out.println("GROUPNAME Value : " + GROUP_NAME);
					} else {
						GROUP_NAME = "";
					}
					if (row.getCell(7) != null) {
						TAX_STATUS = row.getCell(7).getStringCellValue();
						System.out.println("TAXSTATUS Value : " + TAX_STATUS);
					} else {
						TAX_STATUS = "";
					}
					if (row.getCell(8) != null) {
						BUSINESS_UNIT = row.getCell(8).getStringCellValue();
						System.out.println("BUSINESSUNIT Value : "
								+ BUSINESS_UNIT);
					} else {
						BUSINESS_UNIT = "";
					}

					if (row.getCell(9).getRichStringCellValue() != null) {
						DATE_OF_ACTIVATION = row.getCell(9)
								.getRichStringCellValue();
						System.out.println("DATEOFACTIVATION Value : "
								+ DATE_OF_ACTIVATION);
					} else {
						DATE_OF_ACTIVATION = null;
					}
					if (row.getCell(10) != null) {
						CONTACT_PERSON_NAME = row.getCell(10)
								.getStringCellValue();
						System.out.println("CONTACTPERSONNAME Value : "
								+ CONTACT_PERSON_NAME);
					} else {
						CONTACT_PERSON_NAME = "";
					}
					if (row.getCell(11) != null) {
						ADDRESS1 = row.getCell(11).getStringCellValue();
						System.out.println("ADDRESS1 Value : " + ADDRESS1);
					} else {
						ADDRESS1 = "";
					}
					if (row.getCell(12) != null) {
						ADDRESS2 = row.getCell(12).getStringCellValue();
						System.out.println("ADDRESS2 Value : " + ADDRESS2);
					} else {
						ADDRESS2 = "";
					}
					if (row.getCell(13) != null) {
						ADDRESS3 = row.getCell(13).getStringCellValue();
						System.out.println("ADDRESS3 Value : " + ADDRESS3);
					} else {
						ADDRESS3 = "";
					}
					if (row.getCell(14) != null) {
						CITY = row.getCell(14).getStringCellValue();
						System.out.println("CITY Value : " + CITY);
					} else {
						CITY = "";
					}
					if (row.getCell(15) != null) {
						STATE = row.getCell(15).getStringCellValue();
						System.out.println("STATE Value : " + STATE);
					} else {
						STATE = "";
					}
					if (row.getCell(16) != null) {
						ZIP = row.getCell(16).getStringCellValue();
						System.out.println("ZIP Value : " + ZIP);
					} else {
						ZIP = "";
					}
					if (row.getCell(17) != null) {
						FAX = row.getCell(17).getStringCellValue();
						System.out.println("FAX Value : " + FAX);
					} else {
						FAX = "";
					}
					if (row.getCell(18) != null) {
						TELEPHONE = row.getCell(18).getStringCellValue();
						System.out.println("TELEPHONE Value : " + TELEPHONE);
					} else {
						TELEPHONE = "";
					}
					if (row.getCell(19) != null) {
						MOBILE_NO = row.getCell(19).getStringCellValue();
						System.out.println("MOBILENO Value : " + MOBILE_NO);
					} else {
						MOBILE_NO = "";
					}
					if (row.getCell(20) != null) {
						EMAIL = row.getCell(20).getStringCellValue();
						System.out.println("EMAIL Value : " + EMAIL);
					} else {
						EMAIL = "";
					}
					if (row.getCell(21) != null) {
						INTRODUCED_BY = row.getCell(21).getStringCellValue();
						System.out.println("INTRODUCEDBY Value : "
								+ INTRODUCED_BY);
					} else {
						INTRODUCED_BY = "";
					}
					if (row.getCell(22) != null) {
						TDS_APPLICABILITY = row.getCell(22)
								.getStringCellValue();
						System.out.println("TDSAPPLICABILITY Value : "
								+ TDS_APPLICABILITY);
					} else {
						TDS_APPLICABILITY = "";
					}
					if (row.getCell(23) != null) {
						PAN_NO = row.getCell(23).getStringCellValue();
						System.out.println("PANNO Value : " + PAN_NO);
					} else {
						PAN_NO = "";
					}
					if (row.getCell(24) != null) {
						BRANCH = row.getCell(24).getStringCellValue();
						System.out.println("BRANCH Value : " + BRANCH);
					} else {
						BRANCH = "";
					}
					if (row.getCell(25) != null) {
						RM_NAME = row.getCell(25).getStringCellValue();
						System.out.println("RMNAME Value : " + RM_NAME);
					} else {
						RM_NAME = "";
					}
					if (row.getCell(26) != null) {
						INTEREST_RATE = row.getCell(26).getStringCellValue();
						System.out.println("INTERESTRATE Value : "
								+ INTEREST_RATE);
					} else {
						INTEREST_RATE = "";
					}
					if (row.getCell(27) != null) {
						TYPE_OF_REQUEST = row.getCell(27).getStringCellValue();
						System.out.println("TYPE_OF_REQUEST Value : "
								+ TYPE_OF_REQUEST);
					} else {
						TYPE_OF_REQUEST = "";
					}

					if (row.getCell(28) != null) {
						CR_DT = row.getCell(28).getRichStringCellValue();
						System.out.println("CR_DT Value : " + CR_DT);
						SimpleDateFormat sdf = new SimpleDateFormat(
								"dd/MM/YYYY HH:MM");
						// created_date = sdf.format(CR_DT.getTime());

						System.out.println("CR_DT Value : " + CR_DT);
					} else {
						CR_DT = null;
					}
					if (row.getCell(29) != null) {
						CR_BY = row.getCell(29).getStringCellValue();
						System.out.println("CR_BY Value : " + CR_BY);
					} else {
						CR_BY = "";
					}
					if (row.getCell(30) != null) {
						ACTIVE = row.getCell(30).getStringCellValue();
						System.out.println("ACTIVE Value : " + ACTIVE);
					} else {
						ACTIVE = "";
					}
					Connection connection = null;
					Statement stmt = null;
					Properties prop = new Properties();
					prop.load(new FileInputStream("config.properties"));
					try {
						connection = DriverManager.getConnection(
								prop.getProperty("database"),
								prop.getProperty("dbuser"),
								prop.getProperty("dbpassword"));
						String sql = "INSERT INTO EMAIL_ATTACHMENT_INFO VALUES(EMAIL_ATTCH_SEQ.nextval,(select Max(E.EMAIL_ID) from EMAIL_LOG e),'"
								+ CLIENT_CODE
								+ "','"
								+ CLIENT_NAME
								+ "','"
								+ APPLICATION_ID
								+ "','"
								+ AMOUNT
								+ "',TO_CHAR(TO_DATE(SUBSTR('"
								+ DATE_OF_BIRTH
								+ "',1,20),'DD-MM-RRRR HH24:MI:SS '),'DD-Mon-YY'),'"
								+ GENDER
								+ "','"
								+ GROUP_NAME
								+ "','"
								+ TAX_STATUS
								+ "','"
								+ BUSINESS_UNIT
								+ "',TO_CHAR(TO_DATE(SUBSTR('"
								+ DATE_OF_ACTIVATION
								+ "',1,20),'DD-MM-RRRR HH24:MI:SS '),'DD-Mon-YY'),'"
								+ CONTACT_PERSON_NAME
								+ "','"
								+ ADDRESS1
								+ "','"
								+ ADDRESS2
								+ "','"
								+ ADDRESS3
								+ "','"
								+ CITY
								+ "','"
								+ STATE
								+ "','"
								+ ZIP
								+ "','"
								+ FAX
								+ "','"
								+ TELEPHONE
								+ "','"
								+ MOBILE_NO
								+ "','"
								+ EMAIL
								+ "','"
								+ INTRODUCED_BY
								+ "','"
								+ TDS_APPLICABILITY
								+ "','"
								+ PAN_NO
								+ "','"
								+ BRANCH
								+ "','"
								+ RM_NAME
								+ "','"
								+ INTEREST_RATE
								+ "','"
								+ TYPE_OF_REQUEST
								+ "',TO_CHAR(TO_DATE(SUBSTR('"
								+ CR_DT
								+ "',1,20),'DD-MM-RRRR HH24:MI:SS '),'DD-Mon-YY'),'"
								+ CR_BY + "','" + ACTIVE + "')";
						System.out.println("Sql Query :  " + sql);
						PreparedStatement pstm = (PreparedStatement) connection
								.prepareStatement(sql);
						pstm.executeUpdate();
						System.out.println("Import rows " + row.getRowNum());

					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public static void main(String[] args) { Inbox inbox = new Inbox();
	 * UpdateEmail updateEmailInfo = new UpdateEmail();
	 * 
	 * updateEmailInfo.emailInfo(inbox);
	 * 
	 * ExcelAttachmentReader poiExample = new ExcelAttachmentReader();
	 * 
	 * String xlsPath = "C:\\Users\\admin\\workspace\\EmailProcess\\" +
	 * attachmentName;
	 * 
	 * poiExample.displayFromExcel(xlsPath); try {
	 * 
	 * File file = new File(xlsPath);
	 * 
	 * if (file.delete()) { System.out.println(attachmentName + " is deleted!");
	 * } else { System.out.println("Delete operation is failed."); }
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * 
	 * }
	 * 
	 * }
	 */

}
