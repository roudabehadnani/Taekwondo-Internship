package se.taekwondointernship.data.service;

import se.taekwondointernship.data.models.dto.EmailDto;
import se.taekwondointernship.data.models.entity.MessageEntity;
import se.taekwondointernship.data.models.form.EmailForm;

public interface EmailService {
    EmailDto create(EmailForm form);
    EmailDto editSender(EmailForm form);
    EmailDto editPassword(EmailForm form);
    EmailDto editSenderName(EmailForm form);
    EmailDto editSubject(EmailForm form);
    void sending(String recipient, String url);
    String getPassword();
    MessageEntity getEmailMessage();
}
