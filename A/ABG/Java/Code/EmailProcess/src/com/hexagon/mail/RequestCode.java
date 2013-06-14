package com.hexagon.mail;

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
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RequestCode {

	public static void main(String args[]) throws Exception {
		try {
			// Get system properties
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
String msg_subject = null;
String msg_content = null;
String msg_endofmail = null;
String from_Email_Address = null;
Statement stmt2 = null;
			try {
				connection = DriverManager.getConnection(
						props.getProperty("database"),
						props.getProperty("dbuser"),
						props.getProperty("dbpassword"));
				// STEP 4: Execute a query
				System.out.println("Creating statement..in Request Code 1.");
				stmt = connection.createStatement();
				System.out.println("Creating statement..in Request Code 2.");
				String preQuery = "select T.MSG_SUBJECT,t.MSG_CONTENT,t.MSG_ENDOFMAIL,T.MSG_TO_BE_SENT_TO from TEMP_RESPONSE_FOR_SYSREQUEST t where T.PROCESS_FLAG = 'N'";
				System.out.println("Creating statement..in Request Code 3." + preQuery);
				ResultSet rs1 = stmt.executeQuery(preQuery);
				System.out.println("Creating statement..in Request Code 4.");
				// STEP 5: Extract data from result set
				while (rs1.next()) {
					msg_subject = rs1.getString("MSG_SUBJECT");
					msg_content = rs1.getString("MSG_CONTENT");
					msg_endofmail = rs1.getString("MSG_ENDOFMAIL");
					from_Email_Address = rs1.getString("MSG_TO_BE_SENT_TO");
				}

				// STEP 6: Clean-up environment
				System.out.println("Fetching Records...");
				// message[i].setFlag(Flags.Flag.DELETED, true);
				try {System.out.println("Creating statement..in from email address." + from_Email_Address);
					if (from_Email_Address != null) {

						System.out.println(" FROM_EMAIL_ADDRESS 5555 : "
								+ from_Email_Address);
						System.out
								.println(" To_Email_Address 5555 : " + sender);
						
						MimeMessage message = new MimeMessage(session);
						message.setFrom(new InternetAddress(sender));
						//message.addRecipient(Message.RecipientType.TO,new InternetAddress(from_Email_Address));
						message.addRecipients(Message.RecipientType.TO,(InternetAddress.parse(from_Email_Address))); 

						message.setSubject(msg_subject);
						System.out.println(" msg_s1 : " + msg_subject);

						message.setContent(" " + msg_content + " "+ msg_endofmail , "text/html");

						Transport.send(message);
						System.out.println("Alert message sent");

					}
				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}

				stmt = connection.createStatement();

				String updateStatus = "update TEMP_RESPONSE_FOR_SYSREQUEST set PROCESS_FLAG = 'Y' ";
				ResultSet rs3 = stmt.executeQuery(updateStatus);

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

		} catch (Exception e) {

			System.out.println("Message Failed! Check output console");
			e.printStackTrace();
			return;

		}
	}
}
