package com.hexagon.mail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;

public class Inbox {
	public String fromEmailAddress = "";
	public String toEmailAddress;
	public String ccEmailAddress = "";
	public static String subject = "";
	public Date sentTime = null;
	public Object content = "";
	public String attachmentFlag = "";
	public static String attachmentName = "";
	public Integer newMessage = 0;
	public String processFlag = "";
	public String sucessFlag = "";
	public String activeFlag = "";
	public File fileName = null;
	public String applicationFlag = "";
	public String requestCode = "";
	public String emailResponseFlag = "";
	public String newContent = "";
	public String mailRequestCode = "";
	public String mailCode = "";

	public void readInbox() {
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

			// Message[] message = folder.getMessages();
			/* Get the messages which is unread in the Inbox */
			Message message[] = folder.search(new FlagTerm(
					new Flags(Flag.SEEN), false));
			newMessage = message.length;
			// Display message.
			for (int i = 0; i < message.length; i++) {

				System.out.println("------------ Message " + (i + 1)
						+ " ------------");
				Address[] a;

				if ((a = message[i].getRecipients(Message.RecipientType.TO)) != null) {

					String from_address = (message[i].getFrom()[0]).toString();
					Pattern p = Pattern.compile("\\<.*\\>");
					Matcher m = p.matcher(from_address);

					if (m.find()) {
						fromEmailAddress = (m.group().subSequence(1, m.group()
								.length() - 1)).toString();
						System.out.println("From Email address is : "
								+ fromEmailAddress);
					} else {
						System.out
								.println("This is without from email address");

					}

					for (int j = 0; j < a.length; j++) {
						String to_EmailAddress = a[j].toString();
						Matcher to = p.matcher(to_EmailAddress);
						if (to.find()) {
							toEmailAddress = (to.group().subSequence(1, to
									.group().length() - 1)).toString();
						} else {
							toEmailAddress = a[j].toString();

						}

					}

					String cc = InternetAddress.toString(message[i]
							.getRecipients(Message.RecipientType.CC));
					if (cc != null) {
						Matcher cc_Email = p.matcher(cc);

						if (cc_Email.find()) {
							ccEmailAddress = (cc_Email.group().subSequence(1,
									cc_Email.group().length() - 1)).toString();
							System.out.println("CC Email address is : "
									+ ccEmailAddress);
						} else {
							System.out
									.println("This is without CC email address");

						}
					}
					subject = (message[i].getSubject()).toString();
					System.out.println("Subjet is : " + subject);
					java.util.Date sent_date = (message[i].getSentDate());

					sentTime = sent_date;
					System.out.println("Sent Date is : " + sentTime);

					content = (message[i].getContent());

					if (message[i].isMimeType("multipart/*")) {
						Multipart multipart = (Multipart) message[i]
								.getContent();
						System.out.println("Multipart Count"
								+ multipart.getCount());
						for (int x = 0; x < 1; x++) {
							BodyPart bodyPart = multipart.getBodyPart(x);

							String disposition = bodyPart.getDisposition();

							if (disposition != null
									&& (disposition.equals(Part.ATTACHMENT))) {
								System.out
										.println("Mail have some attachment : ");

								DataHandler handler = bodyPart.getDataHandler();
								System.out.println("file name : "
										+ handler.getName());
							} else {
								System.out
										.println("Content of multipart email : "
												+ bodyPart.getContent());
								Object multiContent = bodyPart.getContent();
								newContent = multiContent.toString();
								System.out
										.println("New Content of the email : "
												+ newContent);
								if (newContent.contains("Request No :")) {
									Pattern c = Pattern.compile("\\<.*\\>");
									Matcher t = c.matcher(newContent);
									if (t.find()) {
										mailRequestCode = (t.group()
												.subSequence(1, t.group()
														.length() - 1))
												.toString();
										System.out
												.println("Mail Content Request Code for Request No : "
														+ mailRequestCode);
									}
								}else if (newContent.contains("Email No :")){
									Pattern c = Pattern.compile("\\<.*\\>");
									Matcher t = c.matcher(newContent);
									if (t.find()) {
										mailCode = (t.group()
												.subSequence(1, t.group()
														.length() - 1))
												.toString();
										System.out
												.println("Mail Content Request Code for Email No  : "
														+ mailCode);
									}
								}
								attachmentFlag = "N";
								// content = getText(bodyPart);
							}
						}
					}

					if (content instanceof Multipart) {
						handleMultipart((Multipart) content);
					} else {
						handlePart(message[i]);
					}
					processFlag = "N";
					sucessFlag = "";
					activeFlag = "Y";
					message[i].setFlag(Flags.Flag.SEEN, true);

				} else {
					System.out.println("There is no email address for this");
				}
			}

			folder.close(true);
			store.close();
		} catch (NullPointerException e) {

			System.out
					.println("Null Pointer Exception! Please check the format");
			e.printStackTrace();
			return;

		} catch (Exception e) {

			System.out.println("Message Failed! Check output console");
			e.printStackTrace();
			return;

		}
	}

	public void handleMultipart(Multipart multipart) throws MessagingException,
			IOException {
		for (int i = 0; i < multipart.getCount(); i++) {
			handlePart(multipart.getBodyPart(i));
		}
	}

	public void handlePart(Part part) throws MessagingException, IOException {
		String dposition = part.getDisposition();
		if (dposition != null && (dposition.equalsIgnoreCase(Part.ATTACHMENT))) {
			attachmentFlag = "Y";
			System.out.println("Attachment: " + part.getFileName());
			attachmentName = part.getFileName();
			if (subject != null) {

				ArrayList al = new ArrayList();
				Connection connection = null;
				Statement stmt = null;
				String subjectQuery = null;
				Properties prop = new Properties();

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
					if (!al.contains(subject)) {
						System.out.println(" Does not Contains");
						attachmentFlag = "Y";
						System.out.println("Attachment: " + part.getFileName());
						attachmentName = part.getFileName();
						saveFile(part.getFileName(), part.getInputStream());
					} else {
						System.out.println("Contains ");
						attachmentFlag = "N";
						System.out.println("Other: " + dposition);
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
			
			}
			}
	}

	public void saveFile(String filename, InputStream input) throws IOException {
		if (filename == null) {
			filename = File.createTempFile("MailAttacheFile", ".out").getName();
		}
		System.out.println("downloading attachment...");
		File file = new File(filename);
		for (int i = 0; file.exists(); i++) {
			file = new File(filename + i);
			fileName = file;
		}
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		BufferedInputStream bis = new BufferedInputStream(input);
		int fByte;
		while ((fByte = bis.read()) != -1) {
			bos.write(fByte);
		}
		bos.flush();

		bos.close();
		bis.close();

		System.out.println("done attachment...");
	}

	public String getFromEmailAddress() {
		return fromEmailAddress;
	}

	public void setFromEmailAddress(String fromAddress) {
		this.fromEmailAddress = fromAddress;
	}

	public String getToEmailAddress() {
		return toEmailAddress;
	}

	public void setToEmailAddress(String toAddress) {
		this.toEmailAddress = toAddress;
	}

	public String getCCEmailAddress() {
		return ccEmailAddress;
	}

	public void setCCEmailAddress(String ccAddress) {
		this.ccEmailAddress = ccAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAttachmentFlag() {
		return attachmentFlag;
	}

	public void setAttachmentFlag(String attachmentFlage) {
		this.attachmentFlag = attachmentFlage;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentNamee) {
		this.attachmentName = attachmentNamee;
	}

	public Integer getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(Integer messageCount) {
		this.newMessage = messageCount;
	}

	public String getProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(String processFlage) {
		this.processFlag = processFlage;
	}

	public String getSucessFlag() {
		return sucessFlag;
	}

	public void setSucessFlag(String sucessFlage) {
		this.sucessFlag = sucessFlage;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlage) {
		this.activeFlag = activeFlage;
	}

	public File getFileName() {
		return fileName;
	}

	public void setFileName(File fileNamee) {
		this.fileName = fileNamee;
	}

	public String getApplicationFlag() {
		return applicationFlag;
	}

	public void setApplicationFlag(String applicationFlag) {
		this.applicationFlag = applicationFlag;
	}

	public String getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	public String getEmailResponseFlag() {
		return emailResponseFlag;
	}

	public void setEmailResponseFlag(String emailResponseFlag) {
		this.emailResponseFlag = emailResponseFlag;
	}

	public String getNewContent() {
		return newContent;
	}

	public void setNewContent(String newContent) {
		this.newContent = newContent;
	}

	public void setCcEmailAddress(String ccEmailAddress) {
		this.ccEmailAddress = ccEmailAddress;
	}

	public String getMailRequestCode() {
		return mailRequestCode;
	}

	public void setMailRequestCode(String mailRequestCode) {
		this.mailRequestCode = mailRequestCode;
	}

	public String getMailCode() {
		return mailCode;
	}

	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}

}
