package de.gessnerfl.fakesmtp.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import de.gessnerfl.fakesmtp.TestResourceUtil;
import de.gessnerfl.fakesmtp.model.Email;
import de.gessnerfl.fakesmtp.util.TimestampProvider;

@RunWith(MockitoJUnitRunner.class)
public class EmailFactoryTest {

    private static final String SENDER = "sender";
    private static final String RECEIVER = "receiver";

    @Mock
    private TimestampProvider timestampProvider;
    @Mock
    private Logger logger;

    @InjectMocks
    private EmailFactory sut;

    @Test
    public void shouldCreateEmailForEmlFileWithSubjectAndContentTypePlain() throws Exception {
        Date now = new Date();
        String testFilename = "mail-with-subject.eml";
        InputStream data = TestResourceUtil.getTestFile(testFilename);
        String rawData = TestResourceUtil.getTestFileContent(testFilename);

        when(timestampProvider.now()).thenReturn(now);

        Email result = sut.convert(SENDER, RECEIVER, data);

        assertEquals(SENDER, result.getFromAddress());
        assertEquals(RECEIVER, result.getToAddress());
        assertEquals("This is the mail title", result.getSubject());
        assertEquals(rawData, result.getRawData());
        assertEquals(now, result.getReceivedOn());
    }

    @Test
    public void shouldCreateEmailForEmlFileWithSubjectAndContentTypeHtml() throws Exception {
        Date now = new Date();
        String testFilename = "mail-with-subject-and-content-type-html.eml";
        InputStream data = TestResourceUtil.getTestFile(testFilename);
        String rawData = TestResourceUtil.getTestFileContent(testFilename);

        when(timestampProvider.now()).thenReturn(now);

        Email result = sut.convert(SENDER, RECEIVER, data);

        assertEquals(SENDER, result.getFromAddress());
        assertEquals(RECEIVER, result.getToAddress());
        assertEquals("This is the mail title", result.getSubject());
        assertEquals(rawData, result.getRawData());
        assertEquals(now, result.getReceivedOn());
    }

    @Test
    public void shouldCreateEmailForEmlFileWithSubjectAndWithoutContentType() throws Exception {
        Date now = new Date();
        String testFilename = "mail-with-subject-without-content-type.eml";
        InputStream data = TestResourceUtil.getTestFile(testFilename);
        String rawData = TestResourceUtil.getTestFileContent(testFilename);

        when(timestampProvider.now()).thenReturn(now);

        Email result = sut.convert(SENDER, RECEIVER, data);

        assertEquals(SENDER, result.getFromAddress());
        assertEquals(RECEIVER, result.getToAddress());
        assertEquals("This is the mail title", result.getSubject());
        assertEquals(rawData, result.getRawData());
        assertEquals(now, result.getReceivedOn());
    }

    @Test
    public void shouldCreateEmailForEmlFileWithoutSubjectAndContentTypePlain() throws Exception {
        Date now = new Date();
        String testFilename = "mail-without-subject.eml";
        InputStream data = TestResourceUtil.getTestFile(testFilename);
        String rawData = TestResourceUtil.getTestFileContent(testFilename);

        when(timestampProvider.now()).thenReturn(now);

        Email result = sut.convert(SENDER, RECEIVER, data);

        assertEquals(SENDER, result.getFromAddress());
        assertEquals(RECEIVER, result.getToAddress());
        assertEquals(EmailFactory.UNDEFINED, result.getSubject());
        assertEquals(rawData, result.getRawData());
        assertEquals(now, result.getReceivedOn());
    }

    @Test
    public void shouldCreateMailForPlainText() throws Exception {
        Date now = new Date();
        String rawData = "this is just some dummy content";
        InputStream data = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.UTF_8));

        when(timestampProvider.now()).thenReturn(now);

        Email result = sut.convert(SENDER, RECEIVER, data);

        assertEquals(SENDER, result.getFromAddress());
        assertEquals(RECEIVER, result.getToAddress());
        assertEquals(EmailFactory.UNDEFINED, result.getSubject());
        assertEquals(rawData, result.getRawData());
        assertEquals(now, result.getReceivedOn());
    }
}