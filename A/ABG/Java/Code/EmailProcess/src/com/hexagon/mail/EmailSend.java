package com.hexagon.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSend extends Inbox {
	public static void main(String args[]) throws Exception {

		Inbox inbox = new Inbox();
		UpdateEmail updateEmailInfo = new UpdateEmail();
		ExcelAttachmentReader excelAttachmentReader = new ExcelAttachmentReader();
		CallProcedure callprocedure = new CallProcedure();
		Mapping mapping = new Mapping();

		updateEmailInfo.emailInfo(inbox);
		String xlsPath = "C:\\Users\\Administrator\\EmailProcess\\"
				+ attachmentName;
		excelAttachmentReader.displayFromExcel(xlsPath);
		callprocedure.callOracleStoredProc();
		mapping.map();
		try {

			File file = new File(xlsPath);

			if (file.delete()) {
				System.out.println(attachmentName + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		try { // Get system properties
			Properties props = new Properties();

			try {
				props.load(new FileInputStream("config.properties"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			String host = props.getProperty("host");
			final String sender = props.getProperty("username");
			final String pwd = props.getProperty("passwoed");

			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(sender, pwd);// change
																			// accordingly
						}
					});

			// Get a Store object that implements the specified protocol.
			Store store = session.getStore("imaps");

			// Connect to the current host using the specified username and
			// password.
			store.connect(host, sender, pwd);

			// Create a Folder object corresponding to the given name.
			Folder folder = store.getFolder("inbox");

			// Open the Folder.
			folder.open(Folder.READ_WRITE);

			System.out
					.println("-------- Oracle JDBC Connection Testing ------");

			try {

				Class.forName("oracle.jdbc.driver.OracleDriver");

			} catch (ClassNotFoundException e) {

				System.out.println("Where is your Oracle JDBC Driver?");
				e.printStackTrace();
				return;

			}

			System.out.println("Oracle JDBC Driver Registered!");

			Connection connection = null;
			Statement stmt = null;
			Statement stmt2 = null;
			String from_Email_Address = null;
			String sucess_flag = null;
			int email_id = 0;
			String email_flag = null;
			String applStage = null;
			String workflow_type = null;
			String client_code = null;
			String application_code = null;
			String stageDesc = null;
			int newRequestCode = 0;
			
			try {
				connection = DriverManager.getConnection(
						props.getProperty("database"),
						props.getProperty("dbuser"),
						props.getProperty("dbpassword"));
				// STEP 4: Execute a query
				System.out.println("Creating statement..in Email Send 1.");
				stmt = connection.createStatement();
				System.out.println("Creating statement..in Email Send 2.");
				String preQueryStatement = "SELECT E.FROM_EMAIL_ADDRESS,E.SUCCESS_FLAG,E.EMAIL_ID, E.APPLSTAGE , U.WORKFLOW_TYPE, EA.CLIENT_CODE,EA.APPLICATION_ID, U.SRC_DESC1, E.REQUEST_CODE FROM EMAIL_LOG E, UM_WORKFLOW_STAGES U,EMAIL_ATTACHMENT_INFO ea WHERE E.APPLSTAGE = U.APPLSTAGE and e.email_id = ea.email_id and  E.EMAIL_ID =  (SELECT MAX(H.EMAIL_ID) FROM email_log H WHERE H.ACTIVE = 'Y' ) AND E.ACTIVE = 'Y'";
				System.out.println("Creating statement..in Email Send 3.");
				ResultSet rs1 = stmt.executeQuery(preQueryStatement);
				System.out.println("Creating statement..in Email Send 4.");
				// STEP 5: Extract data from result set
				while (rs1.next()) {
					from_Email_Address = rs1.getString("FROM_EMAIL_ADDRESS");
					sucess_flag = rs1.getString("SUCCESS_FLAG");
					email_id = rs1.getInt("EMAIL_ID");
					applStage = rs1.getString("applStage");
					workflow_type = rs1.getString("WORKFLOW_TYPE");
					client_code = rs1.getString("CLIENT_CODE");
					application_code = rs1.getString("APPLICATION_ID");
					stageDesc = rs1.getString("SRC_DESC1");
					newRequestCode = rs1.getInt("REQUEST_CODE");
					// Display values
					System.out.println(" FROM_EMAIL_ADDRESS : "
							+ from_Email_Address);
					System.out.println(" SUCCESS_FLAG : " + sucess_flag);
					System.out.println(" email_id : " + email_id);
					System.out.println(" email_flag : " + email_flag);
					System.out.println(" applStage : " + applStage);
					System.out.println(" workflow_type : " + workflow_type);
					System.out.println(" Client Code : " + client_code);
					System.out.println(" APPLICATION_ID" + application_code);
					System.out.println("SRC_DESC1 " + stageDesc);
					System.out.println("REQUEST_CODE " + newRequestCode);
				}

				// STEP 6: Clean-up environment
				System.out.println("Fetching Records...");
				// message[i].setFlag(Flags.Flag.DELETED, true);

				String msg_s1 = null;
				String msg_g1 = null;
				String msg_t1 = null;
				String msg_t2 = null;
				String msg_t3 = null;
				String msg_t4 = null;
				String msg_t5 = null;
				String msg_e1 = null;
				String msg_e2 = null;
				String msg_e3 = null;
				String msg_e4 = null;

				if (sucess_flag != null && sucess_flag.equals("S")) {
					System.out.println(" SUCCESS_FLAG inside if condition : "
							+ sucess_flag);
					if (applStage != null && applStage.equals("DINIT_MAIL")) {
						System.out.println("Creating statement...");
						stmt2 = connection.createStatement();
						System.out
								.println("Creating statement.1 in Dinit mail stage..");
						String successQuery = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 1 ";
						System.out
								.println("Creating statement.2.Dinit mail stage.");
						ResultSet rs2 = stmt2.executeQuery(successQuery);
						System.out
								.println("Creating statement..3 Dinit mail stage."
										+ successQuery);
						while (rs2.next()) {
							msg_s1 = rs2.getString("MSG_SUBJECT1");
							msg_g1 = rs2.getString("MSG_GREETING1");
							msg_t1 = rs2.getString("MSG_TEXT1");
							msg_t2 = rs2.getString("MSG_TEXT2");
							msg_t3 = rs2.getString("MSG_TEXT3");
							msg_t4 = rs2.getString("MSG_TEXT4");
							msg_t5 = rs2.getString("MSG_TEXT5");
							msg_e1 = rs2.getString("MSG_ENDOFMAIL_MSG1");
							msg_e2 = rs2.getString("MSG_ENDOFMAIL_MSG2");
							msg_e3 = rs2.getString("MSG_ENDOFMAIL_MSG3");
							msg_e4 = rs2.getString("MSG_ENDOFMAIL_MSG4");

							System.out.println(" msg_s1 : " + msg_s1);
							System.out.println(" msg_g1 : " + msg_g1);
							System.out.println(" msg_t1 : " + msg_t1);
							System.out.println(" msg_t2 : " + msg_t2);
							System.out.println(" msg_t3 : " + msg_t3);
							System.out.println(" msg_t4 : " + msg_t4);
							System.out.println(" msg_t5 : " + msg_t5);
							System.out.println(" msg_e1 : " + msg_e1);
							System.out.println(" msg_e2 : " + msg_e2);
							System.out.println(" msg_e3 : " + msg_e3);
							System.out.println(" msg_e4 : " + msg_e4);
						}
						try {
							if (from_Email_Address != null) {

								System.out
										.println(" FROM_EMAIL_ADDRESS 5555 : "
												+ from_Email_Address);
								System.out.println(" To_Email_Address 5555 : "
										+ sender);
								MimeMessage message = new MimeMessage(session);
								message.setFrom(new InternetAddress(sender));
								message.addRecipient(Message.RecipientType.TO,
										new InternetAddress(from_Email_Address));

								message.setSubject(msg_s1);
								System.out.println(" msg_s1 : " + msg_s1);

								message.setContent(" " + msg_g1 + "<br><br>"
										+ msg_t1 + newRequestCode + " "+msg_t2+ " "
										+ workflow_type+ " " + msg_t3 + " "+ client_code+ " "
										+ msg_t4 + " "+ application_code + " "+ msg_t5
										+ "<br><br>" + msg_e1 + "<br><br>"
										+ msg_e2 + "<br><br>" + msg_e3
										+ "<br><br><i>" + msg_e4 + "</i>",
										"text/html");
								System.out.println(" messageBodyPart : ");

								Transport.send(message);
								System.out.println("Alert message sent");

							}
						} catch (MessagingException e) {
							throw new RuntimeException(e);
						}

					} else if (applStage != null && applStage.equals("PINIT_MAIL")){

						System.out.println("Creating statement...");
						stmt2 = connection.createStatement();
						System.out
								.println("Creating statement.1 in PINIT_MAIL stage..");
						String successQuery = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 26 ";
						System.out
								.println("Creating statement.2.PINIT_MAIL stage.");
						ResultSet rs2 = stmt2.executeQuery(successQuery);
						System.out
								.println("Creating statement..3 PINIT_MAIL stage."
										+ successQuery);
						while (rs2.next()) {
							msg_s1 = rs2.getString("MSG_SUBJECT1");
							msg_g1 = rs2.getString("MSG_GREETING1");
							msg_t1 = rs2.getString("MSG_TEXT1");
							msg_t2 = rs2.getString("MSG_TEXT2");
							msg_t3 = rs2.getString("MSG_TEXT3");
							msg_t4 = rs2.getString("MSG_TEXT4");
							msg_t5 = rs2.getString("MSG_TEXT5");
							msg_e1 = rs2.getString("MSG_ENDOFMAIL_MSG1");
							msg_e2 = rs2.getString("MSG_ENDOFMAIL_MSG2");
							msg_e3 = rs2.getString("MSG_ENDOFMAIL_MSG3");
							msg_e4 = rs2.getString("MSG_ENDOFMAIL_MSG4");

							System.out.println(" msg_s1 : " + msg_s1);
							System.out.println(" msg_g1 : " + msg_g1);
							System.out.println(" msg_t1 : " + msg_t1);
							System.out.println(" msg_t2 : " + msg_t2);
							System.out.println(" msg_t3 : " + msg_t3);
							System.out.println(" msg_t4 : " + msg_t4);
							System.out.println(" msg_t5 : " + msg_t5);
							System.out.println(" msg_e1 : " + msg_e1);
							System.out.println(" msg_e2 : " + msg_e2);
							System.out.println(" msg_e3 : " + msg_e3);
							System.out.println(" msg_e4 : " + msg_e4);
						}
						try {
							if (from_Email_Address != null) {

								System.out
										.println(" FROM_EMAIL_ADDRESS 5555 : "
												+ from_Email_Address);
								System.out.println(" To_Email_Address 5555 : "
										+ sender);
								MimeMessage message = new MimeMessage(session);
								message.setFrom(new InternetAddress(sender));
								message.addRecipient(Message.RecipientType.TO,
										new InternetAddress(from_Email_Address));

								message.setSubject(msg_s1);
								System.out.println(" msg_s1 : " + msg_s1);

								message.setContent(" " + msg_g1 + "<br><br>"
										+ msg_t1 + newRequestCode + " "+msg_t2+ " "
										+ workflow_type+ " " + msg_t3 + " "+ client_code+ " "
										+ msg_t4 + " "+ application_code + " "+ msg_t5
										+ "<br><br>" + msg_e1 + "<br><br>"
										+ msg_e2 + "<br><br>" + msg_e3
										+ "<br><br><i>" + msg_e4 + "</i>",
										"text/html");
								System.out.println(" messageBodyPart : ");

								Transport.send(message);
								System.out.println("Alert message sent");

							}
						} catch (MessagingException e) {
							throw new RuntimeException(e);
						}

					
					}else {


						System.out.println("Creating statement...");
						stmt2 = connection.createStatement();
						System.out
								.println("Creating statement.1 in RINIT_MAIL stage..");
						String successQuery = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 58 ";
						System.out
								.println("Creating statement.2.RINIT_MAIL stage.");
						ResultSet rs2 = stmt2.executeQuery(successQuery);
						System.out
								.println("Creating statement..3 RINIT_MAIL stage."
										+ successQuery);
						while (rs2.next()) {
							msg_s1 = rs2.getString("MSG_SUBJECT1");
							msg_g1 = rs2.getString("MSG_GREETING1");
							msg_t1 = rs2.getString("MSG_TEXT1");
							msg_t2 = rs2.getString("MSG_TEXT2");
							msg_t3 = rs2.getString("MSG_TEXT3");
							msg_t4 = rs2.getString("MSG_TEXT4");
							msg_t5 = rs2.getString("MSG_TEXT5");
							msg_e1 = rs2.getString("MSG_ENDOFMAIL_MSG1");
							msg_e2 = rs2.getString("MSG_ENDOFMAIL_MSG2");
							msg_e3 = rs2.getString("MSG_ENDOFMAIL_MSG3");
							msg_e4 = rs2.getString("MSG_ENDOFMAIL_MSG4");

							System.out.println(" msg_s1 : " + msg_s1);
							System.out.println(" msg_g1 : " + msg_g1);
							System.out.println(" msg_t1 : " + msg_t1);
							System.out.println(" msg_t2 : " + msg_t2);
							System.out.println(" msg_t3 : " + msg_t3);
							System.out.println(" msg_t4 : " + msg_t4);
							System.out.println(" msg_t5 : " + msg_t5);
							System.out.println(" msg_e1 : " + msg_e1);
							System.out.println(" msg_e2 : " + msg_e2);
							System.out.println(" msg_e3 : " + msg_e3);
							System.out.println(" msg_e4 : " + msg_e4);
						}
						try {
							if (from_Email_Address != null) {

								System.out
										.println(" FROM_EMAIL_ADDRESS 5555 : "
												+ from_Email_Address);
								System.out.println(" To_Email_Address 5555 : "
										+ sender);
								MimeMessage message = new MimeMessage(session);
								message.setFrom(new InternetAddress(sender));
								message.addRecipient(Message.RecipientType.TO,
										new InternetAddress(from_Email_Address));

								message.setSubject(msg_s1);
								System.out.println(" msg_s1 : " + msg_s1);

								message.setContent(" " + msg_g1 + "<br><br>"
										+ msg_t1 + newRequestCode + " "+msg_t2+ " "
										+ workflow_type+ " " + msg_t3 + " "+ client_code+ " "
										+ msg_t4 + " "+ application_code + " "+ msg_t5
										+ "<br><br>" + msg_e1 + "<br><br>"
										+ msg_e2 + "<br><br>" + msg_e3
										+ "<br><br><i>" + msg_e4 + "</i>",
										"text/html");
								System.out.println(" messageBodyPart : ");

								Transport.send(message);
								System.out.println("Alert message sent");

							}
						} catch (MessagingException e) {
							throw new RuntimeException(e);
						}

					
					
					}
				}else if (sucess_flag != null && sucess_flag.equals("D")){
					System.out.println(" Duplicate flage inside if condition : "
							+ sucess_flag);
					if (applStage != null && applStage.equals("DINIT_MAIL")) {
						System.out.println("Creating statement...");
						stmt2 = connection.createStatement();
						System.out
								.println("Creating statement.1 in Dinit mail stage..");
						String successQuery = "SELECT U.MSG_SUBJECT1,u.MSG_GREETING1,u.MSG_TEXT1,U.MSG_TEXT2,U.MSG_TEXT3,U.MSG_TEXT4, U.MSG_TEXT5, U.MSG_ENDOFMAIL_MSG1, U.MSG_ENDOFMAIL_MSG2,  U.MSG_ENDOFMAIL_MSG3,   U.MSG_ENDOFMAIL_MSG4 FROM UM_EMAIL_RESPONSE U WHERE u.RESPONSE_ID = 1 ";
						System.out
								.println("Creating statement.2.Dinit mail stage.");
						ResultSet rs2 = stmt2.executeQuery(successQuery);
						System.out
								.println("Creating statement..3 Dinit mail stage."
										+ successQuery);
						while (rs2.next()) {
							msg_s1 = rs2.getString("MSG_SUBJECT1");
							msg_g1 = rs2.getString("MSG_GREETING1");
							msg_t1 = rs2.getString("MSG_TEXT1");
							msg_t2 = rs2.getString("MSG_TEXT2");
							msg_t3 = rs2.getString("MSG_TEXT3");
							msg_t4 = rs2.getString("MSG_TEXT4");
							msg_t5 = rs2.getString("MSG_TEXT5");
							msg_e1 = rs2.getString("MSG_ENDOFMAIL_MSG1");
							msg_e2 = rs2.getString("MSG_ENDOFMAIL_MSG2");
							msg_e3 = rs2.getString("MSG_ENDOFMAIL_MSG3");
							msg_e4 = rs2.getString("MSG_ENDOFMAIL_MSG4");

							System.out.println(" msg_s1 : " + msg_s1);
							System.out.println(" msg_g1 : " + msg_g1);
							System.out.println(" msg_t1 : " + msg_t1);
							System.out.println(" msg_t2 : " + msg_t2);
							System.out.println(" msg_t3 : " + msg_t3);
							System.out.println(" msg_t4 : " + msg_t4);
							System.out.println(" msg_t5 : " + msg_t5);
							System.out.println(" msg_e1 : " + msg_e1);
							System.out.println(" msg_e2 : " + msg_e2);
							System.out.println(" msg_e3 : " + msg_e3);
							System.out.println(" msg_e4 : " + msg_e4);
						}
						try {
							if (from_Email_Address != null) {

								System.out
										.println(" FROM_EMAIL_ADDRESS 5555 : "
												+ from_Email_Address);
								System.out.println(" To_Email_Address 5555 : "
										+ sender);
								MimeMessage message = new MimeMessage(session);
								message.setFrom(new InternetAddress(sender));
								message.addRecipient(Message.RecipientType.TO,
										new InternetAddress(from_Email_Address));

								message.setSubject(msg_s1);
								System.out.println(" msg_s1 : " + msg_s1);

								message.setContent(" " + msg_g1 + "<br><br>"
										+ msg_t1 + email_id + " "+msg_t2+ " "
										+ workflow_type+ " " + msg_t3 + " "+ client_code+ " "
										+ msg_t4 + " "+ application_code + " "+ msg_t5
										+ "<br><br>" + msg_e1 + "<br><br>"
										+ msg_e2 + "<br><br>" + msg_e3
										+ "<br><br><i>" + msg_e4 + "</i>",
										"text/html");
								System.out.println(" messageBodyPart : ");

								Transport.send(message);
								System.out.println("Alert message sent");

							}
						} catch (MessagingException e) {
							throw new RuntimeException(e);
						}

					} 
					
				}
				stmt = connection.createStatement();

				String updateStatus = "update EMAIL_LOG set EMAIL_RESPONSE_FLAG = 'Y' where EMAIL_ID =  "
						+ email_id;
				ResultSet rs2 = stmt.executeQuery(updateStatus);

				stmt.close();
				connection.close();
			} catch (SQLException e) {

				System.out.println("Connection Failed! Check output console");
				e.printStackTrace();
				return;

			}

			if (connection != null) {
				System.out
						.println("You made it, take control your database now!");
			} else {
				System.out.println("Failed to make connection!");
			}

			folder.close(true);
			store.close();

		} catch (NullPointerException e) {

			System.out
					.println("Null Pointer Exception! Please check the format");
			e.printStackTrace();
			return;

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (MessagingException e) {
			e.printStackTrace();
			System.exit(2);
		} catch (Exception e) {

			System.out.println("Message Failed! Check output console");
			e.printStackTrace();
			return;

		}
	}
}
