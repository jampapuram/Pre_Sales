package com.hexagon.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
			String mailCode = inbox.getMailCode();
			System.out.println("applicationFlag: " + applicationFlag);
			System.out.println("Request Code : " + mailReqCode);
			System.out.println("Mail Code : " + mailCode);
			Integer messageCount = inbox.getNewMessage();
			Properties prop = new Properties();
			prop.load(new FileInputStream("config.properties"));
			connection = DriverManager.getConnection(
					prop.getProperty("database"), prop.getProperty("dbuser"),
					prop.getProperty("dbpassword"));

			prop.put("mail.smtp.host", "smtp.gmail.com");
			prop.put("mail.smtp.socketFactory.port", "465");
			prop.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.port", "465");

			String host = prop.getProperty("host");
			final String sender = prop.getProperty("username");
			final String pwd = prop.getProperty("passwoed");

			Session session = Session.getInstance(prop,
					new javax.mail.Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(sender, pwd);// change
																			// accordingly
						}
					});

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = connection.createStatement();
			System.out.println("Message Count..." + messageCount);
			if (messageCount != 0) {
				String preQueryStatement = "Insert Into EMAIL_LOG (EMAIL_ID,EMAIL_TYPE,FROM_EMAIL_ADDRESS,CC_EMAIL_ADDRESS,TO_EMAIL_ADDRESS,SUBJECT,ATTACHMENT_FLAG,ATTACHMENT_NAME,PROCESS_FLAG,SUCCESS_FLAG,SUCCESS_FLAG_UPD_DT,ACTIVE,CR_DT,ORG_ID,APPLSTAGE,REQUEST_CODE,EMAIL_RESPONSE_FLAG)Values (mail_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement preparedStmt = connection
						.prepareStatement(preQueryStatement);
System.out.println("PreQueryStatement : 333: "+ preQueryStatement);
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
				System.out.println("Updated Table..in the Update Email File.");

				if (mailContent != null) {
					ArrayList al = new ArrayList();
					
				
					String subjectQuery = null;
					try {
						prop.load(new FileInputStream("config.properties"));

						connection = DriverManager.getConnection(
								prop.getProperty("database"), prop.getProperty("dbuser"),
								prop.getProperty("dbpassword"));
						Class.forName("oracle.jdbc.driver.OracleDriver");
						stmt = connection.createStatement();
						subjectQuery = "select U.EMAIL_SUBJECT from UM_WF_STAGE_EMAIL_MAPPING u ";
						ResultSet rsm = stmt.executeQuery(subjectQuery);

						while (rsm.next()) {
							al.add(rsm.getString("EMAIL_SUBJECT"));
						}
						System.out.println("Array list : " + al);
						if (al.contains(subject)) {
							System.out.println("Contains");
						} else {
							System.out.println("Does not contains ");
						}
					} catch (ClassNotFoundException e) {
						System.out.println("Where is your Oracle JDBC Driver?");
						e.printStackTrace();
						return;
					} catch (SQLException e) {
						System.out.println("Sql Exception ?");
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {

						System.out.println("Connection Failed! Check output console");
						e.printStackTrace();
						return;
					}
					
			
					
							     if (subject != null && mailReqCode != null  && al.contains(subject) && mailCode != null){
							    		connection = DriverManager.getConnection(
												prop.getProperty("database"), prop.getProperty("dbuser"),
												prop.getProperty("dbpassword"));
										Class.forName("oracle.jdbc.driver.OracleDriver");
										stmt = connection.createStatement();
										
								System.out.println("Subject : " + subject);
								// STEP 4: Execute a query
								System.out
										.println("Creating statement.2 in Update Email..");
								Statement stmt1 = connection.createStatement();
								Statement stmt2 = connection.createStatement();
								Statement stmt3 = connection.createStatement();
								Statement stmt4 = connection.createStatement();
								Statement stmt34 = connection.createStatement();
								String tranRequestType = null;
								String newWorkflowType = null;
								stmt1 = connection.createStatement();
								String applStage = null;
								
								// For the email code 
								// Searching for type of the request
								
								if (mailCode != null){
								String mailTxn = "SELECT EA.TYPE_OF_REQUEST FROM EMAIL_ATTACHMENT_INFO EA WHERE EA.EMAIL_ID = '" 
										+ mailCode 
										+ "' AND EA.ACTIVE = 'Y'";
								
								
								ResultSet rs34 = stmt34
										.executeQuery(mailTxn);

								// STEP 5: Extract data from result set
								while (rs34.next()) {
									tranRequestType = rs34
											.getString("TYPE_OF_REQUEST");
									
								}
								}
								
								if (mailReqCode != null){
								
								// End of the email code
								// Changed the type of the request from hi_request_txn to email attachment  and email log
								// Changed by Ram on 07/06/2013.
							String mailRequestTxn = "SELECT E.TYPE_OF_REQUEST FROM EMAIL_ATTACHMENT_INFO E, EMAIL_LOG EL WHERE  E.EMAIL_ID = EL.EMAIL_ID AND EL.REQUEST_CODE = '"
										+ mailReqCode + "' AND El.ACTIVE= 'Y'";
								System.out
										.println("Query of Mail Request Transcation : "
												+ mailRequestTxn);
								ResultSet rs1 = stmt1
										.executeQuery(mailRequestTxn);

								// STEP 5: Extract data from result set
								while (rs1.next()) {
									tranRequestType = rs1
											.getString("TYPE_OF_REQUEST");
									// Display values
									System.out
											.println(" Transcation Request Type from the mail content is : "
													+ tranRequestType);
								}
								}System.out.println("444444444444444" + tranRequestType);
								stmt2 = connection.createStatement();
								String updateQuery = "SELECT UW.APPLSTAGE,UW.STAGE_DESC1 FROM UM_WF_STAGE_EMAIL_MAPPING U,  UM_WORKFLOW_STAGES UW WHERE U.WS_ID = UW.WS_ID AND U.EMAIL_SUBJECT   = '"
										+ subject
										+ "' AND U.ORG_ID = 9000 AND U.TYPE_OF_REQUEST = '"
										+ tranRequestType
										+ "' AND UW.ACTIVE = 'Y'";
								System.out
										.println("Query of selection of the application stage : "
												+ updateQuery);
								ResultSet rs2 = stmt2.executeQuery(updateQuery);

								// STEP 5: Extract data from result set
								while (rs2.next()) {
									applStage = rs2.getString("APPLSTAGE");
									newWorkflowType = rs2
											.getString("STAGE_DESC1");
									// Display values
									System.out
											.println(" Application stage of the mail content is : "
													+ applStage);
								}
								
								if (mailCode != null){
									// STEP 4: Execute a query
									System.out
											.println("Creating statement.3.in Update Email. Stage : "
													+ applStage);
									
									stmt3 = connection.createStatement();
									
									String updateTableSQL = "UPDATE EMAIL_LOG SET APPLSTAGE = '"
											+ applStage
											+ "',SUCCESS_FLAG = 'S' ,PROCESS_FLAG = 'Y',SUCCESS_FLAG_UPD_DT = TO_CHAR(SYSDATE,'dd/MON/yy') WHERE EMAIL_ID = '"
											+ mailCode
											+ "' AND ACTIVE = 'Y'";
									System.out.println("Query 11111111: "
											+ updateTableSQL);
									ResultSet updateResult = stmt3
											.executeQuery(updateTableSQL);
									System.out.println("Query : " + updateTableSQL);
									System.out
											.println("Update EMAIL Stage : "
													+ updateResult);
								}
								
								
								
								if (mailReqCode != null){
								// STEP 4: Execute a query
								System.out
										.println("Creating statement.3.in Update Email. .Application Stage : "
												+ applStage
												+ "Email Request Code : "
												+ mailReqCode);
								System.out.println("rrrrrrrrrrrr : ");
								stmt3 = connection.createStatement();
								System.out.println("Update Table Sql : ");
								String updateTableSQL = "UPDATE EMAIL_LOG SET APPLSTAGE = '"
										+ applStage
										+ "',SUCCESS_FLAG = 'S' ,PROCESS_FLAG = 'Y',SUCCESS_FLAG_UPD_DT = TO_CHAR(SYSDATE,'dd/MON/yy') WHERE REQUEST_CODE = '"
										+ mailReqCode
										+ "' AND EMAIL_ID = (SELECT MAX(EMAIL_ID) FROM EMAIL_LOG WHERE ACTIVE = 'Y') AND ACTIVE = 'Y'";
								System.out.println("Query 11111111: "
										+ updateTableSQL);
								ResultSet updateResult = stmt3
										.executeQuery(updateTableSQL);
								System.out.println("Query : " + updateTableSQL);
								System.out
										.println("Update Application Stage : "
												+ updateResult);
								}
								String updatedApplstage = null;
								String updatedSuccessFlag = null;
								String from_Email_Address = null;
								String request_Code = null;
								String updateStage = null;
								String updateClientCode = null;
								String applicationID = null;
								String workflowType = null;
								try {
									connection = DriverManager.getConnection(
											prop.getProperty("database"),
											prop.getProperty("dbuser"),
											prop.getProperty("dbpassword"));
									// STEP 4: Execute a query
									System.out
											.println("Creating statement..in Update Email 1.");
									stmt = connection.createStatement();
									System.out
											.println("Creating statement..in UpdateEmail Send 2.");
									String stageQuery = "SELECT E.APPLSTAGE,E.SUCCESS_FLAG,E.FROM_EMAIL_ADDRESS,E.REQUEST_CODE,U.STAGE_DESC1,EA.CLIENT_CODE, ea.APPLICATION_ID, U.WORKFLOW_TYPE FROM EMAIL_LOG E,  UM_WORKFLOW_STAGES U,  EMAIL_ATTACHMENT_INFO ea WHERE E.APPLSTAGE   = U.APPLSTAGE AND EA.EMAIL_ID     = e.email_id AND E.REQUEST_CODE IS NOT NULL AND E.EMAIL_RESPONSE_FLAG = 'Y' AND E.SUCCESS_FLAG = 'S' And e.request_code = trim('"
											+ mailReqCode
											+ " ') AND E.ACTIVE = 'Y'";
									System.out
											.println("Creating statement..in UpdateEmail Send 3."
													+ stageQuery);
									ResultSet rsq1 = stmt
											.executeQuery(stageQuery);
									System.out
											.println("Creating statement..in UpdateEmail Send 4.");
									// STEP 5: Extract data from result set
									while (rsq1.next()) {
										updatedApplstage = rsq1
												.getString("APPLSTAGE");
										updatedSuccessFlag = rsq1
												.getString("SUCCESS_FLAG");
										from_Email_Address = rsq1
												.getString("FROM_EMAIL_ADDRESS");
										request_Code = rsq1
												.getString("request_Code");
										updateStage = rsq1
												.getString("STAGE_DESC1");
										updateClientCode = rsq1
												.getString("CLIENT_CODE");
										applicationID = rsq1
												.getString("APPLICATION_ID");
										workflowType = rsq1
												.getString("WORKFLOW_TYPE");
									}

									// STEP 6: Clean-up environment
									System.out.println("Fetching Records..."
											+ updatedSuccessFlag);
									System.out
											.println("Fetching Records.updatedApplstage."
													+ applStage);
									// message[i].setFlag(Flags.Flag.DELETED,
									// true);

									String msg_s1 = null;
									String msg_g1 = null;
									String msg_t1 = null;
									String msg_t2 = null;
									String msg_t3 = null;
									String msg_t4 = null;
									String msg_t5 = null;
									String msg_t6 = null;
									String msg_e1 = null;
									String msg_e2 = null;
									String msg_e3 = null;
									String msg_e4 = null;

									if (updatedSuccessFlag != null
											&& updatedSuccessFlag.equals("S")) {
										if (applStage != null
												&& applStage
														.equals("DAPPR_REQ_MAIL")) {

											System.out
													.println("Creating statement...");
											stmt2 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in Dinit mail stage..");
											String successQuery = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 6 ";
											System.out
													.println("Creating statement.2.DAPPR_REQ_MAIL mail stage.");
											ResultSet rsReq = stmt2
													.executeQuery(successQuery);
											System.out
													.println("Creating statement..3 DAPPR_REQ_MAIL mail stage."
															+ successQuery);
											while (rsReq.next()) {
												msg_s1 = rsReq
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq
														.getString("MSG_GREETING1");
												msg_t1 = rsReq
														.getString("MSG_TEXT1");
												msg_t2 = rsReq
														.getString("MSG_TEXT2");
												msg_t3 = rsReq
														.getString("MSG_TEXT3");
												msg_t4 = rsReq
														.getString("MSG_TEXT4");
												msg_t5 = rsReq
														.getString("MSG_TEXT5");
												msg_t6 = rsReq
														.getString("MSG_text6");
												msg_e1 = rsReq
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1
																	+ "subject"
																	+ subject);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}

										} else if (applStage != null
												&& applStage
														.equals("DDECISION_APP_MAIL")) {
											System.out
													.println("Creating statement.DDECISION_APP_MAIL..");
											stmt3 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in DDECISION_APP_MAIL stage..");
											String successQuery1 = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 10 ";
											System.out
													.println("Creating statement.2.DDECISION_APP_MAIL stage.");
											ResultSet rsReq1 = stmt3
													.executeQuery(successQuery1);
											System.out
													.println("Creating statement..3 DDECISION_APP_MAIL stage."
															+ successQuery1);
											while (rsReq1.next()) {
												msg_s1 = rsReq1
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq1
														.getString("MSG_GREETING1");
												msg_t1 = rsReq1
														.getString("MSG_TEXT1");
												msg_t2 = rsReq1
														.getString("MSG_TEXT2");
												msg_t3 = rsReq1
														.getString("MSG_TEXT3");
												msg_t4 = rsReq1
														.getString("MSG_TEXT4");
												msg_t5 = rsReq1
														.getString("MSG_TEXT5");
												msg_t6 = rsReq1
														.getString("MSG_text6");
												msg_e1 = rsReq1
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq1
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq1
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq1
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}
										} else if (applStage != null
												&& applStage
												.equals("DCONF_BANK_MAIL")){


											System.out
													.println("Creating statement.DCONF_BANK_MAIL..");
											stmt4 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in DCONF_BANK_MAIL stage..");
											String successQuery4 = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 37 ";
											System.out
													.println("Creating statement.2.DCONF_BANK_MAIL stage.");
											ResultSet rsReq4 = stmt4
													.executeQuery(successQuery4);
											System.out
													.println("Creating statement..3 DCONF_BANK_MAIL stage."
															+ successQuery4);
											while (rsReq4.next()) {
												msg_s1 = rsReq4
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq4
														.getString("MSG_GREETING1");
												msg_t1 = rsReq4
														.getString("MSG_TEXT1");
												msg_t2 = rsReq4
														.getString("MSG_TEXT2");
												msg_t3 = rsReq4
														.getString("MSG_TEXT3");
												msg_t4 = rsReq4
														.getString("MSG_TEXT4");
												msg_t5 = rsReq4
														.getString("MSG_TEXT5");
												msg_t6 = rsReq4
														.getString("MSG_text6");
												msg_e1 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}

										
										}else if (applStage != null
												&& applStage
														.equals("DHOVER_ACCNT_MAIL")) {

											System.out
													.println("Creating statement.DHOVER_ACCNT_MAIL..");
											stmt4 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in DHOVER_ACCNT_MAIL stage..");
											String successQuery4 = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 31 ";
											System.out
													.println("Creating statement.2.DHOVER_ACCNT_MAIL stage.");
											ResultSet rsReq4 = stmt4
													.executeQuery(successQuery4);
											System.out
													.println("Creating statement..3 DHOVER_ACCNT_MAIL stage."
															+ successQuery4);
											while (rsReq4.next()) {
												msg_s1 = rsReq4
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq4
														.getString("MSG_GREETING1");
												msg_t1 = rsReq4
														.getString("MSG_TEXT1");
												msg_t2 = rsReq4
														.getString("MSG_TEXT2");
												msg_t3 = rsReq4
														.getString("MSG_TEXT3");
												msg_t4 = rsReq4
														.getString("MSG_TEXT4");
												msg_t5 = rsReq4
														.getString("MSG_TEXT5");
												msg_t6 = rsReq4
														.getString("MSG_text6");
												msg_e1 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}

										} else if (applStage != null
												&& applStage
														.equals("PAPPR_REQ_MAIL")) {

											System.out
													.println("Creating statement...");
											stmt2 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in PAPPR_REQ_MAIL stage..");
											String successQuery = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 13 ";
											System.out
													.println("Creating statement.2.PAPPR_REQ_MAIL mail stage.");
											ResultSet rsReq = stmt2
													.executeQuery(successQuery);
											System.out
													.println("Creating statement..3 PAPPR_REQ_MAIL mail stage."
															+ successQuery);
											while (rsReq.next()) {
												msg_s1 = rsReq
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq
														.getString("MSG_GREETING1");
												msg_t1 = rsReq
														.getString("MSG_TEXT1");
												msg_t2 = rsReq
														.getString("MSG_TEXT2");
												msg_t3 = rsReq
														.getString("MSG_TEXT3");
												msg_t4 = rsReq
														.getString("MSG_TEXT4");
												msg_t5 = rsReq
														.getString("MSG_TEXT5");
												msg_t6 = rsReq
														.getString("MSG_text6");
												msg_e1 = rsReq
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1
																	+ "subject"
																	+ subject);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}

										} else if (applStage != null
												&& applStage
														.equals("PDECISION_APP_MAIL")) {

											System.out
													.println("Creating statement.PDECISION_APP_MAIL..");
											stmt3 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in PDECISION_APP_MAIL stage..");
											String successQuery1 = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 10 ";
											System.out
													.println("Creating statement.2.PDECISION_APP_MAIL stage.");
											ResultSet rsReq1 = stmt3
													.executeQuery(successQuery1);
											System.out
													.println("Creating statement..3 PDECISION_APP_MAIL stage."
															+ successQuery1);
											while (rsReq1.next()) {
												msg_s1 = rsReq1
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq1
														.getString("MSG_GREETING1");
												msg_t1 = rsReq1
														.getString("MSG_TEXT1");
												msg_t2 = rsReq1
														.getString("MSG_TEXT2");
												msg_t3 = rsReq1
														.getString("MSG_TEXT3");
												msg_t4 = rsReq1
														.getString("MSG_TEXT4");
												msg_t5 = rsReq1
														.getString("MSG_TEXT5");
												msg_t6 = rsReq1
														.getString("MSG_text6");
												msg_e1 = rsReq1
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq1
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq1
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq1
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}

										} else if (applStage != null
												&& applStage
														.equals("PHOVER_DP_MAIL")) {

											System.out
													.println("Creating statement.PHOVER_DP_MAIL..");
											stmt4 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in PHOVER_DP_MAIL stage..");
											String successQuery4 = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 31 ";
											System.out
													.println("Creating statement.2.PHOVER_DP_MAIL stage.");
											ResultSet rsReq4 = stmt4
													.executeQuery(successQuery4);
											System.out
													.println("Creating statement..3 PHOVER_DP_MAIL stage."
															+ successQuery4);
											while (rsReq4.next()) {
												msg_s1 = rsReq4
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq4
														.getString("MSG_GREETING1");
												msg_t1 = rsReq4
														.getString("MSG_TEXT1");
												msg_t2 = rsReq4
														.getString("MSG_TEXT2");
												msg_t3 = rsReq4
														.getString("MSG_TEXT3");
												msg_t4 = rsReq4
														.getString("MSG_TEXT4");
												msg_t5 = rsReq4
														.getString("MSG_TEXT5");
												msg_t6 = rsReq4
														.getString("MSG_text6");
												msg_e1 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}

											// PHOVER_DP_FILE
										} else if (applStage != null
												&& applStage
														.equals("PHOVER_DP_FILE")) {

											System.out
													.println("Creating statement.PHOVER_DP_FILE..");
											stmt4 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in PHOVER_DP_FILE stage..");
											String successQuery4 = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 31 ";
											System.out
													.println("Creating statement.2.PHOVER_DP_FILE stage.");
											ResultSet rsReq4 = stmt4
													.executeQuery(successQuery4);
											System.out
													.println("Creating statement..3 PHOVER_DP_FILE stage."
															+ successQuery4);
											while (rsReq4.next()) {
												msg_s1 = rsReq4
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq4
														.getString("MSG_GREETING1");
												msg_t1 = rsReq4
														.getString("MSG_TEXT1");
												msg_t2 = rsReq4
														.getString("MSG_TEXT2");
												msg_t3 = rsReq4
														.getString("MSG_TEXT3");
												msg_t4 = rsReq4
														.getString("MSG_TEXT4");
												msg_t5 = rsReq4
														.getString("MSG_TEXT5");
												msg_t6 = rsReq4
														.getString("MSG_text6");
												msg_e1 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}

										}else if (applStage != null
												&& applStage
														.equals("PCONF_DP_MAIL")){
										System.out
													.println("Creating statement.PCONF_DP_MAIL..");
											stmt4 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in PCONF_DP_MAIL stage..");
											String successQuery4 = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 52 ";
											System.out
													.println("Creating statement.2.PCONF_DP_MAIL stage.");
											ResultSet rsReq4 = stmt4
													.executeQuery(successQuery4);
											System.out
													.println("Creating statement..3 PCONF_DP_MAIL stage."
															+ successQuery4);
											while (rsReq4.next()) {
												msg_s1 = rsReq4
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq4
														.getString("MSG_GREETING1");
												msg_t1 = rsReq4
														.getString("MSG_TEXT1");
												msg_t2 = rsReq4
														.getString("MSG_TEXT2");
												msg_t3 = rsReq4
														.getString("MSG_TEXT3");
												msg_t4 = rsReq4
														.getString("MSG_TEXT4");
												msg_t5 = rsReq4
														.getString("MSG_TEXT5");
												msg_t6 = rsReq4
														.getString("MSG_text6");
												msg_e1 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}
										}
										
										else if (applStage != null
												&& applStage
														.equals("RHOVER_DP_MAIL")){
										System.out
													.println("Creating statement.RHOVER_DP_MAIL..");
											stmt4 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in RHOVER_DP_MAIL stage..");
											String successQuery4 = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 47 ";
											System.out
													.println("Creating statement.2.RHOVER_DP_MAIL stage.");
											ResultSet rsReq4 = stmt4
													.executeQuery(successQuery4);
											System.out
													.println("Creating statement..3 RHOVER_DP_MAIL stage."
															+ successQuery4);
											while (rsReq4.next()) {
												msg_s1 = rsReq4
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq4
														.getString("MSG_GREETING1");
												msg_t1 = rsReq4
														.getString("MSG_TEXT1");
												msg_t2 = rsReq4
														.getString("MSG_TEXT2");
												msg_t3 = rsReq4
														.getString("MSG_TEXT3");
												msg_t4 = rsReq4
														.getString("MSG_TEXT4");
												msg_t5 = rsReq4
														.getString("MSG_TEXT5");
												msg_t6 = rsReq4
														.getString("MSG_text6");
												msg_e1 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}
										}
										
										else if (applStage != null
												&& applStage
														.equals("RHOVER_DP_FILE")){
										System.out
													.println("Creating statement.RHOVER_DP_FILE..");
											stmt4 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in RHOVER_DP_FILE stage..");
											String successQuery4 = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 51 ";
											System.out
													.println("Creating statement.2.RHOVER_DP_FILE stage.");
											ResultSet rsReq4 = stmt4
													.executeQuery(successQuery4);
											System.out
													.println("Creating statement..3 RHOVER_DP_FILE stage."
															+ successQuery4);
											while (rsReq4.next()) {
												msg_s1 = rsReq4
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq4
														.getString("MSG_GREETING1");
												msg_t1 = rsReq4
														.getString("MSG_TEXT1");
												msg_t2 = rsReq4
														.getString("MSG_TEXT2");
												msg_t3 = rsReq4
														.getString("MSG_TEXT3");
												msg_t4 = rsReq4
														.getString("MSG_TEXT4");
												msg_t5 = rsReq4
														.getString("MSG_TEXT5");
												msg_t6 = rsReq4
														.getString("MSG_text6");
												msg_e1 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}
										}
										else if (applStage != null
												&& applStage
														.equals("RCONF_DP_MAIL")){
										System.out
													.println("Creating statement.RCONF_DP_MAIL..");
											stmt4 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in RCONF_DP_MAIL stage..");
											String successQuery4 = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 55 ";
											System.out
													.println("Creating statement.2.RCONF_DP_MAIL stage.");
											ResultSet rsReq4 = stmt4
													.executeQuery(successQuery4);
											System.out
													.println("Creating statement..3 RCONF_DP_MAIL stage."
															+ successQuery4);
											while (rsReq4.next()) {
												msg_s1 = rsReq4
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq4
														.getString("MSG_GREETING1");
												msg_t1 = rsReq4
														.getString("MSG_TEXT1");
												msg_t2 = rsReq4
														.getString("MSG_TEXT2");
												msg_t3 = rsReq4
														.getString("MSG_TEXT3");
												msg_t4 = rsReq4
														.getString("MSG_TEXT4");
												msg_t5 = rsReq4
														.getString("MSG_TEXT5");
												msg_t6 = rsReq4
														.getString("MSG_text6");
												msg_e1 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq4
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}
										}else if (applStage != null
												&& applStage
														.equals("RAPPR_REQ_MAIL")) {

											System.out
													.println("Creating statement.RAPPR_REQ_MAIL..");
											stmt3 = connection
													.createStatement();
											System.out
													.println("Creating statement.1 in RAPPR_REQ_MAIL stage..");
											String successQuery1 = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_text6, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 60 ";
											System.out
													.println("Creating statement.2.RAPPR_REQ_MAIL stage.");
											ResultSet rsReq1 = stmt3
													.executeQuery(successQuery1);
											System.out
													.println("Creating statement..3 RAPPR_REQ_MAIL stage."
															+ successQuery1);
											while (rsReq1.next()) {
												msg_s1 = rsReq1
														.getString("MSG_SUBJECT1");
												msg_g1 = rsReq1
														.getString("MSG_GREETING1");
												msg_t1 = rsReq1
														.getString("MSG_TEXT1");
												msg_t2 = rsReq1
														.getString("MSG_TEXT2");
												msg_t3 = rsReq1
														.getString("MSG_TEXT3");
												msg_t4 = rsReq1
														.getString("MSG_TEXT4");
												msg_t5 = rsReq1
														.getString("MSG_TEXT5");
												msg_t6 = rsReq1
														.getString("MSG_text6");
												msg_e1 = rsReq1
														.getString("MSG_ENDOFMAIL_MSG1");
												msg_e2 = rsReq1
														.getString("MSG_ENDOFMAIL_MSG2");
												msg_e3 = rsReq1
														.getString("MSG_ENDOFMAIL_MSG3");
												msg_e4 = rsReq1
														.getString("MSG_ENDOFMAIL_MSG4");

												System.out.println(" msg_s1 : "
														+ msg_s1);
												System.out.println(" msg_g1 : "
														+ msg_g1);
												System.out.println(" msg_t1 : "
														+ msg_t1);
												System.out.println(" msg_t2 : "
														+ msg_t2);
												System.out.println(" msg_t3 : "
														+ msg_t3);
												System.out.println(" msg_t4 : "
														+ msg_t4);
												System.out.println(" msg_t5 : "
														+ msg_t5);
												System.out.println(" msg_t6 : "
														+ msg_t6);
												System.out.println(" msg_e1 : "
														+ msg_e1);
												System.out.println(" msg_e2 : "
														+ msg_e2);
												System.out.println(" msg_e3 : "
														+ msg_e3);
												System.out.println(" msg_e4 : "
														+ msg_e4);
											}
											try {
												if (from_Email_Address != null) {

													System.out
															.println(" FROM_EMAIL_ADDRESS 5555 : "
																	+ from_Email_Address);
													System.out
															.println(" To_Email_Address 5555 : "
																	+ sender);
													MimeMessage message = new MimeMessage(
															session);
													message.setFrom(new InternetAddress(
															sender));
													message.addRecipient(
															Message.RecipientType.TO,
															new InternetAddress(
																	from_Email_Address));

													message.setSubject(msg_s1
															+ " " + subject);
													System.out
															.println(" msg_s1 : "
																	+ msg_s1);

													message.setContent(" "
															+ msg_g1
															+ "<br><br>"
															+ msg_t1 + " "
															+ newWorkflowType
															+ " " + msg_t2
															+ " "
															+ request_Code
															+ " " + msg_t3
															+ " "
															+ workflowType
															+ " " + msg_t4
															+ " "
															+ updateClientCode
															+ " " + msg_t5
															+ " "
															+ applicationID
															+ " " + msg_t6
															+ "<br><br>"
															+ msg_e1
															+ "<br><br>"
															+ msg_e2
															+ "<br><br>"
															+ msg_e3
															+ "<br><br> <i>"
															+ msg_e4 + "</i> ",
															"text/html");
													System.out
															.println(" messageBodyPart : ");

													Transport.send(message);
													System.out
															.println("Alert message sent");
												}
											} catch (MessagingException e) {
												throw new RuntimeException(e);
											}

										}
									} else if (updatedSuccessFlag != null
											&& updatedSuccessFlag.equals("F")) {

									}
								} catch (SQLException e) {

									System.out
											.println("Connection Failed! Check output console");
									e.printStackTrace();
									return;

								}

								if (connection != null) {
									System.out
											.println("You made it, take control your database now!");
								} else {
									System.out
											.println("Failed to make connection!");
								}

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

	}

}