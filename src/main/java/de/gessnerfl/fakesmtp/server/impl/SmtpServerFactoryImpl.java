package de.gessnerfl.fakesmtp.server.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter;
import org.subethamail.smtp.server.SMTPServer;

import de.gessnerfl.fakesmtp.server.SmtpServer;
import de.gessnerfl.fakesmtp.server.SmtpServerFactory;

@Profile("default")
@Service
public class SmtpServerFactoryImpl implements SmtpServerFactory {

    private EmailPersister emailPersister;
    
    private SmtpServerConfigurator configurator;

    @Autowired
    public void setEmailPersister(EmailPersister emailPersister) {
    	this.emailPersister = emailPersister;
    }
    
    @Autowired
    public void setConfigurator(SmtpServerConfigurator configurator) {
    	this.configurator = configurator;
    }

    @Override
    public SmtpServer create() {
        SimpleMessageListenerAdapter simpleMessageListenerAdapter = new SimpleMessageListenerAdapter(emailPersister);
        SMTPServer smtpServer = new SMTPServer(simpleMessageListenerAdapter);
        configurator.configure(smtpServer);
        return new SmtpServerImpl(smtpServer);
    }
	
}
