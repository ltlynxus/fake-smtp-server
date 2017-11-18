package de.gessnerfl.fakesmtp.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gessnerfl.fakesmtp.model.Email;
import de.gessnerfl.fakesmtp.util.TimestampProvider;

@Service
public class EmailFactory {
    public static final String UNDEFINED = "<undefined>";

    private TimestampProvider timestampProvider;
    
    @Autowired
    public void setTimestampProvider(TimestampProvider timestampProvider) {
    	this.timestampProvider = timestampProvider;
    }

    public Email convert(String from, String to, InputStream data) throws IOException {
        String rawData = convertStreamToString(data);
        try {
            Session s = Session.getDefaultInstance(new Properties());
            MimeMessage mimeMessage = new MimeMessage(s, new ByteArrayInputStream(rawData.getBytes(StandardCharsets.UTF_8)));
            String subject = mimeMessage.getSubject() != null ? mimeMessage.getSubject() : UNDEFINED;
            return createEmail(from, to, subject, rawData);
        } catch (MessagingException e) {
            data.reset();
            return createEmail(from, to, UNDEFINED, rawData);
        }
    }

    private Email createEmail(String from, String to, String subject, String rawData){
        Email email = new Email();
        email.setFromAddress(from);
        email.setToAddress(to);
        email.setReceivedOn(timestampProvider.now());
        email.setSubject(subject);
        email.setRawData(rawData);
        return email;
    }

    private String convertStreamToString(InputStream data) throws IOException {

        return IOUtils.toString(data, StandardCharsets.UTF_8);
    }
}
