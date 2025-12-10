package manuel.pruebatecnica.infrastructure.service;

import lombok.extern.slf4j.Slf4j;
import manuel.pruebatecnica.application.service.EmailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
@ConditionalOnProperty(name = "email.service.mock", havingValue = "true", matchIfMissing = false)
public class MockEmailService implements EmailService {

    @Override
    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String attachmentName) {
        log.info("MOCK: Sending email to: {}", to);
        log.info("MOCK: Subject: {}", subject);
        log.info("MOCK: Body: {}", body);
        log.info("MOCK: Attachment name: {}", attachmentName);
        log.info("MOCK: Attachment size: {} bytes", attachment.length);
        
        try {
            String fileName = "mock_" + System.currentTimeMillis() + "_" + attachmentName;
            String filePath = System.getProperty("user.home") + "/" + fileName;
            
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(attachment);
            }
            
            log.info("MOCK: Attachment saved to: {}", filePath);
            
            log.info("MOCK: Email sent successfully!");
            
        } catch (IOException e) {
            log.error("MOCK: Error saving attachment", e);
            throw new RuntimeException("MOCK: Failed to save attachment", e);
        }
    }
}
