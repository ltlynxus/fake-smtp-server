package de.gessnerfl.fakesmtp;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import de.gessnerfl.fakesmtp.model.Email;
import de.gessnerfl.fakesmtp.repository.EmailRepository;

@Service("emailBootstrap")
@Profile("bootstrap")
public class EmailBootstrap implements InitializingBean {

	private EmailRepository emailRepository;
	
	private Logger logger;
	
	@Autowired
	public void setEmailRepository(EmailRepository emailRepository) {
		this.emailRepository = emailRepository;
	}
	
	@Autowired
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		logger.info("Bootstrapping email");
		
		Email email = new Email();
		email.setFromAddress("user1@example.com");
		email.setToAddress("user2@example.com");
		email.setReceivedOn(new Date());
		email.setSubject("Test 1");
		email.setRawData("mail-with-subject.emlDelivered-To: receiver@example.com\n" + 
				"Received: by 10.100.155.4 with SMTP id 987654328765;\n" + 
				"        Mon, 8 May 2017 02:12:12 -0700 (PDT)\n" + 
				"X-Received: by 10.223.133.200 with SMTP id 0987654387698.65.098765443543;\n" + 
				"        Mon, 08 May 2017 02:12:12 -0700 (PDT)\n" + 
				"Return-Path: <sender@example.com>\n" + 
				"Received: from host.example.com\n" + 
				"Received-SPF: pass (example.com: domain of sender@example.com designates 123.123.123.123 as permitted sender) client-ip=123.123.123.123;\n" + 
				"Authentication-Results: mx.example.com;\n" + 
				"       dkim=pass header.i=@example.com;\n" + 
				"       spf=pass (example.com: domain of sender@example.com designates 123.123.123.123 as permitted sender) smtp.mailfrom=sender@example.com\n" + 
				"Received: from proxy.example.com ([234.234.234.234]:12345 helo=Authentication)\n" + 
				"	by ns1.example.com with esmtpa (Exim 4.89)\n" + 
				"	(envelope-from <sender@example.com>)\n" + 
				"	id 123456-123456-12\n" + 
				"	for receiver@example.com; Mon, 08 May 2017 11:12:07 +0200\n" + 
				"From: \"Sender\" <user1@example.com>\n" + 
				"To: \"'Receiver'\" <user2@example.com>\n" + 
				"References: <2323232323-2323-2323-2323-232323232323@example.com>\n" + 
				"In-Reply-To: <2323232323-2323-2323-2323-2323232323239@example.com>\n" + 
				"Subject: This is the mail title\n" + 
				"Date: Mon, 8 May 2017 11:12:09 +0200\n" + 
				"Message-ID: <772437572438758852084$@com>\n" + 
				"MIME-Version: 1.0\n" + 
				"Content-Type: text/html;charset=utf-8\n" + 
				"Content-Transfer-Encoding: quoted-printable\n" + 
				"X-Mailer: Microsoft Office Outlook 12.0\n" + 
				"Content-Language: de\n" + 
				"\n" + 
				"\n" + 
				"<html><head></head><body>Mail Body</body></html>");
		
		
		emailRepository.saveAndFlush(email);	
		
		logger.info("Bootstrapping email done");
	}
	
}
