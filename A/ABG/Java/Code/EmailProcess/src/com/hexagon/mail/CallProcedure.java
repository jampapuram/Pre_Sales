package com.hexagon.mail;

import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class CallProcedure extends Inbox  {

	//private static void callOracleStoredProcINParameter() throws SQLException {
//	public static void main(String[] args) {
	public void callOracleStoredProc(){
		Connection dbConnection = null;
		CallableStatement callableStatement = null;

		String insertStoreProc = "{call sp_el_dedup(?,?,?,?)}";

		try {
			try {

				Class.forName("oracle.jdbc.driver.OracleDriver");

			} catch (ClassNotFoundException e) {

				System.out.println("Where is your Oracle JDBC Driver?");
				e.printStackTrace();
				return;

			}

			try {
				Properties prop = new Properties();
				prop.load(new FileInputStream("config.properties"));
				dbConnection = DriverManager.getConnection(
						prop.getProperty("database"),
						prop.getProperty("dbuser"),
						prop.getProperty("dbpassword"));

				Statement stmt = null;
				int email_id = 0;
				String client_code = "";
				String application_id = "";
				String amount = "";
				System.out.println("Creating statement..in Call Procedure.");
//
				stmt = dbConnection.createStatement();
				String procedureQuery = "SELECT email_id,E.CLIENT_CODE,E.APPLICATION_ID,E.AMOUNT FROM EMAIL_ATTACHMENT_INFO E WHERE email_id = (SELECT MAX(t.email_id) FROM EMAIL_ATTACHMENT_INFO t ) ";
				ResultSet rs1 = stmt.executeQuery(procedureQuery);

				// STEP 5: Extract data from result set
				while (rs1.next()) {
					email_id = rs1.getInt("email_id");
					client_code = rs1.getString("CLIENT_CODE");
					application_id = rs1.getString("APPLICATION_ID");
					amount = rs1.getString("AMOUNT");
					// Display values
					System.out.println(" Email Id : " + email_id);
					System.out.println(" Client Code : " + client_code);
					System.out.println(" APPLICATION_ID : " + application_id);
					System.out.println(" AMOUNT : " + amount);
				}
				callableStatement = dbConnection.prepareCall(insertStoreProc);

				callableStatement.setInt(1, email_id);
				callableStatement.setString(2, client_code);
				callableStatement.setString(3, application_id);
				callableStatement.setString(4, amount);

				// execute insertDBUSER store procedure
				callableStatement.executeUpdate();

				System.out.println("Process done!");

			} catch (SQLException e) {

				System.out.println(e.getMessage());

			} finally {

				if (callableStatement != null) {
					callableStatement.close();
				}

				if (dbConnection != null) {
					dbConnection.close();
				}

			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} catch (Exception e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}
	}
//	}
	//}
}
