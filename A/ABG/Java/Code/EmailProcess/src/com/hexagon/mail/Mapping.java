package com.hexagon.mail;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Mapping extends Inbox {

	public static void main(String[] args) {

		Inbox inbox = new Inbox();
		UpdateEmail updateEmailInfo = new UpdateEmail();
		ExcelAttachmentReader excelAttachmentReader = new ExcelAttachmentReader();
		CallProcedure callprocedure = new CallProcedure();

		updateEmailInfo.emailInfo(inbox);
		String xlsPath = "C:\\Users\\admin\\workspace\\EmailProcess\\"
				+ attachmentName;
		excelAttachmentReader.displayFromExcel(xlsPath);
		callprocedure.callOracleStoredProc();

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

		Connection connection = null;
		Statement stmt = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		Statement stmt3 = null;
		Statement stmt4 = null;
		Statement stmt5 = null;
		Statement stmt6 = null;
		Statement stmt7 = null;
		Statement stmt8 = null;
		Statement stmt9 = null;
		Statement stmt10 = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;

		}
		try {
			String type_request = "";
			String subject = "";
			String applStage = "";
			int org_id = 0;
			int email_id = 0;
			Properties prop = new Properties();
			prop.load(new FileInputStream("config.properties"));
			connection = DriverManager.getConnection(
					prop.getProperty("database"), prop.getProperty("dbuser"),
					prop.getProperty("dbpassword"));
			// STEP 4: Execute a query
			System.out.println("Creating statement.1..");
			stmt = connection.createStatement();
			String preQueryStatement = "SELECT EAI.TYPE_OF_REQUEST, E.SUBJECT, E.ORG_ID, E.EMAIL_ID FROM EMAIL_ATTACHMENT_INFO EAI,EMAIL_LOG E WHERE EAI.EMAIL_ID = E.EMAIL_ID AND E.SUCCESS_FLAG = 'W' AND EAI.ACTIVE = 'Y'";
			ResultSet rs = stmt.executeQuery(preQueryStatement);
			// System.out.println("Result set  "+ rs.next());
			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				type_request = rs.getString("TYPE_OF_REQUEST");
				subject = rs.getString("SUBJECT");
				org_id = rs.getInt("ORG_ID");
				email_id = rs.getInt("EMAIL_ID");
				// Display values
				System.out
						.println(" Type of Request 22222222: " + type_request);
				System.out.println(" Subject 1111111: " + subject);
				System.out.println(" Org Id: " + org_id);
				System.out.println("Email Id : " + email_id);
			}

			if (org_id != 0 && type_request != null && subject != null) {
				if (org_id != 0 && type_request != null
						&& type_request.equals("D")) {
					if (subject.equalsIgnoreCase("Approval Request")) {
						System.out.println(" Subject : " + subject);
						System.out.println(" Type of the Request : "
								+ type_request);

						// STEP 4: Execute a query
						System.out.println("Creating statement.2..");
						stmt1 = connection.createStatement();
						String approvalRequestQuery = "SELECT U.APPLSTAGE FROM UM_WORKFLOW_STAGES U ,UM_WF_STAGE_EMAIL_MAPPING S WHERE U.WS_ID = S.WS_ID AND S.TYPE_OF_REQUEST = '"
								+ type_request
								+ "'AND S.EMAIL_SUBJECT = '"
								+ subject
								+ "'AND U.ORG_ID = "
								+ org_id
								+ "AND U.ACTIVE = 'Y'";
						ResultSet rs1 = stmt1
								.executeQuery(approvalRequestQuery);

						// STEP 5: Extract data from result set
						while (rs1.next()) {
							applStage = rs1.getString("APPLSTAGE");
							// Display values
							System.out
									.print(" D Approval Application Stage is : "
											+ applStage);
						}

					} else if (subject.equalsIgnoreCase("Approved")) {
						System.out.println(" Subject : " + subject);
						System.out.println(" Type of the Request : "
								+ type_request);
						// STEP 4: Execute a query
						System.out.println("Creating statement..3.");
						stmt2 = connection.createStatement();
						String approvedQuery = "SELECT U.APPLSTAGE FROM UM_WORKFLOW_STAGES U ,UM_WF_STAGE_EMAIL_MAPPING S WHERE U.WS_ID = S.WS_ID AND S.TYPE_OF_REQUEST = '"
								+ type_request
								+ "'AND S.EMAIL_SUBJECT = '"
								+ subject
								+ "'AND U.ORG_ID = "
								+ org_id
								+ "AND U.ACTIVE = 'Y'";
						ResultSet rs2 = stmt2.executeQuery(approvedQuery);

						// STEP 5: Extract data from result set
						while (rs2.next()) {
							applStage = rs2.getString("APPLSTAGE");
							// Display values
							System.out.print(" D Approved Application Stage : "
									+ applStage);
						}
					} else {
						System.out.println(" Subject : " + subject);
						System.out.println(" Type of the Request : "
								+ type_request);
						// STEP 4: Execute a query
						System.out.println("Creating statement.4..");
						stmt3 = connection.createStatement();
						String initQuery = "SELECT U.APPLSTAGE FROM UM_WORKFLOW_STAGES U ,UM_WF_STAGE_EMAIL_MAPPING S WHERE U.WS_ID = S.WS_ID AND S.TYPE_OF_REQUEST = '"
								+ type_request
								+ "'AND S.EMAIL_SUBJECT is null AND U.ORG_ID = "
								+ org_id + "AND U.ACTIVE = 'Y'";

						ResultSet rs3 = stmt3.executeQuery(initQuery);
						System.out.println("Result set 3 Query " + initQuery);
						// STEP 5: Extract data from result set
						while (rs3.next()) {
							applStage = rs3.getString("APPLSTAGE");
							// Display values
							System.out.print(" D Initial Application Stage : "
									+ applStage);
						}
					}
				} else if (org_id != 0 && type_request != null
						&& type_request.equals("P")) {
					if (subject.equalsIgnoreCase("Approval Request")) {
						System.out.println(" Subject : " + subject);
						System.out.println(" Type of the Request : "
								+ type_request);
						// STEP 4: Execute a query
						System.out.println("Creating statement.5.");
						stmt4 = connection.createStatement();
						String pledgeApprovalRequestQuery = "SELECT U.APPLSTAGE FROM UM_WORKFLOW_STAGES U ,UM_WF_STAGE_EMAIL_MAPPING S WHERE U.WS_ID = S.WS_ID AND S.TYPE_OF_REQUEST = '"
								+ type_request
								+ "'AND S.EMAIL_SUBJECT = '"
								+ subject
								+ "'AND U.ORG_ID = "
								+ org_id
								+ "AND U.ACTIVE = 'Y'";
						ResultSet rs4 = stmt4
								.executeQuery(pledgeApprovalRequestQuery);

						// STEP 5: Extract data from result set
						while (rs4.next()) {
							applStage = rs4.getString("APPLSTAGE");
							// Display values
							System.out.print(" P Approval Application Stage : "
									+ applStage);
						}
					} else if (subject.equalsIgnoreCase("Approved")) {
						System.out.println(" Subject : " + subject);
						System.out.println(" Type of the Request : "
								+ type_request);
						// STEP 4: Execute a query
						System.out.println("Creating statement..6.");
						stmt5 = connection.createStatement();
						String pledgeApprovedQuery = "SELECT U.APPLSTAGE FROM UM_WORKFLOW_STAGES U ,UM_WF_STAGE_EMAIL_MAPPING S WHERE U.WS_ID = S.WS_ID AND S.TYPE_OF_REQUEST = '"
								+ type_request
								+ "'AND S.EMAIL_SUBJECT = '"
								+ subject
								+ "'AND U.ORG_ID = "
								+ org_id
								+ "AND U.ACTIVE = 'Y'";

						ResultSet rs5 = stmt5.executeQuery(pledgeApprovedQuery);

						// STEP 5: Extract data from result set
						while (rs5.next()) {
							applStage = rs5.getString("APPLSTAGE");
							// Display values
							System.out.print("P Approved Application Stage : "
									+ applStage);
						}

					} else {
						System.out.println(" Subject : " + subject);
						System.out.println(" Type of the Request : "
								+ type_request);
						// STEP 4: Execute a query
						System.out.println("Creating statement..7.");
						stmt6 = connection.createStatement();
						String initQuery = "SELECT U.APPLSTAGE FROM UM_WORKFLOW_STAGES U ,UM_WF_STAGE_EMAIL_MAPPING S WHERE U.WS_ID = S.WS_ID AND S.TYPE_OF_REQUEST = '"
								+ type_request
								+ "'AND S.EMAIL_SUBJECT is null AND U.ORG_ID = "
								+ org_id + "AND U.ACTIVE = 'Y'";
						ResultSet rs6 = stmt6.executeQuery(initQuery);

						// STEP 5: Extract data from result set
						while (rs6.next()) {
							applStage = rs6.getString("APPLSTAGE");
							// Display values
							System.out.print(" P Initial Application Stage : "
									+ applStage);
						}
					}
				} else if (org_id != 0 && type_request != null
						&& type_request.equals("R")) {
					if (subject.equalsIgnoreCase("Approval Request")) {
						System.out.println(" Subject : " + subject);
						System.out.println(" Type of the Request : "
								+ type_request);

						// STEP 4: Execute a query
						System.out.println("Creating statement..8.");
						stmt7 = connection.createStatement();
						String releaseRequestQuery = "SELECT U.APPLSTAGE FROM UM_WORKFLOW_STAGES U ,UM_WF_STAGE_EMAIL_MAPPING S WHERE U.WS_ID = S.WS_ID AND S.TYPE_OF_REQUEST = '"
								+ type_request
								+ "'AND S.EMAIL_SUBJECT = '"
								+ subject
								+ "'AND U.ORG_ID = "
								+ org_id
								+ "AND U.ACTIVE = 'Y'";

						ResultSet rs7 = stmt7.executeQuery(releaseRequestQuery);

						// STEP 5: Extract data from result set
						while (rs7.next()) {
							applStage = rs7.getString("APPLSTAGE");
							// Display values
							System.out.print(" R Approval Application Stage : "
									+ applStage);
						}

					} else if (subject.equalsIgnoreCase("Approved")) {
						System.out.println(" Subject : " + subject);
						System.out.println(" Type of the Request : "
								+ type_request);
						// STEP 4: Execute a query
						System.out.println("Creating statement..9.");
						stmt8 = connection.createStatement();
						String releaseApprovedQuery = "SELECT U.APPLSTAGE FROM UM_WORKFLOW_STAGES U ,UM_WF_STAGE_EMAIL_MAPPING S WHERE U.WS_ID = S.WS_ID AND S.TYPE_OF_REQUEST = '"
								+ type_request
								+ "'AND S.EMAIL_SUBJECT = '"
								+ subject
								+ "'AND U.ORG_ID = "
								+ org_id
								+ "AND U.ACTIVE = 'Y'";

						ResultSet rs8 = stmt8
								.executeQuery(releaseApprovedQuery);

						// STEP 5: Extract data from result set
						while (rs8.next()) {
							applStage = rs8.getString("APPLSTAGE");
							// Display values
							System.out
									.print(" R Approval Application Stage  : "
											+ applStage);
						}
					} else {
						System.out.println(" Subject : " + subject);
						System.out.println(" Type of the Request : "
								+ type_request);
						// STEP 4: Execute a query
						System.out.println("Creating statement..10.");
						stmt9 = connection.createStatement();
						String initQuery = "SELECT U.APPLSTAGE FROM UM_WORKFLOW_STAGES U ,UM_WF_STAGE_EMAIL_MAPPING S WHERE U.WS_ID = S.WS_ID AND S.TYPE_OF_REQUEST = '"
								+ type_request
								+ "'AND S.EMAIL_SUBJECT is null AND U.ORG_ID = "
								+ org_id + "AND U.ACTIVE = 'Y'";
						ResultSet rs9 = stmt9.executeQuery(initQuery);

						// STEP 5: Extract data from result set
						while (rs9.next()) {
							applStage = rs9.getString("APPLSTAGE");
							// Display values
							System.out.print("R Initial Application Stage  : "
									+ applStage);
						}
					}

				} else {
					// STEP 4: Execute a query
					System.out
							.println("Creating statement.12..Type of request is other than D/P/R : "
									+ applStage + "Email Id : " + email_id);
					stmt10 = connection.createStatement();
					String updateFlagSQL = "UPDATE EMAIL_LOG SET SUCCESS_FLAG = 'F' , PROCESS_FLAG= 'Y', SUCCESS_FLAG_UPD_DT = TO_CHAR(SYSDATE,'dd/MON/yy') WHERE EMAIL_ID = '"
							+ email_id + "' AND ACTIVE = 'Y'";
					ResultSet updateResult = stmt10.executeQuery(updateFlagSQL);
					System.out.println("Query : " + updateFlagSQL);
					// STEP 6: Clean-up environment
					System.out
							.println("Updated Flag Only when the Type of Request is not matching");
				}
			}
			// STEP 4: Execute a query
			System.out.println("Creating statement.11..Application Stage : "
					+ applStage + "Email Id : " + email_id);
			stmt10 = connection.createStatement();
			String updateTableSQL = "UPDATE EMAIL_LOG SET APPLSTAGE = '"
					+ applStage
					+ "', SUCCESS_FLAG = 'S' , PROCESS_FLAG= 'Y', SUCCESS_FLAG_UPD_DT = TO_CHAR(SYSDATE,'dd/MON/yy') WHERE EMAIL_ID = '"
					+ email_id + "' AND ACTIVE = 'Y'";
			ResultSet updateResult = stmt10.executeQuery(updateTableSQL);
			System.out.println("Query : " + updateTableSQL);
			// STEP 6: Clean-up environment
			System.out.println("Updated Flag");
			updateResult.close();
			rs.close();
			stmt.close();
			connection.close();
		} catch (Exception e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}
	}
}