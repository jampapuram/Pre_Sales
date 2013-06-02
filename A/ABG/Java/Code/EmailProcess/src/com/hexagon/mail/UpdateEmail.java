package com.hexagon.mail;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

public class UpdateEmail {
	// public static void main(String[] argv) {
	public void emailInfo(Inbox inbox) {
		inbox = new Inbox();

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;

		}

		Connection connection = null;
		Statement stmt = null;
		try {
			inbox.readInbox();
			String from = inbox.getFromEmailAddress();
			String to = inbox.getToEmailAddress();
			System.out.println("Main Class : " + to);
			String cc = inbox.getCCEmailAddress();
			String subject = inbox.getSubject();
			Date mailTime = inbox.getSentTime();
			String attachmentFlag = inbox.getAttachmentFlag();
			String attachmentName = inbox.getAttachmentName();
			String processFlag = inbox.getProcessFlag();
			String successFlag = inbox.getSucessFlag();
			String activeFlag = inbox.getActiveFlag();
			File fileName = inbox.getFileName();
			String applicationFlag = inbox.getApplicationFlag();
			String requestCode = inbox.getRequestCode();
			String emailResponseFlag = inbox.getEmailResponseFlag();
			Object content = inbox.getContent();
			String inboxContent = content.toString();
			String mailReqCode = inbox.getMailRequestCode();
			String mailContent = inbox.getNewContent();

			System.out.println("Request Code : " + mailReqCode);
			Integer messageCount = inbox.getNewMessage();
			Properties prop = new Properties();
			prop.load(new FileInputStream("config.properties"));
			connection = DriverManager.getConnection(
					prop.getProperty("database"), prop.getProperty("dbuser"),
					prop.getProperty("dbpassword"));
			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = connection.createStatement();
			System.out.println("Message Count..." + messageCount);
			if (messageCount != 0) {
				String preQueryStatement = "Insert Into EMAIL_LOG (EMAIL_ID,EMAIL_TYPE,FROM_EMAIL_ADDRESS,CC_EMAIL_ADDRESS,TO_EMAIL_ADDRESS,SUBJECT,ATTACHMENT_FLAG,ATTACHMENT_NAME,PROCESS_FLAG,SUCCESS_FLAG,SUCCESS_FLAG_UPD_DT,ACTIVE,CR_DT,ORG_ID,APPLSTAGE,REQUEST_CODE,EMAIL_RESPONSE_FLAG)Values (mail_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement preparedStmt = connection
						.prepareStatement(preQueryStatement);

				preparedStmt.setString(1, "Email");
				if (from != null) {
					preparedStmt.setString(2, from);
				} else {
					preparedStmt.setString(2, null);
				}
				System.out.println("From : " + from);
				// cc email address
				if (cc != null) {
					preparedStmt.setString(3, cc);
				} else {
					preparedStmt.setString(3, null);
				}
				System.out.println("CC : " + cc);
				// To email address
				if (to != null) {
					preparedStmt.setString(4, to);
				} else {
					preparedStmt.setString(4, null);
				}
				System.out.println("to : " + to);
				// subject
				if (subject != null) {
					preparedStmt.setString(5, subject);
				} else {
					preparedStmt.setString(5, null);
				}
				System.out.println("subject : " + subject);
				// attachment flag
				if (attachmentFlag != null) {
					preparedStmt.setString(6, attachmentFlag);
				} else {
					preparedStmt.setString(6, null);
				}
				System.out.println("attachmentFlag : " + attachmentFlag);
				// attachment name
				if (attachmentName != null) {
					preparedStmt.setString(7, attachmentName);
				} else {
					preparedStmt.setString(7, null);
				}
				System.out.println("attachmentName : " + attachmentName);
				// Process Flag
				if (processFlag != null) {
					preparedStmt.setString(8, processFlag);
				} else {
					preparedStmt.setString(8, null);
				}
				System.out.println("processFlag : " + processFlag);
				// Sucess Flag
				if (successFlag != null) {
					preparedStmt.setString(9, successFlag);
				} else {
					preparedStmt.setString(9, null);
				}
				System.out.println("successFlag : " + successFlag);
				// received time
				if (mailTime != null) {
					java.sql.Date sqlDate = new java.sql.Date(
							mailTime.getTime());
					preparedStmt.setDate(10, sqlDate);
					System.out.println("sqlDate : " + sqlDate);
				} else {
					preparedStmt.setDate(10, null);
				}

				// Active time
				if (activeFlag != null) {
					preparedStmt.setString(11, activeFlag);
				} else {
					preparedStmt.setString(11, null);
				}
				System.out.println("activeFlag : " + activeFlag);
				// created date
				if (mailTime != null) {
					java.sql.Date sqlDate = new java.sql.Date(
							mailTime.getTime());
					preparedStmt.setDate(12, sqlDate);
					System.out.println("sqlDate : " + sqlDate);
				} else {
					preparedStmt.setDate(12, null);
				}

				preparedStmt.setInt(13, 9000);

				// Application Stage
				if (applicationFlag != null) {
					preparedStmt.setString(14, applicationFlag);
				} else {
					preparedStmt.setString(14, null);
				}
				System.out.println("applicationFlag : " + applicationFlag);
				
				
				// Request Code
				if (mailReqCode != null) {
					
					preparedStmt.setString(15, mailReqCode);
				} else {
					preparedStmt.setString(15, null);
				}
				System.out.println("mailReqCode : " + mailReqCode);
				// EMAIL_RESPONSE_FLAG
				if (emailResponseFlag != null) {
					preparedStmt.setString(16, emailResponseFlag);
				} else {
					preparedStmt.setString(16, null);
				}
				System.out.println("emailResponseFlag : " + emailResponseFlag);
				System.out.println("Query : " + preparedStmt.toString());
				preparedStmt.executeUpdate();
				// STEP 6: Clean-up environment
				System.out.println("Updated Table...");

				if (mailContent != null) {
					if (subject != null
							&& (subject.equalsIgnoreCase("Approval Request")
									|| subject.equalsIgnoreCase("Approved") || subject
										.equalsIgnoreCase("Rejected")|| subject.equalsIgnoreCase("Handover to Accounts")||  subject.equalsIgnoreCase("Handover to DP"))) {
						System.out.println("Subject : " + subject);
						// STEP 4: Execute a query
						System.out.println("Creating statement.2..");
						Statement stmt1 = null;
						Statement stmt2 = null;
						Statement stmt3 = null;
						String tranRequestType = "";
						stmt1 = connection.createStatement();
						String	applStage = "";
						String mailRequestTxn = "SELECT H.TYPE_OF_REQUEST FROM HI_REQUEST_TXN H,  EMAIL_LOG e WHERE E.REQUEST_CODE = h.REQUEST_CODE AND H.REQUEST_CODE   = '"
								+ mailReqCode + "' And E.ACTIVE = 'Y' ";
						System.out
								.println("Query of Mail Request Transcation : "
										+ mailRequestTxn);
						ResultSet rs1 = stmt1.executeQuery(mailRequestTxn);

						// STEP 5: Extract data from result set
						while (rs1.next()) {
							tranRequestType = rs1.getString("TYPE_OF_REQUEST");
							// Display values
							System.out
									.println(" Transcation Request Type from the mail content is : "
											+ tranRequestType);
						}
						stmt2 = connection.createStatement();
						String updateQuery = "SELECT UW.APPLSTAGE FROM UM_WF_STAGE_EMAIL_MAPPING U,  UM_WORKFLOW_STAGES UW WHERE U.WS_ID = UW.WS_ID AND U.EMAIL_SUBJECT   = '"
								+ subject
								+ "' AND U.ORG_ID = 9000 AND U.TYPE_OF_REQUEST = '"
								+ tranRequestType + "' AND UW.ACTIVE = 'Y'";
						System.out
						.println("Query of selection of the application stage : "
								+ updateQuery);
						ResultSet rs2 = stmt2.executeQuery(updateQuery);

						// STEP 5: Extract data from result set
						while (rs2.next()) {
						applStage = rs2.getString("APPLSTAGE");
							// Display values
							System.out
									.println(" Application stage of the mail content is : "
											+ applStage);
						}
						// STEP 4: Execute a query
						System.out.println("Creating statement.3..Application Stage : " + applStage + "Email Request Code : " + mailReqCode);
						stmt3 = connection.createStatement();
						String updateTableSQL = "UPDATE EMAIL_LOG SET APPLSTAGE = '"
								+ applStage 
								+"',SUCCESS_FLAG = 'S' ,PROCESS_FLAG = 'Y',SUCCESS_FLAG_UPD_DT = TO_CHAR(SYSDATE,'dd/MON/yy') WHERE REQUEST_CODE = '"
								+ mailReqCode
								+"' AND EMAIL_ID = (SELECT MAX(EMAIL_ID) FROM EMAIL_LOG WHERE ACTIVE = 'Y') AND ACTIVE = 'Y'";
						ResultSet updateResult = stmt3.executeQuery(updateTableSQL);
						System.out.println("Query : " + updateTableSQL);
						System.out.println("Update Application Stage : "+ updateResult);


					}
				}
			}
			stmt.close();
			connection.close();

		} catch (Exception e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		}

		// }

	}

}