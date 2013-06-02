package com.hexagon.mail;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

public class EmailSend {
	public static void main(String args[]) throws Exception {
		try { // Get system properties
			Properties prop = new Properties();
			prop.load(new FileInputStream("config.properties"));

			String host = prop.getProperty("host");
			String username = prop.getProperty("username");
			String passwoed = prop.getProperty("passwoed");
			// Get the default Session object.
			Session session = Session.getDefaultInstance(prop);

			// Get a Store object that implements the specified protocol.
			Store store = session.getStore("imaps");

			// Connect to the current host using the specified username and
			// password.
			store.connect(host, username, passwoed);

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
			try {
				connection = DriverManager.getConnection(
						prop.getProperty("database"),
						prop.getProperty("dbuser"),
						prop.getProperty("dbpassword"));
				// STEP 4: Execute a query
				System.out.println("Creating statement...");
				stmt = connection.createStatement();

				String preQueryStatement = "";
				PreparedStatement preparedStmt = connection
						.prepareStatement(preQueryStatement);

				// STEP 6: Clean-up environment
				System.out.println("Updated Table...");
				// message[i].setFlag(Flags.Flag.DELETED, true);
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
