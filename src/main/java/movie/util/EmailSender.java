package movie.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    // Configure these with your email server details
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String USERNAME = "mvtcktdmn@gmail.com"; // Change to your email
    private static final String PASSWORD = "fenh thre hngu oekn"; // Use app password for Gmail

    public static void sendEmail(String recipient, String subject, String content) throws Exception {
        // Set email server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            // Set HTML content
            message.setContent(content, "text/html; charset=utf-8");

            // Send message
            Transport.send(message);
        } catch (MessagingException e) {
            throw new Exception("Failed to send email: " + e.getMessage());
        }
    }
}