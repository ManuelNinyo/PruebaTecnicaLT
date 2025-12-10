package manuel.pruebatecnica.infrastructure.service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import manuel.pruebatecnica.application.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.nio.ByteBuffer;

@Service
@RequiredArgsConstructor
public class AwsSesEmailService implements EmailService {



 

    @Override
    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String attachmentName) {
        try {

            
        } catch (Exception e) {
            throw new RuntimeException("Error sending email with AWS SES: " + e.getMessage(), e);
        }
    }
}
