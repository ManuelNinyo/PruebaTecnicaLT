package manuel.pruebatecnica.application.service;

public interface EmailService {
    void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String attachmentName);
}
