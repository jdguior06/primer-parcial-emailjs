package coneccion.comunicacion;

import coneccion.utils.Email;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

//SOLO TOCAR LAS CREDENCIALES
public class SendEmailThread implements Runnable {
    
    /*
    private final static String PORT_SMTP = "465";
    private final static String PROTOCOL = "smtp";
    private final static String HOST = "smtp.gmail.com";
    private final static String USER = "davidcahuasiriporoso@gmail.com";
    private final static String MAIL = "davidcahuasiriporoso@gmail.com";
    private final static String MAIL_PASSWORD = "abnv lcuo cvft ftdx";
    */
   
    
    private final static String PORT_SMTP = "25";
    private final static String PROTOCOL = "smtp";
    private final static String HOST = "mail.tecnoweb13sa.online";
    private final static String USER = "grupo13sa";
    private final static String PASSWORD = "grupo13sagrupo13sa";
    private final static String MAIL = "grupo13sa@tecnoweb13sa.online";
    private final static String MAIL_PASSWORD = "grupo13sagrupo13sa";
    
   
    private Email email;
    
    public SendEmailThread(Email email) {
        this.email = email;
    }
    
    @Override
    public void run() {

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", PROTOCOL);
        properties.setProperty("mail.smtp.host", HOST);
        properties.setProperty("mail.smtp.port", PORT_SMTP);
        properties.setProperty("mail.smtp.tls.enable", "true");//cuando user tecnoweb
        //properties.setProperty("mail.smtp.ssl.enable", "true");//cuando usen Gmail
        properties.setProperty("mail.smtp.auth", "false");    //false en tecno y true en gmail    
        
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println(USER);
                return new PasswordAuthentication(USER, MAIL_PASSWORD);
            }
        });
        
        try {    
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MAIL));
            
            InternetAddress[] toAddresses = {new InternetAddress(email.getTo())};
            
            message.setRecipients(MimeMessage.RecipientType.TO, toAddresses);
            message.setSubject(email.getSubject());
            
            Multipart multipart = new MimeMultipart("alternative");
            MimeBodyPart htmlPart = new MimeBodyPart();
            
            htmlPart.setContent(email.getMessage(), "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);
            message.saveChanges();
            
            Transport.send(message);
            System.out.println("MENSAJE ENVIADO CON EXITO");
        } catch (NoSuchProviderException | AddressException ex) {
            Logger.getLogger(SendEmailThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(SendEmailThread.class.getName()).log(Level.SEVERE, null, ex);
        }        
 
    }

    
}
