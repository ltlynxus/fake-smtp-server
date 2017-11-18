package de.gessnerfl.fakesmtp.server.impl;

import de.gessnerfl.fakesmtp.config.FakeSmtpConfigurationProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.subethamail.smtp.auth.EasyAuthenticationHandlerFactory;
import org.subethamail.smtp.server.SMTPServer;

@Service
public class SmtpServerConfigurator {

    private FakeSmtpConfigurationProperties fakeSmtpConfigurationProperties;
    
    private BasicUsernamePasswordValidator basicUsernamePasswordValidator;
    
    private Logger logger;

    @Autowired
    public void setFakeSmtpConfigurationProperties(FakeSmtpConfigurationProperties fakeSmtpConfigurationProperties) {
    	this.fakeSmtpConfigurationProperties = fakeSmtpConfigurationProperties;
    }
    
    @Autowired
    public void setBasicUsernamePasswordValidator(BasicUsernamePasswordValidator basicUsernamePasswordValidator) {
    	this.basicUsernamePasswordValidator = basicUsernamePasswordValidator;
    }
    
    @Autowired
    public void setLogger(Logger logger) {
    	this.logger = logger;
    }

    public void configure(SMTPServer smtpServer) {
        smtpServer.setPort(fakeSmtpConfigurationProperties.getPort());
        smtpServer.setBindAddress(fakeSmtpConfigurationProperties.getBindAddress());
        if (fakeSmtpConfigurationProperties.getAuthentication() != null) {
            configureAuthentication(smtpServer, fakeSmtpConfigurationProperties.getAuthentication());
        }
    }

    private void configureAuthentication(SMTPServer smtpServer, FakeSmtpConfigurationProperties.Authentication authentication) {
        if (StringUtils.isEmpty(authentication.getUsername())) {
            logger.error("Username is missing; skip configuration of authentication");
        } else if (StringUtils.isEmpty(authentication.getPassword())) {
            logger.error("Password is missing; skip configuration of authentication");
        } else {
            logger.info("Setup simple username and password authentication for SMTP server");
            smtpServer.setAuthenticationHandlerFactory(new EasyAuthenticationHandlerFactory(basicUsernamePasswordValidator));
        }
    }
}
