package de.gessnerfl.fakesmtp.server.impl;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.subethamail.smtp.TooMuchDataException;
import org.subethamail.smtp.helper.SimpleMessageListener;

import de.gessnerfl.fakesmtp.model.Email;
import de.gessnerfl.fakesmtp.repository.EmailRepository;
import de.gessnerfl.fakesmtp.server.EmailFactory;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = IOException.class)
public class EmailPersister implements SimpleMessageListener {
	
    private EmailFactory emailFactory;
    
    private EmailRepository emailRepository;
    
    private Logger logger;
    
    @Autowired
    public void setEmailFactory(EmailFactory emailFactory) {
    	this.emailFactory = emailFactory;
    }
    
    @Autowired
    public void setEmailRepository(EmailRepository emailRepository) {
    	this.emailRepository = emailRepository;
    }
    
    @Autowired
    public void setLogger(Logger logger) {
    	this.logger = logger;
    }
    
    @Override
    public boolean accept(String from, String recipient) {
        return true;
    }

    @Override
    public void deliver(String sender, String recipient, InputStream data) throws TooMuchDataException, IOException {
        logger.info("Received email from {} for {}", sender, recipient);
        Email email = emailFactory.convert(sender, recipient, data);
        emailRepository.save(email);
    }
}
